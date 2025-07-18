package kg.com.taskmanager.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String EMAIL_FROM;

    @Override
    public void sendEmailTaskCreated(String toEmail, TaskDto taskDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(EMAIL_FROM);
        helper.setTo(toEmail);

        String subject = "Task Created Successfully!";
        String content = "<p>Hello,</p>"
                + "<p>Congratulations! A new task has been successfully created in the Task Manager system.</p>"
                + "<p>Here are the details:</p>"
                + "<ul>"
                + "<li><strong>Task Name:</strong> " + taskDto.getName() + "</li>"
                + "<li><strong>Status:</strong> " + taskDto.getTaskStatus() + "</li>"
                + "</ul>"
                + "<br>"
                + "<p>If you did not create this task, please ignore this message.</p>"
                + "<p>Best regards,<br/>Task Manager Team</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
