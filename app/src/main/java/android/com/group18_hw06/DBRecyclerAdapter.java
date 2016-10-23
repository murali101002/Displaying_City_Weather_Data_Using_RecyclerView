package android.com.group18_hw06;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by murali101002 on 10/20/2016.
 */
public class DBRecyclerAdapter extends RecyclerView.Adapter<DBRecyclerAdapter.LayoutHolder> {
    ArrayList<WeatherPrefs> weatherPrefsArrayList;
    Context context;
    WeatherPrefs weatherPrefs;
    DBRecycler dbRecycler = null;
    WeatherDBManager weatherDBManager = null;
    SharedPreferences sharedPreferences = null;
    public DBRecyclerAdapter(ArrayList<WeatherPrefs> weatherPrefsArrayList, Context context) {
        this.weatherPrefsArrayList = weatherPrefsArrayList;
        this.context = context;
        this.dbRecycler = (DBRecycler) context;
    }

    @Override
    public LayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prefs_layout,parent,false);
        DBRecyclerAdapter.LayoutHolder viewHolder = new LayoutHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LayoutHolder holder, int position) {
        String tempUnit = sharedPreferences.getString("temperatureUnit", "0");
        weatherPrefs = weatherPrefsArrayList.get(position);
        String cf = weatherPrefs.getTemperature() + "°C";
        if (tempUnit.equals("2")) cf = String.valueOf((Double.parseDouble(weatherPrefs.getTemperature()) * 1.8) + 32).substring(0,5)+"°F";

        holder.locPrefs.setText(weatherPrefs.city.toUpperCase()+", "+weatherPrefs.country.toUpperCase());
        if(weatherPrefs.getFavs().equalsIgnoreCase("true")){
            holder.favIcon.setImageResource(R.drawable.star_gold);
        }else {
            holder.favIcon.setImageResource(R.drawable.star_gray);
        }
        holder.favIcon.setOnClickListener(new favIconChanged(weatherPrefs, holder, position));
        holder.tempPrefs.setText(cf);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String currDate = formatter.format(date);
        holder.datePrefs.setText("Updated On: "+currDate);
        holder.relativeLayout.setOnLongClickListener(new itemRemoved(position));

    }

    @Override
    public int getItemCount() {
        return weatherPrefsArrayList.size();
    }

    public class LayoutHolder extends RecyclerView.ViewHolder {
        TextView locPrefs, tempPrefs, datePrefs;
        ImageView favIcon;
        RelativeLayout relativeLayout;
        public LayoutHolder(View itemView) {
            super(itemView);
            locPrefs  = (TextView) itemView.findViewById(R.id.loc_prefs);
            tempPrefs  = (TextView) itemView.findViewById(R.id.temp_prefs);
            datePrefs  = (TextView) itemView.findViewById(R.id.date_prefs);
            favIcon = (ImageView) itemView.findViewById(R.id.favIcon);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rlMain);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        }
    }

    private class favIconChanged implements View.OnClickListener {
        WeatherPrefs weatherPrefs;
        LayoutHolder holder;
        int index;
        public favIconChanged(WeatherPrefs weatherPrefs, LayoutHolder holder, int position) {
            this.weatherPrefs = weatherPrefs;
            this.holder = holder;
            this.index = position;
        }

        @Override
        public void onClick(View v) {
            weatherDBManager = new WeatherDBManager(v.getContext());
            if(weatherPrefs.getFavs().equals("false")){
                weatherPrefs.setFavs("true");
                weatherDBManager.updateWeather(weatherPrefs);
            }
            holder.favIcon.setImageResource(R.drawable.star_gold);
            weatherPrefsArrayList.remove(weatherPrefs);
            weatherPrefsArrayList.add(0,weatherPrefs);
            notifyDataSetChanged();
            notifyItemMoved(index, 0);
        }
    }

    public interface  DBRecycler{
        public void refreshData();
    }

    private class itemRemoved implements View.OnLongClickListener {
        int removedIndex;
        public itemRemoved(int position) {
            this.removedIndex = position;
        }

        @Override
        public boolean onLongClick(View v) {
            weatherDBManager = new WeatherDBManager(v.getContext());
            weatherDBManager.deleteWeather(weatherPrefsArrayList.get(removedIndex));
            weatherPrefsArrayList.remove(removedIndex);
            notifyDataSetChanged();
            notifyItemRemoved(removedIndex);
            Toast.makeText(v.getContext(), "Favourite Deleted", Toast.LENGTH_SHORT).show();
            dbRecycler.refreshData();
            return false;
        }

    }
}
