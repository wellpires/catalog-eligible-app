package com.catalog.eligibleads.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.function.Meli2MeliDTOFunction;
import com.catalog.eligibleads.repository.MeliRepository;
import com.catalog.eligibleads.service.MeliService;

@Service
public class MeliServiceImpl implements MeliService {

	@Autowired
	private MeliRepository meliRepository;

	@Override
	public List<MeliDTO> findAllActivatedMeli() {
		return meliRepository.findByActiveTrue().stream().map(new Meli2MeliDTOFunction()).collect(Collectors.toList());
	}

}
