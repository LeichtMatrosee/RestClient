package client;

public class App {
    public static void main( String[] args ) throws Exception {
        RestCommunicator rc = new RestCommunicator("localhost", 5000);
        PostData pd = new PostData("entry", "Testing Java Posts", "Hello World", "f5b8b2f8-4858-4786-a73d-4d4c2d847ffb");

        rc.addNewList(pd);
    }
}
