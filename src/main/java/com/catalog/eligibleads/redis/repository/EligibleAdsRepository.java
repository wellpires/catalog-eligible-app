package com.catalog.eligibleads.redis.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.catalog.eligibleads.redis.model.EligibleAdvertisement;

public interface EligibleAdsRepository extends CrudRepository<EligibleAdvertisement, String>,
		PagingAndSortingRepository<EligibleAdvertisement, String> {

	Page<EligibleAdvertisement> findByMeliId(String meliId, Pageable pageable);

	Optional<EligibleAdvertisement> findByVariationIdAndMeliIdAndMlbId(Long variationId, String meliId, String mlbId);

}
