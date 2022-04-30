package parkingsystem;

import java.sql.Connection;
import java.sql.DriverManager;

// database connectivity using singleton design pattern
public class DBConnection {
	
	private static Connection link = null; // to store link
	
	private static DBConnection db_instance = null;
	
	// private constructor 
	private DBConnection() {
		
	}
	// get instance
	public static DBConnection getInstance() {
		if(db_instance==null) {
			db_instance = new DBConnection();
		}		
		return db_instance;		
	}	
	
	public Connection getLink() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // load and register mysql driver
			
			// get connection object 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking_system","root","mysql");
			
			link = conn;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return link;
	}
}
