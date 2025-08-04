package kg.com.taskmanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String email;
}