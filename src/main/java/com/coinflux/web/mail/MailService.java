package com.coinflux.web.mail;

import com.coinflux.web.mail.dtos.requests.MailSendRequest;
import com.coinflux.web.mail.enums.MailStatus;
import com.coinflux.web.mail.enums.MailType;
import com.coinflux.web.mail.mappers.MailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    private final MailMapper mailMapper;
    private final TemplateEngine templateEngine;


    /**
     * Sends a plain or HTML mail.
     */
    public void sendMail(MailSendRequest request) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.getToAddress());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true);
            mailSender.send(message);

            log.info("✅ Email sent successfully to {}", request.getToAddress());
        } catch (MessagingException e) {
            log.error("❌ Failed to send email to {}", request.getToAddress(), e);
            throw new RuntimeException("Failed to send email to " + request.getToAddress(), e);
        }
    }

    /**
     * Saves mail record in DB.
     */
    private MailEntity createMail(MailSendRequest request, MailStatus status) {
        MailEntity entity = mailMapper.toEntity(request);
        entity.setStatus(status);
        return mailRepository.save(entity);
    }

    /**
     * Sends mail and saves status in DB.
     */
    public void createAndSendMail(MailSendRequest request) {
        MailStatus status;
        try {
            sendMail(request);
            status = MailStatus.SUCCESS;
        } catch (Exception e) {
            status = MailStatus.FAILED;
            log.error("Error sending mail: {}", e.getMessage());
        }
        createMail(request, status);
    }

    /**
     * Sends a Thymeleaf-based email and logs it.
     */
    public void createAndSendTemplateEmail(
            String toAddress,
            String subject,
            String templateName,
            Map<String, Object> variables,
            MailType type
    ) {

        try {
            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Template email '{}' sent to {}", templateName, toAddress);

            MailSendRequest request = new MailSendRequest(toAddress, subject, htmlContent, type);
            createMail(request, MailStatus.SUCCESS);

        } catch (Exception e) {
            log.error(" Failed to send template email '{}' to {}: {}", templateName, toAddress, e.getMessage());
            MailSendRequest request = new MailSendRequest(toAddress, subject, null, type);
            createMail(request, MailStatus.FAILED);
        }
    }
}
