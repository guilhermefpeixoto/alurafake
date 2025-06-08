package br.com.alura.AluraFake.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.AluraFake.models.Task;
import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCourseId(Long courseId);

    List<Task> findByCourseIdOrderByOrderAsc(Long courseId);
}
