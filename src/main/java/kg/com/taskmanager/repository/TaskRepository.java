package kg.com.taskmanager.repository;

import kg.com.taskmanager.enums.TaskStatus;
import kg.com.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("update Task t set t.status = :taskStatus where t.id = :id")
    int updateTaskStatus(Long id, TaskStatus taskStatus);
}
