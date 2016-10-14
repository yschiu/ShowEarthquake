package com.eason.earthquakedatafetcher;

import com.eason.earthquakedatafetcher.model.Earthquake;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by chiu_mac on 2016/10/15.
 */

public class EathquakeDataParserTest {
    private static final String MOCK_EARTHQUAKE_JSON_STRING = "{\"count\":\"21740\",\"earthquakes\":[{\"src\":\"us\",\"eqid\":\"c000is61\",\"timedate\":\"2013-07-29 22:22:48\",\"lat\":\"7.6413\",\"lon\":\"93.6871\",\"magnitude\":\"4.6\",\"depth\":\"40.90\",\"region\":\"Nicobar Islands, India region\"}]}\n" +
            "\n"; //1 earthquake
    @Test
    public void parser_correctData(){
        EarthquakeDataParser parser = new EarthquakeDataParser(MOCK_EARTHQUAKE_JSON_STRING);
        parser.parse();
        assertEquals(21740, parser.getEarthquakeTotalCount());
        List<Earthquake> earthquakes = parser.getEarthquakes();
        assertEquals(1, earthquakes.size());
        Earthquake quake = earthquakes.get(0);
        assertEquals("us", quake.getSource());
        assertEquals("c000is61", quake.getId());
        SimpleDateFormat format = new SimpleDateFormat(EarthquakeDataParser.DATE_FORMAT);
        Date date = null;
        try {
            date = format.parse("2013-07-29 22:22:48");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(date, quake.getDate());
        assertEquals(7.6413, quake.getLatitude(), 0.005);
        assertEquals(93.6871, quake.getLongitude(), 0.005);
        assertEquals(4.6, quake.getMagnitude(), 0.3);
        assertEquals(40.90, quake.getDepth(), 2);
        assertEquals("Nicobar Islands, India region", quake.getRegionName());
    }
}
