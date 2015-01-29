package com.accel.acme.web.test;

import com.accel.acme.web.model.Newspaper;

import java.util.Date;

public class NewspaperBuilder {
    private Newspaper newspaper;

    public NewspaperBuilder() {
        newspaper = new Newspaper();
    }

    public NewspaperBuilder id(Integer id) {
        newspaper.setId(id);
        return this;
    }

    public NewspaperBuilder name(String name) {
        newspaper.setName(name);
        return this;
    }

    public NewspaperBuilder code(String code) {
        newspaper.setCode(code);
        return this;
    }

    public NewspaperBuilder language(String language) {
        newspaper.setLanguage(language);
        return this;
    }

    public NewspaperBuilder ratePerUnit(Float ratePerUnit) {
        newspaper.setRatePerUnit(ratePerUnit);
        return this;
    }

    public NewspaperBuilder createdAt(Date createdAt) {
        newspaper.setCreated_at(createdAt);
        return this;
    }

    public NewspaperBuilder updatedAt(Date updatedAt) {
        newspaper.setUpdated_at(updatedAt);
        return this;
    }

    public Newspaper build() {
        return newspaper;
    }
}