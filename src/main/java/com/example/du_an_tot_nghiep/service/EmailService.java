package com.example.du_an_tot_nghiep.service;
import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    public void sendPaymentEmail(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.setFrom("thuylqph39306@fpt.edu.vn");  // Đảm bảo là email của bạn
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
