package cn.xa.rating;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findByReference(String reference);
}
