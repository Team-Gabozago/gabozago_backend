package com.gabozago.backend.user.interfaces.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class GetMeResponseDto {
    private String email;
    private String nickname;
    private String phoneNumber;

    public String parseJson() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode()
                .put("email", email)
                .put("nickname", nickname)
                .put("phoneNumber", phoneNumber)
                .toString();
    }
}
