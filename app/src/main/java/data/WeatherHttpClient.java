package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import utils.Utils;

/**
 * Created by speci on 6/11/2017.
 */

public class WeatherHttpClient {

    public WeatherHttpClient() {
    }

    public String getWeatherData(String place) throws MalformedURLException {

        HttpURLConnection connection;
        InputStream inputStream;

        try {
            connection = (HttpURLConnection)(new URL(Utils.BASE_URL + place + "&APPID=a63e993a387d7054d5c66ea4c619e62a")).openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                    //Read the respond
                    StringBuffer stringBuffer = new StringBuffer();
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\r\n");
                    }

                    inputStream.close();
                    connection.disconnect();

                    return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String geForecastData(String place) throws MalformedURLException {

        HttpURLConnection connection;
        InputStream inputStream;

        try {
            connection = (HttpURLConnection)(new URL(Utils.FORECAST_URL + place + "&APPID=a63e993a387d7054d5c66ea4c619e62a")).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //Read the respond
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close();
            connection.disconnect();

            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
