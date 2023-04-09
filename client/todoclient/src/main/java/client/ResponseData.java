package client;

import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ResponseData {

    public class EntryData {
        private String name = "";
        private String id = "";
        private String description = "";
        private String listId = "";

        public EntryData() {}

        public String getName() { return this.name; }
        public void setName(String name) { this.name = name; }

        public String getId() { return this.id; }
        public void setId(String id) { this.id = id; }

        public String getDescription() { return this.description; }
        public void setDescription(String description) { this.description = description; }

        public String getListId() { return this.listId; }
        public void setListId(String listId) { this.listId = listId; }

    }
    
    private EntryData[] entries;
    private int written;
    private int total;

    // Delete param
    private int deleted;

    // Update Param
    private int entriesUpdated;

    private boolean success;
    private String message;
    
    public ResponseData (Map<String,Object> map) {
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

        if (map.get("deleted") != null) {
            this.deleted = (int) map.get("deleted");
        }

        if (map.get("written") != null) {
            this.written = (int) map.get("written");
        }

        if (map.get("total") != null) {
            this.total = (int) map.get("total");
        }

        if (map.get("entries_updated") != null) {
            this.entriesUpdated = (int) map.get("entries_updated");
        }

        this.success = true;
    }

    public ResponseData(Exception e) {
        this.success = false;

        if (e.getMessage() != null) {
            this.message = e.getMessage();
        } else {
            this.message = "No further cause for exception found.";
        }
    }

    /*************** SETTERS AND GETTERS ***********************/
    
    public void setEntries(EntryData[] entries) {
        this.entries = entries;
    }

    public EntryData[] getEntries() { return this.entries; }

    public int getWritten() {
        return this.written;
    }

    public void setWritten(int written) {
        this.written = written;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDeleted() {
        return this.deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getEntriesUpdated() {
        return this.entriesUpdated;
    }

    public void setEntriesUpdated(int entriesUpdated) {
        this.entriesUpdated = entriesUpdated;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean s) {
        this.success = s;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

}
