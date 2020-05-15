package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import lombok.RequiredArgsConstructor;
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
public class CollaborationController {
    private final CollaborationService collaborationService;

    @PostMapping("/create")
    public CollaborationDto createCollaboration(
            @RequestBody @Valid CollaborationDto collaborationDto) {
        return collaborationService.create(collaborationDto);
    }
}
