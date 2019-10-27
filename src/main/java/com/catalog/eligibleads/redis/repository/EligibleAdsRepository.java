package com.catalog.eligibleads.redis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catalog.eligibleads.redis.model.EligibleAdvertisement;

public interface EligibleAdsRepository extends CrudRepository<EligibleAdvertisement, String> {

	List<EligibleAdvertisement> findByMeliId(String meliId);

}
