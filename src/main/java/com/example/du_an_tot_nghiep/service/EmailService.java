package com.example.du_an_tot_nghiep.service;
import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    public void sendPaymentEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true để hỗ trợ đính kèm

        try {
            // Đặt người nhận và chủ đề
            helper.setTo(to);
            helper.setSubject(subject);

            // Đặt nội dung email dưới dạng HTML
            helper.setText(htmlContent, true);

            // Đặt địa chỉ gửi email (đảm bảo là email của bạn)
            helper.setFrom("thuylqph39306@fpt.edu.vn");

            // Đính kèm logo vào email (Đảm bảo đường dẫn đúng)
            ClassPathResource logoFile = new ClassPathResource("static/images/images.png"); // Đảm bảo đường dẫn đúng
            helper.addInline("logo", logoFile);  // "logo" là ID tham chiếu trong nội dung HTML

            // Gửi email
            emailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
