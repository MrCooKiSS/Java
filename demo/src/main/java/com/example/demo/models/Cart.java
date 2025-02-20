package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    // Геттеры и сеттеры
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "tourId", nullable = false)
    private Long tourId;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}