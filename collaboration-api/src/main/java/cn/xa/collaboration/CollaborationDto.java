package cn.xa.collaboration;

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
public class CollaborationDto {
    private Long id;
    @NotNull
    private String txId;
    @NotNull
    private Long parentObjectId;
    @NotNull
    private Long parentObjectClassId;
    @NotNull
    private Long objectId;
    @NotNull
    private Long objectClassId;
    @NotNull
    private TccState state;

    public String uniqueCode() {
        return txId + "-" + parentObjectId + "-" + parentObjectClassId + "-" + objectId + "-" + objectClassId;
    }
}
