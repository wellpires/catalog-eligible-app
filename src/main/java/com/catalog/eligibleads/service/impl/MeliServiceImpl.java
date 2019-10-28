package com.catalog.eligibleads.service.impl;

import java.util.Arrays;
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
		return this.meliRepository.findAllById(Arrays.asList("228145111", "111412004")).stream()
				.map(new Meli2MeliDTOFunction()).collect(Collectors.toList());
//		return meliRepository.findByActiveIs(true).stream().map(new Meli2MeliDTOFunction())
//				.collect(Collectors.toList());
	}

}
