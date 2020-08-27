import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

public class ReplaceColumnValues {

  private static Connection DBcon = null;
  
  //preparedstatement로 치환
    static class ReplaceColumnAndLongValues extends ExpressionDeParser {

        // 여기도 Colum / Value 로 분리 해야 되는데...
        @Override
        public void visit(StringValue stringValue) {
          if( stringValue.toString().length() > 100 ){  
            this.getBuffer().append("?");
          }else{ this.getBuffer().append(stringValue); }
        }

        @Override
        public void visit(LongValue longValue) {
            this.getBuffer().append(longValue);
        }
        
       
        @Override
        public void visit(Function function){
          this.getBuffer().append(function);
        }
       
        
    }

    public static String cleanStatement(String sql) throws JSQLParserException {
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expr = new ReplaceColumnAndLongValues();
        SelectDeParser selectDeparser = new SelectDeParser(expr, buffer);
        expr.setSelectVisitor(selectDeparser);
        expr.setBuffer(buffer);
        StatementDeParser stmtDeparser = new StatementDeParser(expr, selectDeparser, buffer);
        Statement stmt = CCJSqlParserUtil.parse(sql);
        stmt.accept(stmtDeparser);
        
        return stmtDeparser.getBuffer().toString();
    }

    public static void main(String[] args) throws JSQLParserException {
        
        try{
         
          File qFile = new File("C:\\Users\\darkkazma\\desktop\\query.sql");
          BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(qFile)));
          String sql = br.readLine(); br.close();
          
          CCJSqlParserManager pm = new CCJSqlParserManager();
          Statement stmt = pm.parse(new StringReader(sql));         // whereMap을 위한 분석
          final HashMap<String, String> whereMap = new HashMap<String, String>();
          // WHERE 절 분리 완료 ( Colum : Value )
          Expression where = ((Delete)stmt).getWhere();
          if( where != null ){
            
            where.accept(new ExpressionVisitorAdapter(){
              @Override
              public void visit(AndExpression exp){
                
                Expression left = exp.getLeftExpression();
                Expression right = exp.getRightExpression();
                
                if( left instanceof AndExpression || left instanceof OrExpression || left instanceof Parenthesis ){
                  left.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) left).getLeftExpression();
                    Expression value = ((BinaryExpression) left).getRightExpression();
                    if(!(value instanceof Function))
                      whereMap.put(column.toString(), value.toString());
                    
                    if( column.toString().indexOf("TEST_COLUMN_02") != -1 ){
                      System.out.println("FIND COLUMN");
                      left = null;
                    }
                  }catch(Exception ea){}
                }
                
                if( right instanceof AndExpression || right instanceof OrExpression || right instanceof Parenthesis ){
                  right.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) right).getLeftExpression();
                    Expression value = ((BinaryExpression) right).getRightExpression();
                    if(!(value instanceof Function))
                    whereMap.put(column.toString(), value.toString());
                    if( column.toString().indexOf("TEST_COLUMN_02") != -1 ){
                      System.out.println("FIND COLUMN");
                      right = null;
                    }
                  }catch(Exception ea){}
                }
                
              }
              
              @Override
              public void visit(OrExpression exp){
               
                Expression left = exp.getLeftExpression();
                Expression right = exp.getRightExpression();
                
                if( left instanceof AndExpression || left instanceof OrExpression || left instanceof Parenthesis ){
                  left.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) left).getLeftExpression();
                    Expression value = ((BinaryExpression) left).getRightExpression();
                    if(!(value instanceof Function))
                      whereMap.put(column.toString(), value.toString());
                    if( column.toString().indexOf("TEST_COLUMN_02") != -1 ){
                      left = null;
                    }
                  }catch(Exception ea){}
                }
                
                if( right instanceof AndExpression || right instanceof OrExpression || right instanceof Parenthesis ){
                  right.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) right).getLeftExpression();
                    Expression value = ((BinaryExpression) right).getRightExpression();
                    if(!(value instanceof Function))
                      whereMap.put(column.toString(), value.toString());
                    if( column.toString().indexOf("TEST_COLUMN_02") != -1 ){
                      right = null;
                    }
                  }catch(Exception ea){}
                }
              }
              
              @Override
              public void visit(EqualsTo exp){
                try{
                  Expression column = ((BinaryExpression) exp).getLeftExpression();
                  Expression value = ((BinaryExpression) exp).getRightExpression();
                  if(!(value instanceof Function))
                    whereMap.put(column.toString(), value.toString());
                }catch(Exception ea){}
              }
              
            });
          }
          
          System.out.println(stmt);
          
          System.out.println("");
          System.out.println("---------------------------------------------------------------------------------------------------------");
          System.out.println("");
          
          //final HashMap<String, String> removeMap = new HashMap<String, String>();
          /*
          StringBuilder buffer = new StringBuilder();
          ExpressionDeParser expr = new ReplaceColumnAndLongValues();
          SelectDeParser selectDeparser = new SelectDeParser(expr, buffer);
          expr.setSelectVisitor(selectDeparser);
          expr.setBuffer(buffer);
          StatementDeParser stmtDeparser = new StatementDeParser(expr, selectDeparser, buffer);
          Statement stmts = CCJSqlParserUtil.parse(sql);
          stmts.accept(stmtDeparser);
          String newQuery = stmtDeparser.getBuffer().toString();
          Statement newstmt = pm.parse(new StringReader(newQuery));         // whereMap을 위한 분석
          Expression wheres = ((Delete)newstmt).getWhere();
          
          if( wheres != null ){
            
            wheres.accept(new ExpressionVisitorAdapter(){
              @Override
              public void visit(AndExpression exp){
                
                Expression left = exp.getLeftExpression();
                Expression right = exp.getRightExpression();
                
                if( left instanceof AndExpression || left instanceof OrExpression || left instanceof Parenthesis ){
                  left.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) left).getLeftExpression();
                    Expression value = ((BinaryExpression) left).getRightExpression();
                    if( value != null ){
                      if( value instanceof StringValue){ if(!value.toString().equals("?")){whereMap.remove(column.toString());} }
                    }
                  }catch(Exception ea){}
                }
                
                if( right instanceof AndExpression || right instanceof OrExpression || right instanceof Parenthesis ){
                  right.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) right).getLeftExpression();
                    Expression value = ((BinaryExpression) right).getRightExpression();
                    if( value instanceof StringValue){ if(!value.toString().equals("?")){whereMap.remove(column.toString());} }
                    
                    
                  }catch(Exception ea){}
                }
                
              }
              
              @Override
              public void visit(OrExpression exp){
               
                Expression left = exp.getLeftExpression();
                Expression right = exp.getRightExpression();
                
                if( left instanceof AndExpression || left instanceof OrExpression || left instanceof Parenthesis ){
                  left.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) left).getLeftExpression();
                    Expression value = ((BinaryExpression) left).getRightExpression();
                    if( value instanceof StringValue){ if(!value.toString().equals("?")){whereMap.remove(column.toString());} }
                  }catch(Exception ea){}
                }
                
                if( right instanceof AndExpression || right instanceof OrExpression || right instanceof Parenthesis ){
                  right.accept(this);
                }else{
                  try{
                    Expression column = ((BinaryExpression) right).getLeftExpression();
                    Expression value = ((BinaryExpression) right).getRightExpression();
                    if( value instanceof StringValue){ if(!value.toString().equals("?")){whereMap.remove(column.toString());} }
                  }catch(Exception ea){}
                }
                
              }
              
            });
          }
          
          
          Iterator<String> coliter = whereMap.keySet().iterator();
          while( coliter.hasNext() ){
            String column = coliter.next();
            Object value = whereMap.get(column);
            System.out.println("removeMap ["+column+"] | ["+value+"]");
          }
          
          
          System.out.println(newQuery);
          DBcon = OracleDBConMng.getInstance().getConnection("192.168.0.176", 1521, "orcl", "system", "oracle");
          PreparedStatement delete_pstmt = DBcon.prepareStatement(newQuery);
          //PreparedStatement delete_pstmt = new LoggableStatement(DBcon, newQuery);
          coliter = whereMap.keySet().iterator();
          int pidx = 1;
          while( coliter.hasNext() ){
            String column = coliter.next();
            String value = whereMap.get(column);
            System.out.println("ORG : ["+value+"]");
            value = value.substring(1, value.length() - 1);
            System.out.println("NEW : ["+value+"]");
            StringReader sr = new StringReader( value );
            delete_pstmt.setCharacterStream(pidx, sr, value.length());
            
            delete_pstmt.setString(pidx, value);
            pidx ++;
            System.out.println("stream call " + value.length());
          }
          delete_pstmt.executeUpdate();
          System.out.println("DELETE QUERY EXEC OK.");
          */
          
          
          
          
        }catch(Exception ea){ ea.printStackTrace(); }
    }
}
