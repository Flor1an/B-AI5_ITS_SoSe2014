package vorgabecode;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Simple Web-Server which implements just the GET request of the http 1.0 - Protocol
 */

class WebServer {

    public final String DOCS_DIR = "C:/Temp/" + "Testweb/";

    public final static int SERVER_PORT = 8080;

    /* Maximal allowed number of parallel running worker threads */
    private final int maxThreads;

    /* Counts the number of parallel running worker threads  */
    private int runningThreads = 0;

    /* Constructor with argument: Maximal allowed number of parallel running worker threads */
    public WebServer(int maxThreads) {
        this.maxThreads = Math.max(maxThreads, 1);
    }

    /**
     * Listen on TCP-connection requests
     */
    private void runWebServer() {
        ServerSocket welcomeSocket;
        Socket connectionSocket;

        int cnt = 0;
        try {
            System.out.println("Waiting for connections using port "
                    + SERVER_PORT + " with root dir " + DOCS_DIR);
            welcomeSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                connectionSocket = welcomeSocket.accept();
                // Start new thread for the handling of new socket connection
                // (one http request)
                (new WorkerThread(this, ++cnt, connectionSocket, DOCS_DIR)).start();
                incRunningThreads();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public synchronized void incRunningThreads() {
    /* Increase the number of parallel running worker threads. If maximum reached: wait */
        runningThreads++;

        if (runningThreads >= maxThreads) {
            try {
                System.out.println(maxThreads +
                        " Threads running --> new Connections will not be handled, but queued ...");
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("INTERRUPTED!");
            }
        }
    }

    public synchronized void decRunningThreads() {
    /* Decrease the number of parallel running worker threads and notify (awake) server */
        runningThreads--;
        this.notify();
    }

    /**
     * Handle one http request
     */
    private class WorkerThread extends Thread {

        private WebServer server; // the caller
        private int name; // name of current thread
        private Socket sock;
        private String defaultDir;

        // headerLines content: (field name, field value)
        private HashMap<String, String> headerLines = new HashMap<>();
        private BufferedReader inFromClient;
        private DataOutputStream outToClient;
        private String fileName = ""; // filename incl. local path
        private String relFileName = ""; // relative filename
        private long fileLength;
        private FileInputStream inFile;

        // constructor
        public WorkerThread(WebServer server, int num, Socket socket, String dir) {
            this.server = server;
            this.name = num;
            this.sock = socket;
            this.defaultDir = dir;
        }

        public void run() {
            System.out.println("New connection-socket " + name
                    + " established!");
            try {
                inFromClient = new BufferedReader(new InputStreamReader(
                        sock.getInputStream()));

                outToClient = new DataOutputStream(sock.getOutputStream());

                // --------- Start of Protocol ------------------------
                if (readGETrequest()) {
                    readHeaderLines(); // for later use
                    if (checkFile()) {
                        sendContent();
                    } else {
                        send404ErrorMessage();
                    }
                }
                // --------- End of Protocol ------------------------
                sock.close();
            } catch (IOException e) {
                System.err.println("Connection aborted by client!");
            } finally {
                server.decRunningThreads();
                System.out.println("Worker-Thread " + name + " stopped!");
            }
        }

        /**
         * Read "GET"-request message and extract filename. Return true if
         * request is syntactically ok
         */
        private boolean readGETrequest() throws IOException {
            String requestMessageLine = readFromClient();
            StringTokenizer tokenizedLine = new StringTokenizer(
                    requestMessageLine);
            if (tokenizedLine.nextToken().equalsIgnoreCase("GET")) {
                relFileName = tokenizedLine.nextToken();
                if (relFileName.contains("..") || relFileName.equals("/")) {
                    // attack with /..-operator or no filename given
                    relFileName = "index.html";
                } else if (relFileName.startsWith("/")) {
                    // cut leading '/' in filename
                    relFileName = relFileName.substring(1);
                }
                fileName = defaultDir + relFileName;
                System.out.println("Filename: " + fileName);
                return true;
            } else {
                writeToClient("HTTP/1.0 400 Bad Request");
                writeToClient("");
                return false;
            }
        }

        /**
         * Read all header lines and put them into the hashMap headerLines
         */
        private void readHeaderLines() throws IOException {
            String line;
            String[] headerLineArray;

            headerLines.clear();
            line = readFromClient();
            while (line.length() > 0) {
                headerLineArray = line.split(":");
                try {
                    headerLines.put(headerLineArray[0].trim(),
                            headerLineArray[1].trim());
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Illegal header line: " + line);
                }
                line = readFromClient();
            }
        }

        /**
         * Instantiate file stream and determine length. Return false if file
         * not found
         */
        private boolean checkFile() {
            try {
                inFile = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                return false;
            }
            File file = new File(fileName);
            fileLength = file.length();
            return true;
        }

        /**
         * Send the headerlines and the file content
         */
        private void sendContent() throws IOException {
            writeToClient("HTTP/1.0 200 OK");
            if (fileName.endsWith(".html"))
                writeToClient("Content-Type:text/html");
            if (fileName.endsWith(".jpg"))
                writeToClient("Content-Type:image/jpeg");
            if (fileName.endsWith(".gif"))
                writeToClient("Content-Type:image/gif");
            if (fileName.endsWith(".pdf"))
                writeToClient("Content-Type:application/pdf");
            writeToClient("Content-Length: " + fileLength);
            writeToClient("Set-Cookie: " + name);
            writeToClient("");
            // write body
            // --------- Start of file copy operation ------------------------
            byte[] buffer = new byte[4096];
            int len;
            try {
                while ((len = inFile.read(buffer)) > 0) {
                    outToClient.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Read the next line from the client socket
         */
        private String readFromClient() throws IOException {
            String inputString = inFromClient.readLine();
            if (inputString == null) {
                throw new IOException();
            }
            System.out.println("Worker-Thread " + name
                    + " has read the message: " + inputString);
            return inputString;
        }

        /**
         * Write one line to the client socket
         */
        private void writeToClient(String returnString) throws IOException {
            try {
                outToClient.writeBytes(returnString + '\r' + '\n');
            } catch (IOException e) {
                System.err.println(e.toString());
            }
            System.out.println("Worker-Thread " + name
                    + " has written the message: " + returnString);
        }

        /**
         * Send 404-Error to the client
         */
        private void send404ErrorMessage() throws IOException {
            String htmlContent = "<html> <head><title>Fehler: Seite nicht gefunden</title></head>"
                    + "<body><h1>Die gew&uuml;nschte Seite konnte leider nicht gefunden werden (HTTP-Fehler 404)!</h1></body></html>";
            writeToClient("HTTP/1.0 404 Not Found");
            writeToClient("Content-Type:text/html");
            writeToClient("Content-Length: " + htmlContent.length());
            writeToClient("");
            writeToClient(htmlContent);
        }
    }

    public static void main(String args[]) throws IOException {
        WebServer myServer = new WebServer(5);
        myServer.runWebServer();
    }
}