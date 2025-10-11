package com.coinflux.web.mail;

import com.coinflux.web.mail.enums.MailStatus;
import com.coinflux.web.mail.enums.MailType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toAddress;

    private String subject;

    @Column(length = 10000)
    private String body;

    @CreationTimestamp
    private LocalDateTime createdAT;

    @Enumerated(EnumType.STRING)
    private MailStatus status;

    @Enumerated(EnumType.STRING)
    private MailType type;
}