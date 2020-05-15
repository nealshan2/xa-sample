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
public class Tracking {
    @Id
    @GeneratedValue
    private Long id;
    private String txId;
    private String title;
    private String detail;
    @Enumerated(EnumType.STRING)
    private TccState state;
}
