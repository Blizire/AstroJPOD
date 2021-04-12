package demo;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class WebRequest
{
    // get API data to fill out apod.class
    public static String getData(String url) throws IOException, InterruptedException
    {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
            URI.create(url))
            .header("accept", "application/json")
            .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        if (response.statusCode() != 200)
        {
            String error = "Error getting a webpage";
            System.out.println(error);
            return error;
        }
        else
        {
            return response.body();
        }
    }

    // download image and save relative to path
    public static String imageDownload(String _url) throws IOException
    {
        String fname = Helper.getUrlEnding(_url);
        URL url = new URL(_url);

        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
           out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        // save image
        FileOutputStream fos = new FileOutputStream(fname);
        fos.write(response);
        fos.close();

        return fname;
    }

}