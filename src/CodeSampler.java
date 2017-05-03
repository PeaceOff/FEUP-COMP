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
    private static HashMap<String,String> sign_map = new HashMap<String,String>();

    public static CodeSampler getCodeSampler(){
        return cs;
    }

    public static CodeSampler createCodeSampler(String filename){
        cs = new CodeSampler(filename);
        lineNumber = 1;
        return cs;
    }

    private int indentation = 0;

    private String moduleName;

    public String getModuleName(){
        return moduleName;
    }

    private CodeSampler(String fileName){
    	
    	if(cs != null)
    		return;
    	
        try {
            fw = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cond_map.put(">", "if_icmpgt");
        cond_map.put("<", "if_icmplt");
        cond_map.put(">=", "if_icmpge");
        cond_map.put("<=", "if_icmple");
        cond_map.put("==", "if_icmpeq");
        cond_map.put("!=", "if_icmpne");
        
        
        sign_map.put("+", "iadd");
        sign_map.put("-", "isub");
        sign_map.put("*", "imul");
        sign_map.put("/", "idiv");

    }

    public void increaseIndentation(){
        indentation++;
    }

    public void decreaseIndentation(){
        indentation--;
    }

    private String identTest = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    public void close(){
        fw.close();
    }

    public void writeBeginModule(String name){
       pr(".class public ");
       prln(name); moduleName = name;
       prln(".super java/lang/Object");
    }

    public int writeStaticVariables(LinkedList<Element> elements){
        int vars = 0;
        for(Element e : elements){
            writeStaticField(e);
            vars += (e.getType() == Element.TYPE_ARRAY || e.getType() == Element.TYPE_INT)? 1 : 0;
        }
        return vars;
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
    
    public void writeWhileLoop(ASTConditionOP cond_node,ASTStatements stat_node,Simple2Visitor v){
    	
    	SymbolTable current = SymbolTable.getTable();
    	
    	comment("WHILE");
    	
    	String loop_label = "ll_" + lineNumber;
    	String end_loop_label = "el_" + lineNumber;
    	
    	//Begin do while
    	pr(loop_label);
    	prln(" :");
    	
    	comment("Load variables");
    	
    	writeCondNode(cond_node,v);

    	//Write das condicoes
    	String condition = (String)cond_node.jjtGetValue();
    	pr(cond_map.get(condition));
    	pr(" ");
    	prln(end_loop_label);
    	
    	//Print statements
    	SymbolTable.pushTable(current.getChildTable());
    	stat_node.jjtAccept(v,null);
    	SymbolTable.popTable();
    	
    	//End while loop
    	pr("goto ");
    	prln(loop_label);
    	pr(end_loop_label);
    	prln(" :");
    }
    
    public void writeIf(ASTConditionOP cond_node,ASTStatements if_node, ASTStatements else_node,Simple2Visitor v) {
    	
    	SymbolTable current = SymbolTable.getTable();
    	
    	comment("IF");
    	
    	String loop_label = "ll_" + lineNumber;
    	String end_loop_label = "el_" + lineNumber;
    	
    	//Begin do while
    	pr(loop_label);
    	prln(" :");
    	
    	comment("Load variables");
    	
    	writeCondNode(cond_node,v);

    	//Write das condicoes
    	String condition = (String)cond_node.jjtGetValue();
    	pr(cond_map.get(condition));
    	pr(" ");
    	prln(end_loop_label);
    	
    	//Print statements
    	SymbolTable.pushTable(current.getChildTable());
    	//stat_node.jjtAccept(v,null);
    	SymbolTable.popTable();
    	
    	//End while loop
    	pr("goto ");
    	prln(loop_label);
    	pr(end_loop_label);
    	prln(" :");
    }
    
    private void writeCondNode(ASTConditionOP cond_node,Simple2Visitor v){
    	
    	ASTAccess left_node = (ASTAccess)cond_node.jjtGetChild(0);
    	left_node.jjtAccept(v,false);
    	
    	SimpleNode right_node = (SimpleNode)cond_node.jjtGetChild(1);
    	right_node.jjtAccept(v,false);
    
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
        fw.print(identTest.substring(0, indentation));
    }

    private void prln(Object s){
        prln(s.toString());
    }
    
    private void comment(String s){
    	

    	pr(';');
    	prln(s);
    }
    
	public void comment(Object s){
	    	
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
	    if(st.getName().equals("main")){
	        writeMainMethod();
	        return;
        }
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

    public void jas_ldc(String s){
        pr("ldc ");
        prln(s);
    }

    public void jas_iload(int pos){
        pr("iload ");
        prln(pos);
    }

    public void jas_getstatic(Element e){
        pr("getstatic ");
        pr(moduleName);
        pr("/");
        pr(e.getName());
        pr(" ");
        prln(e.jas_getType());
    }

    public void jas_storestatic(Element e){
        pr("putstatic ");
        pr(moduleName);
        pr("/");
        pr(e.getName());
        pr(" ");
        prln(e.jas_getType());
    }

    public void jas_iaload(){
        prln("iaload");
    }

    public void jas_aload(int pos){
        pr("aload ");
        prln(pos);
    }

    public void jas_arrayLength(){
        prln("arraylength");
    }

    public void jas_newArray(){
        prln("newarray int");
    }

    public void jas_istore(int n){
        pr("istore ");
        prln(n);
    }

    public void jas_iastore(){
        prln("iastore ");
    }
    
    public void jas_sign(String sign){
    	prln(sign_map.get(sign));
    }

    public void jas_ineg(){
    	prln("ineg");
    }

    public void jas_astore(int n){
        pr("astore ");
        prln(n);
    }

    public void jas_putElement(Element e, boolean arrayIndexStore){

        if(e.getType() == Element.TYPE_INT){
            if(e.getJasIndex() != -1){
                jas_istore(e.getJasIndex());
            }else
                jas_storestatic(e);
        }else if(e.getType() == Element.TYPE_ARRAY){
            if(arrayIndexStore){
                jas_iastore();
            }else{
                if(e.getJasIndex() != -1){
                    jas_astore(e.getJasIndex());
                }else{
                    jas_storestatic(e);
                }
            }
        }

    }

    public void jas_invokeStatic(String module, String elementName, String types, String _return){
        pr("invokestatic ");
        pr(module);
        pr("/");
        pr(elementName);
        pr("(");
        pr(types);
        pr(")");
        prln(_return);
    }

    public void jas_invokeStatic(Element e){
        jas_invokeStatic(moduleName,e.getName(), e.jas_getTypes(), e.jas_getReturnType());
    }

    public void jas_loadElement(Element e){
        if(e.getType() == Element.TYPE_INT) {
            if (e.getJasIndex() != -1)
                jas_iload(e.getJasIndex());
            else
                jas_getstatic(e);

        }else if(e.getType() == Element.TYPE_ARRAY){
            if (e.getJasIndex() != -1)
                jas_aload(e.getJasIndex());
            else
                jas_getstatic(e);
        }
    }
}
