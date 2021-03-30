package com.example.demo.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class stores dynamic setting. Created by vietpd on 18/06/2018.
 */
public class Settings {

    private static Logger logger = LoggerFactory.getLogger(Settings.class);
    private static volatile Settings instance = null;
    private static Object mutex = new Object();

    public String MYSQL_IP = "localhost";
    public int MYSQL_PORT = 11183;
    public String MYSQL_DB = "revenue_test";
    public String MYSQL_USER = "revenue_root";
    public String MYSQL_PASS = "Revenue@2021";
    public int MYSQL_POOL_SIZE = 10;

    public Set<String> ALLOW_ORIGIN = Collections.singleton("*");

    public static Settings getInstance() {
       Settings result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                        File file = new File("settings.yaml");
                        System.out.println("Overiding Application Settings:");
                        System.out.println(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
                        System.out.println("##################\n");

                        instance = result = mapper.readValue(file, Settings.class);
                    } catch (FileNotFoundException e1) {
                        instance = result = new Settings();
                        logger.info("Using default settings!");
                    } catch (Exception e) {
                        instance = result = new Settings();
                        logger.error("Error when loading setting properties", e);
                        logger.info("Using default settings!");
                    }
                }
            }
        }
        return result;
    }
}
