import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PLSQL {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new PLSQL();
  }
  
  public PLSQL(){
    
    
    try{
      
      Connection con = OracleDBConMng.getInstance().getConnection("192.168.0.175", 1521, "orcl", "system", "oracle");
      
      String sql = "create or replace trigger aa_trigger after insert on darkkazma.ac82 for each ROW declare begin insert into darkkazma.ac8 values ( :NEW.A , :NEW.B , sysdate ) ; end";
      //PreparedStatement pstm = con.prepareStatement(sql);
      //System.out.println( pstm.executeUpdate() );
      //pstm.close(); pstm = null;
      Statement st = con.createStatement();
      st.executeUpdate(sql);
      st.close(); st = null;
      
    }catch(Exception ea){ ea.printStackTrace(); }
  }

}
