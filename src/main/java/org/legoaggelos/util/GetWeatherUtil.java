package org.legoaggelos.util;

import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GetWeatherUtil {
    private final HashMap<String, ArrayList<Double>> locationValuesHashMap = new HashMap<String, ArrayList<Double>>();
    public GetWeatherUtil(){
        locationValuesHashMap.put("crete",new ArrayList<>(Arrays.asList(35.3419, 25.1424)));
        locationValuesHashMap.put("athens",new ArrayList<>(Arrays.asList(37.9838, 23.7278)));
    }
    public ArrayList<String> getForecast(String location, int days){
        if(days<1||days>16){
            return new ArrayList<>(List.of("Invalid number of days! Please select a number between 1 and 16!"));
        }
        if(locationValuesHashMap.getOrDefault(location, null)==null){
            return new ArrayList<>(List.of("Only places accepted yet are crete and athens!"));
        }
        try {
            StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append("https://api.open-meteo.com/v1/forecast?latitude=").append(locationValuesHashMap.get(location).getFirst()); //add latitude
            uriBuilder.append("&longitude=").append(locationValuesHashMap.get(location).get(1)); // add longitude
            uriBuilder.append("&current=temperature_2m&hourly=temperature_2m&timezone=Europe%2FMoscow&forecast_days=").append(days); //add days and finish the uri
            //only works for 1 day!
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uriBuilder.toString()))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] splitBody = response.body().split(",");
            StringBuilder formattedBody = new StringBuilder();
            formattedBody.append(splitBody[39].split("\\[")[1]);
            for (int i = 40; i < splitBody.length; i++) {
                formattedBody.append(" ").append(splitBody[i].split("]")[0]);
            }
            ArrayList<String> timeAndTemperatureList = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                timeAndTemperatureList.add((i+":00 "+formattedBody.toString().split(" ")[i] +"C"));
            }
            return timeAndTemperatureList;
        } catch(URISyntaxException uriSyntaxException){
            uriSyntaxException.printStackTrace();
            return new ArrayList<>(List.of("Invalid URI! Please report this as a bug!"));
        }catch(IOException ioException){
            ioException.printStackTrace();
            return new ArrayList<>(List.of("Sending request failed! Either weather servers are down, you dont have wifi or this is a bug!"));
        }catch(InterruptedException interruptedException){
            interruptedException.printStackTrace();
            return new ArrayList<>(List.of("Sending request was interrupted! This is a bug!"));
        }
    }
}
