package com.coinflux.web.user_coin_watchlist;

import com.coinflux.web.queue.coin_exchange_rate.dtos.CoinExchangeRateMessageDTO;
import com.coinflux.web.queue.email.EmailMessageProducer;
import com.coinflux.web.queue.email.dtos.EmailMessageDTO;
import com.coinflux.web.queue.notification.NotificationMessageProducer;
import com.coinflux.web.queue.notification.dtos.NotificationMessageDTO;
import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user_coin_watchlist.dtos.UserCoinWatchlistDTO;
import com.coinflux.web.user_coin_watchlist.dtos.requests.CreateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.GetAllUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.UpdateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.responses.CreateUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.dtos.responses.GetAllUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.dtos.responses.UpdateUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.exceptions.DuplicateWatchlistItemException;
import com.coinflux.web.user_coin_watchlist.exceptions.UserCoinWatchlistNotFoundException;
import com.coinflux.web.user_coin_watchlist.mappers.UserCoinWatchlistMapper;
import com.coinflux.web.user_coin_watchlist.specifications.UserCoinWatchlistSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCoinWatchlistService {
    private final UserCoinWatchlistRepository watchlistRepository;
    private final UserCoinWatchlistMapper mapper;
    private final NotificationMessageProducer notificationProducer;
    private final EmailMessageProducer emailProducer;

    @Transactional
    public CreateUserCoinWatchlistResponse createUserCoinWatchlist(CreateUserCoinWatchlistRequest request, UserDTO currentUser) {
        // Check if already exists
        watchlistRepository.findByUserIdAndCoinSymbol(currentUser.getId(), request.getCoinSymbol())
                .ifPresent(existing -> {
                    throw new DuplicateWatchlistItemException("Coin is already in watchlist");
                });
        // Map request to entity
        UserCoinWatchlistEntity entity = mapper.toEntity(request, currentUser.getId());

        // Save entity
        UserCoinWatchlistEntity saved = watchlistRepository.save(entity);

        // Build response
        return CreateUserCoinWatchlistResponse.builder()
                .watchItem(mapper.toDTO(saved))
                .build();
    }

    public GetAllUserCoinWatchlistResponse getAllForUser(Long userId, GetAllUserCoinWatchlistRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<UserCoinWatchlistEntity> spec =
                UserCoinWatchlistSpecification.filterBy(request, userId);

        Page<UserCoinWatchlistEntity> page = watchlistRepository.findAll(spec, pageable);

        List<UserCoinWatchlistDTO> dtos = mapper.toDTOList(page.getContent());

        return mapper.toGetAllResponse(page, dtos);
    }


    @Transactional
    public UpdateUserCoinWatchlistResponse updateUserWatchCoin(Long watchlistId, UpdateUserCoinWatchlistRequest request) {
        UserCoinWatchlistEntity entity = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new UserCoinWatchlistNotFoundException("Watchlist not found with ID: " + watchlistId));

        entity = mapper.toEntityFromUpdateRequest(request, entity);

        UserCoinWatchlistEntity saved = watchlistRepository.save(entity);

        return UpdateUserCoinWatchlistResponse.builder()
                .watchlist(mapper.toDTO(saved))
                .build();
    }

    @Transactional
    public void deleteWatchlistItem(Long id, Long userId) {
        UserCoinWatchlistEntity entity = watchlistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new UserCoinWatchlistNotFoundException(
                        "Watchlist item not found for id: " + id + " and userId: " + userId));

        watchlistRepository.delete(entity);
    }

    public void evaluateUserWatchlistRules(CoinExchangeRateMessageDTO message) {

        // Load active watchlist rules for this coin
        List<UserCoinWatchlistEntity> watchlists =
                watchlistRepository.findByCoin_SymbolAndIsActiveTrue(message.getCoinSymbol());

        if (watchlists.isEmpty()) {
            return;
        }

        for (UserCoinWatchlistEntity watchlist : watchlists) {
            boolean isTriggered = false;

            switch (watchlist.getAlertRuleKeyword()) {
                case GREATER_THAN ->
                        isTriggered = message.getCurrentPrice().compareTo(watchlist.getTargetValue()) > 0;
                case LESS_THAN ->
                        isTriggered = message.getCurrentPrice().compareTo(watchlist.getTargetValue()) < 0;
                case EQUAL ->
                        isTriggered = message.getCurrentPrice().compareTo(watchlist.getTargetValue()) == 0;
            }

            if (isTriggered) {
                log.info("⚡ Alert triggered for user {} on coin {}: {} {} {} (current={})",
                        watchlist.getUser().getId(),
                        watchlist.getCoin().getSymbol(),
                        watchlist.getCoin().getSymbol(),
                        watchlist.getAlertRuleKeyword(),
                        watchlist.getTargetValue(),
                        message.getCurrentPrice());

                // 🔔 Send notification
                NotificationMessageDTO notification = NotificationMessageDTO.builder()
                        .userId(watchlist.getUser().getId())
                        .coinSymbol(message.getCoinSymbol())
                        .message("Alert triggered: " + message.getCoinSymbol()
                                + " is now " + message.getCurrentPrice())
                        .timestamp(System.currentTimeMillis())
                        .build();

                notificationProducer.sendNotification(notification);

                // 📧 Send email
                EmailMessageDTO email = EmailMessageDTO.builder()
                        .userId(watchlist.getUser().getId())
                        .email(watchlist.getUser().getEmail()) // assuming email exists
                        .subject("Coin Alert: " + message.getCoinSymbol())
                        .body("Your alert for " + message.getCoinSymbol()
                                + " has been triggered at price: " + message.getCurrentPrice()
                                + " (Rule: " + watchlist.getAlertRuleKeyword()
                                + " " + watchlist.getTargetValue() + ")")
                        .timestamp(System.currentTimeMillis())
                        .build();
                emailProducer.sendEmail(email);
            }
        }
    }


}
