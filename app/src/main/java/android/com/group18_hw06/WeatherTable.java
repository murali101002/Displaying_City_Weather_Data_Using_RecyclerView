package android.com.group18_hw06;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by murali101002 on 10/18/2016.
 */
public class WeatherTable {
    static final String TABLE_NAME = "Cities";
    static final String WEATHER_CITY = "city";
    static final String WEATHER_COUNTRY = "country";
    static final String WEATHER_TEMP = "temperature";
    static final String WEATHER_FAV = "favourite";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+WeatherTable.TABLE_NAME+" (");
        sb.append(WeatherTable.WEATHER_CITY+" text primary key, ");
        sb.append(WeatherTable.WEATHER_COUNTRY+" text not null, ");
        sb.append(WeatherTable.WEATHER_TEMP+" text not null, ");
        sb.append(WeatherTable.WEATHER_FAV+" text not null ");
        sb.append(");");
        try{
            db.execSQL(sb.toString());
        }catch(SQLException ex){
            ex.printStackTrace();
        }

    }
    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS");
        WeatherTable.onCreate(db);
    }
}
