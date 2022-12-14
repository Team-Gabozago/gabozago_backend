package com.gabozago.backend.feed.interfaces;

import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.interfaces.dto.FeedCardPaginationResponse;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.feed.interfaces.dto.RecentRequestParams;
import com.gabozago.backend.feed.service.CategoryService;
import com.gabozago.backend.feed.service.FeedService;
import com.gabozago.backend.feed.service.LikeService;
import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    private final LikeService likeService;

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<FeedRequest> registerFeed(@AuthenticationPrincipal User user,
            @RequestBody @Valid FeedRequest request) {
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
            @RequestBody @Valid FeedRequest request) {
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
    public ResponseEntity<FeedCardPaginationResponse> retrieveAllFeed(@AuthenticationPrincipal User user,
            @Valid RecentRequestParams recentRequestParams) {

        FeedCardPaginationResponse response = feedService.findRecentFeeds(
                user,
                recentRequestParams.getCategories(),
                recentRequestParams.getKeyword(),
                recentRequestParams.getNextFeedId(),
                recentRequestParams.getCountPerPage(),
                recentRequestParams.getSortType());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> retrieveAllCategory() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

}
