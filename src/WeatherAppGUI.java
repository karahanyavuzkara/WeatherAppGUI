import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {
    private JSONObject weatherData;
    public WeatherAppGUI(){
        //Set up our gui and add a title
        super("Weather App");

        //configure gui to end program
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //set size in pixels
        setSize(450,650);

        //make our layout manager null to manually position our components
        setLayout(null);

        //prevent any resize of our gui
        setResizable(false);

        addGuiComponents();

    }
    private void addGuiComponents(){
        //SEARCH FIELD
        JTextField searchTextField = new JTextField();

        //setting the loc and size of search bar
        searchTextField.setBounds(15, 15, 351, 45);

        //Set the font style and size
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(searchTextField);


        //weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        //temperature text
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));

        //center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405,450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //humidity image
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        //humidity text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        //windspeed image
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        //windspeed text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h </html>");
        windspeedText.setBounds(310,500,85,55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN,16));
        add(windspeedText);

        //search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        //change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get loc from user
                String userInput = searchTextField.getText();

                //validate input - remove whitespace to ensure non-empty text
                if (userInput.replaceAll("\\s", "").length() <=0){
                    return;
                }

                //retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                //update gui

                //update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                //depending on the condition, we will update the weather image that corresponds with the condition
                switch (weatherCondition) {
                    case "Clear sky":
                        weatherCondition.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherCondition.setIcon(loadImage("src/assets/cloudy.png"));
                    case "Foggy":
                        weatherCondition.setIcon(loadImage("src/assets/cloudy.png"));
                    case "Drizzle":
                        weatherCondition.setIcon(loadImage("src/assets/rain.png"));
                    case "Freezing Drizzle":
                        weatherCondition.setIcon(loadImage("src/assets/rain.png"));
                    case "Snow fall":
                        weatherCondition.setIcon(loadImage("src/assets/snow.png"));
                    case "Snow Grains":
                        weatherCondition.setIcon(loadImage("src/assets/snow.png"));
                    case "Rain showers":
                        weatherCondition.setIcon(loadImage("src/assets/rain.png"));
                    case "Snow showers":
                        weatherCondition.setIcon(loadImage("src/assets/snow.png"));
                    case "Thunderstorm":
                        weatherCondition.setIcon(loadImage("src/assets/thunderstorm.png"));
                    case "Thunderstorm with Hail":
                        weatherCondition.setIcon(loadImage("src/assets/thunderstorm.png"));

                }
            }
        })
        add(searchButton);
    }
    //used to create images in our gui components
    private ImageIcon loadImage(String resourcePath){
        try {
            //read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            //return an image icon so that our component can render it
            return new ImageIcon(image);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;

    }
}
























