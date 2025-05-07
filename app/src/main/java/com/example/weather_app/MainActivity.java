package com.example.weather_app;



import android.os.Bundle;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button search;
    TextView show;
    String url;

    class getWeather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main = jsonObject.getJSONObject("main");

                // Extract temperature values from the JSON response (in Kelvin)
                double tempKelvin = main.getDouble("temp");
                double tempMaxKelvin = main.getDouble("temp_max");
                double tempMinKelvin = main.getDouble("temp_min");
                double pressure = main.getDouble("pressure");
                int humidity = main.getInt("humidity");

                // Convert temperatures to Celsius
                double tempCelsius = tempKelvin - 273.15;
                double tempMaxCelsius = tempMaxKelvin - 273.15;
                double tempMinCelsius = tempMinKelvin - 273.15;
                double pressureAtm = pressure / 1013.25;
                Log.d("Humidity", String.valueOf(humidity));

                // Create a formatted string for the weather information
                String weatherInfo = "Temperature: " + String.format("%.2f", tempCelsius) + "°C\n";
                weatherInfo += "Feels Like: " + String.format("%.2f", tempMaxCelsius) + "°C\n";
                weatherInfo += "Max Temperature: " + String.format("%.2f", tempMaxCelsius) + "°C\n";
                weatherInfo += "Min Temperature: " + String.format("%.2f", tempMinCelsius) + "°C\n";
                weatherInfo += "Pressure: " + pressure + " hPa\n";
                weatherInfo += "Humidity: " + humidity + "%";

                show.setText(weatherInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show = findViewById(R.id.weather);

        final String[] temp = {""};

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Botton Clicked! ", Toast.LENGTH_LONG).show();
                String city = cityName.getText().toString().trim();
                try {
                    if (city != null) {
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=910e32d5f26e5a9a9af143bc905324fc";
                    } else {
                        Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_LONG).show();
                    }
                    getWeather task = new getWeather();
                    temp[0] = task.execute(url).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (temp[0] == null) {
                    show.setText("Cannot able to find Weather");
                }

            }
        });

    }
}
