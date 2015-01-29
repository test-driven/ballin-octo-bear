package com.accel.acme.web.service;

import com.accel.acme.web.dao.NewspaperDao;
import com.accel.acme.web.dto.NewspaperDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.model.Newspaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NewspaperService {

    @Autowired
    private NewspaperDao newspaperDao;

    public boolean addNewspaper(NewspaperDto newspaperDto) throws ItemAlreadyExistException {

        if (null != newspaperDao.getNewspaperByCode(newspaperDto.getCode()))
            throw new ItemAlreadyExistException("Newspaper with code " + newspaperDto.getCode() + " already exists.");
        if (null != newspaperDao.getNewspaperByName(newspaperDto.getName()))
            throw new ItemAlreadyExistException("Newspaper with name " + newspaperDto.getName() + " already exists.");

        Date now = new Date();
        Newspaper newspaper = new Newspaper();

        newspaper.setCode(newspaperDto.getCode());
        newspaper.setLanguage(newspaperDto.getLanguage());
        newspaper.setName(newspaperDto.getName());
        newspaper.setRatePerUnit(newspaperDto.getRatePerUnit());
        newspaper.setCreated_at(now);
        newspaper.setUpdated_at(now);

        return newspaperDao.addNewspaper(newspaper);
    }

    public boolean updateNewspaper(NewspaperDto updatedNewspaperDto) throws ItemNotFoundException,
            ItemAlreadyExistException {

        Newspaper selectedNewspaper = newspaperDao.getNewspaperById(updatedNewspaperDto.getId());

        if (null == selectedNewspaper)
            throw new ItemNotFoundException("Newspaper with id " + updatedNewspaperDto.getId() + " not found.");

        verifyUpdatedNewspaperNameandCode(selectedNewspaper, updatedNewspaperDto);

        selectedNewspaper.setCode(updatedNewspaperDto.getCode());
        selectedNewspaper.setLanguage(updatedNewspaperDto.getLanguage());
        selectedNewspaper.setName(updatedNewspaperDto.getName());
        selectedNewspaper.setRatePerUnit(updatedNewspaperDto.getRatePerUnit());
        selectedNewspaper.setUpdated_at(new Date());

        return newspaperDao.updateNewspaper(selectedNewspaper);
    }

    private void verifyUpdatedNewspaperNameandCode(Newspaper selectedNewspaper, NewspaperDto updatedNewspaperDto)
            throws ItemAlreadyExistException {

        Newspaper newspaper = newspaperDao.getNewspaperByName(updatedNewspaperDto.getName());

        if (null != newspaper && newspaper.getId() != selectedNewspaper.getId())
            throw new ItemAlreadyExistException("Newspaper with name " + updatedNewspaperDto.getName() + " already " +
                    "exists.");

        newspaper = newspaperDao.getNewspaperByCode(updatedNewspaperDto.getCode());
        if (null != newspaper && newspaper.getId() != selectedNewspaper.getId())
            throw new ItemAlreadyExistException("Newspaper with code " + updatedNewspaperDto.getCode() + " already " +
                    "exists.");
    }

    public NewspaperDto getNewspaperById(Integer newspaperId) throws ItemNotFoundException {

        Newspaper newspaper = newspaperDao.getNewspaperById(newspaperId);

        if (null == newspaper)
            throw new ItemNotFoundException("Newspaper with id " + newspaperId + " not found.");
        return buildDtoFromModel(newspaper);
    }

    public boolean removeNewspaper(Integer newspaperId) throws ItemNotFoundException {
        Newspaper newspaper = newspaperDao.getNewspaperById(newspaperId);

        if (null == newspaper)
            throw new ItemNotFoundException("Newspaper with id " + newspaperId + " not found.");

        return newspaperDao.removeNewspaper(newspaper);

    }

    public List<NewspaperDto> listNewspapers() {

        List<NewspaperDto> newspapersList = new ArrayList<NewspaperDto>();
        for (Newspaper newspaper : newspaperDao.listNewspapers())
            newspapersList.add(buildDtoFromModel(newspaper));

        return newspapersList;
    }

    private NewspaperDto buildDtoFromModel(Newspaper newspaper) {
        NewspaperDto newspaperDto = new NewspaperDto();
        newspaperDto.setId(newspaper.getId());
        newspaperDto.setCode(newspaper.getCode());
        newspaperDto.setLanguage(newspaper.getLanguage());
        newspaperDto.setName(newspaper.getName());
        newspaperDto.setRatePerUnit(newspaper.getRatePerUnit());

        return newspaperDto;
    }

}
