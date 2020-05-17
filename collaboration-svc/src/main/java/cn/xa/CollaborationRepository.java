package cn.xa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface CollaborationRepository extends JpaRepository<Collaboration, Long>{
    Collaboration findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(String txId, Long parentObjectId, Long parentObjectClassId, Long objectId, Long objectClassId);
}
