package edu.gcu.cst341.project;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserInterface {

	static Scanner sc = new Scanner(System.in);

	public static String [] login() {
		
		String [] login = new String[2];
		
		System.out.println("LOG-IN");
		System.out.println("==================");
		System.out.println("Enter username: ");
		login[0] = sc.nextLine();
		System.out.println("Enter password: ");
		login[1]= sc.nextLine();
		

		return login;
	}
	
	public static int menuMain() {
		int option = 0;
		do {
			System.out.println("Main MENU");
			System.out.println("==================");
			System.out.println("0] Exit");
			System.out.println("1] Shop");
			System.out.println("2] Admin");
			System.out.println("==================");
			option = sc.nextInt();
			sc.nextLine();
		} while (option < 0 || option > 2);
		return option;
	}

	public static int menuShop() {
		int option = 0;
		do {
			System.out.println("Shopping MENU");
			System.out.println("==================");
			System.out.println("0] Back to Main Menu");
			System.out.println("1] Add items to cart");
			System.out.println("2] See items in cart");
			System.out.println("3] Remove all items from cart");
			System.out.println("==================");
			option = sc.nextInt();
			sc.nextLine();
		} while (option < 0 || option > 3);
		return option;
	}
	
	public static int menuAdmin() {
		int option = 0;
		do {
			System.out.println("Administration MENU");
			System.out.println("==================");
			System.out.println("0] Back to Main Menu");
			System.out.println("1] Add product");
			System.out.println("2] See all products");
			System.out.println("3] Update a product");
			System.out.println("4] Remove a product");			
			System.out.println("==================");
			option = sc.nextInt();
			sc.nextLine();
		} while (option < 0 || option > 4);
		return option;
	}
	
	//Part of Update method
	//Austin Bartram
	public static boolean menuUpdate(){
		int option = 0;
		boolean opt = false;
		do {
			System.out.println("Restocked or Out of Stock?");
			System.out.println("==================");
			System.out.println("0] Out of Stock");
			System.out.println("1] Restocked");			
			System.out.println("==================");
			option = sc.nextInt();
			sc.nextLine();
		} while (option < 0 || option > 1);
			if (option == 0) {
				opt = false;
			}
			if(option == 1) {
				opt = true;
			}
		return opt;
	}

}
