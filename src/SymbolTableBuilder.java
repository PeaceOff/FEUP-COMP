import java.util.LinkedList;

import com.sun.glass.ui.CommonDialogs.Type;

import javafx.util.Pair;

public class SymbolTableBuilder implements Simple2Visitor {
	
	
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public Object visit(ASTStart node, Object data) {
		
		node.jjtGetChild(0).jjtAccept(this, data);
		
		return null;
	}

	public Object visit(ASTModule node, Object data) {
		
		String name = (String)node.jjtGetValue();
		SymbolTable.pushTable(new SymbolTable(name));
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return null;
	}

	public Object visit(ASTDeclaration node, Object data) {
		SymbolTable st = SymbolTable.getTable();
		
		int variableType = 0;
		boolean initialized = false;
		
		Element typeV = (Element)node.jjtGetChild(0).jjtAccept(this, data);
		Element typeD = null;
		if(node.jjtGetNumChildren() == 2){
			
			typeD = (Element)node.jjtGetChild(1).jjtAccept(this, data);
			
			if(typeV.getType() == Element.TYPE_INT && typeD.getType() == Element.TYPE_INT){
				variableType = typeD.getType();
				initialized = true;
				
			}else if(typeD.isInitialized()){
				
				if(typeD.getType() == Element.TYPE_ARRAY){
					variableType = Element.TYPE_ARRAY;
					initialized = true;
				}else{
					ErrorManager.addError(node.line,
					"Error Assigning Var:" + typeV.getName() + " Type Array Incompatible with type Int");
					//Error!
					//Types Array incompatible with type int;
				}
			}else{
				ErrorManager.addError(node.line,
						"Error Assigning Var:" + typeV.getName() + " Cannot Assign a variable to Undefined");
				//Error! Can't assign Undefined!
			}
		}else{
			variableType = typeV.getType();
		}
		
		Element e = null;
		
		e = new Element(typeV.getName(), variableType);
		e.setInitialized(initialized);
		
		st.addElement(e);
		
		return null;
	}

	public Object visit(ASTNumericDeclaration node, Object data) {
		return new Element("", Element.TYPE_INT);
		//return new Pair<Integer, Boolean>(Element.TYPE_INT,true);
	}

	public Object visit(ASTSign node, Object data) {
		return null;
	}

	public Object visit(ASTConstant node, Object data) {
		return new Element(null, Element.TYPE_INT, true);
	}

	public Object visit(ASTArrayDeclaration node, Object data){
		
		Element valid = (Element)node.jjtGetChild(0).jjtAccept(this, true);
		
		if(valid == null)
			return new Element("", Element.TYPE_ARRAY, false);
		
		return new Element("", Element.TYPE_ARRAY, valid.isInitialized());
	}

	public Object visit(ASTFunction node, Object data){
		String functionName = (String)node.jjtGetValue();
		Element _return = (Element)node.jjtGetChild(0).jjtAccept(this, data);
		
		LinkedList<Element> args = (LinkedList<Element>)node.jjtGetChild(1).jjtAccept(this, data);
		
		Element function = new Element((String)node.jjtGetValue(), Element.TYPE_FUNCTION);
		function.setReturn(_return);
		function.addArguments(args);
		function.setInitialized(true);

		SymbolTable currentST = SymbolTable.getTable();
		
		currentST.addElement(function);
		
		currentST.addChildTable(new SymbolTable(functionName, _return));
		
		SymbolTable.pushTable(currentST.getChildTable());
		currentST = SymbolTable.getTable();
		currentST.addParameters(args);
		
		
		node.jjtGetChild(2).jjtAccept(this, data);
		
		
		SymbolTable.popTable();
		return null;
	}

	public Object visit(ASTReturn node, Object data){
		
		String name = (String) node.value;
		
		if(node.jjtGetNumChildren()==1)
			return new Element(name, Element.TYPE_ARRAY);
		
		return new Element(name, (name==null)? Element.TYPE_UNDEFINED : Element.TYPE_INT);
	}

	public Object visit(ASTParameters node, Object data){
		
		LinkedList<Element> e = new LinkedList<Element>();
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			
			Element elmnt  = (Element) node.jjtGetChild(i).jjtAccept(this, data);
			elmnt.setInitialized(true);
			e.add(elmnt);
		}
		return e;
	}

	public Object visit(ASTVariable node, Object data){
		
		if(node.jjtGetNumChildren() > 0)
			return new Element((String)node.jjtGetValue(), Element.TYPE_ARRAY);
		
		return new Element((String)node.jjtGetValue(), Element.TYPE_INT);
	}

	public Object visit(ASTElementArray node, Object data){
		return null;
	}

	public Object visit(ASTStatements node, Object data){
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return null;
	}

	public Object visit(ASTAssign node, Object data){
		return null;
	}

	public Object visit(ASTCalcOP node, Object data){
		return null;
	}

	public Object visit(ASTAccess node, Object data){
		System.out.println("ACCESS");
		System.out.println("ID:" + node.getId() + " " + (String)node.jjtGetValue());
		SymbolTable st = SymbolTable.getTable();
		Element e = st.getElement((String)node.jjtGetValue());
		
		if(e == null){
			ErrorManager.addError(node.line,
					"Error Var:" + node.jjtGetValue() + " Not declared!!");
	
			return null;
		}
		
		if(node.jjtGetNumChildren() == 1 && e.getType() != Element.TYPE_ARRAY){
			ErrorManager.addError(node.line,
					"Error Var:" + node.jjtGetValue() + " Variable Isn't an array!");
		
			return null;
		}else if(node.jjtGetNumChildren()== 1){
			
			 Element child = (Element)node.jjtGetChild(0).jjtAccept(this, null);
			 if(!child.isInitialized()){
				 Element childTable = st.getElement(child.getName());
				 if(childTable == null){
						ErrorManager.addError(node.line,
								"Error Var:" + child.getName() + " Variable Not Declared!");
						return null;
				 }else if(!childTable.isInitialized()){
						ErrorManager.addError(node.line,
								"Error Var:" + child.getName() + " Variable Not Initialized!");
				 }
			 }
		}
		
		return e;
		

	}

	public Object visit(ASTTerm node, Object data){
		return null;
	}

	public Object visit(ASTInteger node, Object data){
		return null;
	}

	public Object visit(ASTCallName node, Object data){
		return null;
	}

	public Object visit(ASTSize node, Object data){
		return null;
	}

	public Object visit(ASTConditionOP node, Object data){
		
		Element lft  = (Element)node.jjtGetChild(0).jjtAccept(this, true);
		if(lft == null){
			ErrorManager.addError(node.line,
					"Left side var: Is Undefined!");
		}else if(!lft.isInitialized()){
			ErrorManager.addError(node.line,
					"Left side var:" + lft.getName() + " Variable Isn't Initialized!");
		}
		
		Pair<Integer, Boolean> pair = (Pair<Integer, Boolean>)node.jjtGetChild(1).jjtAccept(this, true);
		
		return null;
	}

	public Object visit(ASTWhile node, Object data){
		
		SymbolTable current = SymbolTable.getTable();
		
		node.jjtGetChild(0).jjtAccept(this, data);
		
		
		current.addChildTable(new SymbolTable(null));
		
		SymbolTable.pushTable(current.getChildTable());
		
		
		node.jjtGetChild(1).jjtAccept(this, data);
		
		
		SymbolTable.popTable();
		
		
		
		return null;
	}

	public Object visit(ASTIf node, Object data){
		return null;
	}

	public Object visit(ASTCall node, Object data){
		System.out.println("Call!" + node.jjtGetValue());
		
		return null;
	}

	public Object visit(ASTArgumentList node, Object data){
		return null;
	}

	public Object visit(ASTString node, Object data){
		return null;
	}

}
