
import java.util.ArrayList;
import java.util.LinkedList;

public class CodeGenerator implements Simple2Visitor {

	@Override
	public Object visit(SimpleNode node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTStart node, Object data) {
		node.jjtGetChild(0).jjtAccept(this,null);
		return null;
	}

	@Override
	public Object visit(ASTModule node, Object data) {

		String identifier = (String)node.jjtGetValue();

		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.writeBeginModule(identifier);

		LinkedList<Element> elements = SymbolTable.getRootTable().getElements();

		ArrayList<Integer> vars = cs.writeStaticVariables(elements);

		if(vars.get(0) > 0)
			cs.writeStaticInit();
		for(int i = 0; i< node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
			if(i == vars.get(1)-1){
				cs.writeEndMethod();
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTDeclaration node, Object data) {

		Element leftSide = (Element)node.jjtGetChild(0).jjtAccept(this,true);
		if(leftSide.getType() != Element.TYPE_ARRAY)
			return null;

		CodeSampler cs = CodeSampler.getCodeSampler();

		if(node.arrayAssign){
			String label = cs.jas_arrayAssignInt1(leftSide);
			node.jjtGetChild(1).jjtAccept(this, false);
			cs.jas_arrayAssignInt2(leftSide,label);
			return null;
		}

		node.jjtGetChild(1).jjtAccept(this,false);

		cs.jas_storestatic(leftSide);

		return null;
	}

	@Override
	public Object visit(ASTNumericDeclaration node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTSign node, Object data) {

		return (String)node.jjtGetValue();

	}

	@Override
	public Object visit(ASTConstant node, Object data) {
		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.jas_ldc((String)node.jjtGetValue());
		return "I";
	}

	@Override
	public Object visit(ASTArrayDeclaration node, Object data) {
		node.jjtGetChild(0).jjtAccept(this,false);
		CodeSampler.getCodeSampler().jas_newArray();
		return null;
	}

	@Override
	public Object visit(ASTFunction node, Object data) {
		SymbolTable current = SymbolTable.getTable();

		SymbolTable.pushTable(current.getChildTable());

		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.increaseIndentation();
		cs.writeBeginMethod(SymbolTable.getTable());
		cs.writeStackAndLocals(20, 1 + SymbolTable.getTable().getMaxJasIndexSize());
		for(int i = 0; i< node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		cs.decreaseIndentation();


		cs.writeEndMethod(SymbolTable.getTable().getReturn());

		SymbolTable.popTable();

		return null;
	}

	@Override
	public Object visit(ASTReturn node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTParameters node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTVariable node, Object data) {
		Element e  = SymbolTable.getTable().getElement((String)node.value);

		if((boolean)data)
			return e;

		CodeSampler.getCodeSampler().jas_loadElement(e);

		return e.jas_getType();
	}

	@Override
	public Object visit(ASTElementArray node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTStatements node, Object data) {

		for(int i = 0; i< node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, true);
		}
		return null;
	}

	private int storeType = -1;

	@Override
	public Object visit(ASTAssign node, Object data) {

		storeType = -1;

		CodeSampler cs = CodeSampler.getCodeSampler();
		Element e1 = (Element)node.jjtGetChild(0).jjtAccept(this, true);

		if(node.arrayAssign){
			String label = cs.jas_arrayAssignInt1(e1);
			node.jjtGetChild(1).jjtAccept(this, false);
			cs.jas_arrayAssignInt2(e1,label);
			return null;
		}


		node.jjtGetChild(1).jjtAccept(this, false);

		cs.jas_putElement(e1,storeType == Element.TYPE_INT);
		storeType = -1;

		return null;
	}

	@Override
	public Object visit(ASTCalcOP node, Object data) {

		node.jjtGetChild(0).jjtAccept(this, false);

		node.jjtGetChild(1).jjtAccept(this, false);

		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.jas_sign((String)node.value);

		return null;
	}

	@Override
	public Object visit(ASTAccess node, Object data) {
		SymbolTable current = SymbolTable.getTable();
		Element e = current.getElement((String)node.value);

		CodeSampler cs = CodeSampler.getCodeSampler();

		if((boolean)data) {

			if(e.getType() == Element.TYPE_ARRAY){

				if(node.jjtGetNumChildren() == 0)
					storeType = Element.TYPE_ARRAY;
				else{
					cs.jas_loadElement(e);
					node.jjtGetChild(0).jjtAccept(this,false);
					storeType = Element.TYPE_INT;
				}
			}else{
				storeType = Element.TYPE_INT;
			}

			return e;
		}


		cs.jas_loadElement(e);

		if(e.getType() != Element.TYPE_ARRAY)
			return null;


		Object o = node.jjtGetChild(0).jjtAccept(this, false);
		if(o != null)
			if(o instanceof Boolean)
				if((Boolean)o == false)
					return null;

		cs.jas_iaload();

		return null;
	}

	@Override
	public Object visit(ASTTerm node, Object data) {

		if(node.jjtGetNumChildren() == 2){
			String st = (String)node.jjtGetChild(0).jjtAccept(this, true); // sign

			node.jjtGetChild(1).jjtAccept(this, false);

			if(st.equals("-")){
				CodeSampler cs = CodeSampler.getCodeSampler();

				cs.jas_ineg();
			}

		}else{
			node.jjtGetChild(0).jjtAccept(this, false);
		}

		return null;
	}

	@Override
	public Object visit(ASTInteger node, Object data) {
		CodeSampler cs = CodeSampler.getCodeSampler();
		String st = (String)node.jjtGetValue();
		cs.jas_loadNumber(Integer.parseInt(st));
		return null;
	}

	@Override
	public Object visit(ASTCall node, Object data) {
		CodeSampler cs = CodeSampler.getCodeSampler();
		SymbolTable st = SymbolTable.getRootTable();

		String types = (String)node.jjtGetChild(node.jjtGetNumChildren()-1).jjtAccept(this,false);
		boolean pop = false;

		if(node.jjtGetNumChildren()==1){
			Element e = st.getElement((String)node.value);
			cs.jas_invokeStatic(e);
			pop = e.getReturn().getName() != null;
		}else{
			String moduleName = (String)node.value;
			String methodName = (String)node.jjtGetChild(0).jjtAccept(this,false);
			String _return = (storeType == Element.TYPE_ARRAY)? "[I" : (storeType == Element.TYPE_INT)? "I" : "V";

			cs.jas_invokeStatic(moduleName,methodName,types, _return);
		}

		if(pop && (boolean)data){
			cs.jas_pop();
		}

		return null;
	}

	@Override
	public Object visit(ASTCallName node, Object data) {

		return node.value;
	}

	@Override
	public Object visit(ASTSize node, Object data) {
		CodeSampler.getCodeSampler().jas_arrayLength();
		return new Boolean(false);
	}

	@Override
	public Object visit(ASTConditionOP node, Object data) {

		ASTAccess left_node = (ASTAccess)node.jjtGetChild(0);
    	left_node.jjtAccept(this,false);

    	SimpleNode right_node = (SimpleNode)node.jjtGetChild(1);
    	right_node.jjtAccept(this,false);

		return null;
	}

	@Override
	public Object visit(ASTWhile node, Object data) {

		CodeSampler cs = CodeSampler.getCodeSampler();
		SymbolTable current = SymbolTable.getTable();

		cs.increaseIndentation();
    	cs.comment("WHILE");

    	String loop_label = "ll_" + CodeSampler.getLineNumber();
    	String end_loop_label = "el_" + CodeSampler.getLineNumber();

    	//Begin do while
    	cs.jas_label(loop_label);

    	//Write das condicoes
    	ASTConditionOP cond_node = (ASTConditionOP)node.jjtGetChild(0);
		cond_node.jjtAccept(this, null);
    	String condition = (String)cond_node.jjtGetValue();
    	cs.jas_cond(condition, end_loop_label);

    	//Print statements
    	SymbolTable.pushTable(current.getChildTable());
    	ASTStatements stat_node = (ASTStatements)node.jjtGetChild(1);
    	stat_node.jjtAccept(this,null);
    	SymbolTable.popTable();

    	//End while loop
    	cs.jas_goto(loop_label);
    	cs.jas_label(end_loop_label);

    	cs.comment("END WHILE");
    	cs.decreaseIndentation();
		return null;
	}

	@Override
	public Object visit(ASTIf node, Object data) {

		SymbolTable current = SymbolTable.getTable();
		CodeSampler cs = CodeSampler.getCodeSampler();
		Boolean has_else = (node.jjtGetNumChildren() == 3);

		cs.increaseIndentation();
		cs.comment("IF");

		//Labels
		String else_label = "else_" + CodeSampler.getLineNumber();
    	String end_if_label = "ei_" + CodeSampler.getLineNumber();

    	//Nodes
		ASTConditionOP cond_node = (ASTConditionOP)node.jjtGetChild(0);
		ASTStatements if_node = (ASTStatements)node.jjtGetChild(1);

		//Write condition variables
		cond_node.jjtAccept(this, null);
		String condition = (String)cond_node.jjtGetValue();

		if(has_else){//has else

			cs.jas_cond(condition, else_label);

			ASTStatements else_node = (ASTStatements)node.jjtGetChild(2);

			//Write statements (if-else)
			SymbolTable.pushTable(current.getChildTable());
				if_node.jjtAccept(this, null);
			SymbolTable.popTable();

			cs.jas_goto(end_if_label);
			cs.jas_label(else_label);

			SymbolTable.pushTable(current.getChildTable());
				else_node.jjtAccept(this, null);
			SymbolTable.popTable();

		} else {//doest not have else

			cs.jas_cond(condition, end_if_label);

			//Write statements (if)
			SymbolTable.pushTable(current.getChildTable());
				if_node.jjtAccept(this, null);
			SymbolTable.popTable();
		}

		//Finalize
		cs.jas_label(end_if_label);

		cs.comment("END_IF");
		cs.decreaseIndentation();
		return null;
	}

	@Override
	public Object visit(ASTArgumentList node, Object data) {
		String s="";

		for(int i =0; i < node.jjtGetNumChildren(); i++){
			s+=node.jjtGetChild(i).jjtAccept(this,false);
		}

		return s;
	}

	@Override
	public Object visit(ASTString node, Object data) {

		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.jas_ldc((String)node.value);

		return "Ljava/lang/String;";
	}

}
