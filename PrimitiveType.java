public class PrimitiveType{
	public static void main(String type[]){
	byte b=1;
	short s=2;
	int i=4;
	long l=8000000;

	float f=4732402.00f;
	double d=8;
	
	boolean bool=true;
	char ch ='A';
	System.out.printf("%03d\t%-30s%.2f\n",1,"Muhammad Hammad Sarwar",3.66);
	System.out.printf("Value in byte :%d \n",b);
	System.out.printf("Value in short :%d \n",s);
	System.out.printf("Value in int :%d \n",i);
	System.out.printf("Value in long :%,d \n",l);
	System.out.printf("Value in float :%,.2f \n",f);
	System.out.printf("Value in doublebyte :%.4f \n",d);
	System.out.printf("Value in bool :%b \n",bool);
	System.out.printf("Value in byte :%c \n",ch);	
	}
}