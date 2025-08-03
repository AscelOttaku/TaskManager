package kg.com.taskmanager.controller;

import jakarta.validation.groups.Default;
import kg.com.taskmanager.dto.PageHolder;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.markers.CreateGroup;
import kg.com.taskmanager.markers.UpdateGroup;
import kg.com.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskDto createTask(
            @Validated({CreateGroup.class, Default.class}) @RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(
            @Validated({UpdateGroup.class, Default.class}) @RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto deleteTaskById(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateTaskStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        taskService.updateTaskStatus(id, status);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<TaskDto> findAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return taskService.findAllTasks(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto findTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }

    @GetMapping("cache/data")
    @ResponseStatus(HttpStatus.OK)
    public Object getCacheData() {
        return taskService.getCacheData();
    }
}
