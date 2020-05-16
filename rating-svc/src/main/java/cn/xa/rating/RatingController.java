package cn.xa.rating;

import cn.xa.common.tcc.TccParticipantController;
import cn.xa.common.tcc.TccState;
import cn.xa.task.TaskDto;
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
@RequestMapping("/v1/rating")
@RequiredArgsConstructor
public class RatingController extends TccParticipantController<RatingDto> {

    private final RatingService ratingService;

    //    @PostMapping("/create")
//    public RatingDto createRfe(@RequestBody RatingDto ratingDto) {
//        return ratingService.create(ratingDto);
//    }

    @Override
    public String getParticipantName() {
        return "rating";
    }

    @Override
    public ResponseEntity executeTry(String txId, RatingDto body) {
        body.setTxId(txId);
        body.setState(TccState.TRY);
        try{
            ratingService.create(body);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @Override
    public ResponseEntity executeCancel(String txId) {
        RatingDto ratingDto = ratingService.findByTxId(txId);
        if (ratingDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        ratingDto.setState(TccState.CANCELED);
        ratingService.delete(ratingDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId) {
        RatingDto ratingDto = ratingService.findByTxId(txId);
        if (ratingDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        ratingDto.setState(TccState.CONFIRMED);
        ratingService.save(ratingDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
