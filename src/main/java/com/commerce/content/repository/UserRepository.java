package com.commerce.content.repository;

import com.commerce.content.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserId(String userId);


    @Query("SELECT u FROM  User u LEFT JOIN FETCH u.posts WHERE  u.userId =:userId")
    Optional<User> findByIdWithArticles(String userId);
}
