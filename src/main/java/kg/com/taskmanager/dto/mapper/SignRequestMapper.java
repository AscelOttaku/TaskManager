package kg.com.taskmanager.dto.mapper;

import kg.com.taskmanager.dto.auth.SignUpRequest;
import kg.com.taskmanager.model.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class SignRequestMapper {
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract User mapToModel(SignUpRequest signInRequest);

    @Named("encodePassword")
    protected String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
