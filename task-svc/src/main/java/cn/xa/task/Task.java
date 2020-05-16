package cn.xa.task;

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
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints = {@UniqueConstraint(name = "task_tx_idx", columnNames = {"txId"})})
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String title;

    private String txId;

    @Enumerated(EnumType.STRING)
    private TccState state;
}
