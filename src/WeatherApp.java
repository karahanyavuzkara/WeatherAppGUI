import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

//retrieve weather data from API - this backend logic will fetch the latest weather
//data from the external API and return it. The GUI will
//display this data to the user
public class WeatherApp {
    //fetch weatcher data for given loc
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);

        //extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //build API request URL with loc coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Europe%2FMoscow";

        try {
            //call api and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            //check for response status
            //200 - means that the conn was a success
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            //store resulting json data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()){
                //read and store into the string builder
                resultJson.append(scanner.nextLine());
            }
            //close the scanner
            scanner.close();

            //close url conn
            conn.disconnect();

            //parse through our data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            //retrieve hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            //we want to get the current hour's data
            //so we need to get the index of our current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            //get temp
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            //get weather code
            JSONArray weathercode= (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            //get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            //get windspeed
            JSONArray windspeedData = (JSONArray) hourly.get("windspeed=10m");
            double windspeed = (double) windspeedData.get(index);

            //build the weather json data object that we are going to access in our frontend
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //retrieve geographic coordinates for given loc name
    public static JSONArray getLocationData(String locationName) {
        //replace any whitespace in location name to + to adhere to API's request
        locationName = locationName.replaceAll(" ", "+");

        //build API url with loc parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try {
            //call api and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            //check response status
            //200 means successful connection
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }else{
                //store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                //read and store the resulting json data into our string builder
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                //close scanner
                scanner.close();

                //close url connection
                conn.disconnect();

                //parse the JSON string into a JSON obj
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                //get the list of location data the API generated from the loc name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //could not find loc
        return null;
    }
    private static HttpURLConnection fetchApiResponse(String urlString){
        try {
            //attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //set request method to get
            conn.setRequestMethod("GET");

            //connect to our API
            conn.connect();
            return conn;
        }catch (IOException e){
            e.printStackTrace();
        }

        //could not make the connection
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        //iterate through the time list and see which one matches our current time
        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                //return the index
                return i;
            }
        }

        return 0;
    }
    public static String getCurrentTime(){
        //get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        //format date to be 2024-05-15T00:00 (this is how it is read in the API)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'HH':00'");

        //format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;

    }
    //convert the weather code to string
    private static String convertWeatherCode (long weathercode){
        String weatherCondition = "";
        if (weathercode == 0L) {
            weatherCondition = "Clear sky";
        } else if (weathercode < 3L && weathercode >0L) {
            weatherCondition ="Cloudy";
        } else if (weathercode == 45 || weathercode == 48) {
            weatherCondition = "Foggy";
        }else if (weathercode == 51 || weathercode == 53 || weathercode == 55){
            weatherCondition = "Drizzle";
        } else if (weathercode == 56 || weathercode == 57) {
            weatherCondition = "Freezing Drizzle";
        } else if (weathercode == 71 || weathercode == 73 || weathercode == 75) {
            weatherCondition = "Snow fall";
        }else if (weathercode == 77){
            weatherCondition = "Snow Grains";
        }else if (weathercode == 80 || weathercode == 81 || weathercode == 82){
            weatherCondition = "Rain showers";
        }else if (weathercode == 85 || weathercode == 86) {
            weatherCondition = "Snow showers";
        }else if (weathercode == 95 ){
            weatherCondition = "Thunderstorm";
        } else if (weathercode == 96 || weathercode == 99) {
            weatherCondition = "Thunderstorm with Hail";

        }
        return weatherCondition;
    }
}










