package android.com.group18_hw06;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murali101002 on 10/18/2016.
 */
public class WeatherDAO {
    private SQLiteDatabase db;

    public WeatherDAO(SQLiteDatabase db) {
        this.db = db;
    }
    public long save(WeatherPrefs weatherPrefs){
        ContentValues values = new ContentValues();
        values.put(WeatherTable.WEATHER_CITY,weatherPrefs.getCity());
        values.put(WeatherTable.WEATHER_COUNTRY,weatherPrefs.getCountry());
        values.put(WeatherTable.WEATHER_TEMP,weatherPrefs.getTemperature());
        values.put(WeatherTable.WEATHER_FAV,weatherPrefs.getFavs());
        return db.insert(WeatherTable.TABLE_NAME,null,values);
    }
    public boolean update(WeatherPrefs weatherPrefs){
        ContentValues values = new ContentValues();
        values.put(WeatherTable.WEATHER_CITY,weatherPrefs.getCity());
        values.put(WeatherTable.WEATHER_COUNTRY,weatherPrefs.getCountry());
        values.put(WeatherTable.WEATHER_TEMP,weatherPrefs.getTemperature());
        values.put(WeatherTable.WEATHER_FAV,weatherPrefs.getFavs());
        return db.update(WeatherTable.TABLE_NAME,values,WeatherTable.WEATHER_CITY+"=?",new String[]{weatherPrefs.getCity()+""})>0;
    }
    public boolean delete(WeatherPrefs weatherPrefs){
        return db.delete(WeatherTable.TABLE_NAME,WeatherTable.WEATHER_CITY+"=?",new String[]{weatherPrefs.getCity()+""})>0;
    }
    public WeatherPrefs get(String city){
        WeatherPrefs weatherPrefs = new WeatherPrefs();
        Cursor cursor = db.query(true,WeatherTable.TABLE_NAME,
                new String[]{WeatherTable.WEATHER_CITY,WeatherTable.WEATHER_COUNTRY,WeatherTable.WEATHER_TEMP,WeatherTable.WEATHER_FAV},
                WeatherTable.WEATHER_CITY+"=?",new String[]{city+""},null,null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            weatherPrefs = buildNoteFromCursor(cursor);
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return weatherPrefs;
    }
    public List<WeatherPrefs> getAll(){
        List<WeatherPrefs> weatherPrefsArrayList = new ArrayList<>();
        Cursor cursor = db.query(WeatherTable.TABLE_NAME,
                new String[]{WeatherTable.WEATHER_CITY,WeatherTable.WEATHER_COUNTRY,WeatherTable.WEATHER_TEMP,WeatherTable.WEATHER_FAV},
                null,null,null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            do{
                WeatherPrefs weatherPrefs = buildNoteFromCursor(cursor);
                if(weatherPrefs!=null){
                    weatherPrefsArrayList.add(weatherPrefs);
                }
            }while(cursor.moveToNext());
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return weatherPrefsArrayList;
    }
    private WeatherPrefs buildNoteFromCursor(Cursor c){
        WeatherPrefs weatherPrefs = null;
        if(c!=null){
            weatherPrefs = new WeatherPrefs();
            weatherPrefs.setCity(c.getString(0));
            weatherPrefs.setCountry(c.getString(1));
            weatherPrefs.setTemperature(c.getString(2));
            weatherPrefs.setFavs(c.getString(3));
        }
        return weatherPrefs;
    }
}

