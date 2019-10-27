package com.catalog.eligibleads.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalog.eligibleads.model.Meli;

public interface MeliRepository extends JpaRepository<Meli, String> {

	List<Meli> findByActiveIs(boolean active);

}
