package android.com.group18_hw06;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by murali101002 on 10/18/2016.
 */
public class GetXMLData extends AsyncTask<String, Void, ArrayList<Weather>> {
    InterfaceData IData;
    ProgressDialog progressDialog;

    public GetXMLData(InterfaceData IData) {
        this.IData = IData;
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        InputStream inputStream = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            inputStream = con.getInputStream();
            return WeatherData.ParseXMLData.GetData(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weatherArrayAdapter) {
        super.onPostExecute(weatherArrayAdapter);
        IData.setUpData(weatherArrayAdapter);
        progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(IData.getContext());
        progressDialog.setTitle("Loading Hourly Data");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public static interface InterfaceData{
        public Context getContext();
        public void setUpData(ArrayList<Weather> weatherDataList);
    }
}
