package com.inved.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.provider.RealEstateManagerContentProvider;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RealEstateManagerContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static String AGENT_ID = "inveders@hotmail.fr";
    private static String PROPERTY_ID = "AyijdhyeIOlsj";

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(Uri.withAppendedPath(RealEstateManagerContentProvider.URI_AGENT,AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }



    @Test
    public void insertAndGetAgent() {
        // BEFORE : Adding demo item
        final Uri agentUri =mContentResolver.insert(RealEstateManagerContentProvider.URI_AGENT, generateAgent());
        assertThat(agentUri, notNullValue());
        // TEST
        final Cursor cursor = mContentResolver.query(Uri.withAppendedPath(RealEstateManagerContentProvider.URI_AGENT,AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("firstname")), is("Alexandra"));
    }

    @Test
    public void getPropertiesWhenNoPropertyInserted() {
        final Cursor cursor = mContentResolver.query(Uri.withAppendedPath(RealEstateManagerContentProvider.URI_PROPERTY, PROPERTY_ID), null, null, null, null);
        assertThat(cursor, CoreMatchers.notNullValue());
        assertThat(cursor.getCount(), CoreMatchers.is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetProperty() {
        // BEFORE : Adding demo property
        mContentResolver.insert(RealEstateManagerContentProvider.URI_AGENT, generateAgent());
        mContentResolver.insert(RealEstateManagerContentProvider.URI_PROPERTY, generateProperty());
        // TEST
        final Cursor cursor = mContentResolver.query(Uri.withAppendedPath(RealEstateManagerContentProvider.URI_PROPERTY, PROPERTY_ID), null, null, null, null);
        assertThat(cursor, CoreMatchers.notNullValue());
        assertThat(cursor.getCount(), CoreMatchers.is(1));
        assertThat(cursor.moveToFirst(), CoreMatchers.is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("zipCode")), CoreMatchers.is("57840"));
    }

    // ---

    private ContentValues generateAgent(){
        final ContentValues values = new ContentValues();
        values.put("realEstateAgentId", "inveders@hotmail.fr");
        values.put("firstname", "Alexandra");
        values.put("lastname", "Gnimadi");
        values.put("urlPicture", "https://www.google.fr");
        values.put("agencyName", "SCI GNIMINVEST");
        values.put("agencyPlaceId", "AuyhdysOZnsee");
        return values;

    }

    private ContentValues generateProperty(){
        final ContentValues values = new ContentValues();

        values.put("propertyId","AyijdhyeIOlsj");
        values.put("typeProperty","house");
        values.put("pricePropertyInEuro",185000);
        values.put("surfaceAreaProperty",130);
        values.put("numberRoomsInProperty","6");
        values.put("numberBathroomsInProperty","2");
        values.put("numberBedroomsInProperty","3");
        values.put("fullDescriptionProperty","Our house in Ottange");

        values.put("streetNumber","19");
        values.put("streetName","rue des écoles");
        values.put("zipCode","57840");
        values.put("townProperty","Ottange");
        values.put("country","France");
        values.put("addressCompl","");
        values.put("pointOfInterest","schools");
        values.put("statusProperty","in progress");
        values.put("dateOfEntryOnMarketForProperty","15/02/2020");
        values.put("dateOfSaleForProperty","");

        values.put("selected",false);
        values.put("photoUri1","");
        values.put("photoUri2","");
        values.put("photoUri3","");
        values.put("photoUri4","");
        values.put("photoUri5","");
        values.put("photoDescription1","");
        values.put("photoDescription2","");
        values.put("photoDescription3","");
        values.put("photoDescription4","");
        values.put("photoDescription5","");
        values.put("realEstateAgentId","inveders@hotmail.fr");
        return values;
    }
}
