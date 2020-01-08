package cn.xa.rfesvc;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface RfeRepository extends JpaRepository<Rfe, Long> {

    Rfe findByReference(String reference);
}
