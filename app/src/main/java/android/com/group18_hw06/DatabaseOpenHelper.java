package android.com.group18_hw06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by murali101002 on 10/18/2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "cities.db";
    static final int DB_VER = 1;
    public DatabaseOpenHelper(Context context) {
        super(context,DB_NAME,null,DB_VER);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        WeatherTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        WeatherTable.onUpgrade(db,oldVersion,newVersion);
    }
}
