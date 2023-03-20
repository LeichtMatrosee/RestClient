package client;

public class App {
    public static void main( String[] args ) throws Exception {
        RestCommunicator rc = new RestCommunicator("localhost", 5000);

        ResponseData rd = rc.addNewList(new PostData("list", "datenbanken bei korte", "Hello World"));
        System.out.println(rd.getEntries()[0].getId());
        
        rd = rc.getEntriesFromList(new PostData("entry", "This is an entry", "Describing is a nice thing", "e636406e-c762-4e98-baac-8bb13c58116b"));
        System.out.println(rd.getEntries()[0].getName());

        rd = rc.searchList(new PostData("list", "Datenbanken bei Korte"));

        System.out.println(rd.getEntries().length);
    }
}
