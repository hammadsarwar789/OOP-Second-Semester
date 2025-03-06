public class TestProduct{

	public static void main(String arg[]){
		ProductDetail obj1= new ProductDetail("Apple",10,5,"Fruit");
		ProductDetail obj2= new ProductDetail("Banana",15,10,"Fruit");
		ProductDetail obj3= new ProductDetail("Orange",20,15,"Fruit");
		ProductDetail obj4= new ProductDetail("Berry",25,20,"Fruit");
		ProductDetail obj5= new ProductDetail("Mango",30,25,"Fruit");
		System.out.printf("%s\t%10s%10s%15s\t%15s\n\n","Id","Product Name","Quantity","Price","Catogry");

		printData(obj1);
		printData(obj2);
		printData(obj3);
		printData(obj4);
		printData(obj5);

	}
	public static void printData(ProductDetail p){
		
		System.out.printf("%s\t%10s\t%d\t\t%.2f\t\t%s\n",p.getId(),p.getName(),p.getQuantity(),p.getPrice(),p.getCatogry());
	}
}