package kg.com.taskmanager.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageHolder<T> implements Serializable {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
}
