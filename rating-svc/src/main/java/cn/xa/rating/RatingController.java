package cn.xa.rating;

import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.context.OmegaContext;
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
public class RatingController {

    private final RatingService ratingService;
    private final OmegaContext omegaContext;

    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody RatingDto body) {
        body.setTxId(omegaContext.globalTxId());
        RatingDto ratingDto = ratingService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingDto);
    }


}
