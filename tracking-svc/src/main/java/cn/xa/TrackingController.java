package cn.xa;

import cn.xa.common.tcc.TccState;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.context.OmegaContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;
    private final OmegaContext omegaContext;

    @PostMapping(value = "/tcc/{txId}/{objectId}/{objectClassId}")
    public ResponseEntity save(TrackingDto body) {
        body.setTxId(omegaContext.globalTxId());
        TrackingDto trackingDto = trackingService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(trackingDto);
    }

}
