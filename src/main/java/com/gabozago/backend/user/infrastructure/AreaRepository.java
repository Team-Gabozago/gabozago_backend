package com.gabozago.backend.user.infrastructure;

import com.gabozago.backend.user.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
}
