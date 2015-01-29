package com.accel.acme.web.test;

import com.accel.acme.web.dto.NewspaperDto;

public class NewspaperDtoBuilder {
    private NewspaperDto dto;

    public NewspaperDtoBuilder() {
        dto = new NewspaperDto();
    }

    public NewspaperDtoBuilder id(Integer id) {
        dto.setId(id);
        return this;
    }

    public NewspaperDtoBuilder name(String name) {
        dto.setName(name);
        return this;
    }

    public NewspaperDtoBuilder code(String code) {
        dto.setCode(code);
        return this;
    }

    public NewspaperDtoBuilder language(String language) {
        dto.setLanguage(language);
        return this;
    }

    public NewspaperDtoBuilder ratePerUnit(Float ratePerUnit) {
        dto.setRatePerUnit(ratePerUnit);
        return this;
    }

    public NewspaperDto build() {
        return dto;
    }
}