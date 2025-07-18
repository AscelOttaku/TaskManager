package kg.com.taskmanager.dto.mapper;

import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.model.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task mapToModel(TaskDto taskDto);
    TaskDto mapToDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(Task task, @MappingTarget TaskDto taskDto);
}
