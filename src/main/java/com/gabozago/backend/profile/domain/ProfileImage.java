package com.gabozago.backend.profile.domain;

import com.gabozago.backend.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_image_id", nullable = false)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long size;

    @OneToOne(mappedBy = "profileImage")
    private User user;
}
