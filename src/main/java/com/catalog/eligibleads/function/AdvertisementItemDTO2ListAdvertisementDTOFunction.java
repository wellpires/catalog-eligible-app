package com.catalog.eligibleads.function;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.catalog.eligibleads.builder.AdvertisementDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.AdvertisementVariationDTO;
import com.catalog.eligibleads.dto.ItemAttributeDTO;
import com.catalog.eligibleads.dto.PictureDTO;

public class AdvertisementItemDTO2ListAdvertisementDTOFunction
		implements Function<AdvertisementItemDTO, List<AdvertisementDTO>> {

	@Override
	public List<AdvertisementDTO> apply(AdvertisementItemDTO advertisementItemDTO) {
		if (CollectionUtils.isEmpty(advertisementItemDTO.getVariations())) {
			return Arrays.asList(new AdvertisementItemDTO2AdvertisementDTOFunction().apply(advertisementItemDTO));
		}
		return advertisementItemDTO.getVariations().stream()
				.map(advertisementVariationDTO2AdvertisementItemDTO(advertisementItemDTO)).collect(Collectors.toList());
	}

	private Function<? super AdvertisementVariationDTO, ? extends AdvertisementDTO> advertisementVariationDTO2AdvertisementItemDTO(
			AdvertisementItemDTO advertisementItemDTO) {
		return variation -> new AdvertisementDTOBuilder().id(advertisementItemDTO.getId())
				.title(advertisementItemDTO.getTitle()).siteId(advertisementItemDTO.getSiteId())
				.status(advertisementItemDTO.getStatus()).subtitle(advertisementItemDTO.getSubtitle())
				.price(advertisementItemDTO.getPrice()).attributes(advertisementItemDTO.getAttributes())
				.domainId(advertisementItemDTO.getDomainId())
				.image(getImageByPictureID(advertisementItemDTO, variation))
				.availableQuantity(advertisementItemDTO.getAvailableQuantity())
				.permalink(
						advertisementItemDTO.getPermalink().concat("?variation=".concat(variation.getId().toString())))
				.variationName(getVariationName(variation)).variationId(variation.getId()).build();
	}

	private String getVariationName(AdvertisementVariationDTO variation) {
		return Objects.nonNull(variation.getName()) ? variation.getName()
				: variation.getAttributes().stream().map(ItemAttributeDTO::getValueName)
						.collect(Collectors.joining(" - "));
	}

	private String getImageByPictureID(AdvertisementItemDTO advertisementItemDTO, AdvertisementVariationDTO variation) {
		return advertisementItemDTO.getPictures().stream().filter(isPictureIdInPictureIdsList(variation))
				.map(PictureDTO::getUrl).findFirst().orElse(advertisementItemDTO.getThumbnail());
	}

	private Predicate<? super PictureDTO> isPictureIdInPictureIdsList(AdvertisementVariationDTO variation) {
		return picture -> Objects.nonNull(variation.getPictureIds())
				&& variation.getPictureIds().contains(picture.getId());
	}
}