/**
 *
 */
package com.accel.acme.web.dao;

import com.accel.acme.web.model.Advertisement;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Salna.K
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})

@DatabaseSetup("file:src/test/resources/acmeAdvertisementTableData.xml")
@DatabaseTearDown("file:src/test/resources/acmeblankDB.xml")
public class AdvertisementDaoTest {
    @Autowired
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    private AdvertisementDao advertisementDao;

    @Test
    public void readingdAdvertisementDetailsWithExistingAdvertisementId() throws Exception {
        Advertisement advertisement = advertisementDao.getAdvertisementById(7);
        assertNotNull(advertisement);
    }

    @Test
    public void checkingAndReturnTrueWithAlreadyExistingAdvertisementReferenceIdDuringNewAdvertisementAddition()
            throws Exception {
        boolean advertisementExist = advertisementDao.isRefIdExists("Samsung-001");
        assertTrue(advertisementExist);
    }

    @Test
    public void checkingAndReturnFalseWithNonExistingExistingAdvertisementReferenceIdDuringNewAdvertisementAddition()
            throws Exception {
        boolean advertisementExist = advertisementDao.isRefIdExists("NOREFERNCE");
        assertFalse(advertisementExist);
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeAdvertisementTableDataUpdated.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void updatingAnAlreadtExistingAdvertisementDetails() throws Exception {
        Advertisement advertisement = advertisementDao.getAdvertisementById(7);
        advertisement.setTitle("Samsung changed");
        boolean success = advertisementDao.updateAdvertisement(advertisement);
        assertTrue(success);
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acme-advertisementInserted.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void addingANewsAdvertisementWithValidDetails() throws Exception {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Sony");
        advertisement.setDescription("Tv Brand");
        advertisement.setReferenceId("Sony-001");

        boolean success = advertisementDao.addAdvertisement(advertisement);
        assertTrue(success);
    }


    @Test
    public void readingdAdvertisementDetailsWithExistingAdvertisementReferenceCode() throws Exception {
        Advertisement advertisement = advertisementDao.getAdvertisementByCode("Samsung-001");
        assertNotNull(advertisement);
    }

    @Test
    public void readingdAdvertisementDetailsWithNonExistingAdvertisementReferenceCode() throws Exception {
        Advertisement advertisement = advertisementDao.getAdvertisementByCode("NOREFERNCE");
        assertNull(advertisement);
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeblankDB.xml", assertionMode = DatabaseAssertionMode
            .NON_STRICT)
    public void removingAdvertisementWithByExistingAdvertisementId() throws Exception {
        Advertisement advertisement = advertisementDao.getAdvertisementById(7);
        boolean removed = advertisementDao.removeAdvertisement(advertisement);
        assertTrue(removed);
    }

    @Ignore
    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acme-advertisementInserted.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void readingListOfAdvertisementsFromDataBase() throws Exception {
        List<Advertisement> advertisementList = advertisementDao.listAdvertisements();
        assertNotNull(advertisementList.size());
    }
}
