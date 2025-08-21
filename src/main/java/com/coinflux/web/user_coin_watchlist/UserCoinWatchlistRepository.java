package com.coinflux.web.user_coin_watchlist;


import com.coinflux.web.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserCoinWatchlistRepository extends JpaRepository<UserCoinWatchlistEntity, Long>,
        JpaSpecificationExecutor<UserCoinWatchlistEntity> {
    List<UserCoinWatchlistEntity> findByUser(UserEntity user);

    Optional<UserCoinWatchlistEntity> findByUserIdAndCoinSymbol(Long userId, String coinSymbol);

    Optional<UserCoinWatchlistEntity> findByIdAndUserId(Long id, Long userId);
    List<UserCoinWatchlistEntity> findByCoin_SymbolAndIsActiveTrue(String coinSymbol);
}

