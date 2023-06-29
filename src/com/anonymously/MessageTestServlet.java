package com.anonymously;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

/**
 * Servlet implementation class MessageTestServlet
 */
public class MessageTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageTestServlet() {
        String url = "jdbc:postgresql://localhost:5432/anonymously_db";
        String username = "postgres";
        String password = "Balliya123@";
        
        
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("sucessfully connected to anonymously_db");
        } catch (Exception e) {
            System.out.println("Failed to create JDBC db connection " + e.toString() + e.getMessage());
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("o balle balle request aa gyi");
	    StringBuilder requestBody = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        requestBody.append(line);
	    }

	    // Parse the JSON data using Gson
	    Gson gson = new Gson();
	    MessageData messageData = gson.fromJson(requestBody.toString(), MessageData.class);
	    String message = messageData.getMessage();
	    String rid = messageData.getRid();
	    System.out.println(message);
	    System.out.println(rid);
	    
	    boolean isRidValid = false;
	    try {
	        isRidValid = isValidId(messageData.getRid());
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the case when a SQL exception occurs during validation
	        // ...
	    }
	    
	    if (isRidValid) {
	        // Proceed with inserting the message into the messages table
	        // Assuming you have established a database connection
	        try {
	            // Insert the message into the messages table
	            insertMessage(Integer.parseInt(rid),message);
	            // Construct the success JSON response
	            String jsonResponse = gson.toJson(new Response("success", "Message sent"));

	            // Set the response content type to JSON
	            response.setContentType("application/json");

	            // Write the JSON response to the response body
	            PrintWriter out = response.getWriter();
	            out.print(jsonResponse);
	            out.flush();
	        } catch (SQLException e) {
	            e.printStackTrace();

	            String jsonResponse = gson.toJson(new Response("failure", "Recipient not found"));

	            // Set the response content type to JSON
	            response.setContentType("application/json");

	            // Write the JSON response to the response body
	            PrintWriter out = response.getWriter();
	            out.print(jsonResponse);
	            out.flush();
	        }
	    } else {
	        // Handle the case when the rid parameter is invalid
	        // ...
	    	System.out.println("recipiant not found !!");
            String jsonResponse = gson.toJson(new Response("failure", "Recipient not found"));

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON response to the response body
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
	    }
	    
	}
	
	private boolean isValidId(String rid) throws SQLException {
	    boolean isValid = false;
	    // Assuming you have established a database connection
	    PreparedStatement statement = conn.prepareStatement("SELECT id FROM users WHERE id = ?");
	    statement.setInt(1, Integer.parseInt(rid));
	    ResultSet resultSet = statement.executeQuery();
	    if (resultSet.next()) {
	        // User ID exists in the users table
	        isValid = true;
	    }
	    resultSet.close();
	    statement.close();
	    return isValid;
	}
	
	private void insertMessage(int userId, String message) throws SQLException {
	    // Assuming you have established a database connection
	    PreparedStatement statement = conn.prepareStatement("INSERT INTO messages (user_id, message_text, created_at) VALUES (?, ?, NOW())");
	    statement.setInt(1, userId);
	    statement.setString(2, message);
	    int rowsInserted = statement.executeUpdate();
	    System.out.println("Rows inserted : "+rowsInserted);
	    statement.close();
	}
	
	
	class Response {
	    private String status;
	    private String log;

	    public Response(String status, String log) {
	        this.status = status;
	        this.log = log;
	    }

	    // Getters and setters
	}

}
