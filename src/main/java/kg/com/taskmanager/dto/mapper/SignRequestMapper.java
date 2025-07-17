package kg.com.taskmanager.dto.mapper;

import kg.com.taskmanager.dto.auth.SignUpRequest;
import kg.com.taskmanager.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignRequestMapper {
    User mapToModel(SignUpRequest signInRequest);
}
