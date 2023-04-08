package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class RestCommunicator {
    private String host = "localhost";
    private int port = 6000;
    private String baseUrl;
    private JSONObject cfg;

    public RestCommunicator() throws FileNotFoundException, IOException, JSONException {
        // Get Config file
        File cfgFile = new File("" + System.getProperty("user.dir") + "\\Config\\RestConfig.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cfgFile)));
        
        // Read the whole config file to a string, in order to later serialize it into a JSONObject
        String cfgString = "";
        String line = br.readLine();

        while (line != null) {
            cfgString += line;
            line = br.readLine();
        }

        br.close();

        this.cfg = new JSONObject(cfgString);

        this.host = this.cfg.get("host").toString();
        this.port = Integer.parseInt(this.cfg.get("port").toString());
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }

    
    public ResponseData addNewList(PostData p) throws IOException, URISyntaxException, InterruptedException, JSONException {

        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(p, "addList");
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
        HttpRequest postRequest = this.buildHttpRequest(p, "getEntries");

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

        HttpRequest postRequest = this.buildHttpRequest(p, "search");

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

    public ResponseData deleteList(PostData p) throws IOException, URISyntaxException, InterruptedException, JSONException {
        if (p.getListId().equals("")) throw new IOException("Must give list id!");

        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(p, "deleteList");

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

    public ResponseData addEntryToList(PostData p) throws IOException, URISyntaxException, InterruptedException, JSONException {
        if (p.getListId().equals("")) throw new IOException("Must give list id!");
        if (p.getName().equals("")) throw new IOException("Must give name!");


        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(p, "addEntry");

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

    public ResponseData updateEntry(PostData p) throws IOException, URISyntaxException, InterruptedException, JSONException {
        if (p.getListId().equals("")) throw new IOException("Must give list id!");
        if (p.getEntryId().equals("")) throw new IOException("Must give EntryId!");

        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(p, "updateEntry");

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

    public ResponseData deleteEntry(PostData p) throws IOException, URISyntaxException, InterruptedException, JSONException {
        if (p.getListId().equals("")) throw new IOException("Must give list id!");
        if (p.getEntryId().equals("")) throw new IOException("Must give EntryId!");


        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(p, "deleteEntry");

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

    public ResponseData getAllLists() throws IOException, URISyntaxException, InterruptedException, JSONException {

        // Build the httprequest
        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI(this.baseUrl + "/todo-list?all=true"))
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

    private JSONObject getEndPoint(String endpointName) throws JSONException {
        JSONObject endPoint = null;
        
        for (int i = 0; i < this.cfg.getJSONArray("endpoints").length(); i++) {
            JSONObject currEp = this.cfg.getJSONArray("endpoints").getJSONObject(i);
            if (currEp.get("name").toString().equals(endpointName)) {
                endPoint = currEp;
                break;
            }
        }

        if (endPoint == null) throw new JSONException("Could not find enpoint search in config");

        return endPoint;
    }

    private String buildEnpointUrl(JSONObject epCfg, PostData p) throws JSONException, IOException {
        String url = this.baseUrl + epCfg.get("endpoint").toString();
        JSONArray plch = epCfg.getJSONObject("params").getJSONArray("PLCH");
        JSONArray urlArr = epCfg.getJSONObject("params").getJSONArray("url");

        for (int i = 0; i < plch.length(); i++) {
            String attr = plch.getJSONObject(i).get("attr").toString();
            String value = "";
            switch (attr) {
                case "listId": value = p.getListId(); break;
                case "id": value = p.getId(); break;
                case "type": value = p.getType(); break;
                case "description": p.getDescription(); break;
                case "name": value = p.getName(); break;
                case "entryId": value = p.getEntryId(); break; 
                default: throw new IOException("Could not resolve " + attr + " to a type in PostData.");
            }

            if (value == "") throw new IOException("No value for " + attr + " given.");

            url = url.replace(
                plch.getJSONObject(i).get("plch").toString(), 
                value
            );
        }

        if (urlArr.length() > 0) url += "?";

        for (int i = 0; i < urlArr.length(); i++) {
            if (i > 0) {
                url += "&";
            }
            String attr = urlArr.get(i).toString();
            String value = "";
            switch (attr) {
                case "listId": value = URLEncoder.encode(p.getListId(), "UTF-8"); break;
                case "id": value = URLEncoder.encode(p.getId(), "UTF-8"); break;
                case "type": value = URLEncoder.encode(p.getType(), "UTF-8"); break;
                case "description": URLEncoder.encode(p.getDescription(), "UTF-8"); break;
                case "name": value = URLEncoder.encode(p.getName(), "UTF-8"); break;
                case "entryId": value = URLEncoder.encode(p.getEntryId(), "UTF-8"); break; 
                default: throw new IOException("Could not resolve " + attr + " to a type in PostData.");
            }

            if (value == "") throw new IOException("No value for attr " + attr + " given.");

            url += "" + attr + "=" + value;
        }

        return url;
    }

    /**
     * 
     * @param p
     * @param entity
     * @return
     * @throws JSONException
     * @throws IOException
     */
    private JSONObject bodyBuilder(PostData p, String entity, JSONObject epCfg) throws JSONException, IOException {
        JSONArray bodyParams = epCfg.getJSONObject("params").getJSONArray("body");
        JSONObject jObj = new JSONObject()
            .put("entity", entity);

        for (int i = 0; i < bodyParams.length(); i++) {
            String attr = bodyParams.get(i).toString();

            switch (attr) {
                case "name": jObj.put("name", p.getName()); break;
                case "listId": jObj.put("list_id", p.getListId()); break;
                case "description": jObj.put("description", p.getDescription()); break;
                case "entryId": jObj.put("entry_id", p.getEntryId()); break;
                default: break;
            }
        }

        return jObj;
    }

    private HttpRequest buildHttpRequest(PostData p, String epName) throws JSONException, URISyntaxException, IOException {
        JSONObject endPoint = this.getEndPoint(epName);
        String url = this.buildEnpointUrl(endPoint, p);

        // Build the httprequest
        Builder requestBuilder = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Content-Type", endPoint.get("contentType").toString());

        switch (endPoint.get("method").toString()) {
            case "GET": requestBuilder = requestBuilder.GET(); break;
            case "POST": requestBuilder = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(this.bodyBuilder(p, "list", endPoint).toString())); break;
            case "PUT": requestBuilder = requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(this.bodyBuilder(p, "list", endPoint).toString())); break;
            case "DELETE": requestBuilder = requestBuilder.DELETE(); break;
            default: throw new IOException("No method found!");
        }

        return requestBuilder.build();
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
