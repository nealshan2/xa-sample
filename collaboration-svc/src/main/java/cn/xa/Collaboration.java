package cn.xa;

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
@AllArgsConstructor
@NoArgsConstructor
public class Collaboration {
    @Id
    @GeneratedValue
    private Long id;
    private Long parentId;
    private Long childId;
    private String type;
}
