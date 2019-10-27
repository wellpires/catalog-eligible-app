package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemResponseDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;

public interface ItemService {
	
	ItemsResponseDTO findItems(FilterDTO filterDTO, MeliDTO meli);

	List<ItemResponseDTO> searchProduct(MeliDTO meli, String[] ids);

}
