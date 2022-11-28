package com.gabozago.backend.feed.service;

import com.gabozago.backend.common.exception.EntityNotFoundException;
import com.gabozago.backend.common.exception.UnauthorizedException;
import com.gabozago.backend.common.response.ErrorCode;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.CategoryRepository;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;

import static com.gabozago.backend.fixture.CategoryFixture.축구_생성;
import static com.gabozago.backend.fixture.CategoryFixture.테니스_생성;
import static com.gabozago.backend.fixture.UserFixture.봄봄_생성;
import static com.gabozago.backend.fixture.UserFixture.신디_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class FeedServiceTest {

    private FeedRequest 빈_FEED_REQUEST = new FeedRequest(2L, "title2",
            "content2", 123.123, 456.456, new ArrayList<>());

    private FeedRequest 테니스_FEED_REQUEST = new FeedRequest(1L, "title1",
            "content1", 123.123, 456.456,
            new ArrayList<>());

    private FeedRequest 축구_FEED_REQUEST = new FeedRequest(2L, "title2",
            "content2", 123.123, 456.456,
            new ArrayList<>());

    private User 봄봄 = 봄봄_생성();

    private User 신디 = 신디_생성();

    private Category 테니스 = 테니스_생성();

    private Category 축구 = 축구_생성();

    // @MockBean
    // private ImageService imageService;
    //

    @Autowired
    private FeedService feedService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.save(봄봄);
        userRepository.save(신디);

        categoryRepository.save(테니스);
        categoryRepository.save(축구);

        테니스_FEED_REQUEST.setCategoryId(테니스.getId());
        축구_FEED_REQUEST.setCategoryId(축구.getId());
    }

    @DisplayName("Feed를 생성한다")
    @Test
    void create() {
        // given
        // User 봄봄 =
        // User.builder().email("rosejap97@gmail.com").username("user").password("password").build();

        // when
        Long feedId1 = feedService.create(봄봄, 테니스_FEED_REQUEST);
        em.flush();
        em.clear();
        Feed savedFeed1 = feedService.findEntityById(feedId1);

        // then
        assertThat(feedId1).isNotNull();
        피드_정보가_같은지_조회(테니스_FEED_REQUEST, savedFeed1);
    }

    // @DisplayName("Feed를 수정한다 - category 변경")
    // @Test
    // void updateCategory() {
    // // given
    // Long feedId1 = feedService.create(봄봄, 테니스_FEED_REQUEST);
    // FeedRequest request = 축구_FEED_REQUEST;
    // // feedService.viewFeed(봄봄, feedId1, true);

    // // when
    // feedService.update(봄봄, feedId1, request);
    // em.flush();
    // em.clear();

    // // then
    // // FeedResponse updateFeed = feedService.viewFeed(봄봄, feedId1, true);
    // 피드_정보가_같은지_조회(request, updateFeed);
    // }

    @DisplayName("작성자가 아닐 경우 Feed를 수정할 수 없다.")
    @Test
    void cantUpdateIfNotAuthor() {
        // given
        Long feedId1 = feedService.create(봄봄, 빈_FEED_REQUEST);
        FeedRequest request = 축구_FEED_REQUEST;

        // when

        // then
        assertThatThrownBy(() -> feedService.update(신디, feedId1, request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED_UPDATE_FEED.getErrorMsg());
    }

    @DisplayName("Feed를 삭제한다.")
    @Test
    void delete() {
        // given
        빈_FEED_REQUEST.setCategoryId(테니스.getId());
        Long feedId = feedService.create(봄봄, 빈_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);
        feedService.delete(봄봄, feedId);
        em.flush();

        // then
        assertThatThrownBy(() -> feedService.findEntityById(feedId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ErrorCode.FEED_NOT_FOUND.getErrorMsg());
    }

    @DisplayName("작성자가 아닐 경우 Feed를 삭제할 수 없다.")
    @Test
    void cantDeleteIfNotAuthor() {
        // given
        빈_FEED_REQUEST.setCategoryId(테니스.getId());
        Long feedId = feedService.create(봄봄, 빈_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        // Feed feed = feedService.findEntityById(봄봄, feedId);
        em.flush();

        // then
        assertThatThrownBy(() -> feedService.delete(신디, feedId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED_DELETE_FEED.getErrorMsg());
    }

    @DisplayName("Feed Id로 entity 객체를 가져올 수 있다.")
    @Test
    void findEntityById() {
        // given
        Long feedId = feedService.create(봄봄, 빈_FEED_REQUEST);

        // when
        Feed feedEntity = feedService.findEntityById(feedId);

        // then
        assertThat(feedEntity.getId()).isEqualTo(feedId);
        피드_정보가_같은지_조회(빈_FEED_REQUEST, feedEntity);
    }

    @DisplayName("존재하지 않는 Feed Id로 entity 객체를 조회하면 예외가 발생한다.")
    @Test
    void findEntityByNonExistsId() {
        // when
        assertThatThrownBy(() -> feedService.findEntityById(Long.MAX_VALUE))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ErrorCode.FEED_NOT_FOUND.getErrorMsg());
    }

    @DisplayName("좋아요를 누른 피드 조회 시 'liked=true'로 반환한다.")
    @Test
    void checkLikeWhenFindFeed() {
        // given
        Long feedId = feedService.create(봄봄, 빈_FEED_REQUEST);
        likeService.addLike(신디, feedId);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);
        FeedResponse feedResponse = FeedResponse.of(신디, feed, 신디.isLiked(feed));

        // then
        assertThat(feedResponse.isLiked()).isTrue();
    }

    @DisplayName("좋아요를 취소한 이후 피드를 조회하면 liked=false를 반환한다.")
    @Test
    void cancelLike() {
        // given
        Long feedId = feedService.create(봄봄, 빈_FEED_REQUEST);
        likeService.addLike(신디, feedId);
        em.flush();
        em.clear();

        // when
        likeService.deleteLike(신디, feedId);
        em.flush();
        em.clear();

        System.out.println();

        Feed feed = feedService.findEntityById(feedId);
        FeedResponse feedResponse = FeedResponse.of(신디, feed, 신디.isLiked(feed));

        // then
        assertThat(feedResponse.isLiked()).isFalse();
    }

    @DisplayName("좋아요를 누르지 않은 피드 조회 시 'liked=false'로 반환한다.")
    @Test
    void checkNotLikeWhenFindFeed() {
        // given
        Long feedId = feedService.create(봄봄, 빈_FEED_REQUEST);
        em.flush();
        em.clear();

        // when
        Feed feed = feedService.findEntityById(feedId);
        FeedResponse feedResponse = FeedResponse.of(신디, feed, 신디.isLiked(feed));

        // then
        assertThat(feedResponse.isLiked()).isFalse();
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, Feed feed) {
        // List<Long> techIds = feed.getTechs().stream()
        // .map(Tech::getId)
        // .collect(Collectors.toList());

        assertThat(request.getTitle()).isEqualTo(feed.getTitle());
        assertThat(request.getCategoryId()).isEqualTo(feed.getCategory().getId());
        assertThat(request.getContent()).isEqualTo(feed.getContent());
        assertThat(request.getLongitude()).isEqualTo(feed.getLocation().getLongitude());
        assertThat(request.getLatitude()).isEqualTo(feed.getLocation().getLatitude());
    }

    private void 피드_정보가_같은지_조회(FeedRequest request, FeedResponse response) {
        // List<Long> techIds = response.getTechs().stream()
        // .map(TechResponse::getId)
        // .collect(Collectors.toList());

        assertThat(request.getTitle()).isEqualTo(response.getTitle());
        // assertThat(request.getTechs()).containsExactlyElementsOf(techIds);
        assertThat(request.getContent()).isEqualTo(response.getContent());
        // assertThat(request.getStep()).isEqualTo(response.getStep());
        // assertThat(request.isSos()).isEqualTo(response.isSos());
        // assertThat(request.getStorageUrl()).isEqualTo(response.getStorageUrl());
        // assertThat(request.getDeployedUrl()).isEqualTo(response.getDeployedUrl());
    }

    // private void 피드_정보가_같은지_조회(FeedRequest request, FeedCardResponse response)
    // {
    // assertThat(request.getTitle()).isEqualTo(response.getTitle());
    // assertThat(request.getContent()).isEqualTo(response.getContent());
    // assertThat(request.getStep()).isEqualTo(response.getStep());
    // assertThat(request.isSos()).isEqualTo(response.isSos());
    // }

}
