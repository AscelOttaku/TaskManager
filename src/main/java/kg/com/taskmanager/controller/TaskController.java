package kg.com.taskmanager.controller;

import jakarta.validation.groups.Default;
import kg.com.taskmanager.dto.PageHolder;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.markers.CreateGroup;
import kg.com.taskmanager.markers.UpdateGroup;
import kg.com.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public TaskDto createTask(
            @Validated({CreateGroup.class, Default.class}) @RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PutMapping
    public TaskDto updateTask(
            @Validated({UpdateGroup.class, Default.class}) @RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskDto);
    }

    @DeleteMapping("/{id}")
    public TaskDto deleteTaskById(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }

    @PatchMapping("/{id}/status")
    public void updateTaskStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        taskService.updateTaskStatus(id, status);
    }

    @GetMapping
    public PageHolder<TaskDto> findAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return taskService.findAllTasks(page, size);
    }

    @GetMapping("/{id}")
    public TaskDto findTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }
}
