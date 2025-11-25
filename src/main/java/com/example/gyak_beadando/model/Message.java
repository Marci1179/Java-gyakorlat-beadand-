package com.example.gyak_beadando.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A név megadása kötelező.")
    @Size(max = 100, message = "A név legfeljebb 100 karakter lehet.")
    private String name;

    @NotBlank(message = "Az e-mail cím megadása kötelező.")
    @Email(message = "Érvényes e-mail címet adjon meg.")
    @Size(max = 150, message = "Az e-mail cím legfeljebb 150 karakter lehet.")
    private String email;

    @NotBlank(message = "A tárgy megadása kötelező.")
    @Size(max = 150, message = "A tárgy legfeljebb 150 karakter lehet.")
    private String subject;

    @NotBlank(message = "Az üzenet megadása kötelező.")
    @Column(name = "message", columnDefinition = "text")
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Message() {
    }

    // --- GETTEREK & SETTEREK ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
