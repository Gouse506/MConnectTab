package in.vmc.mconnecttab.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mukesh on 17/11/15.
 */
public class JSONParser implements TAG {
    static JSONObject jObj = null;


    public JSONParser() {

    }

    public static JSONObject getJSONFromUrl(String url) {
        HttpURLConnection urlConnection = null;

        StringBuilder result = new StringBuilder();

        try {
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            //urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public static String insertIntoJSONFromUrl(String url1) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", "Freddie the Fish");
        params.put("email", "fishie@seamail.example.com");
        params.put("user", 10394);
        params.put("pass", "Shark attacks in Botany Bay have gotten out of control. " +
                "We need more defensive dolphins to protect the schools here, but Mayor " +
                "Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        // jObj = new JSONObject(result.toString());
        return result.toString();

    }

    public static JSONObject login(String url1, String email, String password) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
           /* URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);*/
            URL url = new URL(src);
            Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return myBitmap;
        } catch (IOException e) {
            Log.d("LOGO", e.toString());
            return null;
        }


    }


    public static JSONObject CheckVisitJASON(String url1, String authkey, String beaconId) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(BEACONID, beaconId);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject GetlocationDetail(String u, String bid, String beaconId, String authkey) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(u);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(ID, bid);
        params.put(BEACONID, beaconId);
        params.put(AUTHKEY, authkey);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject GetImages(String u, String sietId) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(u);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SITEID, sietId);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject DeleteSite(String u, String sietId, String authkey) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(u);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SITEID, sietId);
        params.put(AUTHKEY, authkey);


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject GetVistDetail(String url1, String authkey) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject GetAllSites(String url1, String bid, String offset, String limit) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(BID, bid);
        params.put(OFFSET, offset);
        params.put(LIMIT, limit);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("RESPONSE", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject SubmitFeedBack(String url1, String authkey, String feedback, String siteid) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(FEEDBACK, feedback);
        params.put(SITEID, siteid);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject Getlikeunlike(String url1, String authkey, String siteid, String bid) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(SITEID, siteid);
        params.put(BID, bid);


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject SubmitQuery(String url1, String authkey, String interest, String query, String siteid, String beaconId) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(SITEID, siteid);
        params.put(INTERESTEDIN, interest);
        params.put(QUERY, query);
        params.put(BEACONID, beaconId);


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject ReferQuery(String url1, String authkey, String siteid, String name, String messagee, String phone, String email) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(SITEID, siteid);
        params.put(NAME, name);
        params.put("message", messagee);
        params.put(NUMBER, phone);
        params.put(EMAIL, email);


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject UpdateUserProfile(String url1, String authkey, String name, String email, String dob, String gender, String password, String image) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(NAME, name);
        params.put(EMAIL, email);
        params.put(DOB, dob);
        params.put(GENDER, gender);
        params.put(PASSWORD, password);
        params.put("image", image);


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("UPDATE", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject GetSiteDetail(String url1, String authkey, String siteId) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        params.put(SITEID, siteId);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject GetProfileDetail(String url1, String authkey) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(AUTHKEY, authkey);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.d("LOG", result.toString());
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject getOTP(String url1, String phone) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(PHONE, phone);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject getChangePassword(String url1, String phone, String NewPassword) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(PHONE, phone);
        params.put("password", NewPassword);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject ChangePass(String url1, String phone, String otp, String password) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(OTP, otp);
        // Log.d("OTP", otp);
        params.put(NUMBER, phone);
        //  Log.d("OTP",phone);
        params.put(PASSWORD, password);
        //  Log.d("OTP", password);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        jObj = new JSONObject(result.toString());
        return jObj;

    }

    public static JSONObject Register(String url1, String name, String email, String number, String address, String city, String state, String pincode, String eid) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(url1);
        Map<String, Object> params = new LinkedHashMap<>();
        Log.d("URL",url1);
        params.put(NAME, name);
        Log.d(NAME, name);
        params.put(EMAIL, email);
        Log.d(EMAIL, email);
        params.put(NUMBER, number);
        Log.d(NUMBER, number);
        params.put(ADDRESS, address);
        Log.d(ADDRESS, address);
        Log.d(CITY, city);
        params.put(CITY, city);
        Log.d(STATE, state);
        params.put(STATE, state);
        Log.d(PINCODE, pincode);
        params.put(PINCODE, pincode);
        params.put(EID, eid);
        Log.d(EID, eid);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        jObj = new JSONObject(result.toString());
        return jObj;

    }
}
