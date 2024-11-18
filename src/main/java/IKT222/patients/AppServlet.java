package IKT222.patients;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import org.mindrot.jbcrypt.BCrypt; // adding bcrypt to hash the password

@SuppressWarnings("serial")
public class AppServlet extends HttpServlet {

  private static final String CONNECTION_URL = "jdbc:sqlite:db.sqlite3";
  private static final String AUTH_QUERY = "select * from user where username=? and password=?";
  private static final String AUTH_QUERY2 = "SELECT password FROM user WHERE username = ?"; // for auth function
  private static final String SEARCH_QUERY = "select * from patient where surname like ?";

  private final Configuration fm = new Configuration(Configuration.VERSION_2_3_28);
  private Connection database;

  @Override
  public void init() throws ServletException {
    configureTemplateEngine();
    connectToDatabase();
    // adding the adduser function to check if the hashing works
    try {
      addAUser("SanderW", "Sander", "password1.");
    } catch (SQLException e) {
        throw new ServletException("Failed to add user: " + e.getMessage(), e);
    }
  }

  private void configureTemplateEngine() throws ServletException {
    try {
      fm.setDirectoryForTemplateLoading(new File("./templates"));
      fm.setDefaultEncoding("UTF-8");
      fm.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
      fm.setLogTemplateExceptions(false);
      fm.setWrapUncheckedExceptions(true);
    }
    catch (IOException error) {
      throw new ServletException(error.getMessage());
    }
  }

  private void connectToDatabase() throws ServletException {
    try {
      database = DriverManager.getConnection(CONNECTION_URL);
    }
    catch (SQLException error) {
      throw new ServletException(error.getMessage());
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
    try {
      Template template = fm.getTemplate("login.html");
      template.process(null, response.getWriter());
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
    }
    catch (TemplateException error) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
     // Get form parameters
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String surname = request.getParameter("surname");

    try {
      if (authenticated(username, password)) {
        Map<String, Object> model = new HashMap<>();
        model.put("records", searchResults(surname));
        Template template = fm.getTemplate("details.html");
        template.process(model, response.getWriter());
      }
      else {
        Template template = fm.getTemplate("invalid.html");
        template.process(null, response.getWriter());
      }
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
    }
    catch (Exception error) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

 // private boolean authenticated(String username, String password) throws SQLException {
 //   try (PreparedStatement stmt = database.prepareStatement(AUTH_QUERY)) {
 //     stmt.setString(1, username);
 //     stmt.setString(2, password);
 //     ResultSet results = stmt.executeQuery();
 //     return results.next();
 //   }
 // }

// the updated authenticated function
private boolean authenticated(String username, String password) throws SQLException {
        // using PreparedStatement to stop sql injection
        try (PreparedStatement stmt = database.prepareStatement(AUTH_QUERY2)) {
            stmt.setString(1, username); 
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                String storedHashPassword = results.getString("password");
                return BCrypt.checkpw(password, storedHashPassword); 
            }
        }
        return false;
    }

// function to hash password using bcrypt
public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
}    

// added just for testing purposes, we use this fuction in the init() function to test if the hashing works
public void addAUser(String name, String username, String plainPassword) throws SQLException {
    String hashedPassword = hashPassword(plainPassword); //hashing the password
    String Query = "INSERT INTO user (name, username, password) VALUES (?, ?, ?)"; //making the insert query
    try (PreparedStatement stmt = database.prepareStatement(Query)) {
        // adding name, username and hased password to the insert query
        stmt.setString(1, name);
        stmt.setString(2, username);
        stmt.setString(3, hashedPassword);
        stmt.executeUpdate();
    }
}

private List<Record> searchResults(String surname) throws SQLException {
    List<Record> records = new ArrayList<>();
    try (PreparedStatement stmt = database.prepareStatement(SEARCH_QUERY)){
      stmt.setString(1, surname);
      ResultSet results = stmt.executeQuery();
      while (results.next()) {
        Record rec = new Record();
        rec.setSurname(results.getString(2));
        rec.setForename(results.getString(3));
        rec.setAddress(results.getString(4));
        rec.setDateOfBirth(results.getString(5));
        rec.setDoctorId(results.getString(6));
        rec.setDiagnosis(results.getString(7));
        records.add(rec);
      }
    }
    return records;
  }
}
