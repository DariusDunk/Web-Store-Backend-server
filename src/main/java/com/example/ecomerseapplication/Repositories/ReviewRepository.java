package com.example.ecomerseapplication.Repositories;

import com.example.ecomerseapplication.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
