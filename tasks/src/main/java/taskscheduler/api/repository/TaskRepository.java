package taskscheduler.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import taskscheduler.api.domain.task.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("FROM Task AS t " +
            "WHERE t.userId = :userId " +
            "AND (LOWER(t.description) LIKE %:searchTerm% " +
            "OR LOWER(t.title) LIKE %:searchTerm% " +
            "OR LOWER(t.priority) LIKE %:searchTerm% " +
            "OR LOWER(t.status) LIKE %:searchTerm%)")
    Page<Task> findAllByUserIdFiltered(@Param("userId") UUID userId, Pageable pageable,
            @Param("searchTerm") String searchTerm);

    Page<Task> findAllByUserId(UUID userId, Pageable pageable);
}