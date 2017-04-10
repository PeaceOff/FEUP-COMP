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
		
		Pair<String, Integer> typeV = (Pair<String, Integer>)node.jjtGetChild(0).jjtAccept(this, data);
		Pair<Integer, Boolean> typeD = null;
		if(node.jjtGetNumChildren() == 2){
			
			typeD = (Pair<Integer, Boolean>)node.jjtGetChild(1).jjtAccept(this, data);
			
			if(typeV.getValue() == Element.TYPE_INT && typeD.getKey() == Element.TYPE_INT){
				variableType = typeD.getKey();
				initialized = true;
				
			}else if(typeD.getValue()){
				
				if(typeD.getKey() == Element.TYPE_ARRAY){
					variableType = Element.TYPE_ARRAY;
					initialized = true;
				}else{
					ErrorManager.addError(node.line,
					"Error Assigning Var:" + typeV.getKey() + " Type Array Incompatible with type int");
					//Error!
					//Types Array incompatible with type int;
				}
			}else{
				ErrorManager.addError(node.line,
						"Error Assigning Var:" + typeV.getKey() + " Cannot Assign a variable to Undefined");
				//Error! Can't assign Undefined!
			}
		}else{
			variableType = typeV.getValue();
		}
		
		Element e = null;
		
		e = new Element(typeV.getKey(), variableType);
		e.setInitialized(initialized);
		
		st.addElement(e);
		
		return null;
	}

	public Object visit(ASTNumericDeclaration node, Object data) {
		return new Pair<Integer, Boolean>(Element.TYPE_INT,true);
	}

	public Object visit(ASTSign node, Object data) {
		return null;
	}

	public Object visit(ASTConstant node, Object data) {
		return true;
	}

	public Object visit(ASTArrayDeclaration node, Object data){
		
		boolean valid = (boolean)node.jjtGetChild(0).jjtAccept(this, data);
		
		return new Pair<Integer, Boolean>(Element.TYPE_ARRAY,valid);
	}

	public Object visit(ASTFunction node, Object data){
		return null;
	}

	public Object visit(ASTReturn node, Object data){
		return null;
	}

	public Object visit(ASTParameters node, Object data){
		return null;
	}

	public Object visit(ASTVariable node, Object data){
		
		if(node.jjtGetNumChildren() > 0)
			return new Pair<String,Integer>((String)node.jjtGetValue(), Element.TYPE_ARRAY);
		
		return new Pair<String,Integer>((String)node.jjtGetValue(), Element.TYPE_INT);
	}

	public Object visit(ASTElementArray node, Object data){
		return null;
	}

	public Object visit(ASTStatements node, Object data){
		return null;
	}

	public Object visit(ASTAssign node, Object data){
		return null;
	}

	public Object visit(ASTCalcOP node, Object data){
		return null;
	}

	public Object visit(ASTAccess node, Object data){
		
		SymbolTable st = SymbolTable.getTable();
		Element e = st.getElement((String)node.jjtGetValue());
		
		if(e == null){
			ErrorManager.addError(node.line,
					"Error Var:" + node.jjtGetValue() + " Not declared!!");
			return false;
		}
		
		if(node.jjtGetNumChildren() == 1 && e.getType() != Element.TYPE_ARRAY){
			ErrorManager.addError(node.line,
					"Error Var:" + node.jjtGetValue() + " Variable Isn't an array!");
			return false;
		}
		
		if(e.isInitialized())
			return true;
		
		ErrorManager.addError(node.line,
				"Error Var:" + node.jjtGetValue() + " Variable Isn't initialized!");
		return false;
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
		return null;
	}

	public Object visit(ASTWhile node, Object data){
		return null;
	}

	public Object visit(ASTIf node, Object data){
		return null;
	}

	public Object visit(ASTCall node, Object data){
		if(node.jjtGetNumChildren() == 1){
			String[] Elements = (String[])node.jjtGetChild(0).jjtAccept(this, null);
			SymbolTable.getTable().getElement((String)node.jjtGetValue());
		}
		return null;
	}

	public Object visit(ASTArgumentList node, Object data){
		return null;
	}

	public Object visit(ASTString node, Object data){
		return null;
	}

}
