import java.util.LinkedList;

public class Element {
	
	private static final String[] type_name = new String[]{"Undefined","Integer","Array","Function"};
	
	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_INT = 1;
	public static final int TYPE_ARRAY = 2;
	public static final int TYPE_FUNCTION = 3;
	
	public static String getTypeName(int type){
		return type_name[type];
	}
	 
	private int type = 0;
	private String name;
	private Object value;
	private boolean initialized = false;
	private LinkedList<Element> arguments = null;
	private Element _return;
	private int jasIndex = -1;

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
	public Element(String name, int type, boolean inialized, Object value){
		this.name = name;
		this.type = type;
		if(type == Element.TYPE_FUNCTION)
			arguments = new LinkedList<Element>();
		this.initialized = inialized;
		this.value = value;
	}

	public void setJasIndex(int index){
		jasIndex = index;
	}

	public int getJasIndex(){
		return jasIndex;
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
	
	public void setType(int type){
		this.type = type;
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

	public Object getValue(){ return value; }

	public void setValue(Object value){ this.value = value;}

	public String jas_getType(){
		return (type == Element.TYPE_ARRAY)? "[I" : (type == TYPE_INT)? "I" : "_UNKNOWNTYPE_";
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ELEMENT");
		sb.append("{"); sb.append(jasIndex); sb.append("}");
		sb.append("Name: ");
		sb.append(name);
		sb.append("\tType: ");
		sb.append(type_name[type]);
		sb.append("\t Status: ");
		sb.append((initialized)? "Initialized":"Not Initialized");
		sb.append(" Value: ");
		sb.append((value == null)? "Null": (String)value);
		sb.append(" ]");
		return sb.toString();
		
	}
	
}
