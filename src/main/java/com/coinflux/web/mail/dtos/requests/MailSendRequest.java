package com.coinflux.web.mail.dtos.requests;

import com.coinflux.web.mail.enums.MailType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailSendRequest {
    private String toAddress;
    private String subject;
    private String body;
    private MailType type; // e.g. ACTIVATION, PASSWORD_RESET, etc.
}