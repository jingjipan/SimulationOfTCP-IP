package uofm.seca.partb;

import java.net.*;

public final class WebServer {

    public static void main(String[] args) throws Exception {
        // Set the port number.
        int port = 8888;
        // Establish the listen socket.
        /**You have to put code here**/ 
        ServerSocket server = new ServerSocket(port);
        //
        Socket clientSocket = null;

        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request to the port
             /**You have to put code here**/ 
            //?
            clientSocket = server.accept();
            // Construct an object to process the HTTP request message
            HttpRequest request = new HttpRequest(clientSocket);  // HttpRequest(?)

            // Create a new thread to process the request
            Thread thread = new Thread(request);

            // Start the thread
            thread.start();
        }
    }
}
