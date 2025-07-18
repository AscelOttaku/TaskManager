package kg.com.taskmanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@Builder
public class ObjectDto {
    private Long id;
    private String name;
    private Map<String, Object> data;
}
