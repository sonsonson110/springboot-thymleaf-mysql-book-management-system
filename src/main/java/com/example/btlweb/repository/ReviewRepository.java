package com.example.btlweb.repository;

import com.example.btlweb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findByUser_idAndBook_id(Integer user_id, Integer book_id);
}
