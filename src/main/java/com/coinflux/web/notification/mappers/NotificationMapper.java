package com.coinflux.web.notification.mappers;

import com.coinflux.web.notification.NotificationEntity;
import com.coinflux.web.notification.dtos.NotificationDTO;
import com.coinflux.web.notification.dtos.responses.CreateNotificationResponse;
import com.coinflux.web.notification.dtos.responses.GetNotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "user.id", target = "userId")
    NotificationDTO toDTO(NotificationEntity entity);

    @Mapping(source = "user.id", target = "userId")
    GetNotificationResponse toGetResponse(NotificationEntity entity);

    @Mapping(source = "user.id", target = "userId")
    CreateNotificationResponse toCreateResponse(NotificationEntity entity);
}
