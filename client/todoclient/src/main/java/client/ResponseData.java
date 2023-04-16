package client;

import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Class encapsulates the Response from the API in an easily and standardized manner.
 * @author LeichtMatrosee
 * @version 1.0
 */
public class ResponseData {

    /**
     * Class represents a single entry from the API Response
     * @author LeichtMatrosee
     * @version 1.0
     */
    public class EntryData {
        /**
         * Name of the entry.
         */
        private String name = "";
        /**
         * ID of the entry.
         */
        private String id = "";
        /**
         * Description of the entry.
         */
        private String description = "";
        /**
         * List ID of the entry.
         */
        private String listId = "";

        /**
         * Default constructor.
         */
        public EntryData() {}

        /**
         * Retrieves name of the entity.
         * @return Name of the entity.
         */
        public String getName() { return this.name; }
        /**
         * Sets the name of the entity.
         * @param name Name of the enity
         */
        public void setName(String name) { this.name = name; }

        /**
         * Retrieves name of the entity.
         * @return Name of the entity.
         */
        public String getId() { return this.id; }
        /**
         * Sets the UUID of the entity.
         * @param id UUID of the entity
         */
        public void setId(String id) { this.id = id; }

        /**
         * Retrieves name of the entity. Only useful if entity is an entry and not a list.
         * @return Name of the entity.
         */
        public String getDescription() { return this.description; }
        /**
         * Sets the description of the entity. Only useful if entity is an entry and not a list.
         * @param description Description of the entity.
         */
        public void setDescription(String description) { this.description = description; }

        /**
         * Retrieves List UUID of the entity. Only useful if entity is an entry and not a list.
         * @return List UUID of the entity.
         */
        public String getListId() { return this.listId; }
        /**
         * Sets the list UUID of the entity. Only useful if entity is an entry and not a list.
         * @param listId List UUID of the entity.
         */
        public void setListId(String listId) { this.listId = listId; }

    }
    
    /**
     * Array of entities from API Response.
     */
    private EntryData[] entries;
    /**
     * Shows, how many entities were written, as declared in API response.
     */
    private int written;

    /**
     * @deprecated
     */
    private int total;

    /**
     * Shows, how many entities were deleted, as declared in API response.
     */
    private int deleted;

    /**
     * Shows, how many entities were updated, as declared in API response.
     */
    private int entriesUpdated;

    /**
     * @deprecated
     * Shows, whether API request was successful.
     */
    private boolean success;
    /**
     * Message from API Response
     */
    private String message;

    /**
     * Status Code of the API Request.
     */
    private int statusCode;

    /**
     * Classname of the thrown exception, when the class is instanciated with an Exception.
     */
    private String exceptionType;
    
    /**
     * Primary Constructor for ResponseData object.
     * Map is the API response parsed into a easily accessable object.
     * @param map Parsed API Response. 
     * @param statusCode Status code of the http response.
     */
    public ResponseData (Map<String,Object> map, int statusCode) {
        // If the api responded with entries, iterate through all of them and instanciate EntryData objects for every one.
        if (map.get("entries") != null) {
            ArrayList<LinkedHashMap<String,String>> rEntries = (ArrayList<LinkedHashMap<String,String>>) map.get("entries");
            this.entries = new EntryData[rEntries.size()];

            for (int i = 0; i < rEntries.size(); i++) {
                this.entries[i] = new EntryData();
                
                if (rEntries.get(i).get("id") != null)          this.entries[i].setId(rEntries.get(i).get("id"));
                if (rEntries.get(i).get("name") != null)        this.entries[i].setName(rEntries.get(i).get("name"));
                if (rEntries.get(i).get("description") != null) this.entries[i].setDescription(rEntries.get(i).get("description"));
                if (rEntries.get(i).get("list_id") != null)     this.entries[i].setListId(rEntries.get(i).get("list_id"));
            }
        }

        // Set the statuscode
        this.statusCode = statusCode;

        // Check every key and set the corresponding instance field
        if (map.get("deleted") != null) {
            this.deleted = (int) map.get("deleted");
        } else this.deleted = -1;

        if (map.get("written") != null) {
            this.written = (int) map.get("written");
        } else this.written = -1;

        if (map.get("total") != null) {
            this.total = (int) map.get("total");
        } else this.total = -1;

        if (map.get("entries_updated") != null) {
            this.entriesUpdated = (int) map.get("entries_updated");
        } else this.entriesUpdated = -1;
    }

    /**
     * Secondary constructor. If API request resulted in an exception, the Class can still be instanciated
     * with the information of the Exception.
     * Message of the exception will be saved in class field {@link #message}.
     * Type of exception (class) will be saved in class field {@link #exceptionType}.
     * @param e Exception that occured during API Request 
     */
    public ResponseData(Exception e) {
        if (e.getMessage() != null) {
            this.message = e.getMessage();
        } else {
            this.message = "No further cause for exception found.";
        }

        this.exceptionType = e.getClass().toString();
    }
    
    /**
     * Retrieves all entries sent by the API Response.
     * @return Entries sent by the API Response.
     */
    public EntryData[] getEntries() {
        return this.entries;
    }

    /**
     * Sets the Entries
     * @param entries All Entries
     */
    public void setEntries(EntryData[] entries) {
        this.entries = entries;
    }

    /**
     * Retrieves how many entries were written as declared by the API Response.
     * @return How many entries were written in API request.
     */
    public int getWritten() {
        return this.written;
    }

    /**
     * Set how many entries were written
     * @param written How many entries were written
     */
    public void setWritten(int written) {
        this.written = written;
    }

    /**
     * @deprecated
     * @return total
     */
    public int getTotal() {
        return this.total;
    }

    /**
     * @deprecated
     * @param total Set, how many entries were written
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Retrieves how many entries were deleted as declared by the API Response.
     * @return How many entries were deleted in API request.
     */
    public int getDeleted() {
        return this.deleted;
    }

    /**
     * Set how many entries were deleted in request
     * @param deleted How many entries were deleted in request
     */
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    /**
     * Retrieves how many entries were updated as declared by the API Response.
     * @return How many entries were updated in API request.
     */
    public int getEntriesUpdated() {
        return this.entriesUpdated;
    }

    /**
     * Set how many entries were updated in request
     * @param entriesUpdated How many entries were updated in request
     */
    public void setEntriesUpdated(int entriesUpdated) {
        this.entriesUpdated = entriesUpdated;
    }

    /**
     * @deprecated
     * @return isSuccess
     */
    public boolean getSuccess() {
        return this.success;
    }

    /**
     * @deprecated
     * @param success Set, whether the API Request was successful
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the message of the API response.
     * @return Message of the API response.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set message of api response
     * @param message message of api response
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves status code of the api response.
     * @return Status code of the API Response.
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Set status code of api response
     * @param statusCode status code of api response.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /**
     * Gets the exceptionType of the API response.
     * @return exceptionType of the API response.
     */
    public String getExceptionType() {
        return this.exceptionType;
    }

    /**
     * Set exceptionType of api response
     * @param exceptionType exceptionType of api response
     */
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

}
