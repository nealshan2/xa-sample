package cn.xa.rfesvc;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Data
@Entity
public class RfeItem {
    @Id
    private Long id;
    private String title;
    private Long quantity;
}
