package kg.com.taskmanager.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.com.taskmanager.dto.PageHolder;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.dto.mapper.TaskMapper;
import kg.com.taskmanager.dto.mapper.impl.PageHolderWrapper;
import kg.com.taskmanager.enums.TaskStatus;
import kg.com.taskmanager.model.Role;
import kg.com.taskmanager.model.Task;
import kg.com.taskmanager.model.User;
import kg.com.taskmanager.producer.TaskEventProducer;
import kg.com.taskmanager.repository.TaskRepository;
import kg.com.taskmanager.service.impl.AuthorizedUserServiceImpl;
import kg.com.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private PageHolderWrapper pageHolderWrapper;

    @Mock
    private AuthorizedUserServiceImpl authorizedUserService;

    @Mock
    private TaskEventProducer taskEventProducer;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testGetTaskByIdFound() {
        Task task = new Task(1L, "Test task", "Test task", TaskStatus.NEW);
        TaskDto dto = TaskDto.builder()
                .id(1L)
                .name("Test task")
                .description("Test task")
                .taskStatus(TaskStatus.NEW)
                .build();
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(taskMapper.mapToDto(task)).thenReturn(dto);

        TaskDto result = taskService.findTaskById(1L);

        assertNotNull(result);
        assertEquals("Test task", result.getName());
    }

    @Test
    void testTaskByIdNotFound() {
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.findTaskById(1L));
    }

    @Test
    void testCreateTask() {
        TaskDto taskDto = TaskDto.builder()
                .name("New Task")
                .description("Task description")
                .taskStatus(TaskStatus.NEW)
                .build();

        Task task = new Task(1L, "New Task", "Task description", TaskStatus.NEW);
        TaskDto savedDto = TaskDto.builder()
                .id(1L)
                .name("New Task")
                .description("Task description")
                .taskStatus(TaskStatus.NEW)
                .build();

        Mockito.when(taskMapper.mapToModel(taskDto)).thenReturn(task);
        Mockito.when(taskRepository.save(task)).thenReturn(task);
        Mockito.when(taskMapper.mapToDto(task)).thenReturn(savedDto);
        Mockito.doNothing().when(taskEventProducer).publishTaskCreatedEvent(Mockito.any(TaskDto.class));

        Role role = new Role();
        role.setId(1L);
        Mockito.when(authorizedUserService.getAuthUser()).thenReturn(new User(1L, "testUser", "TestPassword", "zhanybek20065732@gmail.com", role));

        TaskDto createdTask = taskService.createTask(taskDto);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getName());
        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
    }

    @Test
    void testUpdateTaskStatus() {
        Mockito.when(taskRepository.updateTaskStatus(1L, TaskStatus.valueOf("IN_PROGRESS"))).thenReturn(1);
        taskService.updateTaskStatus(1L, "IN_PROGRESS");
        Mockito.verify(taskRepository).updateTaskStatus(1L, TaskStatus.IN_PROGRESS);
    }

    @Test
    void testUpdateTaskStatusNotFound() {
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTaskStatus(1L, "ll"));
    }

    @Test
    void deleteTaskById() {
        Task task = new Task(1L, "Test task", "Test task", TaskStatus.NEW);
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(taskMapper.mapToDto(task)).thenReturn(
                TaskDto.builder().id(1L).name("Test task").description("Test task").taskStatus(TaskStatus.NEW).build()
        );

        taskService.deleteTaskById(1L);
        Mockito.verify(taskRepository).delete(task);
    }

    @Test
    void deleteTaskByIdNotFound() {
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.deleteTaskById(1L));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task(1L, "Test task", "Test task", TaskStatus.NEW);
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDto taskDto = TaskDto.builder()
                .id(1L)
                .name("Updated task")
                .description("Updated task description")
                .taskStatus(TaskStatus.IN_PROGRESS)
                .build();

        TaskDto updatedTaskDto = TaskDto.builder()
                .id(1L)
                .name("Updated task")
                .description("Updated task description")
                .taskStatus(TaskStatus.IN_PROGRESS)
                .build();

        Mockito.doAnswer(invocation -> {
            task.setName(taskDto.getName());
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getTaskStatus());
            return null;
        }).when(taskMapper).updateModel(taskDto, task);

        Mockito.when(taskMapper.mapToDto(task)).thenReturn(updatedTaskDto);

        TaskDto result = taskService.updateTask(taskDto);

        assertNotNull(result);
        assertEquals("Updated task", result.getName());
        assertEquals("Updated task description", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getTaskStatus());
    }

    @Test
    void testFindAllTasks() {
        Task task1 = new Task(1L, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2L, "Task 2", "Description 2", TaskStatus.IN_PROGRESS);

        TaskDto dto1 = TaskDto.builder()
                .id(1L)
                .name("Task 1")
                .description("Description 1")
                .taskStatus(TaskStatus.NEW)
                .build();
        TaskDto dto2 = TaskDto.builder()
                .id(2L)
                .name("Task 2")
                .description("Description 2")
                .taskStatus(TaskStatus.IN_PROGRESS)
                .build();

        Page<Task> tasksPage = new PageImpl<>(List.of(task1, task2));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("updatedTime").descending());
        Mockito.when(taskRepository.findAll(pageRequest)).thenReturn(tasksPage);
        Mockito.when(taskMapper.mapToDto(task1)).thenReturn(dto1);
        Mockito.when(taskMapper.mapToDto(task2)).thenReturn(dto2);

        PageHolder<TaskDto> pageHolder = PageHolder.<TaskDto>builder()
                .content(List.of(dto1, dto2))
                .page(0)
                .size(10)
                .totalPages(1)
                .totalElements(2L)
                .hasNextPage(false)
                .hasPreviousPage(false)
                .build();

        Mockito.when(pageHolderWrapper.wrapPageHolder(Mockito.any(Page.class))).thenReturn(pageHolder);

        PageHolder<TaskDto> result = taskService.findAllTasks(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Task 1", result.getContent().get(0).getName());
        assertEquals("Task 2", result.getContent().get(1).getName());
        Mockito.verify(taskRepository).findAll(pageRequest);
    }
}
