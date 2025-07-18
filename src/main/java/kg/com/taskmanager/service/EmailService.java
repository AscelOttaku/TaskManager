package kg.com.taskmanager.service;

import jakarta.mail.MessagingException;
import kg.com.taskmanager.dto.TaskDto;

public interface EmailService {
    void sendEmailTaskCreated(String toEmail, TaskDto taskDto) throws MessagingException;
}
