package ca.utoronto.utsc.tracademia;


import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPClient {
    private static final String TAG = "HTTPClient";
    static CookieManager mCookieManager = createCookieManager();


    public static CookieManager createCookieManager() {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        return manager;
    }

    public static HttpURLConnection getOpenHttpConnection(String url, String requestMethod) {
        HttpURLConnection urlConnection = null;
        try {
            URL parsedUrl = new URL(url);
            urlConnection = (HttpURLConnection)parsedUrl.openConnection();
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.setRequestProperty("x-no-csrf", "1");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return urlConnection;

    }

    public static HashMap<String, String> getHeaders() {
        HashMap<String, String> params = new HashMap<>();
        params.put("x-no-csrf", "1");
        return params;
    }

    public static void setCookie(HttpURLConnection urlConnection) {
        if (HTTPClient.mCookieManager.getCookieStore().getCookies().size() > 0) {
            String cookie = TextUtils.join(";", HTTPClient.mCookieManager.getCookieStore().getCookies());
            urlConnection.setRequestProperty("Cookie", cookie);
        }
    }

    public static String readInputStream(HttpURLConnection urlConnection) {
        String responseBody = "";

        try {
            urlConnection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            responseBody = sb.toString();
            urlConnection.disconnect();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return responseBody;
    }

    public static String getWebpage(String url) {
        HttpURLConnection urlConnection = getOpenHttpConnection(url, "GET");
        setCookie(urlConnection);
        return readInputStream(urlConnection);

    }

    public static String buildRequestBody(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }
        } catch (java.io.UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        }

        // remove final &
        return result.substring(0, result.length() - 1);
    }

    public static int writeOutputStream(HttpURLConnection urlConnection, String requestBody) {
        try {
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            os.close();

            return urlConnection.getResponseCode();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    public static int postWebpage(String url, Map<String, String> params) {

        HttpURLConnection urlConnection = HTTPClient.getOpenHttpConnection(url, "POST");
        setCookie(urlConnection);
        String requestBody = HTTPClient.buildRequestBody(params);
        int responseStatus = HTTPClient.writeOutputStream(urlConnection, requestBody);
        HTTPClient.readInputStream(urlConnection);
        return responseStatus;
    }
}
