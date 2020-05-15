package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.common.tcc.TccParticipantController;
import cn.xa.common.tcc.TccState;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/tracking")
@RequiredArgsConstructor
public class TrackingController extends TccParticipantController<TrackingDto>{
    private final TrackingService trackingService;

//    @PostMapping("/create")
//    public TrackingDto createJob(@RequestBody TrackingDto jobRequest) {
//        return trackingService.create(jobRequest);
//    }

    @Override
    public String getParticipantName() {
        return "tracking";
    }

    @Override
    public ResponseEntity executeTry(String txId, TrackingDto body) {
        body.setTxId(txId);
        body.setState(TccState.TRY);
        try{
            trackingService.create(body);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @Override
    public ResponseEntity executeCancel(String txId) {
        TrackingDto trackingDto = trackingService.findByTxId(txId);
        if (trackingDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        trackingDto.setState(TccState.CANCELED);
        trackingService.save(trackingDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId) {
        TrackingDto trackingDto = trackingService.findByTxId(txId);
        if (trackingDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        trackingDto.setState(TccState.CONFIRMED);
        trackingService.save(trackingDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
