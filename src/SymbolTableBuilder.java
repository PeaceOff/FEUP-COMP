import java.util.LinkedList;

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

					node.arrayAssign = true;

				}
			}else{
				ErrorManager.addError(node.line,
						"Error Assigning Var:" + typeD.getName() + " Not Initialized!");
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

		if(typeD != null){
			e.setValue(typeD.getValue());
		}
		st.addElement(e);

		return null;
	}

	public Object visit(ASTNumericDeclaration node, Object data) {

	    Element last;
		last = (Element)node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this,null);

		if(node.jjtGetNumChildren() == 2) {
			last.setValue("-" + last.getValue());
		}

		return new Element("", Element.TYPE_INT, true, last.getValue());

	}

	public Object visit(ASTSign node, Object data) {
		return null;
	}

	public Object visit(ASTConstant node, Object data) {
		return new Element("", Element.TYPE_INT, true, node.value);
	}

	public Object visit(ASTArrayDeclaration node, Object data){

		Element valid = (Element)node.jjtGetChild(0).jjtAccept(this, true);

		if(valid == null)
			return new Element(valid.getName(), Element.TYPE_ARRAY, false);

		return new Element(valid.getName(), Element.TYPE_ARRAY, valid.isInitialized());
	}

	public Object visit(ASTFunction node, Object data){
		String functionName = (String)node.jjtGetValue();
		Element _return = (Element)node.jjtGetChild(0).jjtAccept(this, data);
		LinkedList<Element> args = (LinkedList<Element>)node.jjtGetChild(1).jjtAccept(this, data);

		Element function = new Element((String)node.jjtGetValue(), Element.TYPE_FUNCTION);
		function.setReturn(_return);
		function.addArguments(args);
		for(Element e : args){
			if(e.getName().equals(_return.getName())){
				_return.setInitialized(true);
			}
		}
		function.setInitialized(true);

		SymbolTable currentST = SymbolTable.getTable();

		currentST.addElement(function);

		currentST.addChildTable(new SymbolTable(functionName, _return));

		SymbolTable.pushTable(currentST.getChildTable());
		currentST = SymbolTable.getTable();
		currentST.addParameters(args);


		node.jjtGetChild(2).jjtAccept(this, data);

		if(!_return.isInitialized() && _return.getType() != Element.TYPE_UNDEFINED){
			ErrorManager.addError(node.line, "Function " + functionName + " Must return a value!");
		}


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

		Element e = (Element)node.jjtGetChild(0).jjtAccept(this, data);

		if(e == null){
			ErrorManager.addError(node.line,
					"Left side var: Is Undefined!");
			return null;
		}

		Element e2 = (Element)node.jjtGetChild(1).jjtAccept(this, data);
		if(e2 == null){
			ErrorManager.addError(node.line,
					"Right side var: Is Undefined!");
			return null;
		}
		if(e.getType() == Element.TYPE_UNDEFINED && !e.isInitialized()){

			e.setType(e2.getType());
			SymbolTable.getTable().addElement(e);

		}else if(e.getType() == Element.TYPE_FUNCTION){

			e = new Element(e.getName(), e2.getType());
			SymbolTable.getTable().addElement(e);
		}

		if(!e2.isInitialized()){
			ErrorManager.addError(node.line,
					"Error Var:"+ e.getName() + " Cannot Assign a variable to Undefined");
		}else if(e2.getType() == Element.TYPE_UNDEFINED || e.getType() == Element.TYPE_UNDEFINED){

			if(e.getType() == Element.TYPE_UNDEFINED)
				e.setType(e2.getType());


			e.setInitialized(e2.isInitialized());

		}else if(e2.getType() == e.getType()){
			e.setInitialized(e2.isInitialized());
		}else if(e2.getType() == Element.TYPE_INT && e.getType() == Element.TYPE_ARRAY){
			e.setInitialized(e2.isInitialized());
			node.arrayAssign = true;
		}else{
			ErrorManager.addError(node.line,
					"Right side var of type" + Element.getTypeName(e.getType()) + " Incompatible with " + Element.getTypeName(e2.getType()));
		}

		return null;
	}

	public Object visit(ASTCalcOP node, Object data){

		Element lft = (Element)node.jjtGetChild(0).jjtAccept(this, data);
		Element rght = (Element)node.jjtGetChild(1).jjtAccept(this, data);


		if(!lft.isInitialized()){

			if(SymbolTable.getTable().getElement(lft.getName()) == null)
				ErrorManager.addError(node.line,
						"Left Side Var:" + lft.getName() + " Is Undefined");
			else
				ErrorManager.addError(node.line,
						"Left Side Var:" + lft.getName() + " Not Initialized");

		}

		if(!rght.isInitialized()){

			if(SymbolTable.getTable().getElement(rght.getName()) == null)
				ErrorManager.addError(node.line,
						"Right Side Var:" + rght.getName() + " Is Undefined");
			else
				ErrorManager.addError(node.line,
						"Right Side Var:" + rght.getName() + " Not Initialized");

		}

		if(lft.getType() == Element.TYPE_UNDEFINED || rght.getType() == Element.TYPE_UNDEFINED){
			return new Element("",Element.TYPE_UNDEFINED,true);
		}
		if(lft.getType() == rght.getType())
			return new Element("",lft.getType(),true);

		ErrorManager.addError(node.line,
				"Right side var of type" + Element.getTypeName(lft.getType()) + " Incompatible with " + Element.getTypeName(rght.getType()));

		return new Element("",Element.TYPE_UNDEFINED);
	}

	public Object visit(ASTAccess node, Object data){
		SymbolTable st = SymbolTable.getTable();
		Element e = st.getElement((String)node.jjtGetValue());

		if(e == null){
			return new Element((String)node.jjtGetValue(), Element.TYPE_UNDEFINED);
		}

		if(node.jjtGetNumChildren() == 1 && e.getType() != Element.TYPE_ARRAY && e.getType() != Element.TYPE_UNDEFINED){
			ErrorManager.addError(node.line,
					"Error Var:" + node.jjtGetValue() + " Variable Isn't an array!");

			return null;
		}else if(node.jjtGetNumChildren()== 1){

			 Element child = (Element)node.jjtGetChild(0).jjtAccept(this, null);

			 if(!child.isInitialized()){
				 Element childTable = st.getElement(child.getName());
				 if(childTable == null){
						ErrorManager.addError(node.line,
								"Error Var:" + child.getName() + " not Declared!");
						return null;
				 }else if(!childTable.isInitialized()){
						ErrorManager.addError(node.line,
								"Error Var:" + child.getName() + " Variable Not Initialized!");
						return null;
				 }
				 return childTable;
			 }
			 return child;
		}

		return e;


	}

	public Object visit(ASTTerm node, Object data){

		return (Element)node.jjtGetChild(node.jjtGetNumChildren()-1).jjtAccept(this, data);

	}

	public Object visit(ASTInteger node, Object data){
		return new Element("", Element.TYPE_INT, true, node.value);
	}

	public Object visit(ASTCallName node, Object data){
		return null;
	}

	public Object visit(ASTSize node, Object data){
		return new Element("", Element.TYPE_INT ,true);
	}

	public Object visit(ASTConditionOP node, Object data){

		Element lft  = (Element)node.jjtGetChild(0).jjtAccept(this, true);
		if(lft == null){
			ErrorManager.addError(node.line,
					"Left side var: Is Undefined!");
			return null;
		}else if(!lft.isInitialized()){
			ErrorManager.addError(node.line,
					"Left side var:" + lft.getName() + " Isn't Initialized!");
		}


		Element rght = (Element)node.jjtGetChild(1).jjtAccept(this, true);
		if(rght == null){
			ErrorManager.addError(node.line,
					"Right side var: Is Undefined!");
			return null;
		}
		if(!rght.isInitialized()){
			ErrorManager.addError(node.line,
					"Right side var:" + rght.getName() + " Isn't Initialized!");
		}else if(rght.getType() != Element.TYPE_UNDEFINED){
			if(lft.getType() != rght.getType())
				ErrorManager.addError(node.line,
						"Right side var of type" + Element.getTypeName(lft.getType()) + " Incompatible with " + Element.getTypeName(rght.getType()));
		}

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

		SymbolTable current = SymbolTable.getTable();

		node.jjtGetChild(0).jjtAccept(this, data);

		current.addChildTable(new SymbolTable(null));

		SymbolTable.pushTable(current.getChildTable());

		node.jjtGetChild(1).jjtAccept(this, data);

		SymbolTable.popTable();


		if(node.jjtGetNumChildren() == 3){
			current.addChildTable(new SymbolTable(null));
			SymbolTable.pushTable(current.getChildTable());

			node.jjtGetChild(2).jjtAccept(this, data);

			SymbolTable.popTable();
		}

		return null;
	}

	public Object visit(ASTCall node, Object data){

		return new Element("",Element.TYPE_UNDEFINED, true);
	}

	public Object visit(ASTArgumentList node, Object data){
		return null;
	}

	public Object visit(ASTString node, Object data){
		return null;
	}

}
