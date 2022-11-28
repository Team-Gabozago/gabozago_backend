package com.gabozago.backend.feed.domain;

import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.feed.service.FeedService;

import com.gabozago.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gabozago.backend.fixture.LocationFixture.A_STATION;
import static com.gabozago.backend.fixture.UserFixture.봄봄_생성;
import static org.assertj.core.api.Assertions.assertThat;

class FeedTest {

    private Feed feed;

    private User 봄봄_2 = 봄봄_생성();

    // private FeedRequest FEED_REQUEST1 = new FeedRequest(1, "title1", "content1",
    // 123.123, 456.456,
    // new ArrayList<>());
    // private FeedRequest FEED_REQUEST2 = new FeedRequest("title2", new
    // ArrayList<>(), "content2", "PROGRESS", true,
    // "www.github.com/woowacourse", "www.github.com/woowacourse", null);
    // private FeedRequest FEED_REQUEST3 = new FeedRequest("title3", new
    // ArrayList<>(), "content3", "PROGRESS", true,
    // "www.github.com/woowacourse", "www.github.com/woowacourse", null);

    @BeforeEach
    void setUp() {
        feed = Feed.builder()
                .category(new Category())
                .title("테니스하실분!!!")
                .content("주말에 테니스 하실 분찾아요!!")
                .location(A_STATION)
                .build();
    }

    @Test
    void writtenBy() {
        // given
        User 봄봄 = User.builder().email("rosejap97@gmail.com").username("user").password("password").build();

        // when
        Feed feed = this.feed.writtenBy(봄봄_2);

        // then
        assertThat(feed.getAuthor()).isEqualTo(봄봄_2);
    }

    // @DisplayName("Feed를 수정한다. (storageUrl, deployUrl, thumbnailUrl을 제외한 나머지만
    // 수정)")
    // @Test
    // void updateNewTechs() {
    // // given
    // Long feedId1 = FeedService.create(봄봄_2, FEED_REQUEST1);
    // FeedRequest request = new FeedRequest(
    // 1,
    // "수정된 제목",
    // "수정된 내용",
    // 12.345,
    // 45.2233,
    // Collections.emptyList());

    // // when
    // FeedService.update(봄봄_2, feedId1, request);
    // em.flush();
    // em.clear();

    // // then
    // FeedResponse updateFeed = FeedService.findById(봄봄_2, feedId1);
    // 피드_정보가_같은지_조회(request, updateFeed);
    // }

    @Test
    void sortByCommentAndReplies() {
        User 봄봄 = 봄봄_생성();

        Comment 댓글1 = new Comment(1L, "댓글1").writtenBy(봄봄, feed);
        Comment 댓글1_답글1 = new Comment(2L, "댓글1_답글1").writtenBy(봄봄, feed);
        Comment 댓글1_답글2 = new Comment(3L, "댓글1_답글2").writtenBy(봄봄, feed);
        댓글1.addChildComment(댓글1_답글1);
        댓글1.addChildComment(댓글1_답글2);

        Comment 댓글2 = new Comment(4L, "댓글2").writtenBy(봄봄, feed);
        Comment 댓글2_답글1 = new Comment(5L, "댓글2_답글1").writtenBy(봄봄, feed);
        댓글2.addChildComment(댓글2_답글1);

        Map<Comment, List<Comment>> commentAndReplies = feed.mapByCommentAndReplies();
        assertThat(commentAndReplies).containsKeys(댓글1, 댓글2);
        assertThat(commentAndReplies.get(댓글1)).containsExactly(댓글1_답글1, 댓글1_답글2);
        assertThat(commentAndReplies.get(댓글2)).containsExactly(댓글2_답글1);
    }

}