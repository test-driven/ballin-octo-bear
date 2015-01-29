package com.accel.acme.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class NewspaperDto {

    private Integer id;
    @NotEmpty
    private String name;
    private String code;
    private String language;
    private Float ratePerUnit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Float getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Float ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }
}
