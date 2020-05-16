package cn.xa.rating;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.CollaborationType;
import cn.xa.common.exception.ServiceException;
import cn.xa.common.tcc.TccConfig;
import cn.xa.common.tcc.TccCoordinatorClient;
import cn.xa.common.tcc.TccParticipantController;
import cn.xa.common.tcc.TccState;
import cn.xa.task.TaskDto;
import cn.xa.tracking.TrackingDto;
import com.atomikos.tcc.rest.ParticipantLink;
import com.atomikos.tcc.rest.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/client/rating")
@RequiredArgsConstructor
public class RatingResource {

    private final RestTemplate restTemplate;
    private final TccCoordinatorClient tccCoordinatorClient;

    @PostMapping("/create")
    public ResponseEntity createRfe(@RequestBody CreateRatingRequest createRatingRequest) {
        String txId = createRatingRequest.getTxId();
        long expireTime = System.currentTimeMillis() + TccConfig.TRANSACTION_TIMEOUT_MS;

        List<ParticipantLink> participantLinks = new ArrayList<>(2);
        String ratingServiceUrl = String.format(TccConfig.RATING_TCC_URL, txId);
        participantLinks.add(new ParticipantLink(ratingServiceUrl, expireTime));

        Transaction transaction = new Transaction(participantLinks);
        try {

            //1. try
            RatingDto ratingDto = RatingDto.builder()
                    .txId(txId)
                    .state(TccState.TRY)
                    .title(createRatingRequest.getTitle())
                    .reference(createRatingRequest.getReference())
                    .build();
            restTemplate.postForEntity(ratingServiceUrl, ratingDto, String.class);


            //2. call coordinator to confirm
            tccCoordinatorClient.confirm(transaction);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            //3. call coordinator to cancel
            tccCoordinatorClient.cancel(transaction);
            String msg = e instanceof HttpStatusCodeException ? ((HttpStatusCodeException) e).getResponseBodyAsString() : e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }


}
