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
import java.sql.Statement;

import com.google.gson.Gson;

/**
 * Servlet implementation class ValidationServlet
 */
public class ValidationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValidationServlet() {
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
		// TODO Auto-generated method stub
		System.out.println("o balle balle request accet ho gyi");
	       // Read the request body
        BufferedReader reader = request.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        
        // Parse the JSON data
        Gson gson = new Gson();
        UserData userData = gson.fromJson(requestBody.toString(), UserData.class);

        String usr = userData.getUsername();
        String pass = userData.getPassword();
        String validation = userData.getValidationType();
        
        int userId = getUserId(usr, pass, conn);
        
        if (userId != -1) {
        	
        	if (validation.equalsIgnoreCase("login")) {
        		// return response, login success
        		sendResponse(userId, "success", "login successfull", response);
        	}else {
        		// return response, usr exist do login
        		sendResponse(userId, "failure", "already user exists", response);
        	}
            
        } else {
        	
        	if (validation.equalsIgnoreCase("login")) {
        		// return response, not exist do signup
        		sendResponse(userId, "failure", "user does not exist", response);
        	}else {
        		
        		userId = createUser(usr, pass, conn);
        		// do write usr, return response user signed up
        		sendResponse(userId, "success", "signup successfull", response);
        	}

        }

	}
	
	public int createUser(String username, String password, Connection conn) {
	    try {
	        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
	        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        statement.setString(1, username);
	        statement.setString(2, password);
	        int affectedRows = statement.executeUpdate();

	        if (affectedRows > 0) {
	            ResultSet generatedKeys = statement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1); // Returns the ID of the newly inserted user
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1; // Returns -1 if the user creation failed or if there is an error
	}

	public int getUserId(String username, String password, Connection conn) {
		// what you are doing is checking for both username and password but haven't
		// handled username password mismatch in login case (forgot password)
	    try {
	        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setString(1, username);
	        statement.setString(2, password);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("id"); // Returns the user ID if a matching entry is found
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1; // Returns -1 if the user doesn't exist or if there is an error
	}
	

	public void sendResponse(int userId, String status, String log, HttpServletResponse response) throws IOException{
	    ResponseData responseData = new ResponseData(userId, status, log);
	
	    Gson gson = new Gson();
	    String jsonResponse = gson.toJson(responseData);
	
	    // Here, you can send the JSON response as needed.
	    // For example, you can send it as an HTTP response in a web application or write it to a file.

        response.setContentType("application/json");

        // Write the JSON response to the response body
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
	    // Example: Print the JSON response
	    System.out.println(jsonResponse);
	}
	
	class ResponseData {
	    private int userId;
	    private String status;
	    private String log;

	    public ResponseData(int userId, String status, String log) {
	        this.userId = userId;
	        this.status = status;
	        this.log = log;
	    }

	    // Getters and setters (if needed) for the ResponseData class
	}

}
