package com.example.skyulee.myweatherproject;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skyulee.myweatherproject.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather currentWeather;

    private ImageView iconImageView; //This is to change our weather icon
                                     // we're using FindViewbyId here to show another way we can achieve binding
                                     //but also to show a problem we might run into if we use this method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this,
                R.layout.activity_main); // this is to bind our data with the layout
                                        //we need to bind our data to our new binding variable
                                        //But there's no data on our OnCreate
                                        //But we do have it on our onResponse (getCurrentDetails)
                                        //so we go down there and do it.
                                        //The binding class must be declared final because the binding is used
                                        //within an inner class.

        TextView darkSky = findViewById(R.id.darkSkyAttribution);

        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        iconImageView = findViewById(R.id.iconImageView);

        String apiKey = "2af44439b37fd5de5d8b19a64e2a3967";

        double latitude = 37.8267;
        double longitude = -122.4233;

        String forecastURL ="https://api.darksky.net/forecast/"
                + apiKey + "/" + latitude + "," + longitude;
        if(isNetworkAvailable()) {                  //This is used to check if there is internet connection available
            OkHttpClient client = new OkHttpClient(); //create a new OkHttpClient object


            Request request = new Request.Builder() //create a Request object to do the Request/Retrieve function
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request); //Call class is an execute method to call an object to get our response from the request
            call.enqueue(new Callback() { //enqueue method executes the call by putting it into a queue which means
                //the code is executed asynchronously in the order they are added to the queue
                //in this case, this is the only method in the queue so it gets executed right away.
                //Basically this code is running in the background so you can do whatever code in the main UI thread
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) { //if response is successful run this code.

                            currentWeather = getCurrentDetails(jsonData);

                            CurrentWeather displayWeather = new CurrentWeather(
                                currentWeather.getLocationLabel(),
                                currentWeather.getIcon(),
                                currentWeather.getTime(),
                                currentWeather.getTemperature(),
                                currentWeather.getHumidity(),
                                currentWeather.getPrecipChance(),
                                currentWeather.getSummary(),
                                currentWeather.getTimeZone()
                            );

                            binding.setWeather(displayWeather); //We're using the binding variable to
                                                                // bind the data to the displayWeather method
                            Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                            //we can do this because we have our drawable resource id (displayWeather) in our data model

                            iconImageView.setImageDrawable(drawable); //Takes a drawable object as an argument and sets our imageview as that
                        }



                        else {
                            alertUserAboutError(); //This is created to alert the user about error
                        }
                    } catch (IOException e) {           //if an IO Exception is caught then run this code.
                        Log.e(TAG, "IO Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception caught: ", e);
                    }
                }
            });
        }
        Log.d(TAG, "Our main UI thread is running!");
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException { //"throws" JSONException moves exception handling responsibility from  this line to where the method is called above
        JSONObject forecast = new JSONObject(jsonData); //JSONObject is a class that has a constructor
                                                        //that allows you to pass a string of JSON data to create a new JSON object

        String timezone = forecast.getString("timezone"); //"timezone" is a line from the DarkSky API that we are trying to find from forecast
        JSONObject current = forecast.getJSONObject("currently"); //this line is to extract a JSONObject from the JSON data and create a new JSONObject named current

        Log.i(TAG, "From JSON" + timezone);
        Log.i(TAG, "FROM JSON" + current);

        CurrentWeather currentWeather = new CurrentWeather(); //This currentWeather object must be created inside

        currentWeather.setHumidity(current.getDouble("humidity"));
        currentWeather.setTime(current.getLong("time"));
        currentWeather.setIcon(current.getString("icon"));
        currentWeather.setLocationLabel("Alcatraz Island, CA");
        currentWeather.setPrecipChance(current.getDouble("precipProbability"));
        currentWeather.setSummary(current.getString("summary"));
        currentWeather.setTemperature(current.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());

        return currentWeather;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //ConnectivityManager android built in class to check if the Network is available
                                                                                                            //Context.CONNECTIVITY_SERVICE is the parameter we need to pass through getSystemService
                                                                                                            //which is the name of the service in string format.
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();                                           //instantiate a network info object

        boolean isAvailable = false;                                                                        //Best android practice for boolean variable is setting it as false

        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        else {
            alertUserAboutNetwork();
            //Toast.makeText(this,getString(R.string.network_unavailable_message),
                    //Toast.LENGTH_LONG).show(); //Length_Long is for the time that it takes for the toast message
                                               //to disappear
        }
        return isAvailable;
    }

    private void alertUserAboutNetwork() {
        NetworkAlertDialogFragment networkDialog = new NetworkAlertDialogFragment();
        networkDialog.show(getFragmentManager(), "error_network");

    }

    private void alertUserAboutError() {        //This is setting up the alertUserAboutError object
        AlertDialogFragment dialog = new AlertDialogFragment();                 //This is the AlertDialogFragment class created in the project
        dialog.show(getFragmentManager(), "error_dialog");                 //dialog.show is used to show the dialog in our Activity which takes in a FragmentManager and a string
                                                                                //which can be anything so we just set it as "error_dialog"
    }
}
