package parkingsystem;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DBFunctions {
	private Connection link = null;
	
	public DBFunctions() {
		DBConnection db = DBConnection.getInstance(); // instance of dbconnection class		
		
		if(link==null) {
			link = db.getLink(); // get the connection object
		}else {
			System.out.println("failed due to technical problem");
		}
	}
	
	// method to store parking information
	public boolean park(Owner owner) {
		boolean status = false;
		
		boolean parking_status = true;
		/*
		 * Date date = new Date(); // get current date and time (by java.util.Date)
		 * java.sql.Date sqldate = new java.sql.Date(date.getDate()); // convert current
		 * date and time into sql format
		 */				
		
		String query = "insert into parking(uname, mobile_no, bank_balance, vehicle_type,modal_number, manufacturer, car_number, entry_datetime, alloted_time, status) values(?,?,?,?,?,?,?, CURRENT_TIMESTAMP,?,?)";
		
		try {
			PreparedStatement stmt = link.prepareStatement(query);
			stmt.setString(1, owner.getName());
			stmt.setString(2, owner.getMobile_no());
			stmt.setInt(3, owner.getBank_balance());			
			stmt.setInt(4, owner.getVehicle().getVehicle_type());
			stmt.setInt(5,owner.getVehicle().getModel_number());
			stmt.setString(6,owner.getVehicle().getManufacturer());
			stmt.setString(7, owner.getVehicle().getCar_number());
			stmt.setInt(8, owner.getAlloted_time());
			stmt.setBoolean(9, parking_status);
			
			int result = stmt.executeUpdate();
			
			if(result>0) {
				status = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	// check for mobile number duplication
	public boolean isMobileAvailable(String mobile) {
		String sql = "select count(*) from parking where mobile_no=? and status=?";
		boolean status = false;
		try {
			PreparedStatement stmt = link.prepareStatement(sql);
			stmt.setString(1, mobile);
			stmt.setBoolean(2, true);
			
			ResultSet result = stmt.executeQuery(); // execute sql query
			
			result.next(); // find 
			int count = result.getInt(1);
			
			if(count>0) {
				status = false;
			}else {
				status = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
	}
	
	// method for displacing vehicle
	public boolean displace(String mobile) {
		
		//String sql = "select bank_balance, vehicle_type,entry_datetime, alloted_time from parking where mobile_no='"+mobile+"'"+" and status=true";
		String sql = "select bank_balance , vehicle_type, alloted_time, hour(timediff( CURRENT_TIMESTAMP,entry_datetime)) as hours,  minute(timediff( CURRENT_TIMESTAMP,entry_datetime)) as minutes	from parking where mobile_no='"+mobile+"'"+" and status=true;";
				
		boolean status = false;
		
		try {
			
			Statement stmt = link.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			
			String entry_datetime;
			
						
			int duration;
			int alloted_time;
			int vehicle_type;
			double paid_amt = 0;
			double total_amt=0;
			double bank_balance;
			while(result.next()) {
				bank_balance = result.getDouble("bank_balance");
				int hours = result.getInt("hours");
				int minutes = result.getInt("minutes");
				
				duration = Math.round(hours + (minutes/60));
				alloted_time = result.getInt("alloted_time");
				vehicle_type = result.getInt("vehicle_type");
				if(vehicle_type==2) {
					if(duration<=alloted_time) {
						if(duration<=3) {
							paid_amt = (duration*20);
							
						}else if(duration>3 && duration<=8) {
							paid_amt = (3*20) + ((duration)-3)*35;
						}else if(duration>8 && (duration<=12)) {
							paid_amt = (3*20) + (5*35) + ((duration-8)*50);
						}else {
							paid_amt = (3*20) + (5*35) + (4*50) + (duration-12)*70;
						}
						
						total_amt = paid_amt;
					}else {
						if(duration<=3) {
							paid_amt = (duration*20);
						}else if(duration>3 && duration<=8) {
							paid_amt = (3*20) + ((duration)-3)*35;
						}else if(duration>8 && (duration<=12)) {
							paid_amt = (3*20) + (5*35) + ((duration-8)*50);
						}else {
							paid_amt = (3*20) + (5*35) + (4*50) + (duration-12)*70;
						}
						
						total_amt = paid_amt + paid_amt*0.15;
					}
				}
				else if(vehicle_type==4) {
					if(duration<=alloted_time) {
						if(duration<=3) {
							paid_amt = (duration*50);
							
						}else if(duration>3 && duration<=8) {
							paid_amt = (3*50) + ((duration)-3)*70;
						}else if(duration>8 && (duration<=12)) {
							paid_amt = (3*50) + (5*70) + ((duration-8)*100);
						}else {
							paid_amt = (3*50) + (5*70) + (4*100) + (duration-12)*120;
						}
						
						total_amt = paid_amt;
					}else {
						if(duration<=3) {
							paid_amt = (duration*50);
						}else if(duration>3 && duration<=8) {
							paid_amt = (3*50) + ((duration)-3)*70;
						}else if(duration>8 && (duration<=12)) {
							paid_amt = (3*50) + (5*70) + ((duration-8)*100);
						}else {
							paid_amt = (3*50) + (5*70) + (4*100) + (duration-12)*120;
						}						
						
						total_amt = paid_amt + paid_amt*0.3;
					}
				}
								
				if(bank_balance<total_amt) {
					System.out.println("You have to pay : "+total_amt);
					System.out.println("In your account there is no sufficient amount..\n Therefor Your vehicle consfiscated");					
				}else {
					System.out.println("You have to pay : "+total_amt);
					bank_balance = bank_balance-total_amt;
					
					String query = "update parking set bank_balance=?, exit_datetime=CURRENT_TIMESTAMP, paid_amt=?, status=? where mobile_no=?";
					
					PreparedStatement update = link.prepareStatement(query);
					update.setDouble(1, bank_balance);
					update.setDouble(2, total_amt);
					update.setBoolean(3, false);
					update.setString(4, mobile);
					
					int r = update.executeUpdate();
					
					if(r>0) {
						System.out.println();
						System.out.println("You have to pay : "+total_amt);
						System.out.println("Now, You can displace your Vehicle ");
						System.out.println("===================================================\n\n");
					}else {
						System.out.println("Something went wrong...");
					}
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	private Object SimpleDateFormat(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// check allocation for parking
	public boolean isSpace() {
		boolean status = true;
		String sql = "select count(*) from parking where status=true";
		
		try {
			Statement stmt = link.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			result.next();
			int res = result.getInt(1);
			
			if(res<40) { 
				status = true; 
			}else { 
				status=false; 
			}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
	}
}






















