package com.inved.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PropertyDaoTest {

    // FOR DATA
    private RealEstateManagerDatabase database;

    // DATA SET FOR TEST
    private static String PROPERTY_ID = "AyijdhyeIOlsj";
    private static String AGENT_ID = "alexandra.gnimadi@gmail.com";
    private static Property PROPERTY_DEMO = new Property(PROPERTY_ID, "house",185000,
            130,"6","2",3,
            "","19","rue des écoles","57840","Ottange",
            "France","","car parks, schools","in progress",
            "15/02/2020","",false,null,null,
            null,null,null,null,null,null,
            null,"",AGENT_ID);

    private static Property PROPERTY_TEST1 = new Property("AJusneupkonspe", "flat",120000,
            75,"3","1",2,
            "","42","rue des écoles","57840","Ottange",
            "France","bis","car parks, schools, oil stations, restaurants","sold",
            "15/09/2019","",false,null,null,
            null,null,null,null,null,null,
            null,"",AGENT_ID);
    private static Property PROPERTY_TEST2 = new Property("A,ncplesuhdeiqoksz", "house",500000,
            140,"8","2",6,
            "","59","rue du regard","94380","Bonneuil-sur-Marne",
            "France","","car parks, schools,restaurants","in progress",
            "19/04/2019","",false,null,null,
            null,null,null,null,null,null,
            null,"",AGENT_ID);
    private static Property PROPERTY_TEST3 = new Property("aoNDYENSLDPPELS", "room",420,
            20,"1","1",0,
            "","25","rue de la gendarmerie","57840","Ottange",
            "France","","car parks, schools","in progress",
            "20/02/2020","",false,null,null,
            null,null,null,null,null,null,
            null,"",AGENT_ID);

    // DATA SET FOR TEST
    private static RealEstateAgents AGENT_DEMO = new RealEstateAgents(AGENT_ID, "Alexandra","Gnimadi", "https://www.google.fr","SCI GNIMINVEST","AuyhdysOZnsee");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException {
        // BEFORE : Adding a new agent
        this.database.realEstateAgentsDao().createRealEstateAgent(AGENT_DEMO);
        this.database.propertyDao().insertProperty(PROPERTY_DEMO);
        // TEST
        Property property = LiveDataTestUtil.getValue(this.database.propertyDao().getOneProperty(PROPERTY_ID));
        assertTrue(property.getCountry().equals(PROPERTY_DEMO.getCountry()) && property.getCountry().equals(PROPERTY_DEMO.getCountry()));
    }


    @Test
    public void getPropertysWhenNoPropertyInserted() throws InterruptedException {
        // TEST
        List<Property> properties = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(AGENT_ID));
        assertTrue(properties.isEmpty());
    }

    @Test
    public void insertAndGetProperties() throws InterruptedException {
        // BEFORE : Adding demo agent & demo property

        this.database.realEstateAgentsDao().createRealEstateAgent(AGENT_DEMO);
        this.database.propertyDao().insertProperty(PROPERTY_TEST1);
        this.database.propertyDao().insertProperty(PROPERTY_TEST2);
        this.database.propertyDao().insertProperty(PROPERTY_TEST3);

        // TEST
        List<Property> properties = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(AGENT_ID));
        assertEquals(3, properties.size());
    }

    @Test
    public void insertAndUpdateProperty() throws InterruptedException {
        // BEFORE : Adding demo agent & demo property. Next, update property added & re-save it
        this.database.realEstateAgentsDao().createRealEstateAgent(AGENT_DEMO);
        this.database.propertyDao().insertProperty(PROPERTY_TEST1);
        Property propertyAdded = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(AGENT_ID)).get(0);
        propertyAdded.setSelected(true);
        this.database.propertyDao().updateSelected(propertyAdded.isSelected(),propertyAdded.getPropertyId());

        //TEST
        List<Property> properties = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(AGENT_ID));
        assertTrue(properties.size() == 1 && properties.get(0).isSelected());
    }

    @Test
    public void insertAndDeleteProperty() throws InterruptedException {
        // BEFORE : Adding demo AGENT & demo property. Next, get the property added & delete it.
        this.database.realEstateAgentsDao().createRealEstateAgent(AGENT_DEMO);
        this.database.propertyDao().insertProperty(PROPERTY_TEST2);
        Property propertyAdded = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(AGENT_ID)).get(0);
        this.database.propertyDao().deleteProperty(propertyAdded.getPropertyId());

        //TEST
        List<Property> properties = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(AGENT_ID));
        assertTrue(properties.isEmpty());
    }


}
