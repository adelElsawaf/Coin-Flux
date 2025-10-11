package com.coinflux.web.mail.mappers;

import com.coinflux.web.mail.MailEntity;
import com.coinflux.web.mail.dtos.requests.MailSendRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MailMapper {

    MailMapper INSTANCE = Mappers.getMapper(MailMapper.class);

    MailEntity toEntity(MailSendRequest dto);
}
