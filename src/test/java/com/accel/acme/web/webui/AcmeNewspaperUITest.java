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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/acme-dispatcher-servlet.xml"})
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("file:src/test/resources/acmeNewspaperTableData.xml")
@DatabaseTearDown("file:src/test/resources/acmeblankDB.xml")
@Ignore
public class AcmeNewspaperUITest {

    @BeforeClass
    public static void setUp() {
        TestUtil.setUpServerNbaseUrlForJWebUnit();
    }

    @AfterClass
    public static void tearDown() {
        TestUtil.stopServer();
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeNewspaperTableData.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void
    clickingNewspaperLinkFromHomepageShouldNavigateToNewspaperPageHavingNewspaperTableListingNewspapersFromDtatabseandALinktoAddNewspaper() throws Exception {
        beginAt("/home");
        clickLinkWithExactText("Newspapers");
        assertTitleEquals("ACME-Newspapers");
        assertTextInElement("subHeading", "Newspapers");
        assertTablePresent("newspaperTable");

        assertLinkPresentWithExactText("+Newspaper");
        assertThat(getTable("newspaperTable").getRowCount(), is(2));
    }

//	private void assertUIandBackendTablesHaveSameNumberofRecords(String htmlTableName, String databaseTableName)
// throws Exception {
//		Table newspaperUITable = getTable(htmlTableName);
//        IDataSet acmeDb = TestUtil.getAcmeDataSet();
//        ITable newspaperDbTable = acmeDb.getTable(databaseTableName);
//        assertThat(newspaperUITable.getRowCount() - 1, is(newspaperDbTable.getRowCount()));
//		
//	}

    @Test
    public void clickingAddNewNewspaperShouldNavigateToAddNewspaperForm() {
        beginAt("/newspapers");
        clickLinkWithExactText("+Newspaper");
        assertTitleEquals("ACME-Add Newspaper");
        assertFormPresent("newspapersForm");
        assertTextInElement("formHeader", "Add Newspaper");
        assertFormElementPresent("name");
        assertFormElementPresent("code");
        assertFormElementPresent("language");
        assertFormElementPresent("ratePerUnit");
        assertSubmitButtonPresent();
        assertResetButtonPresent();
    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeNewspaperTableData.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void submittingEmptyFormShouldDisplayTheValidationMessagesInPage() throws Exception {

        String nameRequiredMessage = "Newspaper Name Required.";
        String codeRequiredMessage = "Newspaper Code Required.";
        String languageRequiredMessage = "Please select a language.";
        String invalidRateMessage = "Invalid rate per unit.";

        beginAt("/newspapers");
        clickLinkWithExactText("+Newspaper");

        submit();

        assertTitleEquals("ACME-Add Newspaper");
        assertTextPresent(nameRequiredMessage);
        assertTextPresent(codeRequiredMessage);
        assertTextPresent(languageRequiredMessage);
        assertTextPresent(invalidRateMessage);

    }

    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acme-newspapersInserted.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void submittingTheNewspaperFormwithValidDataShouldPresentSuccessResultPage() throws Exception {

        String newspaperName = "The Sentinel";
        String expectedMessage = "newspaper \"" + newspaperName + "\" successfully added";

        beginAt("/newspapers");
        clickLinkWithExactText("+Newspaper");

        setTextField("name", newspaperName);
        setTextField("code", "SENT12");
        selectOptionByValue("language", "Hindi");
        setTextField("ratePerUnit", "5.5f");
        submit();

        assertTitleEquals("ACME-Success");
        assertTextPresent(expectedMessage);

        clickLinkWithExactText("here");
        assertTitleEquals("ACME-Add Newspaper");

    }


    @Test
    @ExpectedDatabase(value = "file:src/test/resources/acmeNewspaperTableDataUpdated.xml", assertionMode =
            DatabaseAssertionMode.NON_STRICT)
    public void
    clickingEditButtonFromNewspaperListingPageShouldRenderTheChangeNewspaperFormAndOnnSubmitShouldUpdatetheChangestoDatabse() {

        String initialNewspaperName = "The Hindu";
        String modifiedNewspaperName = "The Hindu Changed";
        String expectedMessage = "newspaper \"" + modifiedNewspaperName + "\" successfully updated";

        beginAt("/newspapers");

        assertThat(getTable("newspaperTable").getRowCount(), is(2));
        assertTextInTable("newspaperTable", "Edit");
        clickLinkWithExactText("Edit");

        assertTitleEquals("ACME-Change Newspaper");
        assertTextInElement("formHeader", "Change Newspaper");
        assertTextFieldEquals("name", initialNewspaperName);
        assertTextFieldEquals("code", "HND001");
        assertSelectedOptionEquals("language", "English");
        assertTextFieldEquals("ratePerUnit", "3.2");

        setTextField("name", modifiedNewspaperName);
        submit();

        assertTitleEquals("ACME-Success");
        assertTextPresent(expectedMessage);

    }

}
