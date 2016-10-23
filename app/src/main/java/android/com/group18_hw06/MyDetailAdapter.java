package android.com.group18_hw06;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by murali101002 on 10/19/2016.
 */
public class MyDetailAdapter extends RecyclerView.Adapter<MyDetailAdapter.DetailViewHolder> {
    ArrayList<Weather> mWeatherList;
    Context activity;
    Weather mWeather, weather;
    int index = 0;
    SharedPreferences sharedPreferences = null;
    public MyDetailAdapter(ArrayList<Weather> mWeather, Context activity, Weather weather) {
        this.activity = activity;
        this.mWeatherList = mWeather;
        this.weather = weather;
    }

    public MyDetailAdapter(Weather clickedItemWeather) {

    }

    @Override
    public MyDetailAdapter.DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_detail,parent,false);
        MyDetailAdapter.DetailViewHolder detailViewHolder = new DetailViewHolder(view);
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(MyDetailAdapter.DetailViewHolder holder, int position) {
        mWeather = mWeatherList.get(position);
        String tempUnit = sharedPreferences.getString("temperatureUnit", "0");
        DecimalFormat df = new DecimalFormat("#.##");
        String cf = mWeather.getTemp() + "°C";
        if (tempUnit.equals("2")) cf = String.valueOf((Double.parseDouble(mWeather.getTemp()) * 1.8) + 32).substring(0,5)+"°F";
        holder.temp.setText(cf);
        holder.date.setText("Three Hourly Forecast on "+mWeather.getDate());
        holder.condition.setText(mWeather.getCondition());
        holder.pressure.setText(mWeather.getPressure());
        holder.humidity.setText(mWeather.getHumidity());
        Picasso.with(activity).load(mWeather.getImg()).into(holder.imageView);
        holder.wind.setText(mWeather.getWindSpeed()+", "+mWeather.getWind());
        String[] time = mWeather.getTime().split(":");
        String amPm = "AM";
        if(time[0].equals("00")) time[0] = "12";
        else if(Integer.parseInt(time[0])>12){
            time[0] = String.valueOf(Integer.parseInt(time[0])-12);
            amPm = "PM";
        }
        else if(Integer.parseInt(time[0])==12) amPm = "PM";
        holder.time.setText(time[0]+":"+time[1]+" "+amPm);
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView temp, condition, pressure, humidity, wind, date, time;
        ImageView imageView;
        LinearLayout linearLayout;
        public DetailViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.timeDetailsView);
            temp = (TextView) itemView.findViewById(R.id.temp);
            condition = (TextView) itemView.findViewById(R.id.condition);
            pressure = (TextView) itemView.findViewById(R.id.pressure);
            humidity = (TextView) itemView.findViewById(R.id.humidity);
            wind = (TextView) itemView.findViewById(R.id.wind);
            date = (TextView) itemView.findViewById(R.id.dateDetailView);
            imageView = (ImageView) itemView.findViewById(R.id.imgLargeView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_recycleDetail);
            linearLayout.getLayoutParams().width = (int)getScreenWidth(itemView.getContext());
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        }
    }
    public static int getScreenWidth(Context context) {
        int screenWidth = 0;
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }
}
