package android.com.group18_hw06;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    static Context activity;
    RecyclerActivity recyCleActivity;
    static Weather weather = null;
    static ArrayList<Weather> mWeatherList;
    static RecyclerView.ViewHolder viewHolder;
    static int index = 0;
    static SharedPreferences sharedPreferences = null;

    public MyAdapter(ArrayList<Weather> weatherList, Context applicationContext, RecyclerActivity recyCleActivity) {
        this.mWeatherList = weatherList;
        this.activity = applicationContext;
        this.recyCleActivity = recyCleActivity;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        MyAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        index = position;
        String tempUnit = sharedPreferences.getString("temperatureUnit", "0");
        weather = mWeatherList.get(position);
        String cf = weather.getAvgTemp() + "°C";
        if (tempUnit.equals("2")) cf = String.valueOf((Double.parseDouble(weather.getAvgTemp()) * 1.8) + 32).substring(0,5)+"°F";
        holder.date.setText(weather.getDate());
        Picasso.with(activity).load(weather.getMedianImg()).into(holder.img);
        holder.temp.setText(cf);
        holder.linearLayout.setOnClickListener(new GetLayoutPosition(holder.getLayoutPosition(), weather));
        viewHolder = holder;
        int i = holder.getLayoutPosition();
    }


    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, temp;
        ImageView img;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.dateRecyclerListItem);
            temp = (TextView) itemView.findViewById(R.id.tempRecyclerListItem);
            img = (ImageView) itemView.findViewById(R.id.imgRecyclerListItem);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_recyclerItem);
            linearLayout.getLayoutParams().width = (int) getScreenWidth(itemView.getContext()) / 3;
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

    public interface RecyclerActivity {
        public Context getAppContext();

        public void InvokeRecycleDetailedView(Weather weather, int layoutPos);
    }

    private class GetLayoutPosition implements View.OnClickListener {
        int layoutPos;
        Weather weather1;

        public GetLayoutPosition(int layoutPosition, Weather weather) {
            this.layoutPos = layoutPosition;
            weather1 = weather;
        }

        @Override
        public void onClick(View v) {
            recyCleActivity.InvokeRecycleDetailedView(weather1, layoutPos);
        }
    }
}
