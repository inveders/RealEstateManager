package com.inved.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.models.RealEstateAgents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateAgentsDaoTest {

    // FOR DATA
    private RealEstateManagerDatabase database;

    // DATA SET FOR TEST
    private static String AGENT_ID = "alexandra.gnimadi@gmail.com";
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
    public void insertAndGetUser() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.realEstateAgentsDao().createRealEstateAgent(AGENT_DEMO);
        // TEST
        RealEstateAgents realEstateAgents = LiveDataTestUtil.getValue(this.database.realEstateAgentsDao().getRealEstateAgentById(AGENT_ID));
        assertTrue(realEstateAgents.getFirstname().equals(AGENT_DEMO.getFirstname()) && realEstateAgents.getRealEstateAgentId().equals(AGENT_DEMO.getRealEstateAgentId()));
    }

}
