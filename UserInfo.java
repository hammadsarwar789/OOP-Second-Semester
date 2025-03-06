import java.util.Scanner;
public class UserInfo{
	
	static String Username="hammad@cuilahore.pk";
	static String Password="Pakistan Zindabad";

	public static void main(String arg[]){
		Scanner sc= new Scanner(System.in);
		System.out.println("Enetr UserName:");
		String username = sc.nextLine();
		System.out.println("Enetr Password:");
		String password = sc.nextLine();
        		if (Username.equals(username) & Password.equals(password)) {
            			System.out.println("Access granted!");
        		} 
			else {
           			 System.out.println("Access denied!");
        	}
	}
}