import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

//retrieve weather data from API - this backend logic will fetch the latest weather
//data from the external API and return it. The GUI will
//display this data to the user
public class WeatherApp {
    //fetch weatcher data for given loc
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);
    }

    //retrieve geographic coordinates for given loc name
    private static JSONArray getLocationData(String locationName) {
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
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson))

                //get the list of location data the API generated from the loc name

            }

        }catch (Exception e){
            e.printStackTrace();
        }
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
}










