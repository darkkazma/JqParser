import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.TokenMgrError;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

public class ParseInsertQuery {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new ParseInsertQuery();
  }
  
  
  public ParseInsertQuery(){
    
    System.out.println("Parser Start");
    Parser();
    System.out.println("Parser End");
    
    
  }
  
  public void Parser(){
    try{
          
          File qFile = new File("C:\\Users\\darkkazma\\desktop\\query.sql");
          BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile)));
          String sql = br.readLine(); br.close();
          
          try{
            //CCJSqlParserManager pm = new CCJSqlParserManager();
            //Statement Insertst = pm.parse(new StringReader(sql));
            Statement Insertst =  (Insert)CCJSqlParserUtil.parse(sql);
            
          }catch(JSQLParserException jpe){}
          catch(TokenMgrError tme){
            System.err.println("tokenerr : " + tme.toString());
          }
          System.out.println("이건뭐.. 나오나??");
          
    }catch(Exception ea){
      ea.printStackTrace();
    }
  }

}
