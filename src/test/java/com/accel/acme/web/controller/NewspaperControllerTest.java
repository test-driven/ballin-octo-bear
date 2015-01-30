package com.accel.acme.web.controller;

import com.accel.acme.web.common.Constants;
import com.accel.acme.web.dto.NewspaperDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.service.NewspaperService;
import com.accel.acme.web.test.NewspaperDtoBuilder;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
@WebAppConfiguration
public class NewspaperControllerTest {

    // Create Mock
    @Mock
    private NewspaperService newspaperServiceMock;

    @InjectMocks
    @Autowired
    private NewspaperController newspaperController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void ACMENavigationToNewspapers_ShouldAddNewspapersEntriesToModelAndRenderNewspaperListView() throws
            Exception {
        NewspaperDto newspaper1 = new NewspaperDtoBuilder().id(1)
                .name("Newspaper1")
                .code("NP1")
                .language("English")
                .ratePerUnit(5f)
                .build();
        NewspaperDto newspaper2 = new NewspaperDtoBuilder().id(2)
                .name("Newspaper2")
                .code("NP2")
                .language("Hindi")
                .ratePerUnit(3f)
                .build();

        when(newspaperServiceMock.listNewspapers()).thenReturn(Arrays.asList(newspaper1, newspaper2));

        mockMvc.perform(get(Constants.NEWSPAPERS_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.NEWSPAPERS))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapers.jsp"))
                .andExpect(model().attributeExists(Constants.MODEL_ATTR_NEWSPAPERLIST))

                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPERLIST, hasItem(
                        allOf(
                                hasProperty("id", is(newspaper1.getId())),
                                hasProperty("name", is(newspaper1.getName())),
                                hasProperty("code", is(newspaper1.getCode())),
                                hasProperty("language", is(newspaper1.getLanguage())),
                                hasProperty("ratePerUnit", is(newspaper1.getRatePerUnit()))
                        )
                )))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPERLIST, hasItem(
                        allOf(
                                hasProperty("id", is(newspaper2.getId())),
                                hasProperty("name", is(newspaper2.getName())),
                                hasProperty("code", is(newspaper2.getCode())),
                                hasProperty("language", is(newspaper2.getLanguage())),
                                hasProperty("ratePerUnit", is(newspaper2.getRatePerUnit()))
                        )
                )));

        verify(newspaperServiceMock, times(1)).listNewspapers();
        verifyNoMoreInteractions(newspaperServiceMock);
    }

    @Test
    public void ACMENavigationToAddNewNewspaper_ShouldRenderNewspaperFormViewWithEmptyFields() throws Exception {

        mockMvc.perform(get(Constants.ADD_NEWSPAPER_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("id", nullValue())))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("name", isEmptyOrNullString
                        ())))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("code", isEmptyOrNullString
                        ())))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("language",
                        isEmptyOrNullString())))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("ratePerUnit",
                        isEmptyOrNullString())))
                .andExpect(model().attribute(Constants.MODEL_ATTR_LANGUAGELIST, is(Constants.LANGUAGE_LIST)));
    }

    @Test
    public void addingNewNewspaperWithValidDataShouldRenderSuccessPageHavingCreatedNewspaperNameViewHavingLinks()
            throws Exception {
        String npname = "NP Name";
        String npcode = "NP Code";
        String nplanguage = "English";
        Float rate = 55.5f;

        when(newspaperServiceMock.addNewspaper(isA(NewspaperDto.class))).thenReturn(true);

        mockMvc.perform(
                post(Constants.ADD_NEWSPAPER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", npname)
                        .param("code", npcode)
                        .param("language", nplanguage)
                        .param("ratePerUnit", rate.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_SUCCESS))
                .andExpect(forwardedUrl("/WEB-INF/pages/success.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ITEM, equalTo("newspaper")))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ITEMIDENTITY, equalTo(npname)))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ACTION, equalTo("added")))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ADDNEWURL, equalTo(Constants.ADD_NEWSPAPER_URL)));
    }

    @Test
    public void addingNewNewspaperWithNoNewspapernameShouldRespondWithViewListingErrorMessages() throws Exception {
        mockMvc.perform(
                post(Constants.ADD_NEWSPAPER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("code", "NP Code")
                        .param("language", "English")
                        .param("ratePerUnit", "55.5"))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder("Newspaper Name Required" +
                        ".")));
    }

    @Test
    public void addingNewNewspaperWithEmptyNewspaperCodeShouldRespondWithViewListingErrorMessages() throws Exception {
        mockMvc.perform(
                post(Constants.ADD_NEWSPAPER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "NP Name")
                        .param("code", "")
                        .param("language", "English")
                        .param("ratePerUnit", "55.5"))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder("Newspaper Code Required" +
                        ".")));
    }

    @Test
    public void addingNewNewspaperWithNoNewspaperLanguageShouldRespondWithViewListingErrorMessages() throws Exception {
        mockMvc.perform(
                post(Constants.ADD_NEWSPAPER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "NP Name").param("code", "NP Code").param("language", "")
                        .param("ratePerUnit", "55.5"))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder("Please select a " +
                        "language.")));
    }

    @Test
    public void addingNewNewspaperWithInvalidPriceShouldRespondWithViewListingErrorMessages() throws Exception {

        mockMvc.perform(
                post(Constants.ADD_NEWSPAPER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "NP Name")
                        .param("code", "NP Code")
                        .param("language", "English")
                        .param("ratePerUnit", "-5.5"))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder("Invalid rate per unit" +
                        ".")));
    }

    @Test
    public void addingNewspaperWhichAlreadyExistsShouldRespondWithTheFormListingErrorMessages()
            throws Exception {

        String npname = "NP Name";
        String npcode = "NP Code";
        String nplanguage = "English";
        Float rate = 55.5f;

        when(newspaperServiceMock.addNewspaper(isA(NewspaperDto.class))).thenThrow(new ItemAlreadyExistException("The" +
                " newspaper Already exists"));

        mockMvc.perform(
                post(Constants.ADD_NEWSPAPER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", npname)
                        .param("code", npcode)
                        .param("language", nplanguage)
                        .param("ratePerUnit", rate.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder("The newspaper Already " +
                        "exists")));
    }

    @Test
    public void
    ACMENavigationToChangeNewsPaper_ShouldRenderChangeNewsPapersFormViewWithFieldsFilledWithSelectedNewspaperData()
            throws Exception {
        int newspaperIdtoFetch = 1;
        NewspaperDto expectedDto = new NewspaperDtoBuilder().id(newspaperIdtoFetch)
                .name("Newspaper" + newspaperIdtoFetch)
                .code("NP" + newspaperIdtoFetch)
                .language("English")
                .ratePerUnit(5f)
                .build();

        when(newspaperServiceMock.getNewspaperById(newspaperIdtoFetch)).thenReturn(expectedDto);

        mockMvc.perform(get(Constants.CHANGE_NEWSPAPER_URL, newspaperIdtoFetch))
                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("id", is(expectedDto.getId()
                ))))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("name", is(expectedDto
                        .getName()))))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("code", is(expectedDto
                        .getCode()))))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("language", is(expectedDto
                        .getLanguage()))))
                .andExpect(model().attribute(Constants.MODEL_ATTR_NEWSPAPER, hasProperty("ratePerUnit", is
                        (expectedDto.getRatePerUnit()))));
    }

    @Test
    public void changeNewspaperWithValidDataShouldRespondWithSuccessPageView() throws Exception {
        Integer newspaperId = 1;
        String npname = "NP New Name";
        String npcode = "NP Code";
        String nplanguage = "English";
        Float rate = 55.5f;

        when(newspaperServiceMock.updateNewspaper(isA(NewspaperDto.class))).thenReturn(true);

        mockMvc.perform(
                post(Constants.CHANGE_NEWSPAPER_URL, newspaperId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", npname)
                        .param("code", npcode)
                        .param("language", nplanguage)
                        .param("ratePerUnit", rate.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_SUCCESS))
                .andExpect(forwardedUrl("/WEB-INF/pages/success.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ITEM, equalTo("newspaper")))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ITEMIDENTITY, equalTo(npname)))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ACTION, equalTo("updated")))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ADDNEWURL, equalTo(Constants.ADD_NEWSPAPER_URL)));
    }

    @Test
    public void navigationToNewsPaper_ThatNotExistsShouldRenderNewspaperNotFoundViewWith() throws Exception {

        int nonExistingNewspaperId = 100;
        when(newspaperServiceMock.getNewspaperById(nonExistingNewspaperId)).thenThrow(new ItemNotFoundException(""));

        mockMvc.perform(get(Constants.CHANGE_NEWSPAPER_URL, nonExistingNewspaperId)).andExpect(status().isNotFound())
                .andExpect(view().name(Constants.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/pages/error/404.jsp"));

        verify(newspaperServiceMock, times(1)).getNewspaperById(nonExistingNewspaperId);
        verifyZeroInteractions(newspaperServiceMock);
    }

    @Test
    public void changingNewspaperNameToAlreadyExistingNewspapernameShouldRenderFormListingErrorMessages() throws
            Exception {
        Integer newspaperId = 1;
        String npname = "NP Name";
        String npcode = "NP Code";
        String nplanguage = "English";
        Float rate = 55.5f;

        String message = "Newspaper with name " + npname + " already exists.";

        when(newspaperServiceMock.updateNewspaper(isA(NewspaperDto.class))).thenThrow(new ItemAlreadyExistException
                (message));

        mockMvc.perform(
                post(Constants.CHANGE_NEWSPAPER_URL, newspaperId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", npname)
                        .param("code", npcode)
                        .param("language", nplanguage)
                        .param("ratePerUnit", rate.toString()))


                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder(message)));
    }

    @Test
    public void changeNewNewspaperWithInvalidPriceShouldRespondWithViewListingErrorMessages() throws Exception {
        Integer newspaperId = 1;
        String npname = "NP Name";
        String npcode = "NP Code";
        String nplanguage = "English";
        Float rate = -14f;
        mockMvc.perform(
                post(Constants.CHANGE_NEWSPAPER_URL, newspaperId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", npname)
                        .param("code", npcode)
                        .param("language", nplanguage)
                        .param("ratePerUnit", rate.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name(Constants.VIEW_NEWSPAPER_FORM))
                .andExpect(forwardedUrl("/WEB-INF/pages/newspapersform.jsp"))
                .andExpect(model().attribute(Constants.MODEL_ATTR_ERRORS, containsInAnyOrder("Invalid rate per unit" +
                        ".")));
    }
}
