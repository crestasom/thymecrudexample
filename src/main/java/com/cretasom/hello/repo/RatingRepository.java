package com.cretasom.hello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cretasom.hello.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
