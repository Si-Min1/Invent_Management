package com.ncp.ai.demo.process;


import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

import static com.example.user.ncpaidemo.BaseActivity.image_path;


public class OcrProc {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String main(String ocrApiUrl, String ocrSecretKey) {
        String apiURL = ocrApiUrl;
        String secretKey = ocrSecretKey;

        String ocrMessage = "";

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            /*image should be public, otherwise, should use data
            image.put("url", "https://kr.object.ncloudstorage.com/fresh-img/KakaoTalk_20210424_225347928.jpg");*/

            FileInputStream inputStream = new FileInputStream(image_path);
            image_path = null;
            byte[] buffer = new byte[inputStream.available()];

            inputStream.read(buffer, 0, buffer.length);
            inputStream.close();
            String imgStr = Base64.getEncoder().encodeToString(buffer);

            image.put("data", imgStr);
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();
            System.out.println("##" + postParams);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //BufferedReader br;
            if (responseCode == 200) {
                //br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                con.getInputStream()));
                String inputLine;
                //StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    //response.append(inputLine);
                    ocrMessage = inputLine;
                }
                in.close();

            } else {
                ocrMessage = con.getResponseMessage();
            }
            System.out.println("@@" + responseCode);
            //br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(">>>>>>>>>>" + ocrMessage);

        return ocrMessage;
    }


}
/*
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;


public class OcrProc {

    public static String main(String ocrApiUrl, String ocrSecretKey) {

        String ocrMessage = "";

        try {

            String apiURL = ocrApiUrl;
            String secretKey = ocrSecretKey;

            String objectStorageURL = "https://kr.object.ncloudstorage.com/labs/ocr/ocr_sample.jpg";

            URL url = new URL(ocrApiUrl);

            String message = getReqMessage(objectStorageURL);
            System.out.println("##" + message);

            long timestamp = new Date().getTime();

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;UTF-8");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            // post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(message.getBytes("UTF-8"));
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            if(responseCode==200) { // 정상 호출
                System.out.println(con.getResponseMessage());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                con.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    ocrMessage = decodedString;
                }
                //chatbotMessage = decodedString;
                in.close();

            } else {  // 에러 발생
                ocrMessage = con.getResponseMessage();

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(">>>>>>>>>>"+ ocrMessage);
        return ocrMessage;
    }

    public static String getReqMessage(String objectStorageURL) {

        String requestBody = "";

        try {

            long timestamp = new Date().getTime();

            JSONObject json = new JSONObject();
            json.put("version", "V1");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", Long.toString(timestamp));
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("url", objectStorageURL);

            image.put("name", "test_ocr");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);

            requestBody = json.toString();

        } catch (Exception e){
            System.out.println("## Exception : " + e);
        }

        return requestBody;

    }}
*/
