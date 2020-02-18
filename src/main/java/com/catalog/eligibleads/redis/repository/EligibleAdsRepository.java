package com.catalog.eligibleads.redis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.catalog.eligibleads.redis.model.EligibleAdvertisement;

public interface EligibleAdsRepository extends CrudRepository<EligibleAdvertisement, String>,
		PagingAndSortingRepository<EligibleAdvertisement, String> {

	Page<EligibleAdvertisement> findByMeliId(String meliId, Pageable pageable);

	List<EligibleAdvertisement> findByMeliId(String meliId);

}
