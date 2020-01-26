package com.example.skyulee.myweatherproject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String locationLabel;
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;

    //two different constructors are required for our class
    //one accepts nothing which will be used in our MainActivity in getCurrentDetails method
    // and one accepts everything which will be used when we bind our data

    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, long time, double temperature,
                          double humidity, double precipChance, String summary, String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    //In order to get all these getter and setters automatically, use android studio's Code tab
    //Press Generate and press Getter and Setter


    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconId(){ //if the icon is an int value it will be easier to work with

        int iconId = R.drawable.clear_day; //set iconId as clear day for default, this will be helpful
                                           //if any new icon is added on to the software, as mentioned in the documentation

        switch(icon){
            case "clear-day":
                iconId = R.drawable.clear_day;
                break;                          //break must always occur because you don't want the code to fallthrough
                                                //to the next case
            case "clear-night":
                iconId = R.drawable.clear_night;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;

        }
        return iconId;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime(){  //this method is created in order to format the time into something that makes sense
        SimpleDateFormat formatter = new SimpleDateFormat ("h:mm a"); //SimpleDateFormat is a class
                                                                                //that formats a date into a String
                                                                                //to make it into a time that makes sense
                                                                                //this is in hours and minutes with am or pm
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));      //This is to set the timezone to the appropriate location
                                                                    //a String "timeZone" must be passed in this function
                                                                    //the timezone is something we extract from the JSON object

        Date dateTime = new Date(time * 1000);                      //The time is changed into current date and time
                                                                    //the time must be passed in milliseconds
                                                                    //according to java documentation for Date

        return formatter.format(dateTime);                          //final step of formatting the date and time
                                                                    //into h:mm a
    }


    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
