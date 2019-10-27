package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.catalog.eligibleads.builder.FilterDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.BuyBoxVariationDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemResponseDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.enums.AdvertisementStatus;
import com.catalog.eligibleads.enums.Order;
import com.catalog.eligibleads.function.AdvertisementItemDTO2ListAdvertisementDTOFunction;
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.ItemService;
import com.catalog.eligibleads.service.MeliService;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	private static final int LIMIT_MULTI_GET = 20;

	@Autowired
	private ItemService itemService;

	@Autowired
	private MeliService meliService;

	private static final String URL_ELIGIBLE_ADS = "https://api.mercadolibre.com/items/{itemId}/catalog_listing_eligibility";

	@Override
	public List<AdvertisementDTO> findAdvertisements() {
		return meliService.findAllActivatedMeli().stream().map(this::findEligibleAds).flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	private List<AdvertisementDTO> findEligibleAds(MeliDTO meli) {

		FilterDTO filterDTO = new FilterDTOBuilder().accessToken(meli.getAccessToken()).limit(50l)
				.order(Order.START_TIME_ASC).status(AdvertisementStatus.ACTIVE).build();

		List<String> items = new ArrayList<>();
		for (int offset = 0; offset < 1000; offset += 50) {
			filterDTO.setOffset(offset);
			ItemsResponseDTO itemsResponse = itemService.findItems(filterDTO, meli);
			if (CollectionUtils.isEmpty(itemsResponse.getItems())) {
				break;
			}
			items.addAll(itemsResponse.getItems());
		}

		List<ElegibleAdvertisementDTO> eligiblesAds = findEachEligibleAds(items, meli);

		List<AdvertisementItemDTO> advertisementBuyBoxes = searchProducts(eligiblesAds, meli);

		List<AdvertisementDTO> advertisementsDTOs = createAdvertisements(eligiblesAds, advertisementBuyBoxes);
		advertisementsDTOs.stream().forEach(eligibleAd -> {
			eligibleAd.setMeliId(meli.getId());
		});
		return advertisementsDTOs;
	}

	private List<AdvertisementDTO> createAdvertisements(List<ElegibleAdvertisementDTO> eligiblesAds,
			List<AdvertisementItemDTO> advertisementBuyBoxes) {
		List<AdvertisementDTO> advertisements = advertisementBuyBoxes.stream()
				.map(new AdvertisementItemDTO2ListAdvertisementDTOFunction()).flatMap(Collection::stream)
				.collect(Collectors.toList());

		advertisements = filterByEligibleAdId(eligiblesAds, advertisements);

		List<BuyBoxVariationDTO> variations = eligiblesAds.stream().map(elegible -> elegible.getBuyBoxVariations())
				.flatMap(Collection::stream).collect(Collectors.toList());

		advertisements = advertisements.stream()
				.filter(advertisement -> Objects.isNull(advertisement.getVariationId())
						|| variations.stream().map(BuyBoxVariationDTO::getId).collect(Collectors.toList())
								.contains(advertisement.getVariationId().toString()))
				.collect(Collectors.toList());

		return advertisements;
	}

	private List<AdvertisementDTO> filterByEligibleAdId(List<ElegibleAdvertisementDTO> elegibleAds,
			List<AdvertisementDTO> advertisements) {
		return advertisements.stream().filter(advertisement -> elegibleAds.stream().map(ElegibleAdvertisementDTO::getId)
				.anyMatch(eligibleAd -> advertisement.getId().equals(eligibleAd))).collect(Collectors.toList());
	}

	private List<AdvertisementItemDTO> searchProducts(List<ElegibleAdvertisementDTO> eligibleAds, MeliDTO meli) {
		List<AdvertisementItemDTO> advertisementBuyBoxes = new ArrayList<>();
		for (int i = 0; i < eligibleAds.size(); i += LIMIT_MULTI_GET) {
			String[] ids = eligibleAds.stream().skip(i).limit(LIMIT_MULTI_GET).map(ElegibleAdvertisementDTO::getId)
					.toArray(String[]::new);
			advertisementBuyBoxes
					.addAll(itemService.searchProduct(meli, ids).stream().filter(ItemResponseDTO::isSuccess)
							.map(ItemResponseDTO::getAdvertisementItemDTO).collect(Collectors.toList()));
		}
		return advertisementBuyBoxes;
	}

	private List<ElegibleAdvertisementDTO> findEachEligibleAds(List<String> items, MeliDTO meli) {
		return items.stream().map(this::setParameter).map(item -> configClient(item, meli))
				.map(ElegibleAdvertisementDTO::selectElegibles).flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	private ElegibleAdvertisementDTO configClient(Map<String, String> parameter, MeliDTO meli) {

		URI findEligibleAd = UriComponentsBuilder.fromHttpUrl(URL_ELIGIBLE_ADS)
				.queryParam("access_token", meli.getAccessToken()).buildAndExpand(parameter).toUri();

		RestTemplate rest = new RestTemplate();
		return rest.getForEntity(findEligibleAd, ElegibleAdvertisementDTO.class).getBody();

	}

	private Map<String, String> setParameter(String itemId) {
		return Collections.singletonMap("itemId", itemId);
	}

}
