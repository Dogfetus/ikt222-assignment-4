package IKT222.patients;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class AppServer {
  public static void main( String[] args ) throws Exception {
    ServletHandler handler = new ServletHandler();
    handler.addServletWithMapping(AppServlet.class, "/*");

    Server server = new Server(8080);
    server.setHandler(handler);

    server.start();
    server.join();
  }
}
