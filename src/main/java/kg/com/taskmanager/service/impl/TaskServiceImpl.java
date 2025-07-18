package kg.com.taskmanager.service.impl;

import kg.com.taskmanager.dto.PageHolder;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.dto.mapper.TaskMapper;
import kg.com.taskmanager.dto.mapper.impl.PageHolderWrapper;
import kg.com.taskmanager.enums.TaskStatus;
import kg.com.taskmanager.model.Task;
import kg.com.taskmanager.repository.TaskRepository;
import kg.com.taskmanager.service.TaskService;
import kg.com.taskmanager.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final PageHolderWrapper pageHolderWrapper;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.mapToModel(taskDto);
        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        Task task = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + taskDto.getId()));

        taskMapper.updateModel(task, taskDto);
        return taskMapper.mapToDto(task);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS)
    @Override
    public TaskDto deleteTaskById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
        taskRepository.delete(task);
        return taskMapper.mapToDto(task);
    }

    @Transactional
    @Override
    public void updateTaskStatus(Long id, String taskStatus) {
        String taskStatusUpper = taskStatus.toUpperCase();
        if (!Util.isValidEnumValue(TaskStatus.class, taskStatus)) {
            throw new IllegalArgumentException("Invalid task status: " + taskStatus);
        }

        TaskStatus status = TaskStatus.valueOf(taskStatusUpper);
        int updated = taskRepository.updateTaskStatus(id, status);
        if (updated == 0) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }

    @Override
    public PageHolder<TaskDto> findAllTasks(int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("updatedTime").descending());
        Page<TaskDto> taskDtos = taskRepository.findAll(pageable)
                .map(taskMapper::mapToDto);
        return pageHolderWrapper.wrapPageHolder(taskDtos);
    }

    @Override
    public TaskDto findTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }
}
