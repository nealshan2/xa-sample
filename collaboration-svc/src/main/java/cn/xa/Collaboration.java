package cn.xa;

import cn.xa.common.tcc.TccState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "collaboration_tx_idx", columnNames = {"txId", "parentObjectId", "parentObjectClassId", "objectId", "objectClassId"})})
public class Collaboration {
    @Id
    @GeneratedValue
    private Long id;
    private Long parentObjectId;
    private Long parentObjectClassId;
    private Long objectId;
    private Long objectClassId;

    private String txId;
    @Enumerated(EnumType.STRING)
    private TccState state;
}
