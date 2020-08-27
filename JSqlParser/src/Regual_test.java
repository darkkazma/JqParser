import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;


public class Regual_test {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new Regual_test();
  }
  
  
  public Regual_test(){
    
    String Delim = ",";
    
    //String data = "INSERT INTO \"DARKKAZMA\".\"TEST_TAB\" (\"COL_01\", \"COL_02\", \"COL_03\", \"COL_04\", \"COL_05\", \"COL_06\", \"COL_07\") VALUES ('가나','다라','마바','',null, '사아'',''!@#!@#''', null)";
    File sqlFile = new File("C:\\Users\\darkkazma\\Desktop\\query.sql");
    String line = null;
    String query = "";
    
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile)));
      query = br.readLine();
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    String data = query;
    
    try{
     CCJSqlParserManager pm = new CCJSqlParserManager();
     Statement st = pm.parse(new StringReader(data));
     System.out.println(st);
     
     ArrayList<String> columns = new ArrayList<String>();
     ArrayList<String> values = new ArrayList<String>();
     
     if( st instanceof Update ){
       
       List<Column> colist =  ((Update) st).getColumns();
       List<Expression> valist = ((Update) st).getExpressions();
       
       /*
       for(Column col : colist ){
         System.out.println(col.getColumnName().replace("\"", "").trim());
       }
       
       for( Expression exp : valist){
         System.out.println( exp.toString() );
       }
       */
       
       System.out.println("");
       System.out.println("WHERE --------------------------");
       
       
       //System.out.println( ((Update) st).getWhere() );
       
       Expression expr = ((Update)st).getWhere();
       
       final ArrayList<Expression> epList = new ArrayList<Expression>();
       expr.accept(new ExpressionVisitorAdapter(){
         
         @Override
         public void visit(AndExpression expr){
           if( expr.getLeftExpression() instanceof AndExpression){
             expr.getLeftExpression().accept(this);
           }else{
             epList.add(expr.getLeftExpression());
           }
           epList.add(expr.getRightExpression());
         }
       });
       
       
       String primary =  "TEST_COLUMN_01";
       for( Expression ex : epList ){
         //System.out.println( ((EqualsTo)ex).getLeftExpression() +" : " + ((EqualsTo)ex).getRightExpression() );
         String Column = ((EqualsTo)ex).getLeftExpression().toString();
         if( Column.equals("\""+primary+"\"") ){
           System.out.println( ex.toString() );
         }
       }
       
       System.out.println(expr);
       
       
       
     }else if( st instanceof Insert ){
       
       List<Column> colist = ((Insert) st).getColumns();
       ItemsList list = ((Insert) st).getItemsList();
       List<Expression> expressions = ((ExpressionList) list).getExpressions();
       
       for(Column col : colist ){
         columns.add(col.getColumnName().replace("\"", "").trim());
       }
       
       if( expressions != null && expressions.size() > 0 ){
         for( Expression exp : expressions){
           values.add( exp.toString() );
         }
       }
       
       
       String longC = "TEST02_COL26";
       System.out.println(columns.indexOf(longC));
       
       System.out.println(values.get(26));
       
       for( String val : values ){
         if( val.toUpperCase().equals("NULL") ){
           System.out.println(val);
         }
       }
       
     }
     
    }catch(Exception ea){ ea.printStackTrace(); }
    
    
    
    
    /*
    //update
    int set_start_idx = data.toUpperCase().indexOf("SET");
    int set_end_idx = data.toUpperCase().indexOf("WHERE");
    
    String setData = data.substring(set_start_idx+3, set_end_idx);
    System.out.println("set : " + setData);
    
    String[] dellist = setData.split(Delim);
    
    for( int i=0; i<dellist.length; i++ ){
      String dels = dellist[i].trim();
      if( dels.length() > 2 && dels.indexOf("''") != -1 ){
        dels += Delim + dellist[i+1].trim();
        i++;
      }
      System.out.println(dels);
      System.out.println("");
    }
    */
    
    
    
    /*
    int col_start_idx = data.indexOf("(");
    int col_end_idx = data.indexOf(")");
    String column = data.substring(col_start_idx+1, col_end_idx);
    StringTokenizer colst = new StringTokenizer(column, Delim);
    ArrayList<String> columns = new ArrayList<String>();
    while( colst.hasMoreTokens() ){
      String col = colst.nextToken();
      columns.add(col.replace("\"", "").trim());
    }
    String valtmp = data.substring(col_end_idx+1);
    
    int val_start_idx = valtmp.indexOf("(");
    int val_end_idx = valtmp.lastIndexOf(")");
    String value = valtmp.substring(val_start_idx+1, val_end_idx);
    
    String[] dellist = value.split(Delim);
    ArrayList<String> values = new ArrayList<String>();
    for( int i=0; i<dellist.length; i++ ){
      String dels = dellist[i].trim();
      if( dels.length() > 2 && dels.indexOf("''") != -1 ){
        dels += Delim + dellist[i+1].trim();
        i++;
      }
      values.add(dels);
    }
    
    System.out.println("컬럼 사이즈 : " + columns.size());
    System.out.println("값 사이즈 : " + values.size());
    
    // 만약 column의 값 중 LONG TYPE이 존재 할 경우 VALUE값이 NULL인지 확인
    
    for( String val : values){ System.out.println(val); }
    
    System.out.println("");
    System.out.println("");
    
    int position = columns.indexOf("COL_04");
    if( position != -1 ){
      String va = values.get(position + 1);
      if( va.toUpperCase().equals("NULL") ){
        values.set(position+1, "''");
      }
    }
    
    for( String val : values){ System.out.println(val); }
    */
  }  
  
  
  
}
