import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by joao on 4/30/17.
 */
public class CodeSampler {

    private PrintWriter fw = null;
    private static int lineNumber = 0;
    private static CodeSampler cs;

    public static CodeSampler getCodeSampler(){
        return cs;
    }

    public static CodeSampler createCodeSampler(String filename){
        cs = new CodeSampler(filename);
        lineNumber = 0;
        return cs;
    }

    private CodeSampler(String fileName){
        try {
            fw = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
