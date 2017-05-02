import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class CodeSampler {

    private PrintWriter fw = null;
    private static int lineNumber = 1;
    private static CodeSampler cs = null;
    private static HashMap<String,String> cond_map = new HashMap<String,String>();

    public static CodeSampler getCodeSampler(){
        return cs;
    }

    public static CodeSampler createCodeSampler(String filename){
        cs = new CodeSampler(filename);
        lineNumber = 1;
        return cs;
    }

    private String methodName;

    public String getMethodName(){
        return methodName;
    }

    private CodeSampler(String fileName){
    	
    	if(cs != null)
    		return;
    	
        try {
            fw = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        cond_map.put(">", "if_icmple");
        cond_map.put("<", "if_icmpge");
        cond_map.put(">=", "if_icmplt");
        cond_map.put("<=", "if_icmpgt");
        cond_map.put("==", "if_icmpne");
        cond_map.put("!=", "if_icmpeq");
    }

    public void close(){
        fw.close();
    }

    public void writeBeginModule(String name){
       pr(".class public ");
       prln(name);
       prln(".super java/lang/Object");
    }

    public void writeStaticVariables(LinkedList<Element> elements){
        for(Element e : elements){
            writeStaticField(e);
        }
    }

    private void writeStaticField(Element e){
        if(e.getType() != Element.TYPE_INT && e.getType() != Element.TYPE_ARRAY)
            return;
        pr(".field static ");
        pr(e.getName());
        pr(" ");
        pr((e.getType() == Element.TYPE_INT)? "I" : "[I");
        if(e.getType() == Element.TYPE_INT && e.getValue() != null){
            pr(" = ");
            prln((String)e.getValue());
        }else{
            prln("");
        }
    }

    public void writeWhileLoop(ASTConditionOP cond_node){
    	
    	//comment("WHILE");
    	
    	String loop_label = "ll_" + lineNumber;
    	String end_loop_label = "el_" + lineNumber;
    	
    	pr(loop_label);
    	prln(" :");
    	
    	//comment("Load variables");
    	//TODO load das constantes
    	String condition = (String)cond_node.jjtGetValue();
    	pr(cond_map.get(condition));
    	pr(" ");
    	prln(end_loop_label);
    	
    	//TODO print dos statemenst
    	
    	pr("goto ");
    	prln(loop_label);
    	
    	pr(end_loop_label);
    	prln(" :");
    }
    
    private void pr(String s){
        fw.print(s);
    }

    private void pr(Object s){

        pr(s.toString());

    }

    private void prln(String s){
        lineNumber++;
        fw.println(s);
    }

    private void prln(Object s){
        prln(s.toString());
    }
    
    private void comment(String s){
    	
    	fw.print(';');
    	fw.println(s);
    }
    
	private void comment(Object s){
	    	
	    comment(s.toString());
    }

    private void writeStackAndLocals(int stack, int locals){
        pr(".limit stack ");
        prln(stack + "");
        pr(".limit locals ");
        prln(locals + "");
    }

    public void writeStaticInit(){
        prln(".method static public <clinit>()V");

    }

	private void writeMainMethod(){
		prln(".method public static main([Ljava/lang/String;)V");
	}	

	public void createArrayStatic(){

    }

	public void writeBeginMethod(SymbolTable st){
		pr(".method public static ");
        pr(st.getName());
        pr("(");

        LinkedList<Element> parameters = st.getParameters();
        for(Element e : parameters){
           pr(e.jas_getType());
        }
        pr(")");
        prln(st.jas_getReturnType());

	}

    public void writeEndMethod(){
        prln("return");
        prln(".end method");
    } 
}
