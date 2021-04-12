package demo;

import java.io.File;
import java.util.Scanner;
import java.util.Hashtable;
import java.io.FileNotFoundException;

public class ConfigParser {
    public static Hashtable<String, String> loader(String filePath) throws FileNotFoundException
    {
        Hashtable<String, String> config = new Hashtable<String, String>();
        File fileHandle = new File(filePath);
        Scanner fileReader = new Scanner(fileHandle);
        while(fileReader.hasNextLine())
        {
            String line = fileReader.nextLine();
            String key = "";
            String value = "";
            try
            {
                key = line.split("=")[0];
                value = line.split("=")[1];
            }
            catch (Exception e)
            {
                System.out.println("Error has been detected in config file");
                e.printStackTrace();
            }
            config.put(key, value);
        }
        fileReader.close();
        return config;
    }
}
