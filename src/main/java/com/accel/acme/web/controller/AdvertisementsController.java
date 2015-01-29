package com.accel.acme.web.controller;

import com.accel.acme.web.dto.AdvertisementDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.service.AdvertisementService;
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
public class AdvertisementsController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private NewspaperService newspaperService;

    @RequestMapping(value = {ACME_ROOT, HOME_URL}, method = RequestMethod.GET)
    public ModelAndView handleHomePageNavigation() {
        ModelAndView model = new ModelAndView(HOME);
        model.addObject("publishedAdvtList", advertisementService.listPublishedAdvertisements());
        return model;
    }

    @RequestMapping(value = ADVERTISEMENTS_URL, method = RequestMethod.GET)
    public ModelAndView handleAdvertisementsPageNavigation() {
        ModelAndView model = new ModelAndView(ADVERTISEMENTS);
        model.addObject("advertisementList", advertisementService.listAdvertisements());
        return model;
    }

    @RequestMapping(value = ADD_ADVERTISEMENT_URL, method = RequestMethod.GET)
    public ModelAndView renderAModelandViewForNewAdvertisement() {
        return buildAdvertisementAddChangeForm(new AdvertisementDto(), true);
    }

    @RequestMapping(value = ADD_ADVERTISEMENT_URL, method = RequestMethod.POST)
    public ModelAndView handleAddNewAdvertisementFormSubmission(
            @ModelAttribute("advertisement") AdvertisementDto advertisementDto) {

        return validateInputandBuildResultView(advertisementDto);
    }

    private ModelAndView validateInputandBuildResultView(AdvertisementDto advertisementDto) {

        List<String> advertisementValidationErrorsList = listErrorsInAdvertisementDto(advertisementDto);
        try {
            if (advertisementValidationErrorsList.isEmpty()) {
                advertisementService.addAdvertisement(advertisementDto);
                return buildSuccessView(advertisementDto, true);
            }
            return buildViewModelWithErrorMessage(true, advertisementDto, advertisementValidationErrorsList);
        } catch (ItemAlreadyExistException ex) {
            advertisementValidationErrorsList.add(ex.getMessage());
            return buildViewModelWithErrorMessage(true, advertisementDto, advertisementValidationErrorsList);
        }
    }

    @RequestMapping(value = CHANGE_ADVERTISEMENT_URL, method = RequestMethod.GET)
    public ModelAndView renderAModelandViewForChangeAdvertisement(@PathVariable("advertisementId") int advertisementId)
            throws ItemNotFoundException {

        AdvertisementDto advertisementDto = advertisementService.getAdvertisementById(advertisementId);
        return buildAdvertisementAddChangeForm(advertisementDto, false);

    }

    @RequestMapping(value = CHANGE_ADVERTISEMENT_URL, method = RequestMethod.POST)
    public ModelAndView handleChangeAdvertisementFormSubmission(@PathVariable("advertisementId") int advertisementId,
                                                                @ModelAttribute(MODEL_ATTR_ADVERTISEMENT)
                                                                AdvertisementDto advertisementDto)
            throws ItemNotFoundException {

        List<String> validationErrorsList = listErrorsInAdvertisementDto(advertisementDto);
        try {
            advertisementDto.setId(advertisementId);
            if (validationErrorsList.isEmpty() && advertisementService.updateAdvertisement(advertisementDto))
                return buildSuccessView(advertisementDto, false);
            return buildViewModelWithErrorMessage(false, advertisementDto, validationErrorsList);
        } catch (ItemAlreadyExistException e) {
            validationErrorsList.add(e.getMessage());
            return buildViewModelWithErrorMessage(false, advertisementDto, validationErrorsList);
        }
    }

    private ModelAndView buildViewModelWithErrorMessage(boolean isAdd, AdvertisementDto advertisementDto,
                                                        List<String> validationErrorsList) {
        String title = isAdd ? "Add Advertisement" : "Change Advertisement";
        ModelAndView model = new ModelAndView(VIEW_ADVERTISEMENT_FORM);

        model.addObject(MODEL_ATTR_PAGETITLE, ACME_TITLE + title);
        model.addObject(MODEL_ATTR_FORMTITLE, title);
        model.addObject(MODEL_ATTR_ADVERTISEMENT, advertisementDto);
        model.addObject(MODEL_ATTR_ERRORS, validationErrorsList);
        model.addObject(MODEL_ATTR_NEWSPAPERLIST, newspaperService.listNewspapers());
        model.addObject("errors", validationErrorsList);

        return model;
    }

    private ModelAndView buildAdvertisementAddChangeForm(AdvertisementDto advertisementDto, boolean isAdd) {
        ModelAndView model = new ModelAndView(VIEW_ADVERTISEMENT_FORM);
        String advttitle = isAdd ? "Add Advertisement" : "Change Advertisement";
        model.addObject(MODEL_ATTR_PAGETITLE, ACME_TITLE + advttitle);
        model.addObject(MODEL_ATTR_FORMTITLE, advttitle);
        model.addObject(MODEL_ATTR_ADVERTISEMENT, advertisementDto);
        model.addObject(MODEL_ATTR_NEWSPAPERLIST, newspaperService.listNewspapers());
        return model;
    }

    private ModelAndView buildSuccessView(AdvertisementDto advertisementDto, boolean isAdd) {
        ModelAndView model = new ModelAndView(VIEW_SUCCESS);
        model.addObject(MODEL_ATTR_ITEM, "Advertisement");
        model.addObject(MODEL_ATTR_ITEMIDENTITY, advertisementDto.getTitle());
        model.addObject(MODEL_ATTR_ACTION, isAdd ? "added" : "updated");
        model.addObject(MODEL_ATTR_NEWSPAPERLIST, newspaperService.listNewspapers());
        model.addObject(MODEL_ATTR_ADDNEWURL, ADD_ADVERTISEMENT_URL);
        return model;
    }

    private List<String> listErrorsInAdvertisementDto(AdvertisementDto advertisementDto) {
        List<String> newspaperErrorList = new ArrayList<String>();

        if (null == advertisementDto.getTitle() || advertisementDto.getTitle().trim().isEmpty())
            newspaperErrorList.add("Advertisement Title Required.");
        if (null == advertisementDto.getReferenceCode() || advertisementDto.getReferenceCode().trim().isEmpty())
            newspaperErrorList.add("Advertisement reference Code Required.");
        if (null == advertisementDto.getDescription() || advertisementDto.getDescription().trim().isEmpty())
            newspaperErrorList.add("Advertisement description is required.");
        if (null == advertisementDto.getNumberOfUnits() || advertisementDto.getNumberOfUnits() < 0)
            newspaperErrorList.add("Invalid number of unit.");

        return newspaperErrorList;

    }
}
