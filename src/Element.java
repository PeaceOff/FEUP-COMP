
public class Element {
	
	public final String[] type_name = new String[]{"Undefined","Integer","Array","Function"};
	
	public final int TYPE_UNDEFINED = 0;
	public final int TYPE_INT = 1;
	public final int TYPE_ARRAY = 2;
	public final int TYPE_FUNCTION = 3;
	
	private int type = 0;
	private String name;
	private int length = -1; //Not Declared Probably not necessary
		
	public void Element(String name, int type){
		this.name = name;
		this.type = type;
	}
	
	public void Element(String name, int type, int length){
		this.name = name;
		this.type = type;
		this.length = length;
	}
	
	public String getName(){
		return name;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getType(){
		return type;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("ELEMENT>");
		sb.append("Name: ");
		sb.append(name);
		sb.append("\tType: ");
		sb.append(type_name[type]);
		sb.append("\tLength: ");
		sb.append(length);
		return sb.toString();
		
	}
	
}
