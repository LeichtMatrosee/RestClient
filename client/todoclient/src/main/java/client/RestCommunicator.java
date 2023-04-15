package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
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

/**
 * Class for communicating with the REST API. This class get's most of the information it's working on
 * from the file Config/RestConfig.json
 */
public class RestCommunicator {
    /**
     * The host the Rest API is running on. Localhost by default.
     */
    private String host = "localhost";
    /**
     * Port the Rest API is running on. 6000 by default.
     */
    private int port = 6000;
    /**
     * Base URL of the Rest API. This is constructed on it's own.
     */
    private String baseUrl;
    /**
     * JSON Object containing all information from the Config/RestConfig.json file.
     */
    private JSONObject cfg;

    /**
     * Standard Constructor for the RestCommunicator. All information about Endpoints, host, port etc.
     * is retrieved from the config file.
     * @throws FileNotFoundException Is thrown, whenever the config file could not be found.
     * @throws IOException Gets thrown, when the Config file could not be read.
     * @throws JSONException Gets thrown, when the config file could not be parsed into a JSON Object.
     * Configuration is probably syntactically wrong.
     */
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

    /**
     * Checks, whether the API is reachable by trying to retreive all lists from it.
     * If connection ends in an Exception, the method returns false, otherwise it returns true.
     * @return true, if API is reachable, false otherwise
     */
    public boolean checkApiConnectivity() {
        try {
            // Build the httprequest
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("type", "list");
            HttpRequest postRequest = this.buildHttpRequest(new PostData(map), "getAllLists");

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
     * Drops the whole of the rest API.
     */
    public void drop() {
        try {
            // Build the httprequest
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("type", "list");
            HttpRequest postRequest = this.buildHttpRequest(new PostData(map), "drop");

            // Build the client for posting
            HttpClient httpClient = HttpClient.newHttpClient();

            // Post the http request
            httpClient.send(postRequest, BodyHandlers.ofString());
        } catch (Exception e) {
        }
    }

    /**
     * Wrapper Method for sending all http requests to the Rest API. First calls {@link #buildHttpRequest(PostData, String)}, which
     * configures the request then sends it and parses the Response into a ResponseData object containing all inforation from the API
     * response.
     * @param p Contains all information, that is needed for the API Request.
     * @param endpointName Name of the endpoint as specified in the config file
     * @return ResponseData object containing all the information from the API Response.
     * @throws IOException Is thrown, when something went wrong.
     * @throws URISyntaxException Is thrown, when the Syntax of the URL is incorrect.
     * @throws InterruptedException Is thrown, when the connection with the API gets interrupted.
     * @throws JSONException Is thrown, when body can't be build properly.
     */
    public ResponseData sendHttpRequest(PostData p, String endpointName) throws IOException, URISyntaxException, InterruptedException, JSONException {
        // Build the httprequest
        HttpRequest postRequest = this.buildHttpRequest(p, endpointName);

        // Build the client for posting
        HttpClient httpClient = HttpClient.newHttpClient();

        // Post the http request
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());
        
        // Deserialize the json string to an object
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(response.body(), Map.class);

        ResponseData data = new ResponseData(map, response.statusCode());
        return data;
    }

    /**
     * Analyses the Config to find the endpoint with the given name and returns that.
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
            url += "" + this.parseAttr(attr) + "=" + value;
        }

        return url;
    }

    /**
     * Parses the attribute name so that it conforms with the Rest API.
     * If listId is given, list_id is returned.
     * If entryId is given, entry_id is returned.
     * @param attr Name of the attribute to be parsed.
     * @return Parsed name.
     */
    private String parseAttr(String attr) {
        if (attr.equals("listId")) {
            return "list_id";
        } else if (attr.equals("entryId")) {
            return "entry_id";
        } else return attr;
    }

    /**
     * Builds the JSON Object for the Body for Post, Patch and Put requests.
     * Checks the Endpoint config epCfg and puts all attributes from p into a JSONObject, that are defined in
     * epCfg.params.body.
     * @param p Object containing all data for the Post Request
     * @param entity Either "List" or "entry". Isn't really needed though.
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
     * Builds the HttpRequest Object for the Request, depending on the config.
     * First calls {@link #getEndPoint(String)} to retrieve the endpoint from the config, depending on epName.
     * Then builds endpoint URL by calling {@link #buildEnpointUrl(JSONObject, PostData)}.
     * Finally builds the HttpRequest. For Patch and post, the body is build by {@link #bodyBuilder(PostData, String, JSONObject)}.
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
            .uri(new URI(url)).header("Content-Type", endPoint.get("contentType").toString());

        // Set method on request
        switch (endPoint.get("method").toString()) {
            case "GET": requestBuilder = requestBuilder.GET(); break;
            case "POST": requestBuilder = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(this.bodyBuilder(p, "list", endPoint).toString())); break;
            case "PATCH": requestBuilder = requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(this.bodyBuilder(p, "list", endPoint).toString())); break;
            case "DELETE": requestBuilder = requestBuilder.DELETE(); break;
            default: throw new IOException("No method found!");
        }

        return requestBuilder.build();
    }

    /**
     * Builds the base url of the API, by concatanating the host and the port
     */
    public void buildBaseUrl() {
        this.baseUrl = "http://" + this.host + ":" + this.port;
    }
    
    /**
     * Sets the host name to the given String and builds the Base URL anew, by calling {@link #buildBaseUrl()}.
     * @param host Hostname or IP Adress
     */
    public void setHost(String host) { this.host = host; this.buildBaseUrl(); }
    /**
     * Retrieves the current hostname.
     * @return Current hostname.
     */
    public String getHost() { return this.host; }
    
    /**
     * Sets the port for the API and builds the Base URL anew, by calling {@link #buildBaseUrl()}.
     * @param port New Port
     */
    public void setPort(int port) { this.port = port; this.buildBaseUrl(); }
    /**
     * Retrieves the current port of the api.
     * @return
     */
    public int getPort() { return this.port; }

}
