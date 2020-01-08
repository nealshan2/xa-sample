package cn.xa.rfesvc;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rfe {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String reference;
}
