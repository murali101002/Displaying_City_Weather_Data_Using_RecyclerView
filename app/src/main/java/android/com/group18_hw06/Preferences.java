package android.com.group18_hw06;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;

public class Preferences extends AppCompatActivity implements View.OnClickListener {
    public static final String UNIT = "unit";
    LinearLayout linearLayout;
    String unit = null;
    PreferencesActivity preferencesActivity;
    CharSequence[] items = new CharSequence[]{"°C","°F"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        linearLayout = (LinearLayout) findViewById(R.id.ll_prefs);
        linearLayout.setOnClickListener(this);
    }



    public static interface PreferencesActivity{
        public void setTempUnit(String unit);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Select Temperature Unit");
        builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unit = (String) items[which];
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                prefsEditor.putString(UNIT, unit);
                prefsEditor.commit();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
