public class AssignChecker implements Simple2Visitor {

  public Object visit(SimpleNode node, Object data){
    return null;
  }

  public Object visit(ASTStart node, Object data){

	node.jjtGetChild(0).jjtAccept(this, data);

    return null;
  }

  public Object visit(ASTModule node, Object data){


	for(int i = 0; i< node.jjtGetNumChildren(); i++){
		node.jjtGetChild(i).jjtAccept(this, data);
	}

    return null;
  }

  public Object visit(ASTDeclaration node, Object data){

	  return null;
  }

  public Object visit(ASTNumericDeclaration node, Object data){

	  return null;
  }

  public Object visit(ASTSign node, Object data){
    return null;
  }

  public Object visit(ASTConstant node, Object data){

	  return new Element("", Element.TYPE_INT);
  }

  public Object visit(ASTArrayDeclaration node, Object data){

	  return new Element("", Element.TYPE_ARRAY);
  }

  public Object visit(ASTFunction node, Object data){

	    SymbolTable current = SymbolTable.getTable();

	    SymbolTable.pushTable(current.getChildTable());

	    node.jjtGetChild(2).jjtAccept(this, data);

	    SymbolTable.popTable();

    return null;
  }

  public Object visit(ASTReturn node, Object data){
    return null;
  }

  public Object visit(ASTParameters node, Object data){
    return null;
  }

  public Object visit(ASTVariable node, Object data){

	if(node.jjtGetNumChildren() == 1){
		ErrorManager.addError(node.line, "cannot declare an array while accessing another");
	}

    return new Element("", Element.TYPE_INT);
  }

  public Object visit(ASTElementArray node, Object data){

	 return new Element("", Element.TYPE_ARRAY);
  }

  public Object visit(ASTStatements node, Object data){

	for(int i = 0; i < node.jjtGetNumChildren(); i++){
		node.jjtGetChild(i).jjtAccept(this, data);
	}
    return null;
  }

  public Object visit(ASTAssign node, Object data){

	  Element left = (Element)node.jjtGetChild(0).jjtAccept(this, data);

	  Element right = (Element)node.jjtGetChild(1).jjtAccept(this, data);

	  if(left.getType() == Element.TYPE_UNDEFINED){
		  return null;
	  }


	  if(left.getName().equals("readonly")){
		  ErrorManager.addError(node.line, "cannot assign a lenght of an array");
	  }


	  if(right.getType() == Element.TYPE_UNDEFINED){
		  return null;
	  }

	  if(left.getType() == Element.TYPE_ARRAY && right.getType() == Element.TYPE_INT){
		  return null;
	  }

	  if(left.getType() != right.getType()){
		  ErrorManager.addError(node.line,"cannot assign a variable with different type");
	  }

	  return null;
  }

  public Object visit(ASTCalcOP node, Object data){

	  Element el1 = (Element)node.jjtGetChild(0).jjtAccept(this, data);
	  Element el2 = (Element)node.jjtGetChild(1).jjtAccept(this, data);

	 if(el1.getType() == Element.TYPE_INT && el2.getType() == Element.TYPE_INT){
		 return new Element("", Element.TYPE_INT);
	 }else if(el1.getType() == Element.TYPE_ARRAY || el2.getType() == Element.TYPE_ARRAY){
		 ErrorManager.addError(node.line, "cannot make operations with different types");
	 }
	 return new Element("", Element.TYPE_UNDEFINED);
  }

  public Object visit(ASTAccess node, Object data){


	  if(node.jjtGetNumChildren() == 0){
		 return SymbolTable.getTable().getElement((String)node.jjtGetValue());
	  }else{

		return node.jjtGetChild(0).jjtAccept(this, data);
	  }

  }

  public Object visit(ASTTerm node, Object data){

	 return (Element)node.jjtGetChild(node.jjtGetNumChildren()-1).jjtAccept(this, data);

  }

  public Object visit(ASTInteger node, Object data){
    return new Element("", Element.TYPE_INT);
  }

  public Object visit(ASTCallName node, Object data){
    return null;
  }

  public Object visit(ASTSize node, Object data){
    return new Element("readonly", Element.TYPE_INT);
  }

  public Object visit(ASTConditionOP node, Object data){
    return null;
  }

  public Object visit(ASTWhile node, Object data){
	SymbolTable current = SymbolTable.getTable();

	node.jjtGetChild(0).jjtAccept(this, data);


	SymbolTable.pushTable(current.getChildTable());


	node.jjtGetChild(1).jjtAccept(this, data); //statements


	SymbolTable.popTable();


    return null;
  }

  public Object visit(ASTIf node, Object data){

		SymbolTable current = SymbolTable.getTable();

		node.jjtGetChild(0).jjtAccept(this, data);

		SymbolTable.pushTable(current.getChildTable());

		node.jjtGetChild(1).jjtAccept(this, data);

		SymbolTable.popTable();


		if(node.jjtGetNumChildren() == 3){

			SymbolTable.pushTable(current.getChildTable());

			node.jjtGetChild(2).jjtAccept(this, data);

			SymbolTable.popTable();
		}

		return null;
  }

  public Object visit(ASTCall node, Object data){

	  if(node.jjtGetNumChildren() == 1){

		 Element function = SymbolTable.getRootTable().getElement((String)node.jjtGetValue());

		 return function.getReturn();

	  }

	  return new Element("", Element.TYPE_UNDEFINED);
  }

  public Object visit(ASTArgumentList node, Object data){
    return null;
  }

  public Object visit(ASTString node, Object data){
    return null;
  }
}
