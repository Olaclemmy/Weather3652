package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Forecast;
import model.Place;
import model.Weather;
import utils.Utils;

/**
 * Created by speci on 6/11/2017.
 */

public class JSONWeatherParser {

    public JSONWeatherParser() {
    }

    public static Weather getWeather(String data){

        Weather weather = new Weather();

        //Create JSONObject from data
        try {
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();

            JSONObject coordObj = Utils.getObject("coord", jsonObject);
            place.setLat(Utils.getFloat("lat", coordObj));
            place.setLon(Utils.getFloat("lon", coordObj));

            //Get the sysObject
            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setLastUpdate(Utils.getInt("dt", jsonObject));
            place.setSunRise(Utils.getInt("sunrise", sysObj));
            place.setSunSet(Utils.getInt("sunset", sysObj));
            place.setCity(Utils.getString("name", jsonObject));
            weather.place = place;

            //Get the weather info. Note:this is an array of elements.
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));



            //Get mainObject info
            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
            weather.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));


            //Get wind info
            JSONObject windObj = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed", windObj));
            weather.wind.setDeg(Utils.getFloat("deg", windObj));

            //Get clouds info
            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static Forecast getForecast(String data){

        Forecast forecast = new Forecast();

        //Create JSONObject from data
        try {
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();


            //Get the cityObject
            JSONObject cityObj = Utils.getObject("city", jsonObject);
            place.setId(Utils.getDouble("id", cityObj));
            place.setCountry(Utils.getString("country", cityObj));
            place.setCity(Utils.getString("name", cityObj));

            JSONObject coordObj = Utils.getObject("coord", cityObj);
            place.setLat(Utils.getFloat("lat", coordObj));
            place.setLon(Utils.getFloat("lon", coordObj));

            forecast.place = place;

            JSONArray listJSONArray = jsonObject.getJSONArray("list");

            //Get the weather info. Note:this is an array of elements.
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            forecast.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            forecast.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            forecast.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            forecast.currentCondition.setIcon(Utils.getString("icon", jsonWeather));



            //Get mainObject info
            JSONObject mainObj = Utils.getObject("main", jsonObject);
            forecast.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
            forecast.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
            forecast.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
            forecast.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            forecast.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));


            //Get wind info
            JSONObject windObj = Utils.getObject("wind", jsonObject);
            forecast.wind.setSpeed(Utils.getFloat("speed", windObj));
            forecast.wind.setDeg(Utils.getFloat("deg", windObj));

            //Get clouds info
            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            forecast.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return forecast;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }
}
