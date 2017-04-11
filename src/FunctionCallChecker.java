public class FunctionCallChecker implements Simple2Visitor {

  public Object visit(SimpleNode node, Object data){
    return null;
  }

  public Object visit(ASTStart node, Object data){
    return null;
  }

  public Object visit(ASTModule node, Object data){
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
    return null;
  }

  public Object visit(ASTArrayDeclaration node, Object data){
    return null;
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
    return null;
  }

  public Object visit(ASTElementArray node, Object data){
    return null;
  }

  public Object visit(ASTStatements node, Object data){
    return null;
  }

  public Object visit(ASTAssign node, Object data){
	  
	  Node variable = node.jjtGetChild(0);
	  
	  return null;
  }

  public Object visit(ASTCalcOP node, Object data){
    return null;
  }

  public Object visit(ASTAccess node, Object data){
    return null;
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
	  
	  if(node.jjtGetNumChildren() == 1){//Se sim, entao e uma funcao do modulo
		node.jjtGetChild(0); //Retorna a lista de argumentos
		//Element function_table = SymbolTable.getRoot().getElement((String)node.jjtGetValue());//Vamos buscar a tabela da funcao
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
