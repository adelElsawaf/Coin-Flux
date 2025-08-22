package com.coinflux.web.notification.specifications;

import com.coinflux.web.notification.NotificationEntity;
import com.coinflux.web.notification.dtos.requests.GetAllNotificationsRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {

    public static Specification<NotificationEntity> filterBy(GetAllNotificationsRequest request, Long userId) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // Match user
            predicate.getExpressions().add(cb.equal(root.get("user").get("id"), userId));

            // Unread only
            if (request.getUnreadOnly() != null && request.getUnreadOnly()) {
                predicate.getExpressions().add(cb.isFalse(root.get("isRead")));
            }

            // Filter by type
            if (request.getType() != null) {
                predicate.getExpressions().add(cb.equal(root.get("type"), request.getType()));
            }

            // From date
            if (request.getFromDate() != null) {
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getFromDate()));
            }

            // To date
            if (request.getToDate() != null) {
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getToDate()));
            }

            return predicate;
        };
    }
}
