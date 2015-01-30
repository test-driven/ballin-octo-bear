package com.accel.acme.web.service;

import com.accel.acme.web.dao.AdvertisementDao;
import com.accel.acme.web.dto.AdvertisementDto;
import com.accel.acme.web.dto.PublishedAdvtDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.model.Advertisement;
import com.accel.acme.web.model.NewsPapersAdvertisements;
import com.accel.acme.web.model.Newspaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdvertisementService {
    @Autowired
    @Qualifier("advertisementDao")
    AdvertisementDao advertisementDao;

    public boolean addAdvertisement(AdvertisementDto advertisementDto) throws ItemAlreadyExistException {
        if (isRefIdExists(advertisementDto.getReferenceCode())) {
            throw new ItemAlreadyExistException(" Advertisement with code " + advertisementDto.getReferenceCode()
                    + " already exists.");
        }
        Advertisement advertisement = new Advertisement();
        advertisement.setCreated_at(advertisementDto.getCreatedDate());
        advertisement.setDescription(advertisementDto.getDescription());
        advertisement.setReferenceId(advertisementDto.getReferenceCode());
        advertisement.setTitle(advertisementDto.getTitle());
        Set<NewsPapersAdvertisements> newsPaperAdvertisementSet = new HashSet<NewsPapersAdvertisements>();
        if (null != advertisementDto.getSelectedNewspapers() && !advertisementDto.getSelectedNewspapers().isEmpty()) {
            for (Integer newspaperId : advertisementDto.getSelectedNewspapers()) {
                Date publishedDate = new Date();
                NewsPapersAdvertisements newsPapersAdvertisements = new NewsPapersAdvertisements();
                newsPapersAdvertisements.setCreated_at(advertisementDto.getCreatedDate());
                newsPapersAdvertisements.setNumberOfUnits(advertisementDto.getNumberOfUnits());
                newsPapersAdvertisements.setPublished_at(publishedDate);
                newsPapersAdvertisements.setUpdated_at(advertisementDto.getUpdatedDate());

                Newspaper newsPaper = new Newspaper();
                newsPaper.setId(newspaperId);
                newsPapersAdvertisements.setNewsPaper(newsPaper);
                newsPapersAdvertisements.setAdvertisement(advertisement);
                newsPaperAdvertisementSet.add(newsPapersAdvertisements);
                advertisement.setNewsPaperAdvertisement(newsPaperAdvertisementSet);
            }
        }
        return advertisementDao.addAdvertisement(advertisement);
    }

    public boolean isRefIdExists(String refId) {
        boolean exists = false;
        exists = advertisementDao.isRefIdExists(refId);
        return exists;
    }

    public boolean updateAdvertisement(AdvertisementDto advertisementDto) throws ItemNotFoundException,
            ItemAlreadyExistException {
        Date nowDate = new Date();
        Advertisement advertisement = advertisementDao.getAdvertisementById(advertisementDto.getId());
        if (null == advertisement) {
            throw new ItemNotFoundException("Advertisement with id " + advertisementDto.getId() + " not found.");
        }

        Advertisement advt = advertisementDao.getAdvertisementByCode(advertisementDto.getReferenceCode());
        if (null != advt && advt.getId() != advertisementDto.getId())
            throw new ItemAlreadyExistException("Advertisement with code " + advertisementDto.getReferenceCode()
                    + " already exists.");

        advertisement.setDescription(advertisementDto.getDescription());
        advertisement.setReferenceId(advertisementDto.getReferenceCode());
        advertisement.setTitle(advertisementDto.getTitle());
        advertisement.setUpdated_at(nowDate);

        Set<NewsPapersAdvertisements> newsPaperAdvertisementSet1 = new HashSet<NewsPapersAdvertisements>();


        if (null != advertisementDto.getSelectedNewspapers() && !advertisementDto.getSelectedNewspapers().isEmpty()) {

            for (Integer newspaperId : advertisementDto.getSelectedNewspapers()) {
                NewsPapersAdvertisements newsPapersAdvertisements = new NewsPapersAdvertisements();
                Newspaper newsPaper = new Newspaper();
                newsPaper.setId(newspaperId);
                newsPapersAdvertisements.setNumberOfUnits(advertisementDto.getNumberOfUnits());
                newsPapersAdvertisements.setPublished_at(nowDate);
                newsPapersAdvertisements.setUpdated_at(advertisementDto.getUpdatedDate());

                newsPapersAdvertisements.setNewsPaper(newsPaper);
                newsPapersAdvertisements.setAdvertisement(advertisement);
                newsPaperAdvertisementSet1.add(newsPapersAdvertisements);
            }
        }


        advertisement.setNewsPaperAdvertisement(newsPaperAdvertisementSet1);
        advertisementDao.updateAdvertisement(advertisement);
        return true;

    }

    public AdvertisementDto getAdvertisementById(Integer advertisementId) throws ItemNotFoundException {

        Advertisement advertisement = advertisementDao.getAdvertisementById(advertisementId);
        if (null == advertisement) {
            throw new ItemNotFoundException("Advertisement with id" + advertisementId + " not found.");
        }
        AdvertisementDto advertisementDto = new AdvertisementDto();
        advertisementDto.setId(advertisement.getId());
        advertisementDto.setReferenceCode(advertisement.getReferenceId());
        advertisementDto.setTitle(advertisement.getTitle());
        advertisementDto.setDescription(advertisement.getDescription());
        advertisementDto.setCreatedDate(advertisement.getCreated_at());
        advertisementDto.setUpdatedDate(advertisement.getUpdated_at());
        List<Integer> selectedNewspaperId = new ArrayList<Integer>();
        if (null != advertisement.getNewsPaperAdvertisement() && !advertisement.getNewsPaperAdvertisement().isEmpty()) {
            for (NewsPapersAdvertisements newsPapersAdvertisements : advertisement.getNewsPaperAdvertisement()) {
                Integer newspaperId = newsPapersAdvertisements.getNewsPaper().getId();
                selectedNewspaperId.add(newspaperId);
                advertisementDto.setSelectedNewspapers(selectedNewspaperId);
                advertisementDto.setNumberOfUnits(newsPapersAdvertisements.getNumberOfUnits());
                advertisementDto.setPublishedDate(newsPapersAdvertisements.getPublished_at());
            }
        }
        return advertisementDto;
    }

    public boolean removeAdvertisement(Integer advertisementId) throws ItemNotFoundException {
        Advertisement advertisement = advertisementDao.getAdvertisementById(advertisementId);

        if (null == advertisement) {
            throw new ItemNotFoundException("Advertisement with id" + advertisementId + " not found.");
        }
        advertisementDao.removeAdvertisement(advertisement);
        return true;
    }

    public List<AdvertisementDto> listAdvertisements() {
        List<Advertisement> advertisementsList = advertisementDao.listAdvertisements();
        List<AdvertisementDto> advertisementDtoList = new ArrayList<AdvertisementDto>();
        for (Advertisement advertisement : advertisementsList) {
            AdvertisementDto advertisementDto = new AdvertisementDto();
            advertisementDto.setId(advertisement.getId());
            advertisementDto.setTitle(advertisement.getTitle());
            advertisementDto.setDescription(advertisement.getDescription());
            advertisementDto.setReferenceCode(advertisement.getReferenceId());
            advertisementDto.setCreatedDate(advertisement.getCreated_at());
            advertisementDto.setUpdatedDate(advertisement.getUpdated_at());

            List<Integer> selectedNewspaperId = new ArrayList<Integer>();
            if (null != advertisement.getNewsPaperAdvertisement()
                    && advertisement.getNewsPaperAdvertisement().size() > 0) {
                for (NewsPapersAdvertisements newsPapersAdvertisements : advertisement.getNewsPaperAdvertisement()) {
                    Integer newspaperId = newsPapersAdvertisements.getNewsPaper().getId();
                    selectedNewspaperId.add(newspaperId);
                    advertisementDto.setSelectedNewspapers(selectedNewspaperId);
                    advertisementDto.setNumberOfUnits(newsPapersAdvertisements.getNumberOfUnits());
                    advertisementDto.setPublishedDate(newsPapersAdvertisements.getPublished_at());
                }
            }
            advertisementDtoList.add(advertisementDto);
        }
        return advertisementDtoList;
    }

    public List<PublishedAdvtDto> listPublishedAdvertisements() {

        List<NewsPapersAdvertisements> publishedAddList = advertisementDao.getAllPublishedAdvertisement();
        List<PublishedAdvtDto> advertDtoList = new ArrayList<PublishedAdvtDto>();

        for (NewsPapersAdvertisements newsPapersAdvertisements : publishedAddList) {
            PublishedAdvtDto advertisementDto = new PublishedAdvtDto();
            advertisementDto.setTitle(newsPapersAdvertisements.getAdvertisement().getTitle());
            advertisementDto.setNewspaperName(newsPapersAdvertisements.getNewsPaper().getName());
            advertisementDto.setNewspaperCode(newsPapersAdvertisements.getNewsPaper().getCode());
            advertisementDto.setPublishedDate(newsPapersAdvertisements.getPublished_at());
            advertDtoList.add(advertisementDto);
        }
        return advertDtoList;
    }
}
