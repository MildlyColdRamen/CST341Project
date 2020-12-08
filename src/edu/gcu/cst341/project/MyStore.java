package edu.gcu.cst341.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyStore {

	private String name; 
	private DBConnect con;

	MyStore (String name){
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
				}
				else {
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

		String [] login = UserInterface.login();

		String sql = "SELECT UserId, UserFirstName FROM users WHERE UserName = ? AND UserPassword = ? AND UserStatus = 'active'";

		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, login[0]);
			ps.setString(2, login[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("UserFirstName");
			}
			else {
				result = null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void shop() {
		switch (UserInterface.menuShop()) {
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
	}

	private void admin() {
		switch (UserInterface.menuAdmin()) {
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
			open();
		}
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

	private void createProduct() {
		System.out.println("Create product...");
		System.out.println();
	}

	private void readProducts() {
		System.out.println("View (Read) all products...");
		System.out.println();
		try(Statement stmt = con.getConnection().createStatement();){

			try (ResultSet rs = stmt.executeQuery("SELECT productId, productName, productPrice, stockStatus FROM cst341n.products;")){
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
	}

	private void updateProduct() {
		System.out.println("Update product...");
		System.out.println();
	}

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}} catch (SQLException e1) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}} catch (SQLException e1) {
				// TODO Auto-generated catch block
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
		System.out.println("Michelo");

	
	public void austin() {
		System.out.println("Austin");

	}
}

