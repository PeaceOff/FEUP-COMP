import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by joao on 4/30/17.
 */
public class CodeSampler {

    private PrintWriter fw = null;

    private static CodeSampler cs;

    public static CodeSampler getCodeSampler(){
        return cs;
    }

    public static CodeSampler createCodeSampler(String filename){
        cs = new CodeSampler(filename);
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
       fw.print(".class public ");
       fw.println(name);
       fw.println(".super java/lang/Object");
    }

    public void writeStaticVariables(LinkedList<Element> elements){
        for(Element e : elements){
            writeStaticField(e);
        }
    }

    private void writeStaticField(Element e){
        if(e.getType() != Element.TYPE_INT && e.getType() != Element.TYPE_ARRAY)
            return;
        fw.print(".field static ");
        fw.print(e.getName());
        fw.print(" ");
        fw.print((e.getType() == Element.TYPE_INT)? "I" : "[I");
        if(e.getType() == Element.TYPE_INT && e.getValue() != null){
            fw.print(" = ");
            fw.println(e.getValue());
        }else{
            fw.print("\n");
        }
    }
}
