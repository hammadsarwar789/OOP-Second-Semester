package main;

import util.PasswordValidator;
import users.UserManager;
import java.io.Console;
import java.util.Scanner;

public class LoginSystem {
	public static void main(String args[]) {
        	Console console = System.console();
        	Scanner sc = new Scanner(System.in);

        	System.out.println("Enter username: ");
        	String username = sc.nextLine();
        	System.out.println("Enter password: ");

        	String password;
        	if (console != null) {
            	char[] passwordArray = console.readPassword();
            	password = new String(passwordArray);
        	} else {
            	password = sc.nextLine();
        	
		}
		boolean isAuthenticated = UserManager.loginProcessor.authentic(username, password);

		if (isAuthenticated) {
		System.out.println("Access Granted!");
		} else {
		System.out.println("Access Denied!!");
		}



	}
}
