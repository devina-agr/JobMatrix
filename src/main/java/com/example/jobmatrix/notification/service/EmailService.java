package com.example.jobmatrix.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    public void sendRecruiterInvitation(
            String to,
            String recruiterName,
            String companyName,
            String inviteLink
    ) {

        try {

            MimeMessage mimeMessage =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail, "JobMatrix");
            helper.setTo(to);

            helper.setSubject(
                    "Recruiter Invitation - " + companyName
            );

            String html = """
                    <html>
                    <body>

                    <h2>Recruiter Invitation</h2>

                    <p>Hello %s,</p>

                    <p>
                    You have been invited to join
                    <b>%s</b>
                    as a recruiter on JobMatrix.
                    </p>

                    <p>
                    Click the button below to accept
                    your invitation:
                    </p>

                    <a href="%s"
                       style="
                       background:#2563eb;
                       color:white;
                       padding:12px 20px;
                       text-decoration:none;
                       border-radius:6px;">
                       Accept Invitation
                    </a>

                    <br><br>

                    <p>
                    This invitation expires in
                    <b>7 days</b>.
                    </p>

                    <p>
                    Regards,<br>
                    JobMatrix Team
                    </p>

                    </body>
                    </html>
                    """
                    .formatted(
                            recruiterName,
                            companyName,
                            inviteLink
                    );

            helper.setText(
                    html,
                    true
            );

            mailSender.send(
                    mimeMessage
            );

        } catch (MessagingException e) {

            throw new RuntimeException(
                    "Failed to send email",
                    e
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email",e);
        }
    }

    public void sendEmail(
            String email,
            String subject,
            String htmlContent
    ) {

        try {

            MimeMessage mimeMessage =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail, "JobMatrix");
            helper.setTo(email);

            helper.setSubject(subject);

            helper.setText(
                    htmlContent,
                    true
            );

            mailSender.send(
                    mimeMessage
            );

        } catch (MessagingException e) {

            throw new RuntimeException(
                    "Failed to send email",
                    e
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email",e);
        }
    }
}