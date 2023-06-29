package com.anonymously;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetMessageServlet
 */
public class GetMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessageServlet() {
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
        int user_id = Integer.parseInt(request.getParameter("rid"));

        // Retrieve the list of messages for the user_id
        List<String> messages = getMessagesByUserId(user_id, conn);
        
        JsonObject jsonObject = new JsonObject();
        
        // Iterate over the list and add key-value pairs to the JSON object
        for (int i = 0; i < messages.size(); i++) {
            jsonObject.addProperty(String.valueOf(i), messages.get(i));
        }
        
        // Convert the JSON object to a JSON string
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        
        // Set response content type to JSON
        response.setContentType("application/json");
        
        // Send the JSON response
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
	}
	
	private List<String> getMessagesByUserId(int user_id, Connection conn) {
	    List<String> messages = new ArrayList<>();

	    try {
	        String sql = "SELECT message_text FROM messages WHERE user_id = ?";
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setInt(1, user_id);
	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            String message = resultSet.getString("message_text");
	            messages.add(message);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return messages;
	}

}
