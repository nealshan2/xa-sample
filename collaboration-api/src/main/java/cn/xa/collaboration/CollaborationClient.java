package cn.xa.collaboration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@FeignClient(name = "collaborationClient", path = "/v1/collaboration", url = "http://localhost:8088")
public interface CollaborationClient {

    @PostMapping("/create")
    CollaborationDto createCollaboration(@RequestBody CollaborationDto jobRequest);

}
