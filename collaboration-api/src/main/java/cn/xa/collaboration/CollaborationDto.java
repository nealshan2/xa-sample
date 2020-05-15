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
    private Long parentId;
    @NotNull
    private Long childId;
    @NotNull
    private String type;
    @NotNull
    private TccState state;
}
