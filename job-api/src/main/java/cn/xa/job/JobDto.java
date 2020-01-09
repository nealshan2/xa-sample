package cn.xa.job;

import lombok.Builder;
import lombok.Data;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Data
@Builder
public class JobDto {
    private Long id;
    private String title;
    private String detail;
}
