package com.coinflux.web.queue.email.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {
    private Long userId;
    private String email;
    private String subject;
    private String body;
    private long timestamp;
}