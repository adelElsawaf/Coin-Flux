package com.coinflux.web.user_coin_watchlist.specifications;

import com.coinflux.web.user_coin_watchlist.UserCoinWatchlistEntity;
import com.coinflux.web.user_coin_watchlist.dtos.requests.GetAllUserCoinWatchlistRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserCoinWatchlistSpecification {

    public static Specification<UserCoinWatchlistEntity> filterBy(GetAllUserCoinWatchlistRequest request, Long userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user").get("id"), userId));
            if (request.getCoinSymbol() != null && !request.getCoinSymbol().isEmpty()) {
                predicates.add(cb.equal(root.get("coin").get("symbol"), request.getCoinSymbol()));
            }

            if (request.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), request.getIsActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
