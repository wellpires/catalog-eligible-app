package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.MeliDTO;

public interface MeliService {

	List<MeliDTO> findAllActivatedMeli();

}
