// package com.company.neurolink.services;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     public void sendVerificationEmail(String toEmail, String verificationToken) {
//         String subject = "Email Verification";
//         String verificationUrl = "https://local/api/auth/verify-email?token=" + verificationToken;
//         String message = "<p>Hi Dear</p>"
//                 + "<p>Thank you for registering. Click the link below to verify your email:</p>"
//                 + "<a href='" + verificationUrl + "'>Verify Email</a>";

//         sendEmail(toEmail, subject, message);
//     }

//     private void sendEmail(String toEmail, String subject, String content) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true);

//             helper.setFrom("noreplythefabit@gmail.com"); 
//             helper.setTo(toEmail);
//             helper.setSubject(subject);
//             helper.setText(content, true);

//             mailSender.send(message);
//             System.out.println(" Email sent successfully to " + toEmail);
//         } catch (MessagingException e) {
//             e.printStackTrace(); 
//             throw new RuntimeException(" Failed to send email: " + e.getMessage());
//         }
//     }
// }
