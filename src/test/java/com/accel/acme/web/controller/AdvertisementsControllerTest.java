package com.accel.acme.web.controller;

import com.accel.acme.web.dto.AdvertisementDto;
import com.accel.acme.web.dto.PublishedAdvtDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.service.AdvertisementService;
import com.accel.acme.web.test.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static com.accel.acme.web.common.Constants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
public class AdvertisementsControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Mock
    private AdvertisementService advertisementServiceMock;

    @InjectMocks
    @Autowired
    private AdvertisementsController advertisementController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void ACMENavigationToHome_ShouldAddPublishedAdvtEntriesToModelAndRenderHomeView() throws Exception {

        List<PublishedAdvtDto> expectedList = new ArrayList<PublishedAdvtDto>();
        when(advertisementServiceMock.listPublishedAdvertisements()).thenReturn(expectedList);

        mockMvc.perform(get(HOME_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME))
                .andExpect(forwardedUrl("/WEB-INF/pages/home.jsp"))
                .andExpect(model().attributeExists("publishedAdvtList"))
                .andExpect(model().attribute("publishedAdvtList", hasSize(expectedList.size())));


        verify(advertisementServiceMock, times(1)).listPublishedAdvertisements();
        verifyNoMoreInteractions(advertisementServiceMock);
    }

    @Test
    public void ACMENavigationToAddAdvertisement_ShouldRenderAdvetisementFormView() throws Exception {

        mockMvc.perform(get(ADD_ADVERTISEMENT_URL)).andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute("advertisement", hasProperty("id", nullValue())))
                .andExpect(model().attribute("advertisement", hasProperty("title", isEmptyOrNullString())))
                .andExpect(model().attribute("advertisement", hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute("advertisement", hasProperty("numberOfUnits", isEmptyOrNullString())));
    }


    @Test
    public void addingNewAdvertisementWithValidDataShouldRespondWithSuccessPageView() throws Exception {


        String title = "Advt Title";
        String referenceCode = "Adv Code";
        String description = "Advt descr";
        String numberOfUnits = "5";
        String clientId = "1";
        when(advertisementServiceMock.addAdvertisement(isA(AdvertisementDto.class))).thenReturn(true);

        mockMvc.perform(
                post(ADD_ADVERTISEMENT_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title).param("referenceCode", referenceCode)
                        .param("description", description).param("numberOfUnits", numberOfUnits)
                        .param("clientId", clientId))
                .andExpect(status().isOk()).andExpect(view().name(VIEW_SUCCESS))
                .andExpect(forwardedUrl("/WEB-INF/pages/success.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ITEM, equalTo("Advertisement")))
                .andExpect(model().attribute(MODEL_ATTR_ITEMIDENTITY, equalTo(title)))
                .andExpect(model().attribute(MODEL_ATTR_ACTION, equalTo("added")))
                .andExpect(model().attribute(MODEL_ATTR_ADDNEWURL, equalTo(ADD_ADVERTISEMENT_URL)));

    }

    @Test
    public void addingNewAdvertisementWithInValidAdvertisementTitleShouldRespondWithViewListingErrorMessages() throws
            Exception {

        mockMvc.perform(
                post(ADD_ADVERTISEMENT_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "Description")
                        .param("referenceCode", "ABCD" + System.currentTimeMillis())
                        .param("numberOfUnits", "5")).andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("Advertisement Title " +
                        "Required.")));

    }

    @Test
    public void addingNewAdvertisementWithInValidAdvertisementDescriptionShouldRespondWithViewListingErrorMessages()
            throws Exception {

        mockMvc.perform(
                post(ADD_ADVERTISEMENT_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Advt Title")
                        .param("referenceCode", "ABCD" + System.currentTimeMillis())
                        .param("numberOfUnits", "5")).andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("Advertisement " +
                        "description is required.")));

    }

    @Test
    public void addingNewAdvertisementWithInValidAdvertisementReferenceCodeShouldRespondWithViewListingErrorMessages
            () throws Exception {

        mockMvc.perform(
                post(ADD_ADVERTISEMENT_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Advt Title")
                        .param("description", "Description")
                        .param("numberOfUnits", "5")).andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("Advertisement reference" +
                        " Code Required.")));

    }

    @Test
    public void addingNewAdvertisementWithInValidAdvertisementNumberofUnitsShouldRespondWithViewListingErrorMessages
            () throws Exception {

        mockMvc.perform(
                post(ADD_ADVERTISEMENT_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Advt Title")
                        .param("description", "Description")
                        .param("referenceCode", "ABCD" + System.currentTimeMillis()))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("Invalid number of unit" +
                        ".")));

    }

    @Test
    public void addingNewAdvertisementWithAlreadyExistingReferenceCodeShouldRespondWithViewListingErrorMessages()
            throws Exception {

        when(advertisementServiceMock.addAdvertisement(isA(AdvertisementDto.class))).thenThrow(new
                ItemAlreadyExistException("Advertisement Already Exist"));

        mockMvc.perform(
                post(ADD_ADVERTISEMENT_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Advt Title")
                        .param("description", "Description")
                        .param("referenceCode", "ABCD")
                        .param("numberOfUnits", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("Advertisement Already " +
                        "Exist")));

    }

    @Test
    public void
    ACMENavigationToChangeAdvertisement_ShouldRenderChangeAdvertisementFormFilledWithSelectedAdvertisementData()
            throws Exception {

        int advtId = 1;
        AdvertisementDto expectedDto = TestUtil.buildAdvertisementDtoById(advtId);

        when(advertisementServiceMock.getAdvertisementById(advtId)).thenReturn(expectedDto);

        mockMvc.perform(get(CHANGE_ADVERTISEMENT_URL, advtId))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attributeExists("advertisement"))
                .andExpect(model().attribute(MODEL_ATTR_ADVERTISEMENT, hasProperty("id", is(expectedDto
                        .getId()))))
                .andExpect(model().attribute(MODEL_ATTR_ADVERTISEMENT, hasProperty("title", is(expectedDto
                        .getTitle()))))
                .andExpect(model().attribute(MODEL_ATTR_ADVERTISEMENT, hasProperty("referenceCode", is
                        (expectedDto.getReferenceCode()))))
                .andExpect(model().attribute(MODEL_ATTR_ADVERTISEMENT, hasProperty("description", is
                        (expectedDto.getDescription()))))
                .andExpect(model().attribute(MODEL_ATTR_ADVERTISEMENT, hasProperty("numberOfUnits", is
                        (expectedDto.getNumberOfUnits()))));

    }

    @Test
    public void changeAdvertisementWithValidAdvertisementDataShouldRespondWithSuccessPageView() throws Exception {

        int advtId = 1;
        when(advertisementServiceMock.updateAdvertisement(isA(AdvertisementDto.class))).thenReturn(true);

        mockMvc.perform(
                post(CHANGE_ADVERTISEMENT_URL, advtId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Adv Title")
                        .param("referenceCode", "ABC")
                        .param("description", "Adv description")
                        .param("numberOfUnits", "4")
                        .param("contactNum", "9" + System.currentTimeMillis())
                        .param("contactName", "abc"))

                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_SUCCESS))
                .andExpect(forwardedUrl("/WEB-INF/pages/success.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ITEM, equalTo("Advertisement")))
                .andExpect(model().attribute(MODEL_ATTR_ITEMIDENTITY, equalTo("Adv Title")))
                .andExpect(model().attribute(MODEL_ATTR_ACTION, equalTo("updated")))
                .andExpect(model().attribute(MODEL_ATTR_ADDNEWURL, equalTo(ADD_ADVERTISEMENT_URL)));

    }

    @Test
    public void navigationToAdvertisement_ThatNotExistsShouldRenderAdvertisementNotFoundViewWith() throws Exception {

        int nonExistingAdvertisementId = 100;
        when(advertisementServiceMock.getAdvertisementById(nonExistingAdvertisementId)).thenThrow(new
                ItemNotFoundException(""));

        mockMvc.perform(get(CHANGE_ADVERTISEMENT_URL, nonExistingAdvertisementId)).andExpect(status()
                .isNotFound())
                .andExpect(view().name(VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/pages/error/404.jsp"));

        verify(advertisementServiceMock, times(1)).getAdvertisementById(nonExistingAdvertisementId);
        verifyZeroInteractions(advertisementServiceMock);
    }

    @Test
    public void changeAdvertisementWithInvalidPriceShouldRespondWithViewListingErrorMessages() throws Exception {

        Integer advtId = 1;
        String title = "Advt Name";
        String description = "ADvt Code";
        String referenceCode = "ABC";
        Integer numberOfUnits = -7;
        mockMvc.perform(
                post(CHANGE_ADVERTISEMENT_URL, advtId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("description", description)
                        .param("referenceCode", referenceCode)
                        .param("numberOfUnits", numberOfUnits.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("Invalid number of unit" +
                        ".")));
    }

    @Test
    public void changingNewspaperNameToAlreadyExistingNewspapernameShouldRenderFormListingErrorMessages() throws
            Exception {

        Integer advtId = 1;
        String title = "Advt Name";
        String description = "ADvt Code";
        String referenceCode = "ABC";
        Integer numberOfUnits = 7;


        when(advertisementServiceMock.updateAdvertisement(isA(AdvertisementDto.class))).thenThrow(new
                ItemAlreadyExistException("The Advertisement is already Exist"));

        mockMvc.perform(
                post(CHANGE_ADVERTISEMENT_URL, advtId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("description", description)
                        .param("referenceCode", referenceCode)
                        .param("numberOfUnits", numberOfUnits.toString()))


                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ADVERTISEMENT_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/advertisementsform.jsp"))
                .andExpect(model().attribute(MODEL_ATTR_ERRORS, containsInAnyOrder("The Advertisement is " +
                        "already Exist")));
    }
}
