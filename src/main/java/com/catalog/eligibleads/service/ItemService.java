package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.MeliNotFoundException;

public interface ItemService {

	ItemsResponseDTO findItems(FilterDTO filterDTO, MeliDTO meli)
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException;

	List<AdvertisementItemDTO> searchProductsByEligibleAds(List<ElegibleAdvertisementDTO> eligiblesAds, MeliDTO meli)
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException;

}
