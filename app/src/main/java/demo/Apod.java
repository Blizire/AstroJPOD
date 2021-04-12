package demo;

import com.google.gson.*;

// represents the JSON response from the NASA APOD API
public class Apod
{
    public class Params
    {
        public String date;
        public String explanation;
        public String hdurl;
        public String media_type;
        public String service_version;
        public String title;
        public String url;
    }

    public Params params;

    public Apod(String json)
    {
        Gson gson = new Gson();
        params = gson.fromJson(json, Params.class);
    }

    public void load(String json)
    {
        Gson gson = new Gson();
        params = gson.fromJson(json, Params.class);
    }
}