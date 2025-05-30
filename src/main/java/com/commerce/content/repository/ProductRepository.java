package com.commerce.content.repository;

import com.commerce.content.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p JOIN FETCH p.tags t ")
    Optional<List<Product>> findAllByProductAndTags();
}
