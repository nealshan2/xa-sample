package cn.xa.rfesvc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfeDto {
    private Long id;
    private String title;
    @NotNull
    private String reference;
    private List<RfeItemDto> rfeItems;

}
