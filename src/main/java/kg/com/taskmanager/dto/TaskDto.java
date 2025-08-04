package kg.com.taskmanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.com.taskmanager.enums.TaskStatus;
import kg.com.taskmanager.markers.CreateGroup;
import kg.com.taskmanager.markers.UpdateGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class TaskDto implements Serializable {
    @Null(message = "Id must be null when creating a task", groups = CreateGroup.class)
    @NotNull(message = "Id must not be null when updating a task", groups = UpdateGroup.class)
    private Long id;

    @NotBlank(message = "Task name must not be blank", groups = CreateGroup.class)
    @Size(max = 255, message = "Task name must be at most 255 characters", groups = CreateGroup.class)
    private String name;

    @NotBlank(message = "Description must not be blank", groups = CreateGroup.class)
    @Size(max = 1000, min = 5, message = "Description must be at most 1000 characters and at least 5 characters", groups = CreateGroup.class)
    private String description;

    @Null(message = "Task status must be null when creating a task", groups = CreateGroup.class)
    private TaskStatus taskStatus;

    @Null(message = "Task user must be null when creating a task and updating", groups = {CreateGroup.class, UpdateGroup.class})
    private UserDto userDto;
}