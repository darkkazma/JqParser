import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class ByteTEST {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    new ByteTEST();
  }
  
  public ByteTEST(){
    
    try{
      /*
      File stFile = new File("C:\\Users\\Darkkazma\\Desktop\\text.txt");
      StringBuffer sb = new StringBuffer();
      char[] c = new char[(int)stFile.length()];
      BufferedReader br = new BufferedReader(new FileReader(stFile));
      br.read(c);
      sb.append(c);
      
      System.out.println(sb.toString());
     System.out.println("길이     : " +sb.toString().length());
     System.out.println("byte길이 : " + sb.toString().getBytes().length);
      */
      
      String data = "                                                                                      ";
      System.out.println(data.length());
      System.out.println(data.getBytes().length);
    }catch(Exception ea){
      ea.printStackTrace();
    }
  }

}
