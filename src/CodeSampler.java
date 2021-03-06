import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CodeSampler {

    private PrintWriter fw = null;
    private static int lineNumber = 1;
    private static CodeSampler cs = null;
    private static HashMap<String,String> cond_map = new HashMap<String,String>();
    private static HashMap<String,String> sign_map = new HashMap<String,String>();

    private ArrayList<Element> elemnts = new ArrayList<>();
    private int elementScopeNumber = -1;

    public static CodeSampler getCodeSampler(){
        return cs;
    }

    public static CodeSampler createCodeSampler(String filename){

        if(cs !=null)
            cs.close();

        cs = new CodeSampler(filename);
        lineNumber = 1;
        return cs;
    }

    public void scopeAddElement(Element e) {
        if (elementScopeNumber < 0)
            return;

        elemnts.add(e);

    }

    public void scopeUpdateMaxLimits(){
        if(elementScopeNumber > -1)
            return;

        for(Element e : elemnts){
            e.updateLineNumber(getLineNumber());
        }
        elemnts.clear();
    }

    public void clear(){
        lineNumber=1;
    }

    private int indentation = 0;

    private String moduleName;

    public String getModuleName(){
        return moduleName;
    }

    private CodeSampler(String fileName){

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


        sign_map.put("+", "iadd");
        sign_map.put("-", "isub");
        sign_map.put("*", "imul");
        sign_map.put("/", "idiv");
        sign_map.put(">>", "ishr");
        sign_map.put("<<", "ishl");
        sign_map.put("|", "ior");
        sign_map.put("^", "ixor");
        sign_map.put(">>>", "iushr");
        sign_map.put("&", "iand");

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
        elemnts.clear();
    }

    public void writeBeginModule(String name){
       pr(".class public ");
       prln(name); moduleName = name;
       prln(".super java/lang/Object");
    }

    public ArrayList<Integer> writeStaticVariables(LinkedList<Element> elements){
        int vars = 0;
        int max = 0;

        for(Element e : elements){
            writeStaticField(e);
            vars += (e.getType() == Element.TYPE_ARRAY )? 1 : 0;
            max += (e.getType() == Element.TYPE_ARRAY || e.getType() == Element.TYPE_INT )? 1 : 0;
        }
        ArrayList<Integer> res = new ArrayList<>();

        res.add(vars);
        res.add(max);

        return res;
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

    public static int getLineNumber() {
		return lineNumber;
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

    public void commentWhile(){
        prln(";WHILE");
        elementScopeNumber++;
    }

    public void commentEndWhile(){
        prln(";END_WHILE");
        elementScopeNumber--;
        scopeUpdateMaxLimits();
    }

    public void comment(String s){


    	pr(';');
    	prln(s);
    }

	public void comment(Object s){

	    comment(s.toString());
    }

    public void writeStackAndLocals(int stack, int locals){
        pr(".limit stack ");
        prln(stack + "");
        pr(".limit locals ");
        prln(locals + "");
    }

    public void writeStaticInit(){
        prln(".method static public <clinit>()V");

    }

    public void jas_label(String label){
    	pr(label);
    	prln(" :");
    }

    public void jas_cond(String condition,String label){
    	pr(cond_map.get(condition));
    	pr(" ");
    	prln(label);
    }

    public void jas_goto(String label){
    	pr("goto ");
    	prln(label);
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

    public void writeEndMethod(Element e){
	    //System.out.println(e + "" + e.getType() +  " "  + Element.TYPE_UNDEFINED);
	    if(e != null && e.getName() != null && e.getType() != Element.TYPE_UNDEFINED){
	        jas_loadElement(e);
        }else {
            writeEndMethod();
            return;
	    }
        if(e.getType() == Element.TYPE_INT){
	        prln("ireturn");
        }else if (e.getType() == Element.TYPE_ARRAY){
            prln("areturn");
        }
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

    public void jas_iconst(int n){
        pr("iconst_");
        prln(n);
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
                e.updateLineNumber(getLineNumber());
                scopeAddElement(e);
                jas_istore(e.getJasIndex());
            }else
                jas_storestatic(e);
        }else if(e.getType() == Element.TYPE_ARRAY){
            if(arrayIndexStore){
                jas_iastore();
            }else{
                if(e.getJasIndex() != -1){
                    e.updateLineNumber(getLineNumber());
                    scopeAddElement(e);
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
            if (e.getJasIndex() != -1) {
                e.updateLineNumber(getLineNumber());
                scopeAddElement(e);
                jas_iload(e.getJasIndex());
            }else
                jas_getstatic(e);

        }else if(e.getType() == Element.TYPE_ARRAY){
            if (e.getJasIndex() != -1) {
                e.updateLineNumber(getLineNumber());
                scopeAddElement(e);
                jas_aload(e.getJasIndex());
            }else
                jas_getstatic(e);
        }
    }


    public void jas_pop(){
        prln("pop");
    }

    public void jas_dup2(){
        prln("dup2");
    }

    public void jas_dup(){
        prln("dup");
    }

    public void jas_cmp(String condition, String label){
        pr(condition);
        pr(" ");
        prln(label);
    }

    public void jas_bipush(Integer number){
      pr("bipush ");
      prln(number);
    }

    public void jas_loadNumber(Integer number){
      if(number >= 0  && number <= 5 ){
        jas_iconst(number);
      }else{
        jas_bipush(number);
      }
    }

    public String jas_arrayAssignInt1(Element array){
        jas_iconst(0);
        String label = "as_"+getLineNumber();
        jas_label(label);
        jas_loadElement(array);
        jas_dup2();
        jas_pop();

        return label;
    }

    public void jas_arrayAssignInt2(Element array, String label){
        jas_iastore();
        jas_iconst(1);
        jas_sign("+");
        jas_dup();
        jas_loadElement(array);
        jas_arrayLength();
        jas_cmp("if_icmplt", label);
        jas_pop();
    }
}
