import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {
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

        //search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        //change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
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
























