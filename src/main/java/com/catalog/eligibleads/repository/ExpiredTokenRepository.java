package com.catalog.eligibleads.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalog.eligibleads.model.ExpiredToken;

public interface ExpiredTokenRepository extends JpaRepository<ExpiredToken, String> {

}
