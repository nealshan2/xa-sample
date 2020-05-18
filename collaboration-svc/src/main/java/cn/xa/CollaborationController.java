package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.common.tcc.TccState;
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
@RequestMapping("/v1/collaboration")
@RequiredArgsConstructor
public class CollaborationController {
    private final CollaborationService collaborationService;
    private final OmegaContext omegaContext;

    @PostMapping(value = "/tcc/{txId}/{parentObjectId}/{parentObjectClassId}/{objectId}/{objectClassId}")
    public ResponseEntity save(CollaborationDto body) {
        body.setTxId(omegaContext.globalTxId());
        body.setState(TccState.CONFIRMED);
        CollaborationDto collaborationDto = collaborationService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(collaborationDto);
    }

}
