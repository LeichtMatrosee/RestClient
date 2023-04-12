package client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private String listId;
    private String entryId;

    @Test
    public void testAddList() {
        try {
            assertTrue(this.addList());
        } catch (Exception e) {
            assertTrue("Add New List failed with Exception " + e.getClass().getName() + ", Message: " + e.getMessage(), false);
        }
    }

    @Test
    public void testAddEntry() {
        try {
            assertTrue(this.addEntry());
        } catch (Exception e) {
            assertTrue("Add New List failed with Exception " + e.getClass().getName() + ", Message: " + e.getMessage(), false);
        }
    }

    @Test
    public void testGetEntries() {
        try {
            assertEquals(2, this.getEntriesFromList(), 0);
        } catch (Exception e) {
            assertTrue("Add New List failed with Exception " + e.getClass().getName() + ", Message: " + e.getMessage(), false);
        }
    }

    @Test
    public void testDeleteEntries() {
        try {
            assertTrue(this.deleteEntry());
        } catch (Exception e) {
            assertTrue("Add New List failed with Exception " + e.getClass().getName() + ", Message: " + e.getMessage(), false);
        }
    }

    @Test
    public void testUpdateEntry() {
        try {
            assertTrue(this.updateEntry());
        } catch (Exception e) {
            assertTrue("Add New List failed with Exception " + e.getClass().getName() + ", Message: " + e.getMessage(), false);
        }
    }

    @Test
    public void testDeleteList() {
        try {
            assertTrue(this.deleteList());
        } catch (Exception e) {
            assertTrue("Add New List failed with Exception " + e.getClass().getName() + ", Message: " + e.getMessage(), false);
        }
    }


    /**************** Test Content Functions *********************/

    private boolean addList() throws IOException, URISyntaxException, InterruptedException, JSONException {
        RestCommunicator rc = new RestCommunicator();
        ResponseData rd = rc.addNewList(new PostData("list", "Rest Test", "description for Rest Test"));

        if (rd.getEntries().length < 1) return false;
        if (!rd.getEntries()[0].getName().equals("Rest Test")) return false;
        if (rd.getEntries()[0].getId().equals("")) return false;

        this.listId = rd.getEntries()[0].getId();

        return true;
    }

    private boolean addEntry() throws IOException, URISyntaxException, InterruptedException, JSONException {
        RestCommunicator rc = new RestCommunicator();
        ResponseData rd = rc.addEntryToList(new PostData("entry", "Test Entry", "description for Test Entry", this.listId));
        if (rd.getEntries().length < 1) return false;
        if (!rd.getEntries()[0].getName().equals("Test Entry")) return false;
        if (rd.getEntries()[0].getId().equals("")) return false;

        return true;
    }

    private int getEntriesFromList() throws IOException, URISyntaxException, InterruptedException, JSONException {
        RestCommunicator rc = new RestCommunicator();
        rc.addEntryToList(new PostData("entry", "Test Entry 2", "description for Test Entry 2", listId));
            
        ResponseData rd = rc.getEntriesFromList(new PostData("entry", "", "", listId));

        this.entryId = rd.getEntries()[0].getId();
        return rd.getEntries().length;
    }

    private boolean deleteEntry() throws IOException, URISyntaxException, InterruptedException, JSONException {
        RestCommunicator rc = new RestCommunicator();
        ResponseData rd = rc.deleteEntry(new PostData(this.listId, this.entryId, "", "", "entry"));
        if (rd.getDeleted() != 1) return false;

        rd = rc.getEntriesFromList(new PostData("entry", "", "", listId));
        if (rd.getEntries().length != 1) return false;

        this.entryId = rd.getEntries()[0].getId();
        return true;
    }

    private boolean updateEntry() throws IOException, URISyntaxException, InterruptedException, JSONException {
        RestCommunicator rc = new RestCommunicator();
        rc.updateEntry(new PostData(this.listId, this.entryId, "Updated Entry", "Updated description", "entry"));
        ResponseData rd = rc.getEntriesFromList(new PostData("entry", "", "", listId));

        if (!rd.getEntries()[0].getName().equals("Updated Entry")) return false;
        if (!rd.getEntries()[0].getDescription().equals("Updated description")) return false;

        return true;
    }

    private boolean deleteList() throws IOException, URISyntaxException, InterruptedException, JSONException {
        RestCommunicator rc = new RestCommunicator();
        ResponseData rd = rc.deleteList(new PostData("list", "", "", this.listId));

        if (rd.getDeleted() != 1) return false;
        rd = rc.searchList(new PostData("list", "Rest Test"));
        if (rd.getEntries().length > 0) return false;

        return true;
    }
}
