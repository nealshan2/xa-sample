package cn.xa.rfesvc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/rfe")
@RequiredArgsConstructor
public class RfeController {

    private final RfeService rfeService;

    @PostMapping("/create")
    public RfeDto createRfe(@RequestBody RfeDto rfeRequest) {
        return rfeService.create(rfeRequest);
    }
}
