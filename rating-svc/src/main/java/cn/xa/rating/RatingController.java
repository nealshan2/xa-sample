package cn.xa.rating;

import lombok.RequiredArgsConstructor;
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

    @PostMapping("/create")
    public RatingDto createRfe(@RequestBody RatingDto rfeRequest) {
        return ratingService.create(rfeRequest);
    }
}
