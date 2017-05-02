import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SymbolTable{
	
	private static LinkedList<SymbolTable> symbolTables = new LinkedList<SymbolTable>();
	public static SymbolTable popTable(){
		return symbolTables.removeFirst();
	} 
	
	public static SymbolTable getTable(){
		return symbolTables.getFirst();
	}
	
	public static void pushTable(SymbolTable table){
		symbolTables.push(table);
	}
	
	public static SymbolTable getRootTable(){ //Useful to check functions
		return symbolTables.getLast();
	}
	
	private String name; //ModuleName || FunctionName
	private boolean function = false;
	private HashMap<String, Element> elements = new HashMap<>();
	private HashMap<String, Element> parameters = null;
	private Element _return = null;
	private SymbolTable parent = null;
	
	
	private LinkedList<SymbolTable> children = new LinkedList<SymbolTable>();
	
	public SymbolTable(String name){
		this.name = name;
	}
	
	public SymbolTable(String name, Element _return){
		this.name = name;
		this.function = true;
		parameters = new HashMap<>();
		this._return = _return;
	}

	public LinkedList<Element> getElements(){
		return new LinkedList<>(elements.values());
	}

	public LinkedList<Element> getParameters(){
		return new LinkedList<>(parameters.values());
	}

	public String getName(){
		return name;
	}

	public Element getElement(String name){ //Check Parent
		if(_return != null)
			if(_return.getName() != null)
				if(_return.getName().equals(name))
					return _return;
		if(elements.containsKey(name)){
			return elements.get(name);
		}
		if(function)
			if(parameters.containsKey(name))
				return parameters.get(name);
		
		if(parent == null)
			return null;
		return parent.getElement(name);
	}
	
	public void setParent(SymbolTable s){
		this.parent = s;
	}
	
	public void addChildTable(SymbolTable e){
		e.setParent(this);
		children.addFirst(e);
	}
	
	public SymbolTable getChildTable(){
		SymbolTable s = children.pop();
		children.addLast(s);
		return s;
	}
	
	public void addElement(Element e){
		elements.put(e.getName(), e);
	}
	
	public void addParameters(LinkedList<Element> elements){
		for(Element e : elements)
			parameters.put(e.getName(), e);
	}
	
	public void addParameter(Element e){
		parameters.put(e.getName(), e);
	}
	
	private String printMap (String name, HashMap<String, Element> map){
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getTab());
		sb.append("|");
		sb.append(name);
		sb.append("\n");
		
		
		if(map == null)
			return sb.toString();
		
		
		for(Map.Entry<String, Element> entry : map.entrySet()){
			String identifier = entry.getKey();
			Element elmnt = entry.getValue();
			
			sb.append(Utils.getTab());
			sb.append("|\t");
			sb.append(identifier);
			sb.append(" > ");
			sb.append(elmnt);
			sb.append("\n");
		}

		return sb.toString();
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(Utils.getTab());
		
		if(name != null){
			sb.append((function)? "Function: " : "Module: ");
			sb.append(name);
		}else{
			sb.append("<->Scope<->\n");
		}
		
		Utils.tabIndex++;
		if(function){
			sb.append("\tReturn: ");
			sb.append((_return==null)? "void" : _return.toString());
			sb.append("\n");
			sb.append(printMap("Parameters", parameters));
		}else
			sb.append("\n");
		
		sb.append(printMap("Locals", elements));
		sb.append(Utils.getTab());
		sb.append("------------------------------------\n");
		
		
		for(SymbolTable s : children){
			sb.append(s.toString());
		}
		
		Utils.tabIndex--;
		return sb.toString();
		
		
	}

	public int startNumbering(){
		for (SymbolTable s : children) {
			s.startNumbering(0);
		}
		return 0;
	}

	public int getLevel(){
		if(parent == null)
			return 0;

		return 1 + parent.getLevel();
	}

	public int startNumbering(int n){

		if(getLevel() == 1) {
			LinkedList<Element> arguments = SymbolTable.getRootTable().getElement(name).getArguments();
			for (Element e : arguments) {
				e.setJasIndex(n++);
			}
		}

		for(Element e : elements.values()){
			e.setJasIndex(n++);
		}

		for(SymbolTable s: children) {
			n = s.startNumbering(n);
		}
		return n;
	}

	public String jas_getReturnType(){
		if(_return == null)
			return "V";
		else
			return _return.jas_getType();
	}
	
	
}