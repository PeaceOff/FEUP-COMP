import java.util.LinkedList;

public class Element {
	
	public final String[] type_name = new String[]{"Undefined","Integer","Array","Function"};
	
	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_INT = 1;
	public static final int TYPE_ARRAY = 2;
	public static final int TYPE_FUNCTION = 3;
	
	private int type = 0;
	private String name;
	private boolean initialized = false;
	private LinkedList<Element> arguments = null;
	private Element _return;
	
	public Element(String name, int type){
		this.name = name;
		this.type = type;
		if(type == Element.TYPE_FUNCTION)
			arguments = new LinkedList<Element>();
	}
	
	public Element(String name, int type, boolean inialized){
		this.name = name;
		this.type = type;
		if(type == Element.TYPE_FUNCTION)
			arguments = new LinkedList<Element>();
		this.initialized = inialized;
	}
	
	public String get_type_string(){
		
		switch(this.type) {
		case TYPE_UNDEFINED:
			return "Undefined";
		case TYPE_INT:
			return "Integer";
		case TYPE_ARRAY:
			return "Array";
		case TYPE_FUNCTION:
			return "Function";
			default:
				return "No type";
			}
		
	}
	
	public boolean compare(Element e){
		
		if(this.type == e.type)
			return true;
		
		return false;
	}
	
	public Element getReturn(){
		return _return;
	}
	
	public void setReturn(Element e){
		_return = e;
	}
	public void addArgument(Element e){
		arguments.add(e);
	}
	
	public void addArguments(LinkedList<Element> e){
		arguments.addAll(e);
	}
	
	public LinkedList<Element> getArguments(){
		return arguments;
	}
	
	public void setInitialized(boolean v){
		initialized = v;
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public String getName(){
		return name;
	}
	
	public int getType(){
		return type;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ELEMENT ");
		sb.append("Name: ");
		sb.append(name);
		sb.append("\tType: ");
		sb.append(type_name[type]);
		sb.append("\t Status: ");
		sb.append((initialized)? "Initialized":"Not Initialized");
		sb.append(" ]");
		return sb.toString();
		
	}
	
}
