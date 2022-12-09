package com.gabozago.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabozago.backend.common.exception.UnauthorizedException;
import com.gabozago.backend.common.response.ErrorCode;
import com.gabozago.backend.feed.domain.Comment;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.profile.domain.ProfileImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @JsonIgnore
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNickName() {
        return this.nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public void leave() {
        this.nickname = "expired_user_" + this.id;
        this.email = "expired_user_" + this.id + "@expired.com";
        this.phoneNumber = "00000000000";
    }

    public void updateProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public boolean sameAs(User user) {
        return this.equals(user);
    }

    public void addFeed(Feed feed) {
        this.feeds.add(feed);
    }

    public Feed findMyFeed(Long feedId) {
        return this.feeds.stream()
                .filter(feed -> feedId.equals(feed.getId()))
                .findAny().orElseThrow(() -> new UnauthorizedException(ErrorCode.UNAUTHORIZED_UPDATE_FEED));
    }

    public boolean isLiked(Feed feed) {
        return this.likes.stream()
                .anyMatch(like -> like.hasFeed(feed));
    }

    public void addLike(Like like) {
        this.likes.add(like);
        like.getFeed().addLike(like);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    public void setLocation(Double latitude, Double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean hasLocation() {
        return this.latitude != 0 && this.longitude != 0;
    }
}