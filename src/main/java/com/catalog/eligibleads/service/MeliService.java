package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.MeliNotFoundException;

public interface MeliService {

	List<MeliDTO> findAllActivatedMeli();

	void updateAccessTokenAndRefreshToken(MeliDTO meliDTOModified) throws MeliNotFoundException;

}
