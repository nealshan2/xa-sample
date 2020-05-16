package cn.xa.rating;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(name = "rating_tx_idx", columnNames = {"txId"})})
public class Rating {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String reference;

    private String txId;

    @Enumerated(EnumType.STRING)
    private TccState state;
}
