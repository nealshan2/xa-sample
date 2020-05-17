package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.common.tcc.TccState;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            CollaborationDto collaborationDto = collaborationService.create(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(collaborationDto);
        }catch (DataIntegrityViolationException e){
            CollaborationDto collaborationDto = collaborationService.findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(txId,
                    body.getParentObjectId(), body.getParentObjectClassId(), body.getObjectId(), body.getObjectClassId());
            return ResponseEntity.status(HttpStatus.CREATED).body(collaborationDto);
        }
    }

    @Override
    public ResponseEntity executeCancel(String txId, Long parentObjectId, Long parentObjectClassId, Long objectId, Long objectClassId) {
        CollaborationDto collaborationDto = collaborationService.findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(txId, parentObjectId, parentObjectClassId, objectId, objectClassId);
        if (collaborationDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        collaborationDto.setState(TccState.CANCELED);
        collaborationService.save(collaborationDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId, Long parentObjectId, Long parentObjectClassId, Long objectId, Long objectClassId) {
        CollaborationDto collaborationDto = collaborationService.findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(txId, parentObjectId, parentObjectClassId, objectId, objectClassId);
        if (collaborationDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        collaborationDto.setState(TccState.CONFIRMED);
        collaborationService.save(collaborationDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
