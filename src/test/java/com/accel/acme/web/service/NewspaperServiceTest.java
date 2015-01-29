package com.accel.acme.web.service;

import com.accel.acme.web.dao.NewspaperDao;
import com.accel.acme.web.dto.NewspaperDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.model.Newspaper;
import com.accel.acme.web.test.NewspaperBuilder;
import com.accel.acme.web.test.NewspaperDtoBuilder;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
@WebAppConfiguration
public class NewspaperServiceTest {

    Integer newspaperId = 1;
    String newspaperName = "The Hindu";
    String newspaperCode = "HND-007";
    String newspaperLanguage = "English";
    Float newspaperRate = 5.5f;

    String newspaperNameUpdated = "The Hindu Ex";
    String newspaperCodeUpdated = "HND-001";

    @Mock
    private NewspaperDao newspaperDaoMock;

    @InjectMocks
    private final NewspaperService newspaperService = new NewspaperService();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void adding_NewNewspaperWhichIsNotAlreadyAddedShouldSaveTheNewspaper() throws Exception {

        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .code(newspaperCode)
                .name(newspaperName)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperByCode(newspaperCode)).thenReturn(null);
        when(newspaperDaoMock.getNewspaperByName(newspaperName)).thenReturn(null);

        ArgumentCaptor<Newspaper> nespaperArgumentData = ArgumentCaptor.forClass(Newspaper.class);
        when(newspaperDaoMock.addNewspaper(nespaperArgumentData.capture())).thenReturn(true);

        assertTrue(newspaperService.addNewspaper(newspaperDto));

        verify(newspaperDaoMock, times(1)).addNewspaper(nespaperArgumentData.capture());
        verify(newspaperDaoMock, times(1)).getNewspaperByCode(newspaperDto.getCode());
        verify(newspaperDaoMock, times(1)).getNewspaperByName(newspaperDto.getName());

        Newspaper model = nespaperArgumentData.getValue();

        assertNull(model.getId());
        assertThat(model.getName(), is(newspaperName));
        assertThat(model.getCode(), is(newspaperCode));
        assertThat(model.getLanguage(), is(newspaperLanguage));
        assertThat(model.getRatePerUnit(), is(newspaperRate));
    }

    @Test
    public void adding_NewNewspaperWithCodeAlreadyAddedShouldThrowItemAlreadyExistException() throws Exception {

        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .code(newspaperCode)
                .name(newspaperName)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperByCode(newspaperCode)).thenReturn(new NewspaperBuilder().build());
        try {
            newspaperService.addNewspaper(newspaperDto);
            fail("No exceptions was thrown from  newspaperService.addNewspaper");
        } catch (Exception e) {
            assertTrue(e instanceof ItemAlreadyExistException);
            assertThat(e.getMessage(), is("Newspaper with code " + newspaperCode + " already exists."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperByCode(newspaperCode);
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @Test
    public void addingNewNewspaperWithNameAlreadyAddedShouldThrowItemAlreadyExistException() throws Exception {

        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .code(newspaperCode)
                .name(newspaperName)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();
        when(newspaperDaoMock.getNewspaperByCode(newspaperCode)).thenReturn(null);
        when(newspaperDaoMock.getNewspaperByName(newspaperName)).thenReturn(new NewspaperBuilder().build());
        try {
            newspaperService.addNewspaper(newspaperDto);
            fail("No exceptions was thrown from  newspaperService.addNewspaper");
        } catch (Exception e) {
            assertTrue(e instanceof ItemAlreadyExistException);
            assertThat(e.getMessage(), is("Newspaper with name " + newspaperName + " already exists."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperByName(newspaperName);
        verify(newspaperDaoMock, times(1)).getNewspaperByCode(newspaperCode);
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @Test
    public void updatingAnAlreadyExistingNewspaperShouldUpdateNewspaperEntry() throws Exception {
        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        Newspaper model = new NewspaperBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperById(newspaperDto.getId())).thenReturn(model);
        when(newspaperDaoMock.getNewspaperByCode(newspaperDto.getCode())).thenReturn(null);
        when(newspaperDaoMock.getNewspaperByName(newspaperDto.getName())).thenReturn(null);
        when(newspaperDaoMock.updateNewspaper(model)).thenReturn(true);

        assertTrue(newspaperService.updateNewspaper(newspaperDto));

        verify(newspaperDaoMock, times(1)).getNewspaperById(newspaperDto.getId());
        verify(newspaperDaoMock, times(1)).getNewspaperByCode(newspaperDto.getCode());
        verify(newspaperDaoMock, times(1)).getNewspaperByName(newspaperDto.getName());
        verify(newspaperDaoMock, times(1)).updateNewspaper(model);
        verifyNoMoreInteractions(newspaperDaoMock);

        assertThat(model.getId(), is(newspaperDto.getId()));
        assertThat(model.getName(), is(newspaperDto.getName()));
        assertThat(model.getCode(), is(newspaperDto.getCode()));
        assertThat(model.getLanguage(), is(newspaperDto.getLanguage()));
        assertThat(model.getRatePerUnit(), is(newspaperDto.getRatePerUnit()));
    }

    @Test
    public void updatingANewspaperWithTheNameofAlreadyExistingShouldThrowItemAlreadyExistException() throws Exception {
        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        Newspaper model = new NewspaperBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        Newspaper nameMatchingmodel = new NewspaperBuilder()
                .id(newspaperId + 5)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperById(newspaperDto.getId())).thenReturn(model);
        when(newspaperDaoMock.getNewspaperByName(newspaperDto.getName())).thenReturn(nameMatchingmodel);

        try {
            newspaperService.updateNewspaper(newspaperDto);
            fail("No exceptions was thrown from  newspaperService.updateNewspaper");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemAlreadyExistException.class));
            assertThat(e.getMessage(), is("Newspaper with name " + newspaperDto.getName() + " already exists."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperById(newspaperDto.getId());
        verify(newspaperDaoMock, times(1)).getNewspaperByName(newspaperDto.getName());
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @Test
    public void updatingANewspaperWithTheCodeofAlreadyExistingShouldThrowItemAlreadyExistException() throws Exception {

        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();
        Newspaper model = new NewspaperBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        Newspaper codeMatchingmodel = new NewspaperBuilder()
                .id(newspaperId + 5)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperById(newspaperDto.getId())).thenReturn(model);
        when(newspaperDaoMock.getNewspaperByCode(newspaperDto.getCode())).thenReturn(codeMatchingmodel);

        try {
            newspaperService.updateNewspaper(newspaperDto);
            fail("No exceptions was thrown from  newspaperService.updateNewspaper");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemAlreadyExistException.class));
            assertThat(e.getMessage(), is("Newspaper with code " + newspaperDto.getCode() + " already exists."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperById(newspaperDto.getId());
        verify(newspaperDaoMock, times(1)).getNewspaperByCode(newspaperDto.getCode());
        verify(newspaperDaoMock, times(1)).getNewspaperByName(newspaperDto.getName());
        verifyNoMoreInteractions(newspaperDaoMock);

    }

    @Test
    public void updatingANewspaperThatNotExistShouldThrowItemNotFoundException() throws Exception {
        NewspaperDto newspaperDto = new NewspaperDtoBuilder()
                .id(newspaperId)
                .code(newspaperCodeUpdated)
                .name(newspaperNameUpdated)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperById(newspaperDto.getId())).thenReturn(null);

        try {
            newspaperService.updateNewspaper(newspaperDto);
            fail("No exceptions was thrown from  newspaperService.updateNewspaper");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Newspaper with id " + newspaperDto.getId() + " not found."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperById(newspaperDto.getId());
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @Test
    public void readingNewspaperByIdShouldReturntheCorrespondingNewspaperDto() throws Exception {
        Integer selectedNewspaperId = 1;
        Newspaper expectedNewspaperModel = new NewspaperBuilder()
                .id(selectedNewspaperId)
                .code(newspaperCode)
                .name(newspaperName)
                .language(newspaperLanguage)
                .ratePerUnit(newspaperRate)
                .build();

        when(newspaperDaoMock.getNewspaperById(selectedNewspaperId)).thenReturn(expectedNewspaperModel);

        NewspaperDto resultingDto = newspaperService.getNewspaperById(selectedNewspaperId);

        assertThat(resultingDto.getId(), is(selectedNewspaperId));
        verify(newspaperDaoMock, times(1)).getNewspaperById(selectedNewspaperId);
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @Test
    public void readingNewspaperByIdThatNotExistShouldThrowItemNotFoundException() throws Exception {
        Integer selectedNewspaperId = 1;
        when(newspaperDaoMock.getNewspaperById(selectedNewspaperId)).thenReturn(null);

        try {
            newspaperService.getNewspaperById(selectedNewspaperId);
            fail("No exceptions was thrown from  newspaperService.getNewspaperById");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Newspaper with id " + selectedNewspaperId + " not found."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperById(selectedNewspaperId);
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @Test
    public void newspaperDeleteByIdShouldDeleteTheMatchedNewspaper() throws Exception {
        Integer selectedNewspaperId = 1;
        Newspaper matchingNewspaperModel = new NewspaperBuilder()
                .id(selectedNewspaperId)
                .build();

        when(newspaperDaoMock.getNewspaperById(selectedNewspaperId)).thenReturn(matchingNewspaperModel);
        when(newspaperDaoMock.removeNewspaper(matchingNewspaperModel)).thenReturn(true);

        assertThat(newspaperService.removeNewspaper(selectedNewspaperId), is(true));

        verify(newspaperDaoMock, times(1)).getNewspaperById(selectedNewspaperId);
        verify(newspaperDaoMock, times(1)).removeNewspaper(matchingNewspaperModel);
        verifyNoMoreInteractions(newspaperDaoMock);

    }

    @Test
    public void deletingNewspaperByIdThatNotExistShouldThrowItemNotFoundException() throws Exception {
        Integer selectedNewspaperId = 1;
        when(newspaperDaoMock.getNewspaperById(selectedNewspaperId)).thenReturn(null);

        try {
            newspaperService.removeNewspaper(selectedNewspaperId);
            fail("No exceptions was thrown from  newspaperService.removeNewspaper");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Newspaper with id " + selectedNewspaperId + " not found."));
        }

        verify(newspaperDaoMock, times(1)).getNewspaperById(selectedNewspaperId);
        verifyNoMoreInteractions(newspaperDaoMock);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void listNewspapersShouldReturnaListOfNewspapers() {
        Newspaper newspaper1 = new NewspaperBuilder()
                .id(1)
                .code("NIE-01")
                .name("New Indian Express")
                .language("English")
                .ratePerUnit(3f)
                .build();
        Newspaper newspaper2 = new NewspaperBuilder()
                .id(2)
                .code("DC-01")
                .name("Deccan Chronicle")
                .language("English")
                .ratePerUnit(2f)
                .build();

        Newspaper newspaper3 = new NewspaperBuilder()
                .id(3)
                .code("TOI-01")
                .name("The Times of India")
                .language("English")
                .ratePerUnit(2.5f)
                .build();

        List<Newspaper> expectedModelList = Arrays.asList(newspaper1, newspaper2, newspaper3);
        when(newspaperDaoMock.listNewspapers()).thenReturn(expectedModelList);

        List<NewspaperDto> actualList = newspaperService.listNewspapers();

        assertThat(actualList.size(), equalTo(expectedModelList.size()));


        assertThat(actualList, (Matcher) hasItem(
                allOf(
                        hasProperty("id", is(newspaper1.getId())),
                        hasProperty("name", is(newspaper1.getName())),
                        hasProperty("code", is(newspaper1.getCode())),
                        hasProperty("language", is(newspaper1.getLanguage())),
                        hasProperty("ratePerUnit", is(newspaper1.getRatePerUnit()))
                )
        ));
        assertThat(actualList, (Matcher) hasItem(
                allOf(
                        hasProperty("id", is(newspaper2.getId())),
                        hasProperty("name", is(newspaper2.getName())),
                        hasProperty("code", is(newspaper2.getCode())),
                        hasProperty("language", is(newspaper2.getLanguage())),
                        hasProperty("ratePerUnit", is(newspaper2.getRatePerUnit()))
                )
        ));

        assertThat(actualList, (Matcher) hasItem(
                allOf(
                        hasProperty("id", is(newspaper3.getId())),
                        hasProperty("name", is(newspaper3.getName())),
                        hasProperty("code", is(newspaper3.getCode())),
                        hasProperty("language", is(newspaper3.getLanguage())),
                        hasProperty("ratePerUnit", is(newspaper3.getRatePerUnit()))
                )
        ));

        verify(newspaperDaoMock, times(1)).listNewspapers();
        verifyNoMoreInteractions(newspaperDaoMock);
    }
}
