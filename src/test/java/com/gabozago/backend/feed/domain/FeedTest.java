package com.gabozago.backend.feed.domain;

import com.gabozago.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.gabozago.backend.fixture.LocationFixture.A_STATION;
import static com.gabozago.backend.fixture.UserFixture.봄봄_생성;
import static org.assertj.core.api.Assertions.assertThat;

class FeedTest {

    private Feed feed;

    private User 봄봄_2 = 봄봄_생성();

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