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

    /**
     * 
     * 
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     * @throws JSONException
     */
    public ResponseData getAllLists() throws IOException, URISyntaxException, InterruptedException, JSONException {

        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(new PostData("list", ""), "getAllLists");

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

    public boolean checkApiConnectivity() {
        try {
            // Build the httprequest
            HttpRequest postRequest = this.buildHttpRequest(new PostData("list", ""), "getAllLists");

            // Build the client for posting
            HttpClient httpClient = HttpClient.newHttpClient();

            // Post the http request
            HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 202) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Analyses the Config to find the endpoint with the given name and returns that.
     * 
     * @param endpointName Name of the endpoint (set in config)
     * @return JSONObject with all information about the found endpoint
     * @throws JSONException Throws, when no endpoint with given name could be found
     */
    private JSONObject getEndPoint(String endpointName) throws JSONException {
        JSONObject endPoint = null;
        
        // Iterate through all endpoints in config
        for (int i = 0; i < this.cfg.getJSONArray("endpoints").length(); i++) {
            JSONObject currEp = this.cfg.getJSONArray("endpoints").getJSONObject(i);
            
            // If name of the endpoint matches given endpoint name, save that object and break loop
            if (currEp.get("name").toString().equals(endpointName)) {
                endPoint = currEp;
                break;
            }
        }

        // Check, if an endpoint has been found and throw exception, if not.
        if (endPoint == null) throw new JSONException("Could not find enpoint " +  endpointName + " in config");

        return endPoint;
    }

    /**
     * Builds the url of the endpoint by analysing the endpoint config (epCfg) and PostData (p).
     * Iterates through array "plch" in config to replace all placeholders in endpoint url with the corresponding
     * values from the PostData object.
     * Iterates through the array "url" to set all url params. If a param in the config matches one of the PostData params,
     * it gets added to the url as a param.
     * 
     * @param epCfg Configuration of current endpoint
     * @param p Data to be posted
     * @return Url of the endpoint
     * @throws JSONException Gets thrown, whenever a key is trying to be accessed, that does not exist in the config
     * @throws IOException Gets thrown, when a param in the config cannot be resolved to a field in PostData object
     */
    private String buildEnpointUrl(JSONObject epCfg, PostData p) throws JSONException, IOException {
        // Build the url by concatenating base endpoint url to the base url
        String url = this.baseUrl + epCfg.get("endpoint").toString();

        // Get the needed config params
        JSONArray plch = epCfg.getJSONObject("params").getJSONArray("PLCH");
        JSONArray urlArr = epCfg.getJSONObject("params").getJSONArray("url");

        // Iterate through all placeholders defined in config
        for (int i = 0; i < plch.length(); i++) {
            String attr = plch.getJSONObject(i).get("attr").toString();
            String value = "";

            // Get the corresponding value from the postData object and encode it
            switch (attr) {
                case "listId": value = URLEncoder.encode(p.getListId(), "UTF-8"); break;
                case "id": value = URLEncoder.encode(p.getId(), "UTF-8"); break;
                case "type": value = URLEncoder.encode(p.getType(), "UTF-8"); break;
                case "description": value = URLEncoder.encode(p.getDescription(), "UTF-8"); break;
                case "name": value = URLEncoder.encode(p.getName(), "UTF-8"); break;
                case "entryId": value = URLEncoder.encode(p.getEntryId(), "UTF-8"); break; 
                default: throw new IOException("Could not resolve " + attr + " to a type in PostData.");
            }

            if (value == "") throw new IOException("No value for " + attr + " given.");

            // Replace the placeholder in the url string with the correct value
            url = url.replace(
                plch.getJSONObject(i).get("plch").toString(), 
                value
            );
        }

        // Check, if url params are needed and add a ? to the end of the url, in order to set all query params
        if (urlArr.length() > 0) url += "?";

        // Iterate through all url query params
        for (int i = 0; i < urlArr.length(); i++) {
            if (i > 0) {
                url += "&";
            }
            // Basically the same mechanism as before. Look at previous comments
            String attr = urlArr.get(i).toString();
            String value = "";
            switch (attr) {
                case "listId": value = URLEncoder.encode(p.getListId(), "UTF-8"); break;
                case "id": value = URLEncoder.encode(p.getId(), "UTF-8"); break;
                case "type": value = URLEncoder.encode(p.getType(), "UTF-8"); break;
                case "description": value = URLEncoder.encode(p.getDescription(), "UTF-8"); break;
                case "name": value = URLEncoder.encode(p.getName(), "UTF-8"); break;
                case "entryId": value = URLEncoder.encode(p.getEntryId(), "UTF-8"); break; 
                default: throw new IOException("Could not resolve " + attr + " to a type in PostData.");
            }

            if (value == "") throw new IOException("No value for attr " + attr + " given.");

            // Add query param to url
            url += "" + attr + "=" + value;
        }

        return url;
    }

    /**
     * Builds the JSON Object for the Body for Post and Put requests
     * 
     * @param p Object containing all data for the Post Request
     * @param entity Either "List" or "entry"
     * @param epCfg Configuration of current endpoint
     * @return JSONObject containing all key value pairs for the body of the post request
     * @throws JSONException Is thrown, when a key in JSONObject is accessed, that does not exist
     */
    private JSONObject bodyBuilder(PostData p, String entity, JSONObject epCfg) throws JSONException, IOException {
        // Get all params for the body
        JSONArray bodyParams = epCfg.getJSONObject("params").getJSONArray("body");
        
        // Put the entity into the body
        JSONObject jObj = new JSONObject()
            .put("entity", entity);

        // Iterate through all body params in config
        for (int i = 0; i < bodyParams.length(); i++) {
            // Get the name of the attribute from config
            String attr = bodyParams.get(i).toString();

            // Put the param from the PostData object into body for request or throw IOException, if attribute doesn't match type.
            switch (attr) {
                case "name": jObj.put("name", p.getName()); break;
                case "listId": jObj.put("list_id", p.getListId()); break;
                case "description": jObj.put("description", p.getDescription()); break;
                case "entryId": jObj.put("entry_id", p.getEntryId()); break;
                default: throw new IOException("Attr " + attr + " does not match a type in PostData object");
            }
        }

        return jObj;
    }

    /**
     * Builds the HttpRequest Object for the Request, depending on the config
     * 
     * @param p Object containing all relevant information for the request
     * @param epName Name of the endpoint (must match endpoint in config)
     * @return Complete HttpRequest object, that can be sent to API
     * @throws JSONException Is thrown, when a key in JSONObject is accessed, that does not exist
     * @throws URISyntaxException Is thrown, when the URI for the request is invalid (invalid characters in url and such)
     * @throws IOException Is thrown, when no method or content type is found in config
     */
    private HttpRequest buildHttpRequest(PostData p, String epName) throws JSONException, URISyntaxException, IOException {
        // Get endpoint config and build endpoint url
        JSONObject endPoint = this.getEndPoint(epName);
        String url = this.buildEnpointUrl(endPoint, p);

        if (endPoint.get("contentType").toString() == null || endPoint.get("contentType").toString().equals("")) 
            throw new IOException("No Content type for endpoint " + epName + " found!");

        // Build the httprequest
        Builder requestBuilder = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Content-Type", endPoint.get("contentType").toString());

        // Set method on request
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
