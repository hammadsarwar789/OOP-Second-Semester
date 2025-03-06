public class ProductDetail{

	private static int productCounter=1;
	String id;
	private String name;
	private int quantity;
	private float price;
	private String catogry;
	ProductDetail(String name,int quantity, float price,String catogry){
		this.id=id;
		this.name=name;
		this.quantity=quantity;
		this.price=price;
		this.catogry=catogry;
		id="E"+String.format("%04d",productCounter++);
	}
	public void setId(String Id){
		id=Id;
	}
	
	public String getId(){
		return id;
	}
	public void setName(String Name){
		name=Name;
	}
	
	public String getName(){
		return name;
	}
	public void setQuantity(int Quantity){
		quantity=Quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setPrice(float Price){
		price=Price;
	}

	public float getPrice(){
		return price;
	}

	public void setCatogry(String Catogry){
		catogry=Catogry;
	}

	public String getCatogry(){
		return catogry;
	}

}