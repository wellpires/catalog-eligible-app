package com.catalog.eligibleads.service.impl;

import java.util.Arrays;
import java.util.List;

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
		return Arrays.asList(
				this.meliRepository.findById("111412004").map(new Meli2MeliDTOFunction()).orElse(new MeliDTO()));
//		return meliRepository.findByActiveIs(true).stream().map(new Meli2MeliDTOFunction())
//				.collect(Collectors.toList());
	}

}
