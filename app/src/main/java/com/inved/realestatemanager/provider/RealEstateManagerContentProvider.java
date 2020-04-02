package com.inved.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.inved.realestatemanager.dao.PropertyDao;
import com.inved.realestatemanager.dao.RealEstateAgentsDao;
import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

public class RealEstateManagerContentProvider extends ContentProvider {

    // FOR DATA
    public static final String AUTHORITY = "com.inved.realestatemanager.provider";
    public static final String TABLE_NAME_AGENT = RealEstateAgents.class.getSimpleName();
    public static final String TABLE_NAME_PROPERTY = Property.class.getSimpleName();
    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME_PROPERTY);
    public static final Uri URI_AGENT = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME_AGENT);

    /**
     * The match code for an agent in the Agent table.
     */
    private static final int CODE_AGENT_ITEM = 1;
    private static final int CODE_PROPERTY_ITEM = 2;

    /**
     * The URI matcher.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_NAME_AGENT + "/*", CODE_AGENT_ITEM);
        MATCHER.addURI(AUTHORITY, TABLE_NAME_PROPERTY + "/*", CODE_PROPERTY_ITEM);

    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final int code = MATCHER.match(uri);
        if (code == CODE_AGENT_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            RealEstateAgentsDao agentsDao = RealEstateManagerDatabase.getInstance(context).realEstateAgentsDao();
            final Cursor cursor;
            String agentemail = null;
            String[] result = uri.toString().split("/");
            for (String s : result) agentemail = s;

            cursor = agentsDao.getAgentsWithCursor(agentemail);

            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else if (code == CODE_PROPERTY_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            PropertyDao propertyDao = RealEstateManagerDatabase.getInstance(context).propertyDao();
            final Cursor cursor;
            String propertyId = null;
            String[] result = uri.toString().split("/");
            for (String s : result) propertyId = s;

            cursor = propertyDao.getPropertiesWithCursor(propertyId);

            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_AGENT_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME_AGENT;
            case CODE_PROPERTY_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME_PROPERTY;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int code2=0;
        if(uri.equals(URI_AGENT)){
            code2=1;
        }else if(uri.equals(URI_PROPERTY)){
            code2=2;
        }
        switch (code2) {
            case CODE_AGENT_ITEM:
                if (getContext() != null) {
                    final long id;
                    if (contentValues != null) {
                        id = RealEstateManagerDatabase.getInstance(getContext()).realEstateAgentsDao().createRealEstateAgent(RealEstateAgents.fromContentValues(contentValues));
                        if (id != 0) {
                            getContext().getContentResolver().notifyChange(uri, null);
                            return Uri.withAppendedPath(uri, contentValues.getAsString("realEstateAgentId"));
                        }
                    }

                }
            case CODE_PROPERTY_ITEM:
                if (getContext() != null) {
                    final long id;
                    if (contentValues != null) {
                        id = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().insertProperty(Property.fromContentValues(contentValues));
                        if (id != 0) {
                            getContext().getContentResolver().notifyChange(uri, null);
                            return Uri.withAppendedPath(uri, contentValues.getAsString("propertyId"));
                        }
                    }

                }
            default:
                throw new IllegalArgumentException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
