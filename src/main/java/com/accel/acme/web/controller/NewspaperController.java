package com.accel.acme.web.controller;

import com.accel.acme.web.dto.NewspaperDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.service.NewspaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static com.accel.acme.web.common.Constants.*;

@Controller
public class NewspaperController {

    @Autowired
    private NewspaperService newspaperService;

    @RequestMapping(value = NEWSPAPERS_URL, method = RequestMethod.GET)
    public ModelAndView handleNewspapersPageNavigation() {
        ModelAndView model = new ModelAndView(NEWSPAPERS);
        model.addObject(MODEL_ATTR_NEWSPAPERLIST, newspaperService.listNewspapers());
        return model;
    }

    @RequestMapping(value = ADD_NEWSPAPER_URL, method = RequestMethod.GET)
    public ModelAndView renderModelandViewForNewNewspaper() {
        return buildNewspaperAddChangeForm(new NewspaperDto(), true);
    }

    @RequestMapping(value = ADD_NEWSPAPER_URL, method = RequestMethod.POST)
    public ModelAndView handleAddNewNewsPaperFormSubmission(
            @ModelAttribute(MODEL_ATTR_NEWSPAPER) NewspaperDto newspaperDto) {
        List<String> validationErrorsList = listErrorsInNewspaperDto(newspaperDto);
        try {
            if (validationErrorsList.isEmpty() && newspaperService.addNewspaper(newspaperDto))
                return buildSuccessView(newspaperDto, true);
            return buildViewModelWithErrorMessage(true, newspaperDto, validationErrorsList);
        } catch (ItemAlreadyExistException e) {
            validationErrorsList.add(e.getMessage());
            return buildViewModelWithErrorMessage(true, newspaperDto, validationErrorsList);
        }
    }

    @RequestMapping(value = CHANGE_NEWSPAPER_URL, method = RequestMethod.GET)
    public ModelAndView renderAModelandViewForChangeNewspapers(@PathVariable("newspaperId") int newspaperId)
            throws ItemNotFoundException {
        NewspaperDto newspaperDto = newspaperService.getNewspaperById(newspaperId);
        return buildNewspaperAddChangeForm(newspaperDto, false);

    }

    private ModelAndView buildNewspaperAddChangeForm(NewspaperDto newspaperDto, boolean isAdd) {
        ModelAndView model = new ModelAndView(VIEW_NEWSPAPER_FORM);

        String title = isAdd ? "Add Newspaper" : "Change Newspaper";
        model.addObject(MODEL_ATTR_PAGETITLE, ACME_TITLE + title);
        model.addObject(MODEL_ATTR_FORMTITLE, title);
        model.addObject(MODEL_ATTR_LANGUAGELIST, LANGUAGE_LIST);
        model.addObject(MODEL_ATTR_NEWSPAPER, newspaperDto);
        return model;
    }

    @RequestMapping(value = CHANGE_NEWSPAPER_URL, method = RequestMethod.POST)
    public ModelAndView handleChangeNewsPaperFormSubmission(@PathVariable("newspaperId") int newspaperId,
                                                            @ModelAttribute(MODEL_ATTR_NEWSPAPER)
                                                            NewspaperDto newspaperDto) throws ItemNotFoundException {
        List<String> validationErrorsList = listErrorsInNewspaperDto(newspaperDto);
        try {
            newspaperDto.setId(newspaperId);
            if (validationErrorsList.isEmpty() && newspaperService.updateNewspaper(newspaperDto))
                return buildSuccessView(newspaperDto, false);
            return buildViewModelWithErrorMessage(false, newspaperDto, validationErrorsList);
        } catch (ItemAlreadyExistException e) {
            validationErrorsList.add(e.getMessage());
            return buildViewModelWithErrorMessage(false, newspaperDto, validationErrorsList);
        }
    }

    private ModelAndView buildSuccessView(NewspaperDto newspaperDto, boolean isAdd) {
        ModelAndView model = new ModelAndView(VIEW_SUCCESS);
        model.addObject(MODEL_ATTR_ITEM, "newspaper");
        model.addObject(MODEL_ATTR_ITEMIDENTITY, newspaperDto.getName());
        model.addObject(MODEL_ATTR_ACTION, isAdd ? "added" : "updated");
        model.addObject(MODEL_ATTR_ADDNEWURL, ADD_NEWSPAPER_URL);

        return model;
    }

    private ModelAndView buildViewModelWithErrorMessage(boolean isAdd, NewspaperDto newspaperDto,
                                                        List<String> validationErrorsList) {
        String title = isAdd ? "Add Newspaper" : "Change Newspaper";
        ModelAndView model = new ModelAndView(VIEW_NEWSPAPER_FORM);
        model.addObject(MODEL_ATTR_PAGETITLE, ACME_TITLE + title);
        model.addObject(MODEL_ATTR_FORMTITLE, title);
        model.addObject(MODEL_ATTR_NEWSPAPER, newspaperDto);
        model.addObject(MODEL_ATTR_LANGUAGELIST, LANGUAGE_LIST);
        model.addObject(MODEL_ATTR_ERRORS, validationErrorsList);

        return model;
    }

    private List<String> listErrorsInNewspaperDto(NewspaperDto newspaperDto) {
        List<String> newspaperErrorList = new ArrayList<String>();

        if (null == newspaperDto.getName() || newspaperDto.getName().trim().isEmpty())
            newspaperErrorList.add("Newspaper Name Required.");
        if (null == newspaperDto.getCode() || newspaperDto.getCode().trim().isEmpty())
            newspaperErrorList.add("Newspaper Code Required.");
        String selectedLanguage = newspaperDto.getLanguage();
        if (null == selectedLanguage || selectedLanguage.trim().isEmpty() || NONE.equals(selectedLanguage))
            newspaperErrorList.add("Please select a language.");
        if (null == newspaperDto.getRatePerUnit() || newspaperDto.getRatePerUnit().isNaN()
                || newspaperDto.getRatePerUnit() < 0)
            newspaperErrorList.add("Invalid rate per unit.");

        return newspaperErrorList;
    }

}
