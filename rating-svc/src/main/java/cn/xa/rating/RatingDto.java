package cn.xa.rating;

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
public class RatingDto {
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String reference;

    @NotNull
    private String txId;

    private @NotNull TccState state;
}
