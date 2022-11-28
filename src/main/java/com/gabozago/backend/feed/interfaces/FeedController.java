package com.gabozago.backend.feed.interfaces;

import com.gabozago.backend.entity.User;
import com.gabozago.backend.feed.interfaces.dto.FeedCardPaginationResponse;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.feed.interfaces.dto.RecentRequestParams;
import com.gabozago.backend.feed.service.FeedService;
import com.gabozago.backend.feed.service.LikeService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<FeedRequest> registerFeed(@AuthenticationPrincipal User user,
            @ModelAttribute @Valid FeedRequest request) {
        Long feedId = feedService.create(user, request);
        return ResponseEntity.created(URI.create("/feeds/" + feedId)).build();
    }

    @GetMapping(value = "/{feedId:[\\d]+}")
    public ResponseEntity<FeedResponse> findById(@AuthenticationPrincipal User user, @PathVariable Long feedId) {
        FeedResponse feedResponse = feedService.viewFeed(user, feedId);
        return new ResponseEntity<>(feedResponse, HttpStatus.OK);
    }

    @PutMapping("/{feedId:[\\d]+}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @PathVariable Long feedId,
            @ModelAttribute @Valid FeedRequest request) {
        feedService.update(user, feedId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feedId:[\\d]+}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long feedId) {
        feedService.delete(user, feedId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{feedId:[\\d]+}/like")
    public ResponseEntity<Void> addLike(@AuthenticationPrincipal User user, @PathVariable Long feedId) {
        likeService.addLike(user, feedId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{feedId:[\\d]+}/unlike")
    public ResponseEntity<Void> deleteLike(@AuthenticationPrincipal User user, @PathVariable Long feedId) {
        likeService.deleteLike(user, feedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent")
    public ResponseEntity<FeedCardPaginationResponse> recentResponse(@Valid RecentRequestParams recentRequestParams) {
        FeedCardPaginationResponse response = feedService.findRecentFeeds(
                recentRequestParams.getNextFeedId(),
                recentRequestParams.getCountPerPage());
        return ResponseEntity.ok(response);
    }

}