package com.catalog.eligibleads.redis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catalog.eligibleads.redis.model.MeliAccount;

public interface MeliAccountRepository extends CrudRepository<MeliAccount, String> {

}
