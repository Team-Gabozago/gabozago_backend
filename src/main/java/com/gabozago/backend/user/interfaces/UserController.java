package com.gabozago.backend.user.interfaces;


import com.gabozago.backend.common.exception.EntityNotFoundException;
import com.gabozago.backend.common.kakaomap.KakaoMapService;
import com.gabozago.backend.common.response.ErrorCode;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.infrastructure.UserRepository;
import com.gabozago.backend.user.interfaces.dto.UserGetAreaResponse;
import com.gabozago.backend.user.interfaces.dto.UserGetAreasResponse;
import com.gabozago.backend.user.interfaces.dto.user.GetMeResponseDto;
import com.gabozago.backend.user.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    private final AreaService areaService;

    private final KakaoMapService kakaoMapService;

    @GetMapping("/me")
    public ResponseEntity<String> getMe(@AuthenticationPrincipal User user) {

        GetMeResponseDto response = GetMeResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickName())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return new ResponseEntity<>(response.parseJson(), HttpStatus.OK);
    }

    @PostMapping("/location")
    public ResponseEntity<?> setLocation(@AuthenticationPrincipal User user,
                                          @RequestParam("latitude") Double latitude,
                                          @RequestParam("longitude")Double longitude){

        user.setLocation(latitude,longitude);
        userRepository.save(user);
        return ResponseEntity.ok("save location");
    }

    @GetMapping("/areas")
    public ResponseEntity<UserGetAreasResponse> getAreas(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(areaService.findAll());
    }

    @GetMapping("/area")
    public ResponseEntity<UserGetAreaResponse> getArea(@AuthenticationPrincipal User user){
        if (!user.hasLocation()) {
            throw new EntityNotFoundException(ErrorCode.USER_LOCATION_NOT_FOUND);
        }

        return ResponseEntity.ok(UserGetAreaResponse.of("유저의 구역 조회가 완료됐습니다.", kakaoMapService.getPlaceName(user.getLatitude(), user.getLongitude())));
    }
}
