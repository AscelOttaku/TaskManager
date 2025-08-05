package kg.com.taskmanager.producer;

import kg.com.taskmanager.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventProducer {
    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendTaskCreationMessage(TaskDto taskDto) {
        if (taskDto == null) {
            throw new IllegalArgumentException("TaskDto cannot be null");
        }

        rabbitTemplate.convertAndSend(exchangeName, routingKey, taskDto);
        log.info("Task sent to the queue with id : {}", taskDto.getId());
    }
}
