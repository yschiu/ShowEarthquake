package com.eason.earthquakedatafetcher;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class handle Http Connection
 */

public class JsonStringFetcher {
    private static final int READ_TIMEOUT = 10000;//10 sec.
    private static final int CONNECT_TIMEOUT = 20000;//20 sec.

    public static String fetchUrl(String serverUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //Starts fetching
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(JsonStringFetcher.class.getSimpleName(),
                    "The response:" + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            Log.e(JsonStringFetcher.class.getSimpleName(),
                    "contentAsString:" + contentAsString);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        char[] buffer = new char[1024];
        int r = -1;
        StringBuffer str = new StringBuffer(1024);
        reader = new InputStreamReader(stream, "UTF-8");
        while((r = reader.read(buffer)) != -1) {
            str.append(buffer,0, r);
        }
        return str.toString();
    }
}
