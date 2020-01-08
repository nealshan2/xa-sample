package cn.xa.rfesvc;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Entity
@Data
@Builder
public class Rfe {
    @Id
    private Long id;
    private String title;
    private String reference;
}
