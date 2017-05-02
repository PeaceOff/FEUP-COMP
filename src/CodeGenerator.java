import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;

public class CodeGenerator implements Simple2Visitor {

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
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

		int vars = cs.writeStaticVariables(elements);

		if(vars > 0)
			cs.writeStaticInit();
		for(int i = 0; i< node.jjtGetNumChildren(); i++){ 
			node.jjtGetChild(i).jjtAccept(this, data);
			if(i == vars-1){
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

		node.jjtGetChild(1).jjtAccept(this,false);

		cs.jas_storestatic(leftSide);

		return null;
	}

	@Override
	public Object visit(ASTNumericDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSign node, Object data) {
		
		String sign = (String)node.jjtGetValue();
		
		CodeSampler cs = CodeSampler.getCodeSampler();
		
		if((Boolean)data == false){
			cs.jas_sign(sign);
			return null;
		}else{
			return sign;
		}
		
	}

	@Override
	public Object visit(ASTConstant node, Object data) {
		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.jas_ldc((String)node.jjtGetValue());
		return null;
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
		cs.writeBeginMethod(SymbolTable.getTable());

		for(int i = 0; i< node.jjtGetNumChildren(); i++){  
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		cs.writeEndMethod();

		SymbolTable.popTable();

		return null;
	}

	@Override
	public Object visit(ASTReturn node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTParameters node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTVariable node, Object data) {
		Element e  = SymbolTable.getTable().getElement((String)node.value);

		if((boolean)data)
			return e;

		CodeSampler.getCodeSampler().jas_loadElement(e);

		return null;
	}

	@Override
	public Object visit(ASTElementArray node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTStatements node, Object data) {

		for(int i = 0; i< node.jjtGetNumChildren(); i++){  
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTAssign node, Object data) {
		
		CodeSampler cs = CodeSampler.getCodeSampler();
		
		node.jjtGetChild(0).jjtAccept(this, false);	
		node.jjtGetChild(1).jjtAccept(this, false);
		
		
		return null;
	}

	@Override
	public Object visit(ASTCalcOP node, Object data) {
	
		node.jjtGetChild(0).jjtAccept(this, false);
		node.jjtGetChild(1).jjtAccept(this, false);
		
		return null;
	}

	@Override
	public Object visit(ASTAccess node, Object data) { //Adiciona Boolean
		SymbolTable current = SymbolTable.getTable();
		Element e = current.getElement((String)node.value);

		if((boolean)data)
			return e;

		CodeSampler cs = CodeSampler.getCodeSampler();

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
			node.jjtGetChild(0).jjtAccept(this, false); // sign
			node.jjtGetChild(1).jjtAccept(this, false);
		}else{
			node.jjtGetChild(0).jjtAccept(this, false);
		}
			
		
		return null;
	}

	@Override
	public Object visit(ASTInteger node, Object data) {
		CodeSampler cs = CodeSampler.getCodeSampler();
		cs.jas_ldc((String)node.jjtGetValue());
		return null;
	}

	@Override
	public Object visit(ASTCall node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCallName node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSize node, Object data) {
		CodeSampler.getCodeSampler().jas_arrayLength();
		return new Boolean(false);
	}

	@Override
	public Object visit(ASTConditionOP node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTWhile node, Object data) {
		
		CodeSampler cs = CodeSampler.getCodeSampler();
		
		ASTConditionOP cond_node = (ASTConditionOP)node.jjtGetChild(0);
		ASTStatements stat_node = (ASTStatements)node.jjtGetChild(1);
		
		cs.writeWhileLoop(cond_node,stat_node,this);
		
		return null;
	}

	@Override
	public Object visit(ASTIf node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTArgumentList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

}
