package demo;

import java.io.*;
import java.time.LocalDate;
import java.util.Hashtable;
import javafx.application.Application;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class App extends Application
{
    // blank construction method
    public App()
    {

    }

    @Override public void start(Stage stage) throws IOException
    {
        // loads a config file as a hashmap ( so far only the API key is necessary )
        Hashtable<String, String> config = ConfigParser.loader("config.cfg");
        String api_key = config.get("APIKEY");
        // query to get todays picture of the day
        String query = "https://api.nasa.gov/planetary/apod?api_key=" + api_key;
        String response = getResponse(query);
        // this holds all the parameters the api sends back
        Apod apod = new Apod(response);

        // in case the picture of a day is actually a video we just go a day behind.
        int i = 1;
        while (apod.params.media_type.equals("video"))
        {
            LocalDate date = LocalDate.now().minusDays(i);
            query = "https://api.nasa.gov/planetary/apod?api_key=" + api_key + "&date=" + date;
            response = getResponse(query);
            apod.load(response);
            i+=1;
        }

        // check if the image has been downloaded so we do not redownload
        String imagePath = Helper.getUrlEnding(apod.params.hdurl);
        if (!Helper.fileExist(imagePath))
        {
            WebRequest.imageDownload(apod.params.hdurl);
        }

        // setup UI elements
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        stage.setTitle(apod.params.title);
        stage.setResizable(false);

        //Creating the image view
        InputStream stream = new FileInputStream(imagePath);
        Image image = new Image(stream);
        ImageView imageView = createImageView(image, scene);
        root.getChildren().add(imageView);

        // Creating date picker to select a different picture of the day
        DatePicker datePicker = new DatePicker(LocalDate.now());
        root.getChildren().add(datePicker);

        // DatePicker listener, this run when choosing different dates
        datePicker.valueProperty().addListener((ov, oldVal, newVal) ->
        {
            String newDate = newVal.toString();
            String newQuery = "https://api.nasa.gov/planetary/apod?api_key=" + api_key + "&date=" + newDate;
            String newResponse = getResponse(newQuery);
            Apod newApod = new Apod(newResponse);
            if (newApod.params.media_type.equals("video"))
            {
                int j = 1;
                while (newApod.params.media_type.equals("video"))
                {
                    LocalDate date = LocalDate.parse(newVal.toString()).minusDays(j);
                    newQuery = "https://api.nasa.gov/planetary/apod?api_key=" + api_key + "&date=" + date;
                    newResponse = getResponse(newQuery);
                    newApod.load(newResponse);
                    datePicker.setValue(date);
                    j += 1;
                }
            }

            stage.setTitle(newApod.params.title);

            String newImagePath = Helper.getUrlEnding(newApod.params.hdurl);
            // download image if it does not exist already
            if (!Helper.fileExist(newImagePath))
            {
                try
                {
                    WebRequest.imageDownload(newApod.params.hdurl);
                }
                catch(Exception e)
                {
                    System.out.println("[error] failed to download image.");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            // load image and change image view
            try
            {

                InputStream newStream = new FileInputStream(newImagePath);
                Image newImage = new Image(newStream);
                imageView.setImage(newImage);
                imageView.maxHeight(newImage.getHeight());
            }
            catch (Exception e)
            {
                System.out.println("[error] failed to change image.");
                e.printStackTrace();
                System.exit(-1);
            }

        });

        //Set scene and display the UI.
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        // launch starts the JAVAFX method
        launch(args);
    }

    // creates the image view for the UI
    private static ImageView createImageView(Image image, Scene scene)
    {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(scene.widthProperty());
        return imageView;
    }

    // gets APOD API data and does the error handling. returns String of JSON for APOD to take in.
    private static String getResponse(String query)
    {
        String response = "";
        try
        {
            response = WebRequest.getData(query);
        }
        catch(Exception e)
        {
            if (e instanceof IOException)
            {
                System.out.println("[error] IOException when doing WebRequest.getData()");
            }
            if (e instanceof InterruptedException)
            {
                System.out.println("[error] InterruptedException when doing WebRequest.getData()");
            }
            System.exit(-1);
        }
        return response;
    }
}