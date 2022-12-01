package com.gabozago.backend.feed.interfaces;

import com.gabozago.backend.feed.interfaces.dto.CommentRequest;
import com.gabozago.backend.feed.interfaces.dto.CommentResponse;
import com.gabozago.backend.feed.interfaces.dto.ReplyResponse;
import com.gabozago.backend.feed.service.CommentService;
import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/feeds/{feedId:[\\d]+}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal User user, @PathVariable Long feedId,
                                                         @RequestBody @Valid CommentRequest request) {
        CommentResponse response = commentService.createComment(user, feedId, request);
        return ResponseEntity.created(URI.create("/feeds/" + feedId + "/comments/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAllCommentsByFeedId(@AuthenticationPrincipal User user, @PathVariable Long feedId) {
        List<CommentResponse> responses = commentService.findAllByFeedId(feedId, user);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{commentId:[\\d]+}")
    public ResponseEntity<CommentResponse> updateComment(@AuthenticationPrincipal User user,
            @PathVariable Long feedId, @PathVariable Long commentId, @RequestBody @Valid CommentRequest request) {
        CommentResponse updatedComment = commentService.updateComment(commentId, request, user);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId:[\\d]+}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal User user,
            @PathVariable Long feedId, @PathVariable Long commentId) {
        commentService.deleteComment(user, commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId:[\\d]+}/replies")
    public ResponseEntity<CommentResponse> createReply(@AuthenticationPrincipal User user,
            @PathVariable Long feedId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest request) {
        CommentResponse replyResponse = commentService.createReply(user, feedId, commentId, request);
        return ResponseEntity
                .created(
                        URI.create("/feeds/" + feedId + "/comments/" + commentId + "/replies/" + replyResponse.getId()))
                .body(replyResponse);
    }

    @GetMapping("/{commentId:[\\d]+}/replies")
    public ResponseEntity<List<ReplyResponse>> findAllRepliesById(@AuthenticationPrincipal User user,
            @PathVariable Long feedId,
            @PathVariable Long commentId) {
        List<ReplyResponse> replyResponses = commentService.findAllRepliesById(user, feedId, commentId);
        return ResponseEntity.ok(replyResponses);
    }

}
