package com.coinflux.web.notification.specifications;

import com.coinflux.web.notification.NotificationEntity;
import com.coinflux.web.notification.dtos.requests.GetAllNotificationsRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification {

    public static Specification<NotificationEntity> filterBy(GetAllNotificationsRequest request, Long userId) {
        return (root, query, cb) -> {
            // Create a list to hold individual conditions
            List<Predicate> predicates = new ArrayList<>();

            // Match user
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            // Unread only
            if (request.getUnreadOnly() != null && request.getUnreadOnly()) {
                predicates.add(cb.isNull(root.get("readAt")));
            }

            // Filter by type
            if (request.getType() != null) {
                predicates.add(cb.equal(root.get("type"), request.getType()));
            }

            // From date
            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getFromDate()));
            }

            // To date
            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getToDate()));
            }

            // Combine all conditions with AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
