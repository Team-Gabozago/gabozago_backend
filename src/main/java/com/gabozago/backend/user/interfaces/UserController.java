package com.gabozago.backend.user.interfaces;


import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.infrastructure.UserRepository;
import com.gabozago.backend.user.interfaces.dto.UserAreasResponse;
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
    public ResponseEntity<?> setlocation(@AuthenticationPrincipal User user,
                                          @RequestParam("latitude") Double latitude,
                                          @RequestParam("longitude")Double longitude){

        user.setLocation(latitude,longitude);
        userRepository.save(user);
        return ResponseEntity.ok("save location");
    }

    @GetMapping("/areas")
    public ResponseEntity<UserAreasResponse> getAreas(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(areaService.findAll());
    }
}
