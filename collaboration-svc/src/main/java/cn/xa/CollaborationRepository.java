package cn.xa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface CollaborationRepository extends JpaRepository<Collaboration, Long>{
    Collaboration findByTxId(String txId);
}
