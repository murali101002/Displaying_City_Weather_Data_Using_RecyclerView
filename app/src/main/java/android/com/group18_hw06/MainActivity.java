package android.com.group18_hw06;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, CityWeather.CityWeatherInterface, DBRecyclerAdapter.DBRecycler {
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    EditText cityEditText, countryEditText;
    Button searchButton;
    String cityName, countryName;
    TextView noEntriesText, savedCitiesText;
    ListView listView;
    WeatherDBManager weatherDBManager = null;
    static ArrayList<WeatherPrefs> weatherPrefsArrayList = new ArrayList<>();
    DBArrayAdapter adapter = null;
    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<WeatherPrefs> tempWeatherPrefsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityEditText = (EditText) findViewById(R.id.cityEditBox);
        countryEditText = (EditText) findViewById(R.id.countryEditBox);
        searchButton = (Button) findViewById(R.id.submit_btn);
        noEntriesText = (TextView) findViewById(R.id.favourites);
        savedCitiesText = (TextView) findViewById(R.id.savedCities);
        weatherDBManager = new WeatherDBManager(this);
        weatherPrefsArrayList = (ArrayList<WeatherPrefs>) weatherDBManager.getAllWeatherData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        searchButton.setOnClickListener(this);
    }

    private void setCustomLayout(ArrayList<WeatherPrefs> weatherPrefsArrayList) {
        if(weatherPrefsArrayList.size()>0) {
            noEntriesText.setVisibility(View.GONE);
            savedCitiesText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            tempWeatherPrefsList = (ArrayList<WeatherPrefs>) weatherPrefsArrayList.clone();
            mAdapter = new DBRecyclerAdapter(tempWeatherPrefsList,this);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
        }else{
            noEntriesText.setVisibility(View.VISIBLE);
            savedCitiesText.setVisibility(View.GONE);
            savedCitiesText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            cityName = cityEditText.getText().toString().trim();
            countryName = countryEditText.getText().toString().trim();
            if (cityName.length() < 1 || countryName.length() < 1) {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
            } else {
                Intent cityWeatherIntent = new Intent(this, CityWeather.class);
                Bundle bundle = new Bundle();
                bundle.putString(CITY,cityName);
                bundle.putString(COUNTRY,countryName);
                cityWeatherIntent.putExtras(bundle);
                startActivity(cityWeatherIntent);
            }
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CityWeather.insertedWeatherPrefs.size()>0) {
            weatherPrefsArrayList = CityWeather.insertedWeatherPrefs;
        }
//        setCustomLayout(weatherPrefsArrayList);
        setCustomLayout((ArrayList<WeatherPrefs>) weatherDBManager.getAllWeatherData());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        weatherDBManager.deleteWeather(weatherPrefsArrayList.get(position));
        weatherPrefsArrayList.remove(position);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
        setCustomLayout(weatherPrefsArrayList);
        Toast.makeText(this, "Favourite Deleted", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void getInsertedWeatherPref(ArrayList<WeatherPrefs> weatherPrefArrayList) {
        weatherPrefsArrayList = (ArrayList<WeatherPrefs>) weatherPrefArrayList.clone();
    }

    @Override
    public void refreshData() {
        if(weatherDBManager.getAllWeatherData().size() == 0){
            noEntriesText.setVisibility(View.VISIBLE);
            savedCitiesText.setVisibility(View.GONE);
            savedCitiesText.setVisibility(View.GONE);
        }
    }
}
