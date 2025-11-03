package com.coinflux.web.mail;

import com.coinflux.web.mail.dtos.requests.MailSendRequest;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/emails")
public class MailController {
    private final MailService mailService;
    @PostMapping("/send")
    public String sendEmail(@RequestBody MailSendRequest request) throws MessagingException {
        mailService.createAndSendMail(request);
        return "Mail sent successfully";
    }

}