package com.gabozago.backend.common.kakaomap;

import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KakaoMapController {

    private final KakaoMapService kakaoMapService;

    @GetMapping("/address")
    public ResponseEntity<?> getPlaceName(@AuthenticationPrincipal User user,
                                                         @RequestParam("latitude") Double latitude,
                                                         @RequestParam("longitude")Double longitude){

        Object response = kakaoMapService.getPlaceName(latitude, longitude);
        if (response.equals("Error")){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(response);
    }



}
