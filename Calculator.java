import java.util.Scanner;
public class Calculator {

    public static void main(String args[]) {        
        System.out.printf("%07d\t%30s\t%15s\n", 1, "Muhammad Hammad Sarwar", "FA24-BCS-068");
	System.out.println("This is the Calculator");
	System.out.println("Press 1 To ADD (+)");
	System.out.println("Press 2 To Subtract (-)");
	System.out.println("Press 3 To multiply (*)");
	System.out.println("Press 4 To Divide (/)");
	System.out.println("Press 5 To Modulus (//)");
	System.out.println("Press 0 To Exit");
	Scanner input = new Scanner(System.in);	
	while(true){
		int num;
		System.out.println("\nPress the Number What You Want To Do");
		num=input.nextInt();
		if (num==0){
			System.out.println("Exit The Method");
			break;
			}
		System.out.println("Enter First Number : ");
		float num1=input.nextFloat();
		System.out.println("Enter Second Number : ");
		float num2=input.nextFloat();
		if (num==1){
			
			float sum= num1+num2;
			System.out.printf("Sum of numbers are %f",sum);
			}
		else if (num==2){
			
			float sub= num1-num2;
			System.out.printf("Sub of numbers are %f",sub);
			}
		else if (num==3){
			
			float mult= num1*num2;
			System.out.printf("Mult of numbers are %f",mult);
			}
		else if (num==4){
			
			if(num2<=0){
				System.out.printf("Invalid Denominator");
				continue;
				}
			float div= num1/num2;
			System.out.printf("Div of numbers are %f",div);
			}
		else if (num==5){
	
			if(num2<=0){
				System.out.printf("Modulus of numbers are %f",num1);
				continue;
				}
			float modu= num1%num2;
			System.out.printf("Modulus of numbers are %f",modu);
			}
		
		}
	
	}
}
