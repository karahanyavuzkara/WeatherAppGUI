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
}










