package firsthttpserver;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Scanner;
/**
 * @author Peter Christensen
 */
public class FirstHtttpServer {
  static int port = 8080;
  static String ip = "127.0.0.1";
  static String contentFolder ="public/";
  public static void main(String[] args) throws Exception {
    if(args.length == 2){
      port = Integer.parseInt(args[0]);
      ip = args[1];
    }
    HttpServer server = HttpServer.create(new InetSocketAddress(ip,port), 0);
    server.createContext("/welcome", new RequestHandler());
    server.createContext("/files", new SimpleFileHandler());
    server.createContext("/pages", new RequestHandlerHandle());
    server.createContext("/headers", new RequestHandlerHeaders());
    server.createContext("/parameters", new RequestHandlerParameter());
    server.createContext("/url", new RequestHandlerHandleOpg5());
    server.setExecutor(null); // Use the default executor
    server.start();
    System.out.println("Server started, listening on port: "+port);
  }
    //Opgave 3
     static class RequestHandlerHandle implements HttpHandler {
    String contentFolder = "public/";
    
    public void handle(HttpExchange he) throws IOException {
    
    File file = new File(contentFolder+"index.html");
    byte[] bytesToSend = new byte[(int) file.length()];
    try {
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
    bis.read(bytesToSend, 0, bytesToSend.length);
    } catch (IOException ie) {
    ie.printStackTrace();
    }
    he.sendResponseHeaders(200, bytesToSend.length);
    try (OutputStream os = he.getResponseBody()) {
    os.write(bytesToSend, 0, bytesToSend.length);
    }
   }
 }
  
    static class RequestHandlerHandleOpg5 implements HttpHandler {
    String contentFolder = "public/";
    
    public void handle(HttpExchange he) throws IOException {
    
    String url = he.getRequestURI().toString();
    url = url.substring(5);
    File file = new File(contentFolder+ url);
    byte[] bytesToSend = new byte[(int) file.length()];
    try {
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
    bis.read(bytesToSend, 0, bytesToSend.length);
    } catch (IOException ie) {
    ie.printStackTrace();
    }
    he.sendResponseHeaders(200, bytesToSend.length);
    try (OutputStream os = he.getResponseBody()) {
    os.write(bytesToSend, 0, bytesToSend.length);
    }
   }
 }
  static class RequestHandlerHeaders implements HttpHandler{
        @Override
        public void handle(HttpExchange he) throws IOException {
        }
  }
  static class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {

      String response = "<h1>Welcome to my very first almost home made Web Server :-)</h1>";
      StringBuilder sb = new StringBuilder();
      sb.append("<!DOCTYPE html>\n");
      sb.append("<html>\n");
      sb.append("<head>\n");
      sb.append("<title>My fancy Web Site</title>\n");
      sb.append("<meta charset='UTF-8'>\n");
      sb.append("</head>\n");
      sb.append("<body>\n");
      sb.append("<h2>Welcome to my very first home made Web Server :-)</h2>\n");
      sb.append("</body>\n");
      sb.append("</html>\n");
      response = sb.toString();   
      response += "<br>"+"URI:"+he.getRequestURI();
      Scanner scan = new Scanner(he.getRequestBody());
      while(scan.hasNext()){
          response += "<br/>" + scan.nextLine();
      }
      Headers h = he.getResponseHeaders();
      h.add("Content-Type", "text/html");
            
      he.sendResponseHeaders(200, response.length());
      try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
        pw.print(response); //What happens if we use a println instead of print --> Explain
      }
      
    }
  }
//opgave 4 get method post method
  static class RequestHandlerParameter implements HttpHandler {

    public void handle(HttpExchange he) throws IOException {

      String response = "<h1>Welcome to my very first almost home made Web Server :-)</h1>";
      StringBuilder sb = new StringBuilder();
      sb.append("<!DOCTYPE html>\n");
      sb.append("<html>\n");
      sb.append("<head>\n");
      sb.append("<title>My fancy Web Site</title>\n");
      sb.append("<meta charset='UTF-8'>\n");
      sb.append("</head>\n");
      sb.append("<body>\n");
      sb.append("<h2>Medhod is:");
      sb.append("</body>\n");
      sb.append("</html>\n");
      response = sb.toString();   
      response += "<br>"+"URI:"+he.getRequestURI();
      
      Scanner scan = new Scanner(he.getRequestBody());
      while(scan.hasNext()){
      sb.append("Request body, with Post-parameters: "+scan.nextLine());
      sb.append("</br>");
      }
      Headers h = he.getResponseHeaders();//getRequestMethod();
      h.add("Content-Type", "text/html");
      
      he.sendResponseHeaders(200, response.length());
      try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
        pw.print(response); //What happens if we use a println instead of print --> Explain
      }
      
    }
  }
  
  
  
  static class SimpleFileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
      File file = new File(contentFolder + "index.html");
      byte[] bytesToSend = new byte[(int) file.length()];
      try {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(bytesToSend, 0, bytesToSend.length);
      } catch (IOException ie) {
        System.out.println(ie);
      }
      he.sendResponseHeaders(200, bytesToSend.length);
      try (OutputStream os = he.getResponseBody()) {
        os.write(bytesToSend, 0, bytesToSend.length);
      }
    }
  }

  
  
}