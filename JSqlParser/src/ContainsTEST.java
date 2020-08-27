import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ContainsTEST {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new ContainsTEST();
  }
  
  public ContainsTEST(){
    
    ArrayList<Oracle_Item> itemlist = new ArrayList<Oracle_Item>();
    
    Oracle_Item o1 = new Oracle_Item();
    o1.setROW_ID("AAAAA"); o1.setSCN(1000d); o1.setREDO_VALUE(1000d); o1.setOPERATION_CODE(1); o1.setTID("1.0.1"); o1.setSQL_REDO("INSERT INTO DARKKAZMA.AAA VALUES (1, 2, 3 )");
    itemlist.add(o1);
    
    Oracle_Item o2 = new Oracle_Item();
    o2.setROW_ID("AAAAAB");o2.setSCN(1002d); o2.setREDO_VALUE(1002d); o2.setOPERATION_CODE(1); o2.setTID("1.0.1"); o2.setSQL_REDO("INSERT INTO DARKKAZMA.AAA VALUES (1, 2, 3 ,4)");
    itemlist.add(o2);
    
    Oracle_Item o3 = new Oracle_Item();
    o3.setROW_ID("AAAAAB");o3.setSCN(1003d); o3.setREDO_VALUE(1003d); o3.setOPERATION_CODE(1); o3.setTID("1.0.1"); o3.setSQL_REDO("INSERT INTO DARKKAZMA.AAA VALUES (1, 2, 3 )");
    itemlist.add(o3);
    
    Oracle_Item o4 = new Oracle_Item();
    o4.setROW_ID("AAAAAC"); o4.setSCN(1004d); o4.setREDO_VALUE(1004d); o4.setOPERATION_CODE(1); o4.setTID("1.0.1"); o4.setSQL_REDO("INSERT INTO DARKKAZMA.AAA VALUES (1, 2, 3 ,5)");
    itemlist.add(o4);
    
    
    //Oracle_Item o5 = new Oracle_Item();
    //o2.setSCN(1002d); o2.setREDO_VALUE(1002d); o2.setOPERATION_CODE(1); o2.setTID("1.0.1"); o2.setSQL_REDO("INSERT INTO DARKKAZMA.AAA VALUES (1, 2, 3 ,4)");
    
    //System.out.println( itemlist.contains(o4));
    
    HashSet<Oracle_Item> list_item = new HashSet<Oracle_Item>(itemlist);
    
    ArrayList<Oracle_Item> processitem = new ArrayList<Oracle_Item>(list_item);
    Collections.sort(processitem);
    
    for( Oracle_Item oi : processitem ){
      System.out.println("-------------------------------------");
      System.out.println("SCN  ["+oi.getSCN()+"]");
      System.out.println("REDO ["+oi.getREDO_VALUE()+"]");
      System.out.println("TID  ["+oi.getTID()+"]");
      System.out.println("SQL  ["+oi.getSQL_REDO()+"]");
      System.out.println("");
      System.out.println("-------------------------------------");
    }
  }

}
