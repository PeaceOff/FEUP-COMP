import java.util.LinkedList;

public class FunctionCallChecker implements Simple2Visitor {

	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public Object visit(ASTStart node, Object data) {

		node.jjtGetChild(0).jjtAccept(this, data);

		return null;
	}

	public Object visit(ASTModule node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTDeclaration node, Object data) {
		return null;
	}

	public Object visit(ASTNumericDeclaration node, Object data) {
		return null;
	}

	public Object visit(ASTSign node, Object data) {
		return null;
	}

	public Object visit(ASTConstant node, Object data) {
		return null;
	}

	public Object visit(ASTArrayDeclaration node, Object data) {
		return null;
	}

	public Object visit(ASTFunction node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTReturn node, Object data) {
		return null;
	}

	public Object visit(ASTParameters node, Object data) {
		return null;
	}

	public Object visit(ASTVariable node, Object data) {
		
		return (Element)SymbolTable.getTable().getElement((String)node.jjtGetValue());
	}

	public Object visit(ASTElementArray node, Object data) {
		return null;
	}

	public Object visit(ASTStatements node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTAssign node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return null;
	}

	public Object visit(ASTCalcOP node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTAccess node, Object data) {
		return null;
	}

	public Object visit(ASTTerm node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTInteger node, Object data) {
		return null;
	}

	public Object visit(ASTCallName node, Object data) {
		return null;
	}

	public Object visit(ASTSize node, Object data) {
		return null;
	}

	public Object visit(ASTConditionOP node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTWhile node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTIf node, Object data) {
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		
		return null;
	}

	public Object visit(ASTCall node, Object data) {

		if (node.jjtGetNumChildren() == 1) {// Se sim, entao e uma funcao do modulo
			
			//Elemento que representa a funcao a ser chamada
			Element function = SymbolTable.getRootTable().getElement((String)node.jjtGetValue());
			
			//Lista de parametros que foi passada ha funcao
			LinkedList<Element> parameters = (LinkedList<Element>)node.jjtGetChild(0).jjtAccept(this, data); // Retorna a lista de argumentos
			
			//Lista de parametros que a funcao recebe
			LinkedList<Element> arguments = function.getArguments();
			
			int menor = arguments.size();
			
			if(parameters.size() != arguments.size()){//Numero de argumentos diferente
				
				//Adicionar o erro
				ErrorManager.addError(node.line,
						"Function call on " + node.jjtGetValue() + " has illegal number of arguments! Should be " + arguments.size() + " argument(s).");
			 	
				if(menor > parameters.size())
					menor = parameters.size();	
			}
			
			//verificar para cada um dos argumentos e parametros a sua compatibilidade
 			for(int i = 0; i < menor; i++){
 				
 				//System.out.println(parameters.get(i).toString() + " | " + arguments.get(i).toString());
 				
 				//Se nao forem do mesmo tipo
 				if(!parameters.get(i).compare(arguments.get(i))){
 					ErrorManager.addError(node.line,
 							"Argument " + parameters.get(i).getName()
 							+ " type does not match expected type! Expected "
							+ arguments.get(i).get_type_string() + " but got " + parameters.get(i).get_type_string() + " instead!");
 				}
			}
		}

		return null;
	}

	public Object visit(ASTArgumentList node, Object data) {
		
		LinkedList<Element> variables = new LinkedList<Element>();
		
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			variables.add((Element)node.jjtGetChild(i).jjtAccept(this, data));
		}
		
		return variables;
	}

	public Object visit(ASTString node, Object data) {
		return null;
	}
}
