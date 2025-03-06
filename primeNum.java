public class primeNum {

    public static void main(String args[]) {        
        System.out.printf("%07d\t%30s\t%15s\n", 1, "Muhammad Hammad Sarwar", "FA24-BCS-068");
        
        int n = 10;
        int num = 2;
        for (int count = 0; count < n; ) {
            if (isPrime(num)) {
                System.out.println(num);
                count++;
            }
            num++;
        }
    }
    static boolean isPrime(int num) {
        if (num <= 1) 
		return false;
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
