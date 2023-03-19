package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
        JSONObject jObj = new JSONObject()
            .put("entity", "list");

        if (p.getName() == "") throw new IOException("Name is empty");
        jObj.put("name", p.getName());

        if (p.getDescription() != "") {
            jObj.put("description", p.getDescription());
        } else {
            jObj.put("description", "");
        }

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

    private void buildBaseUrl() {
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }
    
    /**************** SETTERS AND GETTERS ****************/
    public void setHost(String host) { this.host = host; this.buildBaseUrl(); }
    public String getHost() { return this.host; }
    
    public void setPort(int port) { this.port = port; this.buildBaseUrl(); }
    public int getPort() { return this.port; }

}
