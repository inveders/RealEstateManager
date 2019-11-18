package com.inved.realestatemanager;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.inved.realestatemanager.utils.RoomSearchQuery;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class QueryTest {

    private RoomSearchQuery roomSearchQuery = Mockito.spy(new RoomSearchQuery());

    @Test
    public void should_BuildGoodQuery_With_Data() {
        //Given
        String type ="House";
        String town ="Ottange";
        double minSurface=15.0;
        double maxSurface=45.0;
        double minPrice=90000.0;
        double maxPrice=150000.0;
        int minBedRoom=0;
        int maxBedRoom=5;
        String country="France";
        String status=null;
        long realEstateAgentId=0;


        //When
        SimpleSQLiteQuery query = roomSearchQuery.queryRoomDatabase(type, town, minSurface, maxSurface, minPrice,
                maxPrice, minBedRoom,maxBedRoom,country,status,realEstateAgentId);

        String queryString="SELECT * FROM Property WHERE town =:town";
        List<Object> args = new ArrayList();

        //Then
        Assert.assertEquals(new SimpleSQLiteQuery(queryString, args.toArray()), query);
    }


}
