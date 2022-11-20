package com.gabozago.backend.feed.interfaces;

import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Location;
import com.gabozago.backend.feed.interfaces.dto.AuthorResponse;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.feed.service.FeedService;
import com.gabozago.backend.feed.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;

import static com.gabozago.backend.fixture.CategoryFixture.축구_생성;
import static com.gabozago.backend.fixture.CategoryFixture.테니스_생성;
import static com.gabozago.backend.fixture.UserFixture.봄봄_생성;
import static com.gabozago.backend.fixture.UserFixture.신디_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FeedApiController.class)
class FeedApiControllerTest extends ControllerTest {

        private static User 봄봄 = 봄봄_생성();

        private User 신디 = 신디_생성();

        private static Category 테니스 = 테니스_생성();

        private Category 축구 = 축구_생성();

        public static Location A_STATION = new Location(127.10016437214135,
                        37.51337688858131);

        private static final long FEED_ID = 1L;

        public static final Feed FEED1 = Feed.builder()
                        .id(1L)
                        .category(테니스)
                        .location(A_STATION)
                        .title("title")
                        .content("난 너무 잘해")
                        .build()
                        .writtenBy(봄봄);

        private static final FeedResponse FEED_RESPONSE = new FeedResponse(AuthorResponse.of(봄봄), FEED1.getId(),
                        FEED1.getCategory(), FEED1.getLocation(), FEED1.getTitle(), FEED1.getContent(),
                        FEED1.getLikes().size(), FEED1.getViews(), true, FEED1.getCreatedAt(), LocalDateTime.now());

        @MockBean
        private FeedService feedService;

        @MockBean
        private LikeService likeService;

        @DisplayName("사용자가 피드를 업로드한다")
        @Test
        void create() throws Exception {
                // given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
                given(feedService.create(any(User.class), any())).willReturn(FEED_ID);

                MockHttpServletRequestBuilder request = multipart("/feeds")
                                .param("categoryId", "1")
                                .param("title", "피드 제목")
                                .param("content", "지금은 일요일 12시 45분")
                                .param("longitude", "127.10016437214135")
                                .param("latitude", "37.51337688858131")
                                .param("latitude", "37.51337688858131")
                                .header("Authorization", "Bearer " + ACCESS_TOKEN);

                mockMvc.perform(request)
                                .andExpect(status().isCreated())
                                // .andExpect(header().string("Location", "/feeds/" + FEED_ID))
                                .andDo(document("feed-create",
                                                getDocumentRequest(),
                                                getDocumentResponse(),
                                                requestParameters(
                                                                parameterWithName("categoryId").description("카테고리 아이디"),
                                                                parameterWithName("title").description("제목"),
                                                                parameterWithName("content").description("내용"),
                                                                parameterWithName("longitude").description("위도"),
                                                                parameterWithName("latitude").description("경도"))));
        }

        @DisplayName("피드를 업데이트한다.")
        @Test
        void update() throws Exception {
                // given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
                willDoNothing().given(feedService).update(any(User.class), any(Long.class), any(FeedRequest.class));

                MockHttpServletRequestBuilder request = multipart("/feeds/" + FEED_ID)
                                .param("categoryId", "1")
                                .param("title", "수전된 피드 제목")
                                .param("content", "수정된 글입니다.")
                                .param("longitude", "127.10016437214135")
                                .param("latitude", "37.51337688858131")
                                .param("latitude", "37.51337688858131")
                                .header("Authorization", "Bearer " + ACCESS_TOKEN);

                MockHttpServletRequestBuilder put = request.with(new RequestPostProcessor() {
                        @Override
                        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                                request.setMethod("PUT");
                                return request;
                        }
                });
        }

        @DisplayName("피드를 삭제한다.")
        @Test
        void deleteFeed() throws Exception {
                // given(authService.findUserByToken(ACCESS_TOKEN)).willReturn(LOGIN_USER);
                willDoNothing().given(feedService).delete(any(User.class), any(Long.class));

                mockMvc.perform(
                                RestDocumentationRequestBuilders.delete("/feeds/{feedId}", FEED_ID)
                                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                                .andExpect(status().isNoContent())
                                .andDo(document("feed-delete",
                                                getDocumentRequest(),
                                                getDocumentResponse(),
                                                pathParameters(
                                                                parameterWithName("feedId").description("삭제할 피드 ID"))));
        }

        @DisplayName("상세페이지를 조회할 수 있다.")
        @Test
        void findById() throws Exception {
                // given(authService.findUserByToken(ACCESS_TOKEN_OPTIONAL)).willReturn(LOGIN_USER);
                given(feedService.viewFeed(any(User.class), any(), anyBoolean())).willReturn(FEED_RESPONSE);

                mockMvc.perform(
                                RestDocumentationRequestBuilders.get("/feeds/{feedId}", FEED_ID)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .header("Authorization", "Bearer " + ACCESS_TOKEN))
                                .andExpect(status().isOk())
                                .andExpect(content().json(objectMapper.writeValueAsString(FEED_RESPONSE)))
                                .andDo(document("feed-findById",
                                                getDocumentRequest(),
                                                getDocumentResponse(),
                                                pathParameters(
                                                                parameterWithName("feedId").description("피드 ID")),
                                                responseFields(
                                                                fieldWithPath("author").type(JsonFieldType.OBJECT)
                                                                                .description("작성자"),
                                                                // fieldWithPath("author.id").type(JsonFieldType.NUMBER).description("작성자
                                                                // ID"),
                                                                // fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("작성자
                                                                // 닉네임"),
                                                                // fieldWithPath("author.imageUrl").type(JsonFieldType.STRING).description("작성자
                                                                // 이미지 URL"),
                                                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                                                .description("피드 ID"),
                                                                fieldWithPath("category").type(JsonFieldType.OBJECT)
                                                                                .description("카테고리"),
                                                                fieldWithPath("location").type(JsonFieldType.OBJECT)
                                                                                .description("장소"),
                                                                fieldWithPath("title").type(JsonFieldType.STRING)
                                                                                .description("피드 제목"),
                                                                fieldWithPath("content").type(JsonFieldType.STRING)
                                                                                .description("피드 내용"),
                                                                // fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("썸네일
                                                                // URL").optional(),
                                                                fieldWithPath("likes").type(JsonFieldType.NUMBER)
                                                                                .description("좋아요 개수"),
                                                                fieldWithPath("views").type(JsonFieldType.NUMBER)
                                                                                .description("조회수"),
                                                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN)
                                                                                .description("로그인한 유저의 글 좋아요 여부"),
                                                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                                                                .description("작성 날짜"),
                                                                fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                                                                .description("수정 날짜"))

                                ));
        }

}