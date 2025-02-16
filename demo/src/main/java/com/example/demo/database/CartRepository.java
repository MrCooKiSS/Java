package com.example.demo.database;

import com.example.demo.models.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);
    void deleteByUserIdAndTourId(Long userId, Long tourId);
}