package client;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Instances of this class encapsulate all information that is sent to the API.
 * @author LeichtMatrosee
 * @version 1.0
 */
public class PostData {
    /**
     * Type of the entity to be updated. Either "list" or "entry"
     */
    private String type = "";
    /**
     * UUID of a todo-list. Must be set, when updating a list or doing anything
     * entry-related.
     */
    private String listId = "";
    /**
     * Name of the entity.
     */
    private String name = "";
    /**
     * Description of the entity. Only useful for entries.
     */
    private String description = "";
    /**
     * UUID of the entity.
     */
    private String id = "";
    /**
     * @deprecated
     */
    private boolean successful = false;
    /**
     * UUID of the entry.
     */
    private String entryId = "";

    /**
     * Standard Constructor.
     * Analyses map and initializes all instance fields with it's information.
     * @param map Map containing all information for posting to API.
     *  Valid attributes are: type, name, description, listId, entryId, id
     *  All other attributes will be ignored.
     */
    public PostData(HashMap<String,String> map) {
        if (!map.get("type").equals("list") && !map.get("type").equals("entry")) {
            throw new InvalidParameterException("Type must either be list or entry, was " + map.get("type") + " instead.");
        } else {
            this.type = map.get("type");
        }

        if (map.get("name") != null) this.name = map.get("name");
        if (map.get("description") != null) this.description = map.get("description");
        if (map.get("listId") != null) this.listId = map.get("listId");
        if (map.get("entryId") != null) this.entryId = map.get("entryId");
        if (map.get("id") != null) this.id = map.get("id");
    }

    /**
     * Sets type of the entity described by the instance.
     * @param type Either "list" or "entry"
     */
    public void setType(String type) { this.type = type; }
    /**
     * Retrieves the type of the entity described by this instance.
     * @return Type of the entity
     */
    public String getType() { return this.type; }
    
    /**
     * Sets name of the entity described by the instance.
     * @param name Name of the entity
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves the name of the entity described by this instance.
     * @return Name of the entity
     */
    public String getName() { return this.name; }

    /**
     * Sets description of the entity described by the instance.
     * @param description Description of the entity
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves the description of the entity described by this instance.
     * @return Description of the entity
     */
    public String getDescription() { return this.description; }

    /**
     * Sets listId of the entity described by the instance.
     * @param listId List ID of the entity
     */
    public void setListId(String listId) { this.listId = listId; }
    /**
     * Retrieves the listId of the entity described by this instance.
     * @return List UUID of the entity
     */
    public String getListId() { return this.listId; }

    /**
     * Sets UUID of the entity described by the instance.
     * @param id UUID of the entity
     */
    public void setId(String id) { this.id = id; }
    /**
     * Retrieves the UUID of the entity described by this instance.
     * @return UUID of the entity
     */
    public String getId() { return this.id; }

    /**
     * @deprecated
     * @param success
     */
    public void setSuccessful(boolean success) { this.successful = success; }
    /**
     * @deprecated
     * @return
     */
    public boolean getSuccessful() { return this.successful; }

    /**
     * Sets EntryID of the entity described by the instance.
     * @param entryId EntryId of the entity
     */
    public void setEntryId(String entryId) { this.entryId = entryId; }
    /**
     * Retrieves the entryId of the entity described by this instance.
     * @return Entry UUID of the entity
     */
    public String getEntryId() { return this.entryId; }
}