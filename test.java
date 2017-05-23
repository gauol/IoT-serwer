import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Gaua on 02.05.2017.
 */
public class test {
    public static void main(String[] args) {
        List<String> myArrayList = new ArrayList<String>();
        myArrayList.add("raz");
        myArrayList.add("dwa");
        String[] myArray = myArrayList.toArray(new String[myArrayList.size()]);
        System.out.println(myArray.toString());
    }
}
