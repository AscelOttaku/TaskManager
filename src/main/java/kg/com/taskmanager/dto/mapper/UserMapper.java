package kg.com.taskmanager.dto.mapper;

import kg.com.taskmanager.dto.UserDto;
import kg.com.taskmanager.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
