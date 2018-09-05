package uofm.seca.parta;

import java.io.*;
import java.net.*;
import java.util.*;


final class HttpRequest implements Runnable {
    
    final static String CRLF = "\r\n";
    Socket clientSocket = null;
    
    // Constructor
    public HttpRequest(Socket clientSocket) throws Exception {
        this.clientSocket = clientSocket;
    }
    
    // Implement the run() method of the Runnable interface
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    // Does the actual processing of the request
    private void processRequest() throws Exception {
        // Get a reference to the socket's input and output streams
        /**You have to put code here**/ 
        //?
        InputStream is = clientSocket.getInputStream();/*?*/;
        DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());/*?*/
        
        // Set up input stream filters
        /**You have to put code here**/ 
        //?// ei pura line ?
        BufferedReader br =  new BufferedReader(new InputStreamReader(is));/*?*/ // br =?
        
        // Get the request line of the HTTP request message
        String requestLine = br.readLine(); //String requestLine = ?; 
        
        // Display the request line
        System.out.println();
        System.out.println(requestLine);
        
        // Get and display the header lines
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        }
        
        // Extract the filename from the request line
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();  // skip over the method, which should be "GET"
        String fileName = tokens.nextToken();
        
        // Prepend a "." so that file request is within the current directory
        fileName = "." + fileName;
        
        // Open the requested file
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }
        
        // Construct the response message
        String statusLine = null;
        String contentTypeLine = null;  // response headers
        String entityBody = null;
        if (fileExists) {
            statusLine = "?";  //  statusLine = "?";  
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        } else {
            statusLine =  "" /*?*/ ; //statusLine = "?";  
            contentTypeLine = "" /*?*/;  // contentTypeLine = "?"
            entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + 
                    "<BODY>Not Found</BODY></HTML>";
        }
        
        // Send the status line
        os.writeBytes(statusLine);
        // Send the content type line
        os.writeBytes(contentTypeLine);
        //Send a blank line to indicate the end of the header lines
        os.writeBytes(CRLF);
        
        // Send the entity body
        if (fileExists) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody);
        }
        
        // Close the streams and socket
        os.close();
        br.close();
        clientSocket.close();        
    }
    
    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the socket
        byte[] buffer = new byte[1024];
        int bytes = 0;
        
        // Copy requested file into the socket's output stream
        while ((bytes = fis.read(buffer)) != -1 ) {
            os.write(buffer, 0, bytes);
        }
    }
    
    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".gif")) {  // "?"
            return "?"; // return ?
        }
        if (fileName.endsWith("jpeg")) { //"?"
            return "?";    // return ?
        }
        return "application/octet-stream";
    }
}