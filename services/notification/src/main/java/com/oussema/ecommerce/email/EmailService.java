package com.oussema.ecommerce.email;

import com.oussema.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oussema.ecommerce.email.EmailTemplate.ORDER_CONFORMATION;
import static com.oussema.ecommerce.email.EmailTemplate.PAYMENT_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {


    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String destination,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage= mailSender.createMimeMessage();
        MimeMessageHelper messageHelper= new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
                );
        messageHelper.setFrom("contact@oussema.com");
        final String templateName= PAYMENT_CONFIRMATION.getTemplate();

        Map<String,Object> variables= new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference",orderReference);

        Context context= new Context();
        context.setVariables(variables);

        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());
        try {
            String htmlTemplate= templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info(String.format("Info - Email sent to %s",destination));
        }catch (MessagingException e){
            log.warn(String.format("Warn - Error sending email to %s",destination),e);

        }
    }

    @Async
    public void sendOrderConfirmationEmail(
            String destination,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage= mailSender.createMimeMessage();
        MimeMessageHelper messageHelper= new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_RELATED,
                UTF_8.name()
        );
        messageHelper.setFrom("contact@oussema.com");
        final String templateName= ORDER_CONFORMATION.getTemplate();

        Map<String,Object> variables= new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("totalAmount",amount);
        variables.put("orderReference",orderReference);
        variables.put("products",products);

        Context context= new Context();
        context.setVariables(variables);

        messageHelper.setSubject(ORDER_CONFORMATION.getSubject());
        try {
            String htmlTemplate= templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info(String.format("Info - Email sent to %s",destination));
        }catch (MessagingException e){
            log.warn(String.format("Warn - Error sending email to %s",destination),e);
        }
    }

}
