package android.com.group18_hw06;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by murali101002 on 10/20/2016.
 */
public class DBArrayAdapter extends ArrayAdapter<WeatherPrefs> {
    Context mContext;
    int mResource;
    List<WeatherPrefs> weatherPrefses;
    ImageView favIcon;
    WeatherPrefs weatherPrefs, weatherPrefsClone;
    public DBArrayAdapter(Context context, int resource, List<WeatherPrefs> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.weatherPrefses = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        RecyclerView.ViewHolder viewHolder=new RecyclerView.ViewHolder();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        weatherPrefs = weatherPrefses.get(position);
        weatherPrefsClone = weatherPrefs;
        TextView locPrefs  = (TextView) convertView.findViewById(R.id.loc_prefs);
        TextView prefsTemp  = (TextView) convertView.findViewById(R.id.temp_prefs);
        TextView prefsDate  = (TextView) convertView.findViewById(R.id.date_prefs);
        favIcon = (ImageView) convertView.findViewById(R.id.favIcon);
        favIcon.setOnClickListener(new favIconChanged(weatherPrefs));
        locPrefs.setText(weatherPrefs.city.toUpperCase()+", "+weatherPrefs.country.toUpperCase());
        if(weatherPrefs.getFavs().equalsIgnoreCase("true")){
            favIcon.setImageResource(R.drawable.star_gold);
        }else {
            favIcon.setImageResource(R.drawable.star_gray);
        }
        prefsTemp.setText(weatherPrefs.getTemperature()+"\u00b0 C");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String currDate = formatter.format(date);
        prefsDate.setText("Updated On: "+currDate);
        return convertView;
    }

    private class favIconChanged implements View.OnClickListener {
        WeatherPrefs weatherPrefs;
        public favIconChanged(WeatherPrefs weatherPrefs) {
            this.weatherPrefs = weatherPrefs;
        }

        @Override
        public void onClick(View v) {
            if(weatherPrefs.getFavs().equals("false")){
                weatherPrefs.setFavs("true");
            }
            favIcon.setImageResource(R.drawable.star_gold);
            weatherPrefses.remove(weatherPrefs);
            weatherPrefses.add(0,weatherPrefsClone);
        }
    }
}
