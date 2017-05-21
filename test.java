import java.text.NumberFormat;
import java.util.Scanner;

/**
 * Created by Gaua on 02.05.2017.
 */
public class test {
    public static void main(String[] args) {
        String s = "?in=2";
        int inte = Integer.parseInt(s.replaceAll("[\\D]", ""));
        System.out.println(inte);
    }
}
