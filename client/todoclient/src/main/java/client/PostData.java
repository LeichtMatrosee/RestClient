package client;

import java.security.InvalidParameterException;

public class PostData {
    private String type = "";
    private String listId = "";
    private String name = "";
    private String description = "";
    private String id = "";
    private boolean successful = false;
    private String entryId = "";

    /**
     * @constructor
     * @brief
     * 
     * @param type
     * @param name
     * @param description
     * @param listId
     * @throws InvalidParameterException
     */
    public PostData(String type, String name, String description, String listId) throws InvalidParameterException {
        if (type != "list" && type != "entry") {
            throw new InvalidParameterException("Type must either be list or entry, was " + type + " instead.");
        }
        this.type = type;
        this.name = name;
        this.description = description;

        if (listId == "" && type == "entry") {
            throw new InvalidParameterException("ListId cannot be empty when pushing new entries for a list.");
        }
        this.listId = listId;
    }

    // TODO this constructor is basically only useful for lists, change error handling
    /**
     * @constructor
     * 
     * @param {String} type
     * @param {String} name
     */
    public PostData(String type, String name, String description) throws InvalidParameterException {
        if (type != "list" && type != "entry") {
            throw new InvalidParameterException("Type must either be list or entry, was " + type + " instead.");
        }
        this.type = type;
        this.name = name;

        this.description = description;
        this.listId = "";
    }

    public PostData(String type, String name) {
        this.type = type;
        this.name = name;
        this.description = "";
        this.listId = "";
    }

    public PostData(String listId, String entryId, String name, String description, String type) throws InvalidParameterException {
        if (type != "list" && type != "entry") {
            throw new InvalidParameterException("Type must either be list or entry, was " + type + " instead.");
        }
        this.type = type;

        this.name = name;
        this.description = description;

        if (listId == "" && type == "entry") {
            throw new InvalidParameterException("ListId cannot be empty when pushing new entries for a list.");
        }

        if (entryId.equals("")) {
            throw new InvalidParameterException("I made this constructor specifically for one endpoint, where you need an entry Id. "
                + "Give me one or use a different constructor for crying out loud."
            );
        }
        this.listId = listId;
        this.entryId = entryId;
    }


    /**************** SETTERS AND GETTERS ****************/
    public void setType(String type) { this.type = type; }
    public String getType() { return this.type; }
    
    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return this.description; }

    public void setListId(String listId) { this.listId = listId; }
    public String getListId() { return this.listId; }

    public void setId(String id) { this.id = id; }
    public String getId() { return this.id; }

    public void setSuccessful(boolean success) { this.successful = success; }
    public boolean getSuccessful() { return this.successful; }

    public void setEntryId(String entryId) { this.entryId = entryId; }
    public String getEntryId() { return this.entryId; }
}