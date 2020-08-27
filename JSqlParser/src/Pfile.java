import java.sql.Connection;
import java.sql.Statement;

public class Pfile {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new Pfile();
  }
  
  public Pfile(){
    
    Connection con = null;
    Statement st = null;
    try{
      
      String query = "create pfile='/app/oracle/dbhome/dbs/initduzon_tmp.ora' from spfile='/app/oracle/dbhome/spfileduzon.ora'";
      con = OracleDBConMng.getInstance().getConnection("192.168.0.175", 1521, "Duzon", "system", "oracle");
      st = con.createStatement();
      st.executeUpdate(query);
      st.close(); st = null;
      
    }catch(Exception ea){ ea.printStackTrace(); }
    finally{
      try{ OracleDBConMng.getInstance().freeConnection(con, st); }catch(Exception ea){}
    }
  }

}
