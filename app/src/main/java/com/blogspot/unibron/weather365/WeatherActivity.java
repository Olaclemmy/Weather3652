package com.blogspot.unibron.weather365;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.text.DecimalFormat;

import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

import static com.blogspot.unibron.weather365.R.id.hum;

public class WeatherActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Weather weather;

    // Widget
    private TextView tempView;
    private ImageView weatherIcon;
    private TextView pressView;
    private TextView humView;
    private TextView windView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //
        tempView = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        pressView = (TextView) findViewById(R.id.pressure);
        humView = (TextView) findViewById(hum);
        windView = (TextView) findViewById(R.id.wind);

        if (!isConnected(WeatherActivity.this)){
            buildDialog(WeatherActivity.this).show();
        }else {
            CityPreference cityPreference = new CityPreference(WeatherActivity.this);
            renderWeatherData(cityPreference.getCity());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_search) {
            // We show the dialog
            Dialog d = createDialog();
            d.show();
        }*/
        if (id == R.id.action_search) {
            showInputDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        builder.setTitle("change city");

        final EditText cityInput = new EditText(WeatherActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Lagos,NG");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreference cityPreference = new CityPreference(WeatherActivity.this);
                cityPreference.setCity(cityInput.getText().toString());
                toolbar.setTitle(cityPreference.getCity());
                String newCity = cityPreference.getCity();

                if (!isConnected(WeatherActivity.this)){
                    buildDialog(WeatherActivity.this).show();
                }else {
                    renderWeatherData(newCity);
                }
            }
        });
        builder.show();
    }



    public void renderWeatherData(String city) {

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        ProgressDialog pd = new ProgressDialog(WeatherActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();
        }

        @Override
        protected Weather doInBackground(String... params) {

            String data;
            try {
                data = ((new WeatherHttpClient()).getWeatherData(params[0]));
                weather = JSONWeatherParser.getWeather(data);
                Log.v("Data: ", weather.place.getCity()+ ","+ weather.place.getCountry());

                //iconId = weather.currentCondition.getIcon();
                return weather;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if(weather == null){
                pd.dismiss();
                return;
            }
            pd.dismiss();

            //DateFormat df = DateFormat.getTimeInstance();
            //String sunriseDate = df.format(new Date(weather.place.getSunRise()));
            //String sunsetDate = df.format((new Date(weather.place.getSunSet())));
            //String updateDate = df.format(new Date((weather.place.getLastUpdate())));

            DecimalFormat decimalFormat = new DecimalFormat("##");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            //cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            //tempView.setText(weather.currentCondition.getTemperature()+ "°C");
            tempView.setText("" + tempFormat + "°C");
            humView.setText(weather.currentCondition.getHumidity()+ "%");
            pressView.setText(weather.currentCondition.getPressure()+ "hpa");
            windView.setText( weather.wind.getSpeed()+ "mps");
            toolbar.setTitle(weather.place.getCity() + "," + weather.place.getCountry());
            toolbar.setSubtitle(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescription()+ ")");
            setToolbarColor((float) weather.currentCondition.getTemperature());
            weatherIcon.setImageResource(WeatherIconMapper.getWeatherResource(weather.currentCondition.getIcon(), weather.currentCondition.getWeatherId()));
        }
    }





    private void setToolbarColor(float temp) {

        int color = -1;
        if (temp < -10)
            color = getResources().getColor(R.color.primary_indigo);
        else if (temp >=-10 && temp <=-5)
            color = getResources().getColor(R.color.primary_blue);
        else if (temp >-5 && temp < 5)
            color = getResources().getColor(R.color.primary_light_blue);
        else if (temp >= 5 && temp < 10)
            color = getResources().getColor(R.color.primary_teal);
        else if (temp >= 10 && temp < 15)
            color = getResources().getColor(R.color.primary_light_green);
        else if (temp >= 15 && temp < 20)
            color = getResources().getColor(R.color.primary_green);
        else if (temp >= 20 && temp < 25)
            color = getResources().getColor(R.color.primary_lime);
        else if (temp >= 25 && temp < 28)
            color = getResources().getColor(R.color.primary_yellow);
        else if (temp >= 28 && temp < 32)
            color = getResources().getColor(R.color.primary_amber);
        else if (temp >= 32 && temp < 35)
            color = getResources().getColor(R.color.primary_orange);
        else if (temp >= 35)
            color = getResources().getColor(R.color.primary_red);
        toolbar.setBackgroundColor(color);
    }

    public boolean isConnected(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting())||(wifi != null && wifi.isConnectedOrConnecting())){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public AlertDialog.Builder buildDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have mobile data or wifi to access this. Press OK to EXIT");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                finish();
            }
        });
        return builder;
    }


}
