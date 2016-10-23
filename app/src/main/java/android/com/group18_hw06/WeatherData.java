package android.com.group18_hw06;

import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by murali101002 on 10/18/2016.
 */
public class WeatherData {
    public static class ParseXMLData extends DefaultHandler {
        public ArrayList<Weather> getWeatherArrayList() {
            return weatherArrayList;
        }

        String windDir, windSpeed, imgUrl;
        ArrayList<Weather> weatherArrayList;
        Weather weather;
        StringBuilder xmlInnerText;
        int flag = 0;

        public static ArrayList<Weather> GetData(InputStream input) throws IOException, SAXException {
            ParseXMLData parseXMLData = new ParseXMLData();
            Xml.parse(input, Xml.Encoding.UTF_8, parseXMLData);
            return parseXMLData.getWeatherArrayList();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals("time")) {
                weatherArrayList.add(weather);
            }
        }

        @Override
        public void startDocument() throws SAXException {
            weatherArrayList = new ArrayList<>();
            xmlInnerText = new StringBuilder();
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            windDir = null;
            windSpeed = null;
            if (localName.equals("time")) {
                weather = new Weather();
            }
            if (localName.equals("symbol")) {
                imgUrl = "http://openweathermap.org/img/w/" + attributes.getValue("var") + ".png";
                weather.setImg(imgUrl);
            }
            if (localName.equals("windDirection")) {
                windDir = attributes.getValue("deg") + " " + attributes.getValue("code");
                weather.setWind(windDir);
            }
            if (localName.equals("windSpeed")) {
                windSpeed = attributes.getValue("mps");
                weather.setWindSpeed(windSpeed);
            }
            if (localName.equals("temperature")) {
                weather.setTemp(attributes.getValue("value"));
            }
            if (localName.equals("pressure")) {
                weather.setPressure(attributes.getValue("value") + " " + attributes.getValue("unit"));
            }
            if (localName.equals("humidity")) {
                weather.setHumidity(attributes.getValue("value") + "" + attributes.getValue("unit"));
            }
            if (localName.equals("clouds")) {
                weather.setCondition(attributes.getValue("value"));
            }
            if (localName.equals("time")) {
                String[] dt = attributes.getValue("to").split("T");
                SimpleDateFormat ft = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    Date date = (Date) dateFormat.parse(dt[0]);
                    weather.setDate(ft.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                weather.setTime(dt[1].trim().substring(0, 5));

//                weather.setDate(ft.format(date));
            }


        }


    }
}
