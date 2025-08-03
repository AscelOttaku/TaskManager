package kg.com.taskmanager.service.impl;

import kg.com.taskmanager.dto.PageHolder;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.dto.mapper.TaskMapper;
import kg.com.taskmanager.dto.mapper.impl.PageHolderWrapper;
import kg.com.taskmanager.enums.TaskStatus;
import kg.com.taskmanager.model.Task;
import kg.com.taskmanager.producer.TaskEventProducer;
import kg.com.taskmanager.repository.TaskRepository;
import kg.com.taskmanager.service.AuthorizedUserService;
import kg.com.taskmanager.service.TaskService;
import kg.com.taskmanager.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final AuthorizedUserService authorizedUserService;
    private final TaskEventProducer taskEventProducer;
    private final CacheManager cacheManager;

    @CacheEvict(value = "tasks", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.mapToModel(taskDto);
        task.setUser(authorizedUserService.getAuthUser());
        TaskDto result = taskMapper.mapToDto(taskRepository.save(task));
        log.info("Task created with id: {}", result.getId());
        taskEventProducer.publishTaskCreatedEvent(result);
        return result;
    }

    @CacheEvict(value = "tasks", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        log.info("Updating task with id: {}", taskDto.getId());
        Task task = taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", taskDto.getId());
                    return new NoSuchElementException("Task not found with id: " + taskDto.getId());
                });

        taskMapper.updateModel(task, taskDto);
        log.info("Updating task with id: {}", taskDto.getId());
        return taskMapper.mapToDto(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS)
    @Override
    public TaskDto deleteTaskById(Long id) {
        log.info("Deleting task with id: {}", id);
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
        taskRepository.delete(task);
        log.info("Task deleted: {}", id);
        return taskMapper.mapToDto(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    @Transactional
    @Override
    public void updateTaskStatus(Long id, String taskStatus) {
        log.info("Updating status for task id: {} to {}", id, taskStatus);
        String taskStatusUpper = taskStatus.toUpperCase();
        if (!Util.isValidEnumValue(TaskStatus.class, taskStatus)) {
            throw new IllegalArgumentException("Invalid task status: " + taskStatus);
        }

        TaskStatus status = TaskStatus.valueOf(taskStatusUpper);
        int updated = taskRepository.updateTaskStatus(id, status);
        if (updated == 0) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
        log.info("Task status updated for id: {}", id);
    }

    @Cacheable(value = "tasks", key = "#page + '-' + #size")
    @Override
    public PageHolder<TaskDto> findAllTasks(int page, int size) {
        log.info("Finding all tasks, page: {}, size: {}", page, size);
        var pageable = PageRequest.of(page, size, Sort.by("updatedTime").descending());
        Page<TaskDto> taskDtos = taskRepository.findAll(pageable)
                .map(taskMapper::mapToDto);
        return pageHolderWrapper.wrapPageHolder(taskDtos);
    }

    @Override
    public TaskDto findTaskById(Long id) {
        log.info("Finding task by id: {}", id);
        return taskRepository.findById(id)
                .map(taskMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", id);
                    return new NoSuchElementException("Task not found with id: " + id);
                });
    }

    @Override
    public Object getCacheData() {
        var cacheData = cacheManager.getCache("tasks");
        if  (cacheData != null) {
            return cacheData.getNativeCache();
        } else {
            log.warn("Cache 'tasks' not found");
            return "Cache 'tasks' not found";
        }
    }
}
