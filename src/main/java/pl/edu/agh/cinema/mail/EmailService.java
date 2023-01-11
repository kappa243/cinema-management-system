package pl.edu.agh.cinema.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(ArrayList<String> receivers, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cinemamanagement2022@gmail.com");

        message.setSubject(subject);
        message.setText(text);

        for (String receiver: receivers) {
            message.setTo(receiver);
            mailSender.send(message);
        }


    }
    public void sendSimpleMessage(SimpleMailMessage message) {
        mailSender.send(message);
    }
    public void sendMessageWithAttachment(
            ArrayList<String> receivers, String subject, String text, ArrayList<File> files) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("cinemamanagement2022@gmail.com");

        helper.setSubject(subject);
        helper.setText(text);

        for (File file: files) {
            helper.addAttachment(file.getPath(), file);
        }

        for (String receiver: receivers) {
            helper.setTo(receiver);
            mailSender.send(message);
        }
    }


    @Bean
    public SimpleMailMessage recommendedShowsSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cinemamanagement2022@gmail.com");
        message.setSubject("Recommended Shows");
        message.setText(
                "Hi, recommended shows are:\n");
        return message;
    }
}
