package kg.com.taskmanager.consumer;

import jakarta.mail.MessagingException;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name}"})
    public void handleTaskCreatedEvent(TaskDto taskDto) {
        try {
            emailService.sendEmailTaskCreated(taskDto.getUserDto().getEmail(), taskDto);
        } catch (MessagingException e) {
            log.error("Error sending email for task creation: {}", e.getMessage());
        }
    }
}
