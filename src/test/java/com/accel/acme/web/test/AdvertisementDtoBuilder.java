package com.accel.acme.web.test;

import com.accel.acme.web.dto.AdvertisementDto;

import java.util.List;

public class AdvertisementDtoBuilder {
    private AdvertisementDto dto;

    public AdvertisementDtoBuilder() {
        dto = new AdvertisementDto();
    }

    public AdvertisementDtoBuilder id(Integer id) {
        dto.setId(id);
        return this;
    }

    public AdvertisementDtoBuilder title(String title) {
        dto.setTitle(title);
        return this;
    }

    public AdvertisementDtoBuilder code(String referenceCode) {
        dto.setReferenceCode(referenceCode);
        return this;
    }

    public AdvertisementDtoBuilder description(String description) {
        dto.setDescription(description);
        return this;
    }

    public AdvertisementDtoBuilder numberOfUnits(Integer numberOfUnits) {
        dto.setNumberOfUnits(numberOfUnits);
        return this;
    }

    public AdvertisementDtoBuilder newspaperId(List<Integer> newspaperIdList) {
        dto.setSelectedNewspapers(newspaperIdList);
        return this;
    }

    public AdvertisementDto build() {
        return dto;
    }
}