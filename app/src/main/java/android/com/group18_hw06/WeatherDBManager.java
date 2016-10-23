package android.com.group18_hw06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by murali101002 on 10/18/2016.
 */
public class WeatherDBManager {
    DatabaseOpenHelper databaseOpenHelper;
    Context context;
    SQLiteDatabase db;

    public WeatherDAO getWeatherDAO() {
        return weatherDAO;
    }

    WeatherDAO weatherDAO;
    public WeatherDBManager(Context context) {
        this.context = context;
        databaseOpenHelper = new DatabaseOpenHelper(context);
        db = databaseOpenHelper.getWritableDatabase();
        weatherDAO = new WeatherDAO(db);
    }
    public void close(){
        if(db!=null){
            db.close();
        }
    }

    

    public long saveWeather(WeatherPrefs weatherPrefs){
        return this.weatherDAO.save(weatherPrefs);
    }
    public boolean updateWeather(WeatherPrefs weatherPrefs){
        return this.weatherDAO.update(weatherPrefs);
    }
    public boolean deleteWeather(WeatherPrefs weatherPrefs){
        return this.weatherDAO.delete(weatherPrefs);
    }
    public WeatherPrefs getWeather(String city){
        return this.weatherDAO.get(city);
    }
    public List<WeatherPrefs> getAllWeatherData(){
        return this.weatherDAO.getAll();
    }
}
