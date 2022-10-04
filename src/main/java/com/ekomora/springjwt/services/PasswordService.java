package com.ekomora.springjwt.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.Random;

public class PasswordService {
    private static final String[] charCategories = new String[]{
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789"
    };

    private static String generate(int length) {
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < length; i++) {
            String charCategory = charCategories[random.nextInt(charCategories.length)];
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }

        return new String(password);
    }

    private static String getHostFromEmail(String email) {
        String host = "";
        for (int i = email.length() - 1; i >= 0; --i) {
            if (email.charAt(i) != '@')
                host = email.charAt(i) + host;
            else break;
        }
        return host;
    }

    private static void mailSenderSettings(String email, JavaMailSenderImpl mailSender,
                                           SimpleMailMessage message, Properties props) {
        mailSender.setHost("smtp." + getHostFromEmail(email));
        mailSender.setPort(25);
        mailSender.setUsername("vmminigames@gmail.com");
        mailSender.setPassword("tfsseetwdoxikuof");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        message.setTo(email);
        message.setSubject("E-Komora");
    }

    public static String generateAndSendToMail(String email, String firstName) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        SimpleMailMessage message = new SimpleMailMessage();
        Properties props = mailSender.getJavaMailProperties();
        String tempPassword = generate(10);
        mailSenderSettings(email, mailSender, message, props);
        message.setText("Вітаємо " + firstName +
                "! Ваш тимчасовий пароль: " + tempPassword +
                ". Змініти його можна профілі.");
        mailSender.send(message);
        return tempPassword;
    }

    public static void sendToMail(String email, String password, String firstName) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        SimpleMailMessage message = new SimpleMailMessage();
        Properties props = mailSender.getJavaMailProperties();
        mailSenderSettings(email, mailSender, message, props);
        message.setText("Вітаємо " + firstName +
                "! Ваш новий пароль: " + password + ".");
        mailSender.send(message);
    }

}
