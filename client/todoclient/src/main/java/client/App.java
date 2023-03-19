package client;

public class App {
    public static void main( String[] args ) throws Exception {
        RestCommunicator rc = new RestCommunicator("localhost", 5000);
        PostData pd = new PostData("list", "Testing Java Posts", "Hello World");

        rc.addNewList(pd);
    }
}
