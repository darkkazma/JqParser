import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TEST_Insert {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new TEST_Insert();
  }
  
  public TEST_Insert(){
    
    
    Connection con = null;
    PreparedStatement pstmt = null;
    
    try{
      /*
      String query = "delete from \"CDB\".\"OBJECT\" where \"ID\" = '3266' and \"TYPE_ID\" = '13' and \"CREATED_AT\" = TO_TIMESTAMP('2018-11-01 14:30:18') and \"OWNER_ID\" = '3' and \"REVISION\" = '2' and \"PROJ_ID\" = '2' and \"PARENT_ID\" = '3265' and \"REFERENCE_ID\" IS NULL and \"DELETED\" = '1'";
      */
      con = OracleDBConMng.getInstance().getConnection("192.168.0.176", 1521, "Duzon", "system", "oracle");
      con.setAutoCommit(false);
      
      /*
      Statement st = con.createStatement();
      st.executeUpdate("ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SSXFF'");
      st.close(); st = null;
      
      pstmt = con.prepareStatement(query);
      System.out.println(pstmt.executeUpdate());
       */
      
      DatabaseMetaData meta = con.getMetaData();
      ResultSet rs = meta.getImportedKeys(null, "CDB", "OBJECT");
      while( rs.next() ){
        String fkTableName = rs.getString("FKTABLE_NAME");
        String fkColumnName = rs.getString("FKCOLUMN_NAME");
        int fkSequence = rs.getInt("KEY_SEQ");
        
        System.out.println("");
        System.out.println("pkTableName = " + rs.getString("PKTABLE_NAME"));
        System.out.println("pkColumnName = " + rs.getString("PKCOLUMN_NAME"));
        System.out.println("fkTableName = " + fkTableName);
        System.out.println("fkColumnName = " + fkColumnName);
        System.out.println("fkSequence = " + fkSequence);
        System.out.println("UPDATE_RULE  = " + rs.getInt("UPDATE_RULE"));
        System.out.println("DELETE_RULE = " + rs.getInt("DELETE_RULE"));
        
        System.out.println(DatabaseMetaData.importedKeyCascade);
      }
      rs.close(); rs = null;
      
    }catch(Exception ea){ ea.printStackTrace(); }
    finally{
      try{
        con.rollback();
        con.setAutoCommit(true);
      }catch(Exception ea){}
      
      try{ OracleDBConMng.getInstance().freeConnection(con, pstmt); }catch(Exception ea){}
    }
  }

}
