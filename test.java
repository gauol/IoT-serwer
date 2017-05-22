import java.text.NumberFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gaua on 02.05.2017.
 */
public class test {
    public static void main(String[] args) {
        String inte = "<SEN=LAZIENKA:TEMP1=2.3:TEMP2=-3.0>";
        Pattern p = Pattern.compile("<(SEN=)(\\D+):(TEMP1=)(.?\\d+.\\d+):(TEMP2=)(.?\\d+.\\d+)>");
        Matcher m = p.matcher(inte);
        if(m.find()) {
            String id = m.group(2);
            String temp1 = m.group(4);
            String temp2 = m.group(6);
            Server.print("Odebrano Dane : SEN = "+id+" : " + temp1 +" : "+ temp2);
        }
    }
}
