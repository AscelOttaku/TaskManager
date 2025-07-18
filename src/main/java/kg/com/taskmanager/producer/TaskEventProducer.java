package kg.com.taskmanager.producer;

import kg.com.taskmanager.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskEventProducer {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    public void publishTaskCreatedEvent(TaskDto taskDto) {
        applicationEventPublisher.publishEvent(taskDto);
    }
}
