package com.example.gyak_beadando.repository;

import com.example.gyak_beadando.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}