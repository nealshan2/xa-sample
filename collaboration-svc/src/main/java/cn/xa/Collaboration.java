package cn.xa;

import cn.xa.common.tcc.TccState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "collaboration_tx_idx", columnNames = {"txId"})})
public class Collaboration {
    @Id
    @GeneratedValue
    private Long id;
    private String txId;
    private Long parentId;
    private Long childId;
    private String type;
    @Enumerated(EnumType.STRING)
    private TccState state;
}
