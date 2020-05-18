package cn.xa.tracking;

import cn.xa.common.tcc.TccState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDto {
    private Long id;
    private String title;
    private String detail;

    @NotNull
    private String txId;

    @NotNull
    private TccState state;

    @NotNull
    private Long objectId;
    @NotNull
    private Long objectClassId;

    public String uniqueCode() {
        return txId + "-" + objectId + "-" + objectClassId;
    }

}
