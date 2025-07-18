package kg.com.taskmanager.repository;

import kg.com.taskmanager.enums.RoleEnum;
import kg.com.taskmanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.roleName = :roleEnum")
    Optional<Role> findByRoleName(RoleEnum roleEnum);
}
