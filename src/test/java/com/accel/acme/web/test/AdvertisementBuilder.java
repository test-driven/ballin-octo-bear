package com.accel.acme.web.test;

import com.accel.acme.web.model.Advertisement;
import com.accel.acme.web.model.NewsPapersAdvertisements;

import java.util.Date;
import java.util.Set;

public class AdvertisementBuilder {
    private Advertisement advertisement;

    public AdvertisementBuilder() {
        advertisement = new Advertisement();
    }

    public AdvertisementBuilder id(Integer id) {
        advertisement.setId(id);
        return this;
    }

    public AdvertisementBuilder title(String title) {
        advertisement.setTitle(title);
        return this;
    }

    public AdvertisementBuilder code(String referenceId) {
        advertisement.setReferenceId(referenceId);
        return this;
    }

    public AdvertisementBuilder description(String description) {
        advertisement.setDescription(description);
        return this;
    }

    public AdvertisementBuilder createdAt(Date createdAt) {
        advertisement.setCreated_at(createdAt);
        return this;
    }

    public AdvertisementBuilder updatedAt(Date updatedAt) {
        advertisement.setUpdated_at(updatedAt);
        return this;
    }

    public AdvertisementBuilder newspaperAdvertisement(Set<NewsPapersAdvertisements> newsPaperAdvertisementSet) {
        advertisement.setNewsPaperAdvertisement(newsPaperAdvertisementSet);
        return this;
    }

    public Advertisement build() {
        return advertisement;
    }
}