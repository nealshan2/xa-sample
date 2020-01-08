package cn.xa.specsvc;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping(path = "/v1/spec")
@RequiredArgsConstructor
public class SpecController {

//    @PostMapping("/create")
    public SpecDto createSpec(@RequestBody SpecDto specRequest) {
        return new SpecDto();
    }


}
