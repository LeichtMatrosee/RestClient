package client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void testAllRestFunctions() {
        assertTrue(this.restWrapper());
    }

    private boolean restWrapper() {
        try {
            RestCommunicator rc = new RestCommunicator();

            ResponseData rd = rc.addNewList(new PostData("list", "Rest Test", "description for Rest Test"));

            if (rd.getEntries().length < 1) return false;
            if (!rd.getEntries()[0].getName().equals("Rest Test")) return false;
            if (rd.getEntries()[0].getId().equals("")) return false;

            String listId = rd.getEntries()[0].getId();

            rd = rc.addEntryToList(new PostData("entry", "Test Entry", "description for Test Entry", listId));
            if (rd.getEntries().length < 1) return false;
            if (!rd.getEntries()[0].getName().equals("Test Entry")) return false;
            if (rd.getEntries()[0].getId().equals("")) return false;

            rc.addEntryToList(new PostData("entry", "Test Entry 2", "description for Test Entry 2", listId));
            
            rd = rc.getEntriesFromList(new PostData("entry", "", "", listId));
            if (rd.getEntries().length != 2) return false;

            String entryId = rd.getEntries()[1].getId();

            rd = rc.deleteEntry(new PostData(listId, entryId, "", "", "entry"));
            if (rd.getDeleted() != 1) return false;

            rd = rc.getEntriesFromList(new PostData("entry", "", "", listId));
            if (rd.getEntries().length != 1) return false;

            entryId = rd.getEntries()[0].getId();
            
            
            rc.updateEntry(new PostData(listId, entryId, "Updated Entry", "Updated description", "entry"));
            rd = rc.getEntriesFromList(new PostData("entry", "", "", listId));

            if (!rd.getEntries()[0].getName().equals("Updated Entry")) return false;
            if (!rd.getEntries()[0].getDescription().equals("Updated description")) return false;

            rd = rc.deleteList(new PostData("list", "", "", listId));

            if (rd.getDeleted() != 1) return false;
            
            rd = rc.searchList(new PostData("list", "Rest Test"));

            if (rd.getEntries().length > 0) return false;
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
