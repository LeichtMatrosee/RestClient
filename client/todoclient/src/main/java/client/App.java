package client;

public class App {
    public static void main( String[] args ) throws Exception {
        RestCommunicator rc = new RestCommunicator("localhost", 5000);
        PostData pd = new PostData("list", "Testing Java Posts", "Hello World", "f5b8b2f8-4858-4786-a73d-4d4c2d847ffb");

        ResponseData rd = rc.addNewList(pd);
        System.out.println(rd.getEntries()[0].getId());

        rd = rc.getEntriesFromList(new PostData("entry", "This is an entry", "Describing is a nice thing", rd.getEntries()[0].getId()));
        System.out.println(rd.getEntries()[0].getName());

    }
}
