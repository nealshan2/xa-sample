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
@Table(uniqueConstraints = {@UniqueConstraint(name = "tracking_tx_idx", columnNames = {"txId", "objectId", "objectClassId"})})
public class Tracking {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String detail;

    private String txId;
    @Enumerated(EnumType.STRING)
    private TccState state;

    private Long objectId;
    private Long objectClassId;
}
