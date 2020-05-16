package cn.xa.task;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface TaskRepository extends JpaRepository<Task, Long>{
    Task findByTxId(String txId);
}
