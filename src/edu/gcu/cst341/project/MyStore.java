package edu.gcu.cst341.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MyStore {

	private String name;
	private DBConnect con;
	
	// Added variables....
	// Mike P - 11/3/20
	private int productNumber = 0;
	private String productName = "";
	private double productPrice = 0.0;
	private boolean productInStock = false;
	
	// Create a Scanner to use for duh inputa......
	private static Scanner sc = new Scanner(System.in);


	MyStore(String name) {
		this.name = name;
		con = new DBConnect();
	}

	public void open() {
		String user = null;
		boolean exit = false;
		do {
			switch (UserInterface.menuMain()) {
			case 0:
				System.out.println("Thank you! Come again!");
				exit = true;
				break;
			case 1:
				user = login();
				if (user != null) {
					System.out.println("Login successful!!");
					shop();
				} else {
					System.out.println("Login unsuccessful");
				}
				break;
			case 2:
				admin();
				break;
			default:
				open();
			}
		} while (!exit);
	}

	private String login() {
		String result = null;

		String[] login = UserInterface.login();

		String sql = "SELECT UserId, UserFirstName FROM users WHERE UserName = ? AND UserPassword = ? AND UserStatus = 1";

		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)) {
			ps.setString(1, login[0]);
			ps.setString(2, login[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("UserFirstName");
			} else {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void shop() {

		// Made this loop to display better
		// Mike P - 11/3/20
		int getMenuOption = -1;

		do {

			getMenuOption = UserInterface.menuShop();

			switch (getMenuOption) {
			case 0:
				return;
			case 1:
				createCartItem();
				break;
			case 2:
				readCartItems();
				break;
			case 3:
				deleteCartItem();
				break;
			default:
				return;
			}
		} while (getMenuOption != 0);
	}

	private void admin() {

		// Made this loop to display better
		// Mike P - 11/3/20
		int getMenuOption = -1;

		do {

			getMenuOption = UserInterface.menuAdmin();

			switch (getMenuOption) {
			case 0:
				return;
			case 1:
				createProduct();
				break;
			case 2:
				readProducts();
				break;
			case 3:
				updateProduct();
				break;
			case 4:
				deleteProduct();
				break;
			default:
				admin();
			}

		} while (getMenuOption != 0);
	}

	private void createCartItem() {
		System.out.println("Add (Create) item to cart...");
		System.out.println();
	}

	private void readCartItems() {
		System.out.println("View (Read) cart...");
		System.out.println();
	}


	private void deleteCartItem() {
		System.out.println("Delete from cart...");
		System.out.println();
	}

	// added to createProduct
	// Mike P - 11/3/20
	private void createProduct() {
		System.out.println("Add/Create product...");

		// Added all logic in this method 30 Nov 20 - MP 
		boolean exit = false;
		boolean exit1 = false;
		
		int count = 0;
		int numOfDecimals = 0;
		int numOfSpaces = 0;
		int option = -1;
		
		String keyInput = "";
		String sql = "";

		/* ***************************************************
		 * This section is responsible for prompting the user for a valid Product Id.
		 * A valid Product Id is a positive integer ...  We then test to see ifin the 
		 * Product Id already exists and act accordingly...* */
		do {
			// Get product ID and check ifin one already exists...
			// Loop until they get a good one...
			// Make sure they enter a positive Int
			do {
				System.out.println("Enter new Product ID (positive numbers only please)");
				
				// Get input.....
				keyInput = sc.nextLine();
				
				// Test for valid input.....  Needin an INT here
				if (keyInput == null) {
					exit1 = false;
					System.out.println("Input Error, Product Id must be a number... Please try again..");
				}
				try {
					option = Integer.parseInt(keyInput);

					// Make sure itza positive, 
					if (option <= 0) {
						exit1 = false;
						System.out.println("Input Error, Product Id must be Positive Please try again..");
					} else {
						exit1 = true;
					}
				} catch (NumberFormatException nfe) {
					exit1 = false;
					System.out.println("Input Error, Product Id must be a number... Please try again..");
				}
			} while (exit1 == false);
			
			// Reset flag for when it is used next...
			exit1 = false;

			// See ifin Product ID already exists...
			if (option > 0) {
				sql = "SELECT productId FROM products " + 
						"WHERE products.productId = ?;";
				
				// Set up and run SQL statement....
				try (PreparedStatement ps = con.getConnection().prepareStatement(sql)) {
					ps.setInt(1, option);
					ResultSet rs = ps.executeQuery();

					//Get results and test.
					if (rs.next()) {
						sql = "SELECT productId FROM cst341project.products ORDER BY productId DESC;";
						
						try (PreparedStatement ps1 = con.getConnection().prepareStatement(sql)) {
							ResultSet rs1 = ps1.executeQuery();
							if (rs1.next()) {
								productNumber = rs1.getInt("productId");
								productNumber += 1;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}							
						System.out.println("Product ID already exists...  Next available number is: " + productNumber);
					} else {
						productNumber = option;
						exit = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}							
			} else {
				System.out.println("InValid Input, Please try again..");
			}
	
		} while (exit == false);
		
		// Reset flag for next loopie..
		// Get Product Name and check for length
		exit = false;
		
		/* ***************************************************
		 * This section is responsible for prompting the user for a valid Product Name.
		 * A valid Product Name is any string that has less than 51 characters...* */
		do {
			System.out.println("Enter new Product Name (maximum of 50 characters)");
			keyInput = sc.nextLine();
			
			// Check input to ensure there are less than 51 characters.....
			for (int i = 0; i <keyInput.length(); i++) {
				if (keyInput.charAt(i) != ' ') {
					count++;
				} else {
					numOfSpaces++;
				}
			}

			// Add letters and spaces..... not living.....
			count = count + numOfSpaces;				
			
			// Are there less than 51 characters in Product Name???
			if (count <= 50) {				// Yes, accept input
				productName = keyInput;
				exit = true;
			} else {						// To many chars... ask agian..
				count =  0;					// Reset variablez.....
				numOfSpaces = 0;
				System.out.println("Product Name must be less than 50 characters, Please try again");
			}
		} while (exit == false);
		
		// Reset flag for next loopie..
		// Get Product Price
		exit = false;
		
		/* ***************************************************
		 * This section is responsible for prompting the user for a valid 
		 * Product Price.  A valid Product Price is a number ...  We then 
		 * test to see ifin the number is a positive double value with 
		 * one decimal point, then act accordingly...*/
		do {
			// Prompt for User input....
		        do {
					System.out.println("Enter new Product Price (one decimal point only and no Dollar sign)");
					keyInput = sc.nextLine();
					
					// Test for valid input... looking for a double here..
					 if (keyInput == null) {		// They hit a return only....
			                exit1 = false;
			                System.out.println("Input Error, Product Price must be a number...  Please try again..");
					    }
					    try {
					    	productPrice = Double.parseDouble(keyInput);
					    	
					    	// Make sure itza positive, number
					    	if (productPrice <= 0) {
				                exit1 = false;
				                System.out.println("Input Error, Product Price must be a positive number... Please try again..");					    		
					    	} else {
						    	exit1 = true;		// Number is a positive value
					    	}
					    } catch (NumberFormatException nfe) {
			                exit1 = false;
			                System.out.println("Input Error, Product Price must be a number... Please try again..");
					    }
		        } while (exit1 == false);

			// Check input to ensure there iz one decimal point.....
			for (int i = 0; i <keyInput.length(); i++) {
				if (keyInput.charAt(i) == '.') {
					numOfDecimals++;
				}
			}
			
			// Make sure they entered ONE AND only one decimal point....
			if (numOfDecimals == 1) {
				exit = true;
				productPrice = Double.parseDouble(keyInput);
			} else {
				numOfDecimals = 0;
				exit1 = false;
				System.out.println("There was a general input error, Please try again.");
			}
			
		} while (exit == false);
		
		// Reset flag for next loopie..
		// Iz Product In Stock????
		exit = false;
		
		/* ***************************************************
		 * This section is responsible for prompting the user if the
		 * Product is in stock....*/
		do {
			System.out.println("Is Product in Stock Y or N?");
			keyInput = sc.nextLine();
			
			if (keyInput.equals("y") || keyInput.equals("Y")) {
				productInStock = true;
				exit = true;
			} else if (keyInput.equals("n") || keyInput.equals("N")) {
				exit = true;
			} else {
				System.out.println("There was an input error, Please try again");
			}
		} while (exit == false);
		
		exit = false;
		
		/* ***************************************************
		 * This section prints out the Product information and then
		 * asks the user if they  wish to add the Product into the 
		 * database.....*/
		do {
			System.out.println("Product ID: \t\t" + productNumber);
			System.out.println("Product Name: \t\t" + productName);
			System.out.println("Product Price: \t\t" + productPrice);
			if (productInStock) {
				System.out.println("Product Is In Stock: \tY");				
			} else {
				System.out.println("Product Is In Stock: \tN");
			}
			
			System.out.println("\nDo you wish to add the item above to the product database? Y or N");

			// Get input.....
			keyInput = sc.nextLine();
			
			// Make sure input is a Y or N only.....
			if (keyInput.equals("y") || keyInput.equals("Y")) {
				System.out.println("Updating database... Please wait....");

				// Generate SQl command using prompted input.....
				sql = "insert  into `products`(`productId`,`productName`,`productPrice`,`stockStatus`) values \n" + 
						"(?, ?, ?, ?);";
				
				// Add product to database.....well, try to
				// but since input was checked, then
				// everything should be ok, in theory...
				try (PreparedStatement ps = con.getConnection().prepareStatement(sql)) {
					ps.setInt(1, productNumber);
					ps.setString(2, productName);
					ps.setDouble(3, productPrice);
					ps.setBoolean(4, productInStock);
					ps.execute();			
				} catch (Exception e) {
					e.printStackTrace();
				}			
				exit = true;
			} else if (keyInput.equals("n") || keyInput.equals("N")) {
				System.out.println("No product was added to the product database....\n");
				exit = true;
			} else {
				System.out.println("There was an input error, Please try again");
			}
		
		} while (exit == false);
	}  // End createProduct()

	private void readProducts() {
		System.out.println("View (Read) all products...");
		try(Statement stmt = con.getConnection().createStatement();){
			try (ResultSet rs = stmt.executeQuery("SELECT productId, productName, productPrice, stockStatus FROM `cst341project`.products;")){
				while(rs.next()) {
					System.out.println("ID: |" + rs.getInt("productId") + "| " + rs.getString("productName") +
							"| Price: " + rs.getBigDecimal("productPrice") + "| In Stock?: " + rs.getBoolean("stockStatus"));
					System.out.println();
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//		System.out.println("View (Read) all products...");
//		System.out.println();
	}

	private void updateProduct() {
		System.out.println("Update product...");
		System.out.println();
	}

	private void deleteProduct() {
		System.out.println("Delete product...");
		System.out.println();
		//-- DELETE FROM products WHERE productId = 120;
	}

	// added method to print out name
	// 19 Nov 20 - MP
	private void michelob() {
		System.out.println("Michelob");
	}
	
		public void austin() {
			System.out.println("Austin");

		}
}
