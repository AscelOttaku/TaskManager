package kg.com.taskmanager.dto.mapper;

import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TaskMapper {

    @Mapping(target = "user", source = "userDto")
    @Mapping(target = "status", source = "taskStatus")
    Task mapToModel(TaskDto taskDto);
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "userDto", source = "user")
    TaskDto mapToDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "taskStatus", source = "status")
    void updateModel(Task task, @MappingTarget TaskDto taskDto);
}
