import java.io.File;
import java.io.PrintStream;


public class CustomPrint {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new CustomPrint();
  }
  
  public CustomPrint(){
    
    try{
      File extlog = new File("C:\\Users\\Darkkazma\\Desktop\\CustomPrint.log");
      PrintStream printstream = new PrintStream(extlog){
        @Override
        public void println(String str){
          super.println("["+getTimeString()+"] ## " + str);
        }
      };
      
      System.setOut(printstream);
      System.setErr(printstream);
      
    }catch(Exception ea){
      ea.printStackTrace();
    }
    
    
   }
  
  
  public static String getTimeString() {
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("HH:mm:ss", java.util.Locale.KOREA);
    return formatter.format(new java.util.Date());
}

}
