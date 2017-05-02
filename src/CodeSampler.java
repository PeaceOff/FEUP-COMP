import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class CodeSampler {

    private PrintWriter fw = null;
    private static int lineNumber = 0;
    private static CodeSampler cs = null;
    private static HashMap<String,String> cond_map = new HashMap<String,String>();

    public static CodeSampler getCodeSampler(){
        return cs;
    }

    public static CodeSampler createCodeSampler(String filename){
        cs = new CodeSampler(filename);
        lineNumber = 0;
        return cs;
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
            pr("\n");
        }
    }

    public void writeWhileLoop(String cond){
    	
    	switch(cond){
    	case ">":
    		//if_icmpgt
    		break;
    	case "<":
    		//if_icmplt
    		break;
    	case ">=":
    		//if_icmpge
    		break;
    	case "<=":
    		//if_icmple
    		break;
    	case "==":
    		//if_icmpeq
    		break;
    	case "!=":
    		//if_icmpne
    		break;
    	}
    	//TODO
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

    private void writeStackAndLocals(int stack, int locals){
        pr(".limit stack ");
        prln(stack + "");
        pr(".limit locals ");
        prln(locals + "");
    }

    private void writeStaticInit(){
        prln(".method static public <clinit>()V");

    }

    private void writeStaticInitEnd(){
        prln("return");
        prln(".end method");
    } 
}
