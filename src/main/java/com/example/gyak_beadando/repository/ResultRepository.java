package com.example.gyak_beadando.repository;

import com.example.gyak_beadando.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("""
           SELECT r
           FROM Result r
           JOIN FETCH r.grandPrix gp
           JOIN FETCH r.pilot p
           ORDER BY gp.date, gp.name, r.place
           """)
    List<Result> findAllWithJoins();
}
