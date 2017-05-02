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

		cs.writeStaticVariables(elements);
		
		for(int i = 0; i< node.jjtGetNumChildren(); i++){  //check
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return null;
	}

	@Override
	public Object visit(ASTDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTNumericDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSign node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTConstant node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTArrayDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTFunction node, Object data) {

		for(int i = 0; i< node.jjtGetNumChildren(); i++){  //check
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTElementArray node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTStatements node, Object data) {

		for(int i = 0; i< node.jjtGetNumChildren(); i++){  //check
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTAssign node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCalcOP node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTAccess node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTTerm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTInteger node, Object data) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTConditionOP node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTWhile node, Object data) {
		
		CodeSampler cs = CodeSampler.getCodeSampler();
		
		//Write code for condition
		ASTConditionOP cond_node = (ASTConditionOP)node.jjtGetChild(0);
		
		String condition = (String)cond_node.jjtGetValue();
		
		//Write code for statements
		ASTStatements stat_node = (ASTStatements)node.jjtGetChild(1);
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
