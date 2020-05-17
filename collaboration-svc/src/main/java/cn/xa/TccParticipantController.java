package cn.xa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class TccParticipantController<T> {

    public static final String TCC_MEDIA_TYPE = "application/tcc";
    public static final String TRANSACTION_ID = "txId";
    protected static final Logger LOGGER = LoggerFactory.getLogger(TccParticipantController.class);

    @PostMapping(value = "/tcc/{txId}/{parentObjectId}/{parentObjectClassId}/{objectId}/{objectClassId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity tryOperation(@PathVariable String txId,
                                       @PathVariable("parentObjectId") Long parentObjectId,
                                       @PathVariable("parentObjectClassId") Long parentObjectClassId,
                                       @PathVariable("objectId") Long objectId,
                                       @PathVariable("objectClassId") Long objectClassId,
                                       @RequestBody T body) {
        LOGGER.info("{} begin to try transaction {}", getParticipantName(), txId);
        ResponseEntity result;
        try {
            result = executeTry(txId, body);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = ResponseEntity.notFound().build();
        }
        LOGGER.info("{} finish try transaction {} ,result {}", getParticipantName(), txId, result.getStatusCode());
        return result;
    }

    @DeleteMapping(value = "/tcc/{txId}/{parentObjectId}/{parentObjectClassId}/{objectId}/{objectClassId}", consumes = TCC_MEDIA_TYPE, produces = TCC_MEDIA_TYPE)
    public ResponseEntity cancel(@PathVariable(TRANSACTION_ID) String txId,
                                 @PathVariable("parentObjectId") Long parentObjectId,
                                 @PathVariable("parentObjectClassId") Long parentObjectClassId,
                                 @PathVariable("objectId") Long objectId,
                                 @PathVariable("objectClassId") Long objectClassId) {
        LOGGER.info("{} begin to cancel transaction {}", getParticipantName(), txId);
        ResponseEntity result;
        try {
            result = executeCancel(txId, parentObjectId, parentObjectClassId, objectId, objectClassId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = ResponseEntity.notFound().build();
        }
        LOGGER.info("{} finish cancel transaction {} ,result {}", getParticipantName(), txId, result.getStatusCode());
        return result;
    }

    @PutMapping(value = "/tcc/{txId}/{parentObjectId}/{parentObjectClassId}/{objectId}/{objectClassId}", consumes = TCC_MEDIA_TYPE, produces = TCC_MEDIA_TYPE)
    public ResponseEntity confirm(@PathVariable(TRANSACTION_ID) String txId,
                                  @PathVariable("parentObjectId") Long parentObjectId,
                                  @PathVariable("parentObjectClassId") Long parentObjectClassId,
                                  @PathVariable("objectId") Long objectId,
                                  @PathVariable("objectClassId") Long objectClassId) {
        LOGGER.info("{} begin to confirm transaction {}", getParticipantName(), txId);
        ResponseEntity result;
        try {
            result = executeConfirm(txId, parentObjectId, parentObjectClassId, objectId, objectClassId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = ResponseEntity.notFound().build();
        }
        LOGGER.info("{} finish confirm transaction {} ,result {}", getParticipantName(), txId, result.getStatusCode());
        return result;
    }


    public abstract String getParticipantName();

    public abstract ResponseEntity executeTry(String txId, T body);

    public abstract ResponseEntity executeCancel(String txId, Long parentObjectId, Long parentObjectClassId, Long objectId, Long objectClassId);

    public abstract ResponseEntity executeConfirm(String txId, Long parentObjectId, Long parentObjectClassId, Long objectId, Long objectClassId);
}
