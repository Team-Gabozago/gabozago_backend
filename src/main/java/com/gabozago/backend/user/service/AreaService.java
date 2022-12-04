package com.gabozago.backend.user.service;

import com.gabozago.backend.user.domain.Area;
import com.gabozago.backend.user.infrastructure.AreaRepository;
import com.gabozago.backend.user.interfaces.dto.UserAreaResponse;
import com.gabozago.backend.user.interfaces.dto.UserGetAreasResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaRepository areaRepository;

    public UserGetAreasResponse findAll() {
        List<Area> areas = areaRepository.findAll();
        List<UserAreaResponse> userAreaResponses = areas
                .stream()
                .map(UserAreaResponse::of)
                .collect(Collectors.toList());

        return UserGetAreasResponse.of("구역 조회가 완료됐습니다.", userAreaResponses);
    }

}
