package users;
import util.PasswordValidator;

public class UserManager {
	private static final String userName = "abc@gmail.com";
	private static final String passwords = "12345";

	PasswordValidator p = new PasswordValidator();

        public static class loginProcessor {
        public static boolean authentic(String userName, String password) {
		if (userName.equals(UserManager.userName) && password.equals(UserManager.passwords)) {
			return true;
		} else {
                	return false;
			}
		}
	}
}
