package parkingsystem;

import java.sql.Connection;
import java.util.Scanner;

class Vehicle {
	private int vehicle_type;
	private String manufacturer, car_number;
	private int model_number;

	public void setVehicle_type(int vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public void setModel_number(int model_number) {
		this.model_number = model_number;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public int getVehicle_type() {
		return vehicle_type;
	}

	public int getModel_number() {
		return model_number;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getCar_number() {
		return car_number;
	}

}

class Owner {
	private String name;
	private String mobile_no;
	private int bank_balance;
	private int alloted_time;

	private Vehicle vehicle; //

	public void setName(String name) {
		this.name = name;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public void setBank_balance(int bank_balance) {
		this.bank_balance = bank_balance;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getName() {
		return name;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public int getBank_balance() {
		return bank_balance;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public int getAlloted_time() {
		return alloted_time;
	}

	public void setAlloted_time(int alloted_time) {
		this.alloted_time = alloted_time;
	}

}

public class ParkingSystem {

	public static void main(String[] args) {

		char parking_choice = ' '; // do you park or not.

		DBFunctions dbfunction = new DBFunctions();
		Scanner scan = new Scanner(System.in);
		System.out.println("--------------------: Welcome to Parking System Application :---------------");
		System.out.println("----------------------------------------------------------------------------");

		do {
			System.out.println("Press A for parking : ");
			System.out.println("Press B for Displacing : ");
			System.out.println("Press N for close application : ");
			System.out.println("Enter your choice : ");
			parking_choice = (new Scanner(System.in)).next().charAt(0); // take input only single character

			parking_choice = Character.toLowerCase(parking_choice);

			if (parking_choice == 'a') {

				if (!dbfunction.isSpace()) {
					System.out.println("There is no space are available for parking");
					System.out.println("<========================================================================>");
				} else {
					Owner owner = new Owner();
					Vehicle vehicle = new Vehicle();

					System.out.println("Enter your name : "); // owner name input
					owner.setName(scan.nextLine());

					System.out.println("Enter your Mobile no : "); // owner's mobile number input
					String mobile = scan.next();

					while (!Validation.isValidMobile(mobile)) {
						System.out.println("Please enter valid mobile number ===> ");
						mobile = scan.next();
					}

					// check for duplicate mobile number
					while (!dbfunction.isMobileAvailable(mobile)) {
						System.out.println(
								"Enter mobile number is already available....\nPlease Enter another mobile number...");
						mobile = scan.next();
					}
					owner.setMobile_no(mobile);

					System.out.println("Enter your bank balance : "); // owner mobile number input
					owner.setBank_balance(scan.nextInt());

					int type;

					// vehicle type input
					System.out.println("Enter your Vechile type \n '2' for two-wheeler \n '4' for four-wheeler : ");
					type = scan.nextInt();

					while (!Validation.isValidVehicleType(type)) {
						System.out.println("Only 2-wheeler and 4-wheeler are allowed.. \n Please try again..");
						type = scan.nextInt();
					}
					vehicle.setVehicle_type(type);

					// input car number
					String car_number;
					System.out.println("Enter car number : ");
					car_number = (new Scanner(System.in)).next();

					while (!Validation.isValidCarNumber(car_number)) {
						System.out.println("Car number must be 10 character : ");
						car_number = (new Scanner(System.in)).next();
					}
					vehicle.setCar_number(car_number);

					System.out.println("Enter modal number : "); // modal number input
					vehicle.setModel_number(scan.nextInt());

					System.out.println("Enter manufacturer name : "); // manufacturer name
					vehicle.setManufacturer((new Scanner(System.in)).nextLine());

					// input for alloted time
					System.out.println("How much hours you want to park ?");
					int time = scan.nextInt();
					while (!Validation.isValidAllotedTime(time)) {
						System.out.println("Only 12 hourse can be alloted for parking :-\n Please, input again :- ");
						time = scan.nextInt();
					}
					owner.setAlloted_time(time);

					owner.setVehicle(vehicle);

					if (dbfunction.park(owner)) {
						System.out.println("Your Ticket Generated Successfully..");
						System.out.println("At the time of Displacing you have to pay ammount..");
						System.out.println();
						System.out.println();
					} else {
						System.out.println("There are some technical problem...");
					}
				}

			} else if (parking_choice == 'b') {
				String mobile_no;

				System.out.println("Enter mobile number ==> ");
				mobile_no = (new Scanner(System.in)).next();

				dbfunction.displace(mobile_no);

			} else if (parking_choice == 'n') {
				System.exit(0);
			} else {
				System.out.println("Invalid choice ..\nPlease try again..");
			}

		} while (parking_choice != 'n');
	}

}
