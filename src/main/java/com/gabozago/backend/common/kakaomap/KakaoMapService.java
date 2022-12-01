package com.gabozago.backend.common.kakaomap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabozago.backend.common.kakaomap.dto.KakaoMapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoMapService {

    @Value("${kakao.key}")
    private String restApiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Object getPlaceName(Double latitude, Double longitude){

        // https://developers.kakao.com/docs/latest/ko/local/dev-guide#coord-to-district

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + restApiKey);

        // 요청 만들기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode";

        // 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("y", latitude)
                .queryParam("x", longitude);

        try {

            // 응답 받기
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    String.class
            );
            KakaoMapResponse kakaoMapResponse = objectMapper.readValue(response.getBody(), KakaoMapResponse.class);


            for (Map<String, Object> kakaoAddressMap : kakaoMapResponse.getDocuments()) {
                if (kakaoAddressMap.get("region_type").equals("H")) {
                    return kakaoAddressMap;
                }

            }

        } catch (Exception e){
            log.error(e.getMessage());
        }
        return "Error";

    }

}
