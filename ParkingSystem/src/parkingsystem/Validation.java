package parkingsystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	// mobile number validation
	public static boolean isValidMobile(String mobile) {
		
		Pattern ptr = Pattern.compile("[6-9]{1}[0-9]{9}");
		
		Matcher matcher = ptr.matcher(mobile);
		
		return (matcher.find() && matcher.group().equals(mobile));
	}
	
	// validation vehicle type
	public static boolean isValidVehicleType(int type) {
		
		if(type == 2 || type == 4) {
			return true;
		}else {
			return false;
		}
	}
	
	// validate car number
	public static boolean isValidCarNumber(String car) {
		if(car.length()>10) {
			return false;
		}else {
			Pattern ptr = Pattern.compile("[0-9a-zA-Z]{10}");
			
			Matcher matcher = ptr.matcher(car);
			
			return (matcher.find() && matcher.group().equals(car));
		}
	}
	
	// alloted time validation
	public static boolean isValidAllotedTime(int t) {
		if(t>12) {
			return false;
		}else {
			return true;
		}
	}
	
	
	
}
