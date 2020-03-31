package com.inved.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.provider.AgentsContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AgentsContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static long AGENT_ID = 1;

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
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(AgentsContentProvider.URI_AGENT, AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetAgent() {
        // BEFORE : Adding demo item
        final Uri userUri =mContentResolver.insert(AgentsContentProvider.URI_AGENT, generateAgent());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(AgentsContentProvider.URI_AGENT,AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("firstname")), is("Alexandra"));
    }

    // ---

    private ContentValues generateAgent(){
        final ContentValues values = new ContentValues();
        values.put("realEstateAgentId", "berengergnim@gmail.com");
        values.put("firstname", "Alexandra");
        values.put("lastname", "Gnimadi");
        values.put("urlPicture", "https://www.google.fr");
        values.put("agencyName", "SCI GNIMINVEST");
        values.put("agencyPlaceId", "AuyhdysOZnsee");
        return values;

    }
}
