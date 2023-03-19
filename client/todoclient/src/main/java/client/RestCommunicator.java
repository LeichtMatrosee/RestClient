package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    /**
     * 
     * 
     * @param {PostData} p Contains the information of the new List to be posted to rest Api
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public Map<String,Object> addNewList(PostData p) throws IOException, URISyntaxException, InterruptedException {
        // Start building the json string
        String jsonString = "{";

        jsonString += "\"entity\": \"list\",";

        if (p.getName() == "") throw new IOException("Name is empty");
        jsonString += "\"name\": \"" + p.getName() + "\","; 

        if (p.getDescription() != "") {
            jsonString += "\"description\": \"" + p.getDescription() + "\"";
        } else {
            jsonString += "\"description\": \"\"";
        }

        jsonString += "}";

        System.out.println(jsonString);

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI(this.baseUrl + "/todo-list"))
            .header("Content-Type", "application/json")
            .PUT(BodyPublishers.ofString(jsonString))
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());

        System.out.println(response.body());
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response.body(), Map.class);

        return map;
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
