package com.accel.acme.web.webui;

import com.accel.acme.web.test.TestUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

@Ignore
public class AcmeHomeUITest {

    @BeforeClass
    public static void setUp() {
        TestUtil.setUpServerNbaseUrlForJWebUnit();
    }

    @AfterClass
    public static void tearDown() {
        TestUtil.stopServer();
    }

    @Test
    public void ItShouldverifyNavigationsFromRootToOtherPages() {
        beginAt("/");

        assertLinkPresentWithExactText("Home");
        assertLinkPresentWithExactText("Newspapers");
        assertLinkPresentWithExactText("Advertisements");
        assertLinkPresentWithExactText("About Us");
        assertLinkPresentWithExactText("Contact");


        clickLinkWithExactText("Home");
        assertTitleEquals("ACME-Home");

        clickLinkWithExactText("Newspapers");
        assertTitleEquals("ACME-Newspapers");

        clickLinkWithExactText("Advertisements");
        assertTitleEquals("ACME-Advertisements");

        clickLinkWithExactText("About Us");
        assertTitleEquals("ACME-About");

        clickLinkWithExactText("Contact");
        assertTitleEquals("ACME-Contactus");

    }

    @Test
    public void ItShouldverifytheContentsIntheHomepage() {
        beginAt("/home");

        assertTextInElement("subHeading", "Published Advertisements");
        assertTablePresent("advtTable");

        assertTextInTable("advtTable", new String[]{"Advertisement Title", "Newspaper Name", "Newspaper Code",
                "Published Date"});
//        assertTextInTable("advtTable", new String[] {"Advt Title5", "NP Name5", "NP Code5", "Dec 8, 2014"});
    }

}
