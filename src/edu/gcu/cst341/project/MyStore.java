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
	// Removed unused variable productID...
	// Mike P - 12/11/20
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
	// Updated method to remove setting Product ID as
	// that field is autoIncrement now.
	// Mike P - 12/11/20
	private void createProduct() {
		System.out.println("Add/Create product...");

		// Added all logic in this method 30 Nov 20 - MP 
		boolean exit = false;
		boolean exit1 = false;
		
		int count = 0;
		int numOfDecimals = 0;
		int numOfSpaces = 0;
		
		String keyInput = "";
		String sql = "";
		
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
			System.out.println("Product Name: \t\t" + productName);
			System.out.println("Product Price: \t\t" + productPrice);
			
			if (productInStock) {
				System.out.println("Product Is In Stock: \tY");				
			} else {
				System.out.println("Product Is In Stock: \tN");
			}
			
			// Ask if they wish to add product....
			System.out.println("\nDo you wish to add the item above to the product database? Y or N");

			// Get input.....
			keyInput = sc.nextLine();
			
			// Make sure input is a Y or N only.....
			if (keyInput.equals("y") || keyInput.equals("Y")) {
				System.out.println("Updating database... Please wait....");

				// Generate SQl command using prompted input.....
				sql = "insert  into `products`(`productName`,`productPrice`,`stockStatus`) values \n" + 
						"(?, ?, ?);";
				
				// Add product to database.....well, try to
				// but since input was checked, then
				// everything should be ok, in theory...
				try (PreparedStatement ps = con.getConnection().prepareStatement(sql)) {
					ps.setString(1, productName);
					ps.setDouble(2, productPrice);
					ps.setBoolean(3, productInStock);
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
		// Modified print output formatting to remove extra line break...
		// Mike P - 12/11/20
		System.out.println("Here is a listing of all available products...");
		try(Statement stmt = con.getConnection().createStatement();){
			try (ResultSet rs = stmt.executeQuery("SELECT productId, productName, productPrice, stockStatus FROM `cst341project`.products;")){
				while(rs.next()) {
					System.out.println("ID: | " + rs.getInt("productId") + " | " + rs.getString("productName") +
							" | Price: " + rs.getBigDecimal("productPrice") + " | In Stock?: " + rs.getBoolean("stockStatus"));
				}
				System.out.println();
			} catch (SQLException e) {
				e.printStackTrace();
			}} catch (SQLException e1) {
				e1.printStackTrace();
			}
	}


	// Added to allow Admin to update the stock status of products
	// Austin Bartram  - 12/1/2020
	// Modified to call readProducts() code that shows product details
	// Mike P - 12/11/20
	private void updateProduct() {
		
		// Try to connect to database.....
		try(Statement stmt = con.getConnection().createStatement();){

			// Print listing of all Products
			readProducts();
	
			//Asking Which to update and stores value
			boolean inOrOut = UserInterface.menuUpdate();
			
			System.out.println("What Stock Status do you want to Update? (By ID Number)");
			
			int idNum = intCheck();
			
			//Prepared statment to use with admins value
			String updateSql = "UPDATE products SET stockStatus = ? WHERE productId = ?";
			try(PreparedStatement ps = con.getConnection().prepareStatement(updateSql)){
				ps.setBoolean(1, inOrOut);
				ps.setInt(2, idNum);
				ps.execute();
			}
			
			// Print listing of all Products
			readProducts();

			System.out.println("Status successfully updated, returning to Administration Menu...");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	// Removed couple of commented out lines...
	// NO changes made to source code..
	// Mike P - 12/11/20
	private void deleteProduct() {
		System.out.println("Delete product...");
		System.out.println("Showing all products...");

		try(Statement stmt = con.getConnection().createStatement();){

			try (ResultSet rs = stmt.executeQuery("SELECT productId, productName, productPrice, stockStatus FROM cst341n.products;")){
				while(rs.next()) {
					System.out.println("ID: |" + rs.getInt("productId") + "| " + rs.getString("productName") +
							"| Price: " + rs.getBigDecimal("productPrice") + "| In Stock?: " + rs.getBoolean("stockStatus"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}} catch (SQLException e1) {
				e1.printStackTrace();
			}
		System.out.println("Which product would you like to delete? (Select ID Number");
		int idNumber = UserInterface.sc.nextInt();
		UserInterface.sc.nextLine();
		String sql = "Delete From cst341n.products where productId = ?;";
		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setInt(1, idNumber);
			ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Deleted Select Product");
		try(Statement stmt = con.getConnection().createStatement();){

			try (ResultSet rs = stmt.executeQuery("SELECT productId, productName, productPrice, stockStatus FROM cst341n.products;")){
//				Remember to changed it to the name of your database for the group project
				while(rs.next()) {
					System.out.println("ID: |" + rs.getInt("productId") + "| " + rs.getString("productName") +
							"| Price: " + rs.getBigDecimal("productPrice") + "| In Stock?: " + rs.getBoolean("stockStatus"));
					System.out.println();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}} catch (SQLException e1) {
				e1.printStackTrace();
			}
		
	}
	//	add method to print out name
	//	Abraham 11/19/2020
    // Added to alllow...
	// Abraham Gamez - 12/07/2020
	private void abraham() {
		System.out.println("Abraham");
	}

	// added method to print out name
	// 19 Nov 20 - MP
	private void michelob() {
		System.out.println("Michelob");
	}
	
	//Added Method to print my name
	//11/19/2020 --Austin B
	public void austin() {
		System.out.println("Austin");
	}
	
	//Added Method for checking if input is an integer
	// MP - 12/11/20 modified to make sure its positive...
	// and put else condition to print try again...
	public int intCheck() {
		
		boolean inputAccepted = false;
		int idNum = 0;
		
        while (!inputAccepted) {
            try {
                System.out.print("Please enter an ID Number: ");
                
                idNum = Integer.valueOf(UserInterface.sc.nextLine());
                
                if(idNum != 0 && idNum > 0) {
                	inputAccepted = true;
                } else {
                    System.out.println("Not a valid number.  Please try again!");               	
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number: ");
            }
        }

        return idNum;
	}
}

//
// End
//