import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import oracle.jdbc.OracleResultSet;
import oracle.sql.CLOB;
import oracle.xdb.XMLType;


public class Insert_XML {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new Insert_XML();
  }
  
  public Insert_XML(){
    
    Connection con = null;
    OracleDBConMng dbmanager = null;
    String LINE_SEP = System.getProperty("line.separator");
    try{
    
      dbmanager = OracleDBConMng.getInstance();
      con = dbmanager.getConnection("192.168.0.175", 1521, "orcl", "system", "oracle");
      

      File qFile = new File("C:\\Users\\darkkazma\\desktop\\query.sql");
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile)));
      String line = "";
      StringBuffer xml = new StringBuffer();
      while( (line = br.readLine()) != null ){
        xml.append(line).append(LINE_SEP);
      }
      br.close(); br = null;

      
      /*
      File qFile = new File("C:\\Users\\darkkazma\\desktop\\query.sql");
      FileInputStream fi = new FileInputStream(qFile);
      BufferedInputStream bi = new BufferedInputStream(fi);
      
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(bi);
      CLOB xmlclob = getXMLAsClob(con, doc);
      
      System.out.println(xmlclob);
       */
      
      String query = "insert into darkkazma.po_xml_tab values (300, empty_clob() ) ";
      Statement st = con.createStatement();
      st.executeUpdate(query);
      
      con.setAutoCommit(false);;
      
      ResultSet rs = st.executeQuery("select  PODOC from darkkazma.po_xml_tab where POID = 300 fro update");
      if( rs.next() ){
        CLOB clob = ((OracleResultSet)rs).getCLOB("PODOC");
        
        BufferedWriter writer = new BufferedWriter(clob.getCharacterOutputStream());
        writer.write(xml.toString());
        writer.close();
      }
      con.setAutoCommit(true);
      
      /*
      PreparedStatement in_xml = con.prepareStatement(query);
      in_xml.setObject(1, xmlclob);
      in_xml.executeUpdate();
      in_xml.close(); in_xml = null;
      */
      
      /*
      String query = "insert into darkkazma.po_xml_tab values (200,  sys.XMLType.createXML(?) )";
      PreparedStatement in_xml = con.prepareStatement(query);
      in_xml.setObject(1, xml.toString());
      in_xml.executeUpdate();
      in_xml.close(); in_xml = null;
      */
    }catch(Exception ea){ 
      ea.printStackTrace();
    }
    finally{ 
      try{ 
        if( con != null ){ dbmanager.freeConnection(con); } con = null; }catch(Exception ea){ }
    }
    
    
    
    
  }
  
  public static CLOB getXMLAsClob(java.sql.Connection conn, Document doc) throws SQLException

  {

  // String SQLTEXT = "";
  XMLType xml = null;
  xml = XMLType.createXML(conn, doc);
  return xml.getClobVal();

  }

}
