package com.example.locationweather;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature,mIcon,mCity,mWeathertype;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject)
    {
        try
        {
           weatherData weatherD=new weatherData();
           weatherD.mCity=jsonObject.getString("name");
           weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
           weatherD.mWeathertype=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
           weatherD.mIcon=updateWeatherIcon(weatherD.mCondition);
           double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
           int roundedValue=(int)Math.rint(tempResult);
           weatherD.mTemperature=Integer.toString(roundedValue);
           return weatherD;
        }

        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String updateWeatherIcon(int condition)
    {
        if (condition>=0 && condition<=300)
        {
            return "storm";
        }
        else if (condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if (condition>=500 && condition<=600)
        {
            return "rain";
        }
        else if (condition>=600 && condition<=799)
        {
            return "snow";
        }
        else if (condition==800)
        {
            return "sun";
        }
        else if (condition>=801 && condition<=804)
        {
            return "cloudy";
        }

        return "dunno";

    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getmWeathertype() {
        return mWeathertype;
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }
}
