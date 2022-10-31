package org.example.toolkit;
//

import com.example.ServiceOne;
import com.example.Station;
import com.example.position;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class RecordsVilib {
    public static final String urlf = "https://api.jcdecaux.com/vls/v1/stations?apiKey=2a5d13ea313bf8dc325f8783f888de4eb96a8c14";

    public static JSONArray getJSONArray(String someurl) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(someurl, String.class);
        return new JSONArray(response);
    }

    public static Double getLngandlat(JSONObject jsonObject, String s) throws JSONException {
        JSONObject jsonObjectpos = jsonObject.getJSONObject("position");
        try {
            return (Double) jsonObjectpos.get(s);
        } catch (JSONException e) {
            System.out.println("error found");
        }
        return null;
    }

    public static Station geOneRecordtStation(JSONArray rec1, int i) throws JSONException {
        JSONObject rec1obj = rec1.getJSONObject(i);
        return Station.newBuilder()
                .setAddress((String) rec1obj.get("address"))
                .setAvailableBikeStands(Long.parseLong(rec1obj.get("available_bike_stands").toString()))
                .setAvailableBikes(Long.parseLong(rec1obj.get("available_bikes").toString()))
                .setBanking((Boolean) rec1obj.get("banking"))
                .setBikeStands(Long.parseLong(rec1obj.get("bike_stands").toString()))
                .setBonus((Boolean) rec1obj.get("bonus"))
                .setContractName((String) rec1obj.get("contract_name"))
                .setName((String) rec1obj.get("name"))
                .setNumber(Long.parseLong(rec1obj.get("number").toString()))
                .setPosition(position.newBuilder().setLng(getLngandlat(rec1obj, "lng")).setLat(getLngandlat(rec1obj, "lat")).build())
                .setStatus((String) rec1obj.get("status"))
                .build();
    }


    public static ServiceOne geOneRecordtServiceone(JSONArray AllJsons, int i) throws JSONException {
        JSONObject getOnJsonWith_i = AllJsons.getJSONObject(i);
        return ServiceOne.newBuilder()
                .setContractName((String) getOnJsonWith_i.get("contract_name"))
                .setStatus((String) getOnJsonWith_i.get("status"))
                .build();
    }













}