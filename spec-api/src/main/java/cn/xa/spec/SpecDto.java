package cn.xa.spec;

import lombok.Builder;
import lombok.Data;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Data
@Builder
public class SpecDto {
    private Long id;
    private String title;
    private Long quantity;
}
