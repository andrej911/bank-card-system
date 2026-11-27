package com.andrej911.bankcardsystem.repository;

import com.andrej911.bankcardsystem.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByNumberHash(String numberHash);
}