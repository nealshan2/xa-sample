package cn.xa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface TrackingRepository extends JpaRepository<Tracking, Long>{
    Tracking findByTxIdAndObjectIdAndObjectClassId(String txId, Long objectId, Long objectClassId);
}
