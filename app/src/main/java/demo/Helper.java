package demo;

import java.io.File;

public class Helper
{
    // check if file exists by just pasing the file path
    public static Boolean fileExist(String fpath)
    {
        File f = new File(fpath);
        if(f.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // gets the string after the last / in a URL
    public static String getUrlEnding(String url)
    {
        String [] fnameSplit = url.split("/");
        return fnameSplit[fnameSplit.length - 1];
    }

}
