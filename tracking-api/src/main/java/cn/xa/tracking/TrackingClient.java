package cn.xa.tracking;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@FeignClient(name = "trackingClient", path = "/v1/tracking", url = "http://localhost:8087")
public interface TrackingClient {

    @PostMapping("/create")
    TrackingDto createTracking(@RequestBody TrackingDto trackingDto);

}
