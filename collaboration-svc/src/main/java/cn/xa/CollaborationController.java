package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.common.tcc.TccParticipantController;
import cn.xa.common.tcc.TccState;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/collaboration")
@RequiredArgsConstructor
public class CollaborationController extends TccParticipantController<CollaborationDto> {
    private final CollaborationService collaborationService;

//    @PostMapping("/create")
//    public CollaborationDto createCollaboration(
//            @RequestBody @Valid CollaborationDto collaborationDto) {
//        return collaborationService.create(collaborationDto);
//    }

    @Override
    public String getParticipantName() {
        return "collaboration";
    }

    @Override
    public ResponseEntity executeTry(String txId, CollaborationDto body) {
        body.setTxId(txId);
        body.setState(TccState.TRY);
        try{
            collaborationService.create(body);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @Override
    public ResponseEntity executeCancel(String txId) {
        CollaborationDto collaborationDto = collaborationService.findByTxId(txId);
        if (collaborationDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        collaborationDto.setState(TccState.CANCELED);
        collaborationService.save(collaborationDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId) {
        CollaborationDto collaborationDto = collaborationService.findByTxId(txId);
        if (collaborationDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        collaborationDto.setState(TccState.CONFIRMED);
        collaborationService.save(collaborationDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
