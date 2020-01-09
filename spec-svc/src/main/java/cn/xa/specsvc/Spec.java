package cn.xa.specsvc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Spec {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Long quantity;
}
