package com.eason.earthquakedatafetcher;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.eason.earthquakedatafetcher.model.Earthquake;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EarthquakeDataFetcherInstrumentedTest {
    private boolean fetchResult;
    private int earthquakesCount;

    @Test
    public void fetch() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        final EarthquakeDataFetcher fetcher = new EarthquakeDataFetcher(new EarthquakeDataFetcher.EarthquakeDataFetcherListener() {
            @Override
            public void onFetchFail() {
                fetchResult = false;
                signal.countDown();
            }

            @Override
            public void onFetchSuccess(int totalEarthquakes, List<Earthquake> earthquakes) {
                fetchResult = true;
                earthquakesCount = totalEarthquakes;
                signal.countDown();
            }
        });
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                fetcher.startFetch();
            }
        });

        signal.await(30, TimeUnit.SECONDS);
        //success or fail to fetch
        assertEquals(true, fetchResult);
    }
}
