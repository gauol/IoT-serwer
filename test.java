import java.text.NumberFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gaua on 02.05.2017.
 */
public class test {
    public static void main(String[] args) {
        String inte = "SEN=LAZIENKA:TEMP1=32.3:TEMP2=33.0";
        Pattern p = Pattern.compile("(SEN=)(\\D+):(TEMP1=)(\\d+.\\d+):(TEMP2=)(\\d+.\\d+)");
        Matcher m = p.matcher(inte);
        if(m.find()) {
            String id = m.group(2);
            String temp1 = m.group(4);
            String temp2 = m.group(6);
            System.out.println("Odebrano Dane : SEN = "+id+" : " + Float.parseFloat(temp1) +" : "+ Float.parseFloat(temp2));
        }
    }
}
