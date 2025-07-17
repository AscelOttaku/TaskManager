package kg.com.taskmanager.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kg.com.taskmanager.annotations.UniqueUserEmail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на регистрацию пользователя")
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "john")
    @NotBlank(message = "Blank name")
    private String name;

    @Schema(description = "Email пользователя", example = "john@example.com")
    @NotBlank(message = "Blank email")
    @UniqueUserEmail(message = "Email should be unique")
    private String email;

    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank(message = "Blank password")
    private String password;
}
