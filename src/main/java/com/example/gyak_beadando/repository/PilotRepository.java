package com.example.gyak_beadando.repository;

import com.example.gyak_beadando.model.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PilotRepository extends JpaRepository<Pilot, Long> {
    boolean existsByLegacyId(Integer legacyId);
}
