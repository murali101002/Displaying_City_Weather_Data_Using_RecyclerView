package android.com.group18_hw06;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;

public class CityWeather extends AppCompatActivity implements GetXMLData.InterfaceData, MyAdapter.RecyclerActivity {
    TextView dailyForecast;
    String city, country, url, key;
    WeatherDBManager weatherDBManager;
    public RecyclerView mRecyclerView, mRecyclerView_detail;
    private RecyclerView.Adapter mAdapter, mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManager1;
    static ArrayList<Weather> weatherArrayList, recyclerViewDataset;
    ArrayList<String> weatherDateList = new ArrayList<>();
    static ArrayList<Weather> recyclerWeatherList = null;
    Weather clickedItemWeather = null;
    double avgTemp = 0.0;

    public CityWeather(CityWeatherInterface cityWeatherInterface) {
        this.cityWeatherInterface = cityWeatherInterface;
    }

    public CityWeather() {
    }

    CityWeatherInterface cityWeatherInterface;
    WeatherPrefs weatherPrefs = null;
    static ArrayList<WeatherPrefs> insertedWeatherPrefs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        dailyForecast = (TextView) findViewById(R.id.dailyForecastLbl);
        key = "5b06d87a61c2b08d3a5c690f6771bb4c";
        city = (String) getIntent().getExtras().get(MainActivity.CITY);
        country = (String) getIntent().getExtras().get(MainActivity.COUNTRY);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView_detail = (RecyclerView) findViewById(R.id.recycler_Detailview);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView_detail.setHasFixedSize(true);
        mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_detail.setLayoutManager(mLayoutManager1);

        url = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&mode=xml&appid=" + key;
        new GetXMLData(this).execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String tempUnit = sharedPreferences.getString("temperatureUnit", "0");
        refreshCityWeatherData();
    }

    private void refreshCityWeatherData() {
        if(recyclerViewDataset!=null){
            mAdapter = new MyAdapter(recyclerViewDataset, getApplicationContext(), this);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            setRecyclerDetailedView(clickedItemWeather);
        }
    }

    public static interface CityWeatherInterface{
        public void getInsertedWeatherPref(ArrayList<WeatherPrefs> weatherPrefsArrayList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSaveToFavs:
                weatherPrefs = new WeatherPrefs();
                weatherPrefs.setCity(city);
                weatherPrefs.setCountry(country);
                weatherPrefs.setFavs("false");
                weatherPrefs.setTemperature(weatherArrayList.get(0).getTemp());
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
                String currDate = formatter.format(date);
                weatherPrefs.setDate(currDate);
                weatherDBManager = new WeatherDBManager(this);
                if(weatherDBManager.saveWeather(weatherPrefs)>0){
                    Toast.makeText(this,"City is saved to Favorites",Toast.LENGTH_SHORT).show();
                    insertedWeatherPrefs.add(weatherPrefs);
                }else{
                    weatherDBManager.updateWeather(weatherPrefs);
                    Toast.makeText(this,"City is updated in Favorites",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.btnSettings:
                Intent i = new Intent(this, MyPreferenceActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<WeatherPrefs> insertedWeather(){
        return insertedWeatherPrefs;
    }

    @Override
    public Context getContext() {
        return this;

    }

    @Override
    public void setUpData(ArrayList<Weather> weatherDataList) {
        dailyForecast.setText("Daily Forecast for " + city.toUpperCase() + ", " + country.toUpperCase());
        weatherArrayList = (ArrayList<Weather>) weatherDataList.clone();
        weatherDateList.add(weatherArrayList.get(0).getDate());
        recyclerViewDataset = new ArrayList<>();
        DecimalFormat df2 = new DecimalFormat(".##");
        ArrayList<Weather> tobeDel = new ArrayList<>();
        for(Weather weather:weatherArrayList){

            if(weather.getDate().equals(weatherArrayList.get(0).getDate())){
                tobeDel.add(weather);
            }
        }
        String temp1[] = null;
        double avgTemp1 = 0.0;
        for(Weather weather:tobeDel){
            temp1 = weather.getTemp().split("°");
            avgTemp1+=Double.parseDouble(temp1[0]);
        }
        weatherArrayList.get(0).setAvgTemp(String.valueOf(df2.format(avgTemp1/tobeDel.size())));
        weatherArrayList.get(0).setMedianImg(weatherArrayList.get(tobeDel.size()/2).getImg());
        recyclerViewDataset.add(weatherArrayList.get(0));
        recyclerWeatherList = new ArrayList<>();
        for (Weather weather : weatherArrayList) {
            if (!weatherDateList.contains(weather.getDate())) {
                weatherDateList.add(weather.getDate());
                String[] temp =null;
                for(Weather weather1:recyclerWeatherList){
                    temp = weather1.getTemp().split("°");
                    avgTemp+=Double.parseDouble(temp[0]);
                }
                weather.setAvgTemp(String.valueOf(df2.format(avgTemp/recyclerWeatherList.size())));
                weather.setMedianImg(weatherDataList.get(recyclerWeatherList.size()/2).getImg());
                recyclerViewDataset.add(weather);
            }else recyclerWeatherList.add(weather);
        }
        mAdapter = new MyAdapter(recyclerViewDataset, getApplicationContext(), this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        setRecyclerDetailedView(clickedItemWeather);
    }

    private void setRecyclerDetailedView(Weather clickedWeather) {
        mAdapter1 = new MyDetailAdapter(weatherArrayList, getApplicationContext(), clickedItemWeather);
        mAdapter1.notifyDataSetChanged();
        mRecyclerView_detail.setAdapter(mAdapter1);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void InvokeRecycleDetailedView(Weather weather, int layoutPos) {
        clickedItemWeather = weather;
        for (Weather weather1 : weatherArrayList) {
            if (clickedItemWeather.getTime().equals(weather1.getTime())) {
                mRecyclerView_detail.getLayoutManager().scrollToPosition(weatherArrayList.indexOf(clickedItemWeather));
                break;
            }
        }
    }

    public String convertToCelcius(String farenheit) {
        return String.valueOf((Integer.parseInt(farenheit) * 0.5556) - 32) + "°C";
    }

    public String convertToFarenheit(String celcius) {
        return String.valueOf((Integer.parseInt(celcius) * 1.8) + 32) + "°F";
    }


}
