/**
 *
 */
package com.accel.acme.web.service;

import com.accel.acme.web.dao.AdvertisementDao;
import com.accel.acme.web.dto.AdvertisementDto;
import com.accel.acme.web.dto.PublishedAdvtDto;
import com.accel.acme.web.exceptions.ItemAlreadyExistException;
import com.accel.acme.web.exceptions.ItemNotFoundException;
import com.accel.acme.web.model.Advertisement;
import com.accel.acme.web.model.NewsPapersAdvertisements;
import com.accel.acme.web.model.Newspaper;
import com.accel.acme.web.test.AdvertisementBuilder;
import com.accel.acme.web.test.AdvertisementDtoBuilder;
import com.accel.acme.web.test.NewspaperBuilder;
import com.accel.acme.web.test.TestUtil;
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

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Salna.K
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
@WebAppConfiguration
public class AdvertisementServiceTest {

    @InjectMocks
    private final AdvertisementService advertisementService = new AdvertisementService();
    Integer advertisementId = 1;
    String advertisementTitle = "LIC";
    String advertisementDescription = "LIC Life Insurence";
    String referenceCode = "LICINDIA";
    Integer numberOfUnits = 3;
    String advertisementTitleUpdated = "LIC Jeevan";
    String advertisementCodeUpdated = "LICINDIAJeevan";
    List<Integer> newspaperIdList = Arrays.asList(1, 2, 3);
    @Mock
    private AdvertisementDao advertisementDaoMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addingNewAdvertisementWhichIsNotAlreadyAddedShouldSaveTheAdvertisement() throws Exception {


        AdvertisementDto advertisementDto = TestUtil.buildAdvertisementDto(null, advertisementTitle,
                advertisementDescription, referenceCode, numberOfUnits, null);

        when(advertisementDaoMock.isRefIdExists(referenceCode)).thenReturn(false);

        ArgumentCaptor<Advertisement> advertisementArgumentData = ArgumentCaptor.forClass(Advertisement.class);
        when(advertisementDaoMock.addAdvertisement(advertisementArgumentData.capture())).thenReturn(true);

        assertTrue(advertisementService.addAdvertisement(advertisementDto));

        verify(advertisementDaoMock, times(1)).addAdvertisement(advertisementArgumentData.capture());
        verify(advertisementDaoMock, times(1)).isRefIdExists(advertisementDto.getReferenceCode());

        Advertisement model = advertisementArgumentData.getValue();

        assertNull(model.getId());
        assertThat(model.getTitle(), is(advertisementDto.getTitle()));
        assertThat(model.getReferenceId(), is(advertisementDto.getReferenceCode()));
        assertThat(model.getDescription(), is(advertisementDto.getDescription()));

    }

    @Test
    public void addingNewAdvertisementWithSelectedNewspapersShouldPublishTheAdvertisement() throws Exception {


        AdvertisementDto advertisementDto = TestUtil.buildAdvertisementDto(null, advertisementTitle,
                advertisementDescription, referenceCode, numberOfUnits, newspaperIdList);

        when(advertisementDaoMock.isRefIdExists(referenceCode)).thenReturn(false);

        ArgumentCaptor<Advertisement> advertisementArgumentData = ArgumentCaptor.forClass(Advertisement.class);
        when(advertisementDaoMock.addAdvertisement(advertisementArgumentData.capture())).thenReturn(true);

        assertTrue(advertisementService.addAdvertisement(advertisementDto));

        verify(advertisementDaoMock, times(1)).addAdvertisement(advertisementArgumentData.capture());
        verify(advertisementDaoMock, times(1)).isRefIdExists(advertisementDto.getReferenceCode());

        Advertisement model = advertisementArgumentData.getValue();

        assertNull(model.getId());
        assertThat(model.getTitle(), is(advertisementDto.getTitle()));
        assertThat(model.getReferenceId(), is(advertisementDto.getReferenceCode()));
        assertThat(model.getDescription(), is(advertisementDto.getDescription()));

    }

    @Test
    public void addingNewAdvertisementWhithAlreadyExistingRefernceCodeShouldShowErrorMessage() throws Exception {

        AdvertisementDto advertisementDto = new AdvertisementDtoBuilder()
                .code(advertisementDescription)
                .description(advertisementDescription)
                .code(referenceCode)
                .numberOfUnits(numberOfUnits)
                .build();

        when(advertisementDaoMock.isRefIdExists(referenceCode)).thenReturn(true);
        try {
            advertisementService.addAdvertisement(advertisementDto);
            fail("No exceptions was thrown from  advertisementService.addAdvertisement");
        } catch (Exception e) {
            assertTrue(e instanceof ItemAlreadyExistException);
            assertThat(e.getMessage(), is(" Advertisement with code " + referenceCode + " already exists."));
        }

        verify(advertisementDaoMock, times(1)).isRefIdExists(referenceCode);
        verifyNoMoreInteractions(advertisementDaoMock);

    }

    @Test
    public void updatingAnAlreadyExistingAdvertisementShouldUpdatetheEntry() throws Exception {
        AdvertisementDto advertisementDto = new AdvertisementDtoBuilder()
                .id(advertisementId)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .code(advertisementCodeUpdated)
                .numberOfUnits(numberOfUnits)
                .build();

        Advertisement model = new AdvertisementBuilder()
                .id(advertisementId)
                .code(advertisementCodeUpdated)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .build();


        when(advertisementDaoMock.getAdvertisementById(advertisementDto.getId())).thenReturn(model);
        when(advertisementDaoMock.getAdvertisementByCode(advertisementDto.getReferenceCode())).thenReturn(null);
        when(advertisementDaoMock.updateAdvertisement(model)).thenReturn(true);

        assertTrue(advertisementService.updateAdvertisement(advertisementDto));


        verify(advertisementDaoMock, times(1)).getAdvertisementById(advertisementDto.getId());
        verify(advertisementDaoMock, times(1)).getAdvertisementByCode(advertisementDto.getReferenceCode());
        verify(advertisementDaoMock, times(1)).updateAdvertisement(model);
        verifyNoMoreInteractions(advertisementDaoMock);

        assertThat(model.getId(), is(advertisementDto.getId()));
        assertThat(model.getTitle(), is(advertisementDto.getTitle()));
        assertThat(model.getReferenceId(), is(advertisementDto.getReferenceCode()));
        assertThat(model.getDescription(), is(advertisementDto.getDescription()));
    }

    @Test
    public void updatingAnAlreadyExistingPublishedAdvertisementShouldUpdatetheEntry() throws Exception {

        Newspaper newspaper1 = new NewspaperBuilder().id(1).build();
        Newspaper newspaper2 = new NewspaperBuilder().id(2).build();

        NewsPapersAdvertisements newsPapersAdvertisements1 = new NewsPapersAdvertisements();
        newsPapersAdvertisements1.setNewsPaper(newspaper1);

        NewsPapersAdvertisements newsPapersAdvertisements2 = new NewsPapersAdvertisements();
        newsPapersAdvertisements2.setNewsPaper(newspaper2);

        Set<NewsPapersAdvertisements> newsPaperAdvertisementSet = new HashSet<NewsPapersAdvertisements>();
        newsPaperAdvertisementSet.add(newsPapersAdvertisements1);
        newsPaperAdvertisementSet.add(newsPapersAdvertisements2);

        AdvertisementDto advertisementDto = new AdvertisementDtoBuilder()
                .id(advertisementId)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .code(advertisementCodeUpdated)
                .numberOfUnits(numberOfUnits)
                .newspaperId(newspaperIdList)
                .build();

        Advertisement model = new AdvertisementBuilder()
                .id(advertisementId)
                .code(advertisementCodeUpdated)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .newspaperAdvertisement(newsPaperAdvertisementSet)
                .build();


        when(advertisementDaoMock.getAdvertisementById(advertisementDto.getId())).thenReturn(model);
        when(advertisementDaoMock.getAdvertisementByCode(advertisementDto.getReferenceCode())).thenReturn(null);
        when(advertisementDaoMock.updateAdvertisement(model)).thenReturn(true);

        assertTrue(advertisementService.updateAdvertisement(advertisementDto));


        verify(advertisementDaoMock, times(1)).getAdvertisementById(advertisementDto.getId());
        verify(advertisementDaoMock, times(1)).getAdvertisementByCode(advertisementDto.getReferenceCode());
        verify(advertisementDaoMock, times(1)).updateAdvertisement(model);
        verifyNoMoreInteractions(advertisementDaoMock);

        assertThat(model.getId(), is(advertisementDto.getId()));
        assertThat(model.getTitle(), is(advertisementDto.getTitle()));
        assertThat(model.getReferenceId(), is(advertisementDto.getReferenceCode()));
        assertThat(model.getDescription(), is(advertisementDto.getDescription()));
    }


    @Test
    public void updatingAnAdvertisementWithTheCodeofAlreadyExistingShouldThrowItemAlreadyExistException() throws
            Exception {

        AdvertisementDto advertisementDto = new AdvertisementDtoBuilder()
                .id(advertisementId)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .code(advertisementTitleUpdated)
                .numberOfUnits(numberOfUnits)
                .build();
        Advertisement model = new AdvertisementBuilder()
                .id(advertisementId)
                .code(advertisementCodeUpdated)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .build();

        Advertisement codeMatchingmodel = new AdvertisementBuilder()
                .id(advertisementId + 5)
                .code(advertisementCodeUpdated)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .build();


        when(advertisementDaoMock.getAdvertisementById(advertisementDto.getId())).thenReturn(model);
        when(advertisementDaoMock.getAdvertisementByCode(advertisementDto.getReferenceCode())).thenReturn
                (codeMatchingmodel);

        try {
            advertisementService.updateAdvertisement(advertisementDto);
            fail("No exceptions was thrown from  advertisementService.updateAdvertisement");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemAlreadyExistException.class));
            assertThat(e.getMessage(), is("Advertisement with code " + advertisementDto.getReferenceCode() + " " +
                    "already exists."));
        }

        verify(advertisementDaoMock, times(1)).getAdvertisementById(advertisementDto.getId());
        verify(advertisementDaoMock, times(1)).getAdvertisementByCode(advertisementDto.getReferenceCode());
        verifyNoMoreInteractions(advertisementDaoMock);

    }

    @Test
    public void updatingAdvertisementWhithNotExistingRefernceCodeShouldShowErrorMessage() throws Exception {

        AdvertisementDto advertisementDto = new AdvertisementDtoBuilder()
                .id(advertisementId)
                .title(advertisementTitleUpdated)
                .description(advertisementDescription)
                .code(advertisementTitleUpdated)
                .numberOfUnits(numberOfUnits)
                .build();

        when(advertisementDaoMock.getAdvertisementById(advertisementDto.getId())).thenReturn(null);

        try {
            advertisementService.updateAdvertisement(advertisementDto);
            fail("No exceptions was thrown from  advertisementService.updateAdvertisement");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Advertisement with id " + advertisementDto.getId() + " not found."));
        }

        verify(advertisementDaoMock, times(1)).getAdvertisementById(advertisementDto.getId());
        verifyNoMoreInteractions(advertisementDaoMock);
    }

    @Test
    public void gettingAdvertisementByIdShouldReturntheCorrespondingAdvertisementDto() throws Exception {

        Integer selectedAdvtId = 1;
        Advertisement expextedAdvertisement = new AdvertisementBuilder()
                .id(selectedAdvtId)
                .code(referenceCode)
                .title(advertisementTitle)
                .description(advertisementDescription)
                .build();

        when(advertisementDaoMock.getAdvertisementById(selectedAdvtId)).thenReturn(expextedAdvertisement);

        AdvertisementDto resultingDto = advertisementService.getAdvertisementById(selectedAdvtId);

        assertThat(resultingDto.getId(), is(selectedAdvtId));
        verify(advertisementDaoMock, times(1)).getAdvertisementById(selectedAdvtId);
        verifyNoMoreInteractions(advertisementDaoMock);
    }

    @Test
    public void gettingPublishedAdvertisementByIdShouldReturntheCorrespondingAdvertisementDto() throws Exception {
        Newspaper newspaper1 = new NewspaperBuilder().id(1).build();
        Newspaper newspaper2 = new NewspaperBuilder().id(2).build();

        NewsPapersAdvertisements newsPapersAdvertisements1 = new NewsPapersAdvertisements();
        newsPapersAdvertisements1.setNewsPaper(newspaper1);

        NewsPapersAdvertisements newsPapersAdvertisements2 = new NewsPapersAdvertisements();
        newsPapersAdvertisements2.setNewsPaper(newspaper2);

        Set<NewsPapersAdvertisements> newsPaperAdvertisementSet = new HashSet<NewsPapersAdvertisements>();
        newsPaperAdvertisementSet.add(newsPapersAdvertisements1);
        newsPaperAdvertisementSet.add(newsPapersAdvertisements2);

        Integer selectedAdvtId = 1;
        Advertisement expextedAdvertisement = new AdvertisementBuilder()
                .id(selectedAdvtId)
                .code(referenceCode)
                .title(advertisementTitle)
                .description(advertisementDescription)
                .newspaperAdvertisement(newsPaperAdvertisementSet)
                .build();

        when(advertisementDaoMock.getAdvertisementById(selectedAdvtId)).thenReturn(expextedAdvertisement);

        AdvertisementDto resultingDto = advertisementService.getAdvertisementById(selectedAdvtId);

        assertThat(resultingDto.getId(), is(selectedAdvtId));
        verify(advertisementDaoMock, times(1)).getAdvertisementById(selectedAdvtId);
        verifyNoMoreInteractions(advertisementDaoMock);
    }

    @Test
    public void gettingAdvertisementByIdThatNotExistShouldThrowItemNotFoundException() throws Exception {
        Integer selectedAdvtId = 1;
        when(advertisementDaoMock.getAdvertisementById(selectedAdvtId)).thenReturn(null);

        try {
            advertisementService.getAdvertisementById(selectedAdvtId);
            fail("No exceptions was thrown from  advertisementService.getAdvertisementById");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Advertisement with id" + selectedAdvtId + " not found."));
        }

        verify(advertisementDaoMock, times(1)).getAdvertisementById(selectedAdvtId);
        verifyNoMoreInteractions(advertisementDaoMock);
    }

    @Test
    public void advertisementDeleteByIdShouldDeleteTheMatchedAdvertisement() throws Exception {
        Integer selectedAdvtId = 1;
        Advertisement matchingAdvertisement = new AdvertisementBuilder()
                .id(selectedAdvtId)
                .build();

        when(advertisementDaoMock.getAdvertisementById(selectedAdvtId)).thenReturn(matchingAdvertisement);
        when(advertisementDaoMock.removeAdvertisement(matchingAdvertisement)).thenReturn(true);

        assertThat(advertisementService.removeAdvertisement(selectedAdvtId), is(true));

        verify(advertisementDaoMock, times(1)).getAdvertisementById(selectedAdvtId);
        verify(advertisementDaoMock, times(1)).removeAdvertisement(matchingAdvertisement);
        verifyNoMoreInteractions(advertisementDaoMock);

    }

    @Test
    public void deletingAdvertisementByIdThatNotExistShouldThrowItemNotFoundException() throws Exception {
        Integer selectedAdvtId = 1;

        when(advertisementDaoMock.getAdvertisementById(selectedAdvtId)).thenReturn(null);

        try {
            advertisementService.removeAdvertisement(selectedAdvtId);
            fail("No exceptions was thrown from  advertisementService.removeAdvertisement");
        } catch (Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Advertisement with id" + selectedAdvtId + " not found."));
        }

        verify(advertisementDaoMock, times(1)).getAdvertisementById(selectedAdvtId);
        verifyNoMoreInteractions(advertisementDaoMock);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void listAdvertisementShouldReturnaListOfAdvertisements() {


        Advertisement advertisement1 = new AdvertisementBuilder()
                .id(1)
                .code("SBI")
                .title("SBI Bank")
                .description("State Bank of India")
                .build();


        Advertisement advertisement2 = new AdvertisementBuilder()
                .id(2)
                .code("ICICI")
                .title("ICICI Bank")
                .description("ICICI Priavte Bank")
                .build();

        Advertisement advertisement3 = new AdvertisementBuilder()
                .id(3)
                .code("IDBI")
                .title("IDBI Bank")
                .description("IDBI Priavte Bank")
                .build();


        List<Advertisement> expectedModelList = Arrays.asList(advertisement1, advertisement2, advertisement3);
        when(advertisementDaoMock.listAdvertisements()).thenReturn(expectedModelList);

        List<AdvertisementDto> actualList = advertisementService.listAdvertisements();

        assertThat(actualList.size(), equalTo(expectedModelList.size()));
        assertThat(actualList, (Matcher) hasItem(
                allOf(
                        hasProperty("id", is(advertisement1.getId())),
                        hasProperty("referenceCode", is(advertisement1.getReferenceId())),
                        hasProperty("title", is(advertisement1.getTitle())),
                        hasProperty("description", is(advertisement1.getDescription()))
                )
        ));
        assertThat(actualList, (Matcher) hasItem(
                allOf(
                        hasProperty("id", is(advertisement2.getId())),
                        hasProperty("referenceCode", is(advertisement2.getReferenceId())),
                        hasProperty("title", is(advertisement2.getTitle())),
                        hasProperty("description", is(advertisement2.getDescription()))
                )
        ));

        assertThat(actualList, (Matcher) hasItem(
                allOf(
                        hasProperty("id", is(advertisement3.getId())),
                        hasProperty("referenceCode", is(advertisement3.getReferenceId())),
                        hasProperty("title", is(advertisement3.getTitle())),
                        hasProperty("description", is(advertisement3.getDescription()))
                )
        ));

        verify(advertisementDaoMock, times(1)).listAdvertisements();
        verifyNoMoreInteractions(advertisementDaoMock);
    }

    @Test
    public void listingPublishedAdvertisementsShouldReturnaListOfPublishedAdvertisements() {

        List<NewsPapersAdvertisements> expectedModelList = new ArrayList<NewsPapersAdvertisements>();
        when(advertisementDaoMock.getAllPublishedAdvertisement()).thenReturn(expectedModelList);

        List<PublishedAdvtDto> actualList = advertisementService.listPublishedAdvertisements();

        assertThat(actualList.size(), equalTo(expectedModelList.size()));
    }
    
   /* @Test
    public void gettingClientByPhonenumberShouldReturntheCorrespondingClientDto() throws Exception {
        
       
        
        Client expectedClient = new ClientBuilder()
                                                    .id(1)
                                                    .name("abc")
                                                    .phoneNumber(phoneNumber)
                                                    .build();
        when(advertisementDaoMock.getClientByPhonenumber(phoneNumber)).thenReturn(expectedClient); 
        
        ClientDto clientDto = advertisementService.getUserbyContactNumber(phoneNumber);
        assertThat(clientDto.getContactNum(),is(expectedClient.getPhoneNumber()));
        verify(advertisementDaoMock, times(1)).getClientByPhonenumber(phoneNumber);
        verifyNoMoreInteractions(advertisementDaoMock);
    }
    
    @Test
    public void gettingClientByPhoneNumberThatNotExistShouldThrowItemNotFoundException() throws Exception {
        when(advertisementDaoMock.getClientByPhonenumber(phoneNumber)).thenReturn(null); 
        try {
            advertisementService.getUserbyContactNumber(phoneNumber);
            fail("No exceptions was thrown from   advertisementService.getUserbyContactNumber");
        }catch(Exception e) {
            assertThat(e, instanceOf(ItemNotFoundException.class));
            assertThat(e.getMessage(), is("Required User Not found"));
        }
    }*/
}
