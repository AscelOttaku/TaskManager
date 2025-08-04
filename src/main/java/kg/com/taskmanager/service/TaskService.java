package kg.com.taskmanager.service;

import kg.com.taskmanager.dto.PageHolder;
import kg.com.taskmanager.dto.TaskDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TaskService {
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    TaskDto createTask(TaskDto taskDto);

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    TaskDto updateTask(TaskDto taskDto);

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS)
    TaskDto deleteTaskById(Long id);

    @Transactional
    void updateTaskStatus(Long id, String taskStatus);

    PageHolder<TaskDto> findAllTasks(int page, int size);

    TaskDto findTaskById(Long id);

    PageHolder<TaskDto> getCachedTasksByPageAndSize(int page, int size);
}
