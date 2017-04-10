import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javafx.util.Pair;

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
	
	public Element getElement(String name){ //Check Parent
		if(_return != null)
			if(_return.equals(name))
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
	
	public void addParameter(Element e){
		parameters.put(e.getName(), e);
	}
	
	private String printMap (String name, HashMap<String, Element> map){
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getTab());
		sb.append("Group: ");
		sb.append(name);
		sb.append("\n");
		
		
		if(map == null)
			return sb.toString();
		
		
		for(Map.Entry<String, Element> entry : map.entrySet()){
			String identifier = entry.getKey();
			Element elmnt = entry.getValue();
			
			sb.append(Utils.getTab());
			sb.append("\t");
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
		
		
		sb.append((function)? "Function: " : "Module: ");
		sb.append(name);
		
		if(function){
			sb.append("\tReturn: ");
			sb.append((_return==null)? "void" : _return.toString());
			sb.append("\n");
			printMap("Parameters", parameters);
		}
		printMap("Locals", elements);
		Utils.tabIndex++;
		
		for(SymbolTable s : children){
			sb.append(s.toString());
		}
		
		Utils.tabIndex--;
		return sb.toString();
		
		
	}
	
	
	
}