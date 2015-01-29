package com.accel.acme.web.webui;

import com.accel.acme.web.test.TestUtil;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("file:src/test/resources/acmeAdvertisementTableData.xml")
@DatabaseTearDown("file:src/test/resources/acmeblankDB.xml")
@Ignore
public class AcmeAdvertisementUITest {

    @BeforeClass
    public static void setUp() {
        TestUtil.setUpServerNbaseUrlForJWebUnit();
    }

    @AfterClass
    public static void tearDown() {
        TestUtil.stopServer();
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeAdvertisementTableData.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void
    clickingAdvertisementLinkFromHomepageShouldNavigateToAdvertisementPageHavingAdvertisementTableandALinktoAddAdvertisement() {
        beginAt("/home");
        clickLinkWithExactText("Advertisements");
        assertTitleEquals("ACME-Advertisements");
        assertTextInElement("subHeading", "Advertisements");
        assertTablePresent("advtTable");
//        TODO Verify Data in Table
        assertLinkPresentWithExactText("+Advertisement");
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeAdvertisementTableData.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void clickingAddAdvertisementShouldNavigateToAddAdvertisementForm() {
        beginAt("/advertisements");
        clickLinkWithExactText("+Advertisement");
        assertTitleEquals("ACME-Add Advertisement");
        assertFormPresent("advertisementForm");
        assertTextInElement("formHeader", "Add Advertisement");
        assertFormElementPresent("title");
        assertFormElementPresent("description");
        assertFormElementPresent("referenceCode");
        assertFormElementPresent("numberOfUnits");
        assertSubmitButtonPresent();
        assertResetButtonPresent();
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeAdvertisementTableData.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void submittingEmptyFormShouldDisplayTheValidationMessagesInPage() {

        String titleMessage = "Advertisement Title Required.";
        String referenceCodeRequiredMessage = "Advertisement reference Code Required.";
        String descriptionRequiredMessage = "Advertisement description is required.";
        String invalidnumberofUnitMessage = "Invalid number of unit.";

        beginAt("/advertisements");
        clickLinkWithExactText("+Advertisement");

        submit();

        assertTitleEquals("ACME-Add Advertisement");
        assertTextPresent(titleMessage);
        assertTextPresent(referenceCodeRequiredMessage);
        assertTextPresent(descriptionRequiredMessage);
        assertTextPresent(invalidnumberofUnitMessage);

    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acme-advertisementInserted.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void submittingTheAdvertisementFormwithValidDataShouldPresentSuccessResultPage() {

        String advtName = "Sony";
        String expectedMessage = "Advertisement \"" + advtName + "\" successfully added.";

        beginAt("/advertisements");
        clickLinkWithExactText("+Advertisement");

        setTextField("title", advtName);
        setTextField("description", "Tv Brand");
        setTextField("referenceCode", "Sony-001");
//		selectOptionByValue("selectedNewspapers",  "The Hindu");
        setTextField("numberOfUnits", "5");
        submit();

        assertTitleEquals("ACME-Success");
        assertTextPresent(expectedMessage);

        clickLinkWithExactText("here");
        assertTitleEquals("ACME-Add Advertisement");
    }
}
