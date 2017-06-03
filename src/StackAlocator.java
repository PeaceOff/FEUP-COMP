import javax.swing.event.ListDataListener;
import java.util.*;


public class StackAlocator {

    private SymbolTable table;

    public StackAlocator(SymbolTable table){
        this.table = table;
        prepareData();
    }

    private void prepareData(){
        table.prepareMinimum();
    }

    public boolean tryAlocation(int n){

        for(SymbolTable table : table.getAllChilds()){
            System.out.println("--" + table.getName());
            if(!AllocateSection(table.getAllElements(), n))
                return false;

        }
        return true;
    }

    private boolean AllocateSection(ArrayList<Element> elements, int colors){

        Collections.sort(elements, (e1,e2)->{
            if(e1.getMinLine() == 0 && e2.getMinLine() == 0)
                return e1.getJasIndex() - e2.getJasIndex();
            else
                return e1.getMinLine() - e2.getMinLine();
        }
        );
        int color = 0;

        while(elements.size() > 0){
            Element e= elements.get(0);
            int offset = e.getMaxLine();
            e.setJasIndex(color);
            elements.remove(0);
            for(int i = 0; i < elements.size(); i++){
                e = elements.get(i);

                if(e.getMinLine() < offset)
                    continue;

                e.setJasIndex(color);
                offset = e.getMaxLine();
                elements.remove(i);
                i--;
            }
            color++;
        }


        for(Element e : elements){
            System.out.println(e);
        }
        if(color >= colors)
            return false;
        return true;
    }

}
