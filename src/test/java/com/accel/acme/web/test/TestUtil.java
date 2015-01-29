package com.accel.acme.web.test;

import com.accel.acme.web.dto.AdvertisementDto;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import static net.sourceforge.jwebunit.junit.JWebUnit.setBaseUrl;
import static org.junit.Assert.fail;

public class TestUtil {

    private static Server jettyServer;
    private static boolean isStarted = false;

    public static AdvertisementDto buildAdvertisementDtoById(int advertisementId) {

        AdvertisementDto advertisementDto = new AdvertisementDto();
        advertisementDto.setId(advertisementId);
        advertisementDto.setTitle("advertisement title" + advertisementId);
        advertisementDto.setDescription("advertisement description" + advertisementId);
        advertisementDto.setReferenceCode("addCode" + System.currentTimeMillis());
        advertisementDto.setNumberOfUnits(advertisementId + 1);

        return advertisementDto;

    }

    public static AdvertisementDto buildAdvertisementDto(Integer advertisementId, String title, String description,
                                                         String referenceCode, Integer numerOfUnits, List<Integer>
            newspaperIdList) {

        AdvertisementDto advertisementDto = new AdvertisementDto();
        advertisementDto.setId(advertisementId);
        advertisementDto.setTitle(title);
        advertisementDto.setDescription(description);
        advertisementDto.setReferenceCode(referenceCode);
        advertisementDto.setNumberOfUnits(numerOfUnits);
        advertisementDto.setSelectedNewspapers(newspaperIdList);
        return advertisementDto;

    }

    public static void ExportFlatXMLForDBUnit() throws Exception {

        IDataSet fullDataSet = getAcmeDataSet();
        if (null != fullDataSet)
            FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
    }

    public static IDataSet getAcmeDataSet() throws IOException {

        InputStream inputStream = null;
        try {

            inputStream = new FileInputStream("src/main/resources/jdbc.properties");

            if (null != inputStream) {
                Properties dbProperties = new Properties();
                dbProperties.load(inputStream);

                String jdbcUrl = dbProperties.getProperty("jdbc.jdbcUrl");
                String jdbcUserName = dbProperties.getProperty("jdbc.username");
                String jdbcPassword = dbProperties.getProperty("jdbc.password");

                Connection jdbcConnection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
                DatabaseConnection connection = new DatabaseConnection(jdbcConnection);
                return connection.createDataSet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream)
                inputStream.close();
        }
        return null;
    }

    public static void setUpServerNbaseUrlForJWebUnit() {
        try {
            if (!isStarted) {
                System.out.println("Initializing Jetty Server...");
                jettyServer = new Server(0);
                jettyServer.addHandler(new WebAppContext("src/main/webapp", "/AcmeWeb"));

                jettyServer.start();

                int actualPort = jettyServer.getConnectors()[0].getLocalPort();
                String baseUrl = "http://localhost:" + actualPort + "/AcmeWeb";
                setBaseUrl(baseUrl);

                isStarted = jettyServer.isStarted();
                if (isStarted)
                    System.out.println("Jetty Started. Base URL = " + baseUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to START the Test Server...");
        }
    }

    public static void stopServer() {

        try {
            jettyServer.stop();

            if (jettyServer.isStopped()) {
                isStarted = false;
                System.out.println("Jetty Server Stopped.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to STOP the Test Server...");
        }
    }

}
