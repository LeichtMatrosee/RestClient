package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RestCommunicator {
    private String host = "localhost";
    private int port = 5000;
    private String baseUrl;

    public RestCommunicator(String host, int port) {
        this.host = host;
        this.port = port;
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }
    
    public RestCommunicator(String host) {
        this.host = host;
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }

    public RestCommunicator(int port) {
        this.port = port;
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }

    public RestCommunicator() { 
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }

    
    public ResponseData addNewList(PostData p) throws IOException, URISyntaxException, InterruptedException, JSONException {
        // Start building the json string
        JSONObject jObj = this.bodyBuilder(p, "list");

        // Build the httprequest
        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI(this.baseUrl + "/todo-list"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(jObj.toString()))
            .build();

        System.out.println(jObj.toString());
        // Build the client for posting
        HttpClient httpClient = HttpClient.newHttpClient();

        // Post the http request
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());
        
        // Deserialize the json string to an object
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response.body(), Map.class);

        ResponseData data = new ResponseData(map);
        return data;
    }

    /**
     * 
     * @param p
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public ResponseData getEntriesFromList(PostData p) throws JSONException, IOException, URISyntaxException, InterruptedException {
        if (p.getListId().equals("")) throw new IOException("Must give list id!");

        // Build the httprequest
        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI(this.baseUrl + "/todo-list/" + p.getListId() + "/entries"))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        // Build the client for posting
        HttpClient httpClient = HttpClient.newHttpClient();

        // Post the http request
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());
        
        // Deserialize the json string to an object
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response.body(), Map.class);
        
        ResponseData data = new ResponseData(map);
        return data;
    }

    public ResponseData searchList(PostData p) throws JSONException, IOException, URISyntaxException, InterruptedException {
        if (p.getName().equals("")) throw new IOException("Must give name!");

        
        // Build the httprequest
        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI(this.baseUrl + "/search?name=" + p.getName()))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        // Build the client for posting
        HttpClient httpClient = HttpClient.newHttpClient();

        // Post the http request
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());
        
        // Deserialize the json string to an object
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response.body(), Map.class);
        
        ResponseData data = new ResponseData(map);
        return data;
    }

    /**
     * 
     * @param p
     * @param entity
     * @return
     * @throws JSONException
     * @throws IOException
     */
    private JSONObject bodyBuilder(PostData p, String entity) throws JSONException, IOException {
        JSONObject jObj = new JSONObject()
            .put("entity", entity);

        if (p.getName() == "") throw new IOException("Name is empty");
        jObj.put("name", p.getName());

        if (p.getListId().equals("")) {
            if (entity.equals("entry")) throw new IOException("For list entities, a listId must be given!");
        } else {
            jObj.put("list_id", p.getListId());
        }

        if (p.getDescription() != "") {
            jObj.put("description", p.getDescription());
        } else {
            jObj.put("description", "");
        }

        return jObj;
    }

    private void buildBaseUrl() {
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }
    
    /**************** SETTERS AND GETTERS ****************/
    public void setHost(String host) { this.host = host; this.buildBaseUrl(); }
    public String getHost() { return this.host; }
    
    public void setPort(int port) { this.port = port; this.buildBaseUrl(); }
    public int getPort() { return this.port; }

}
