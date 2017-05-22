import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by Gau on 27.04.2017.
 */

public class JDBrun {
    private static Random random = new Random();
    private static DecimalFormat df = new DecimalFormat("#,#");
    public static void main(String[] args) throws InterruptedException {
        JDB jdb =  new JDB();
        //jdb.addData();
        //jdb.getData();
        //jdb.createSchema(jdb.schemaName);

//        jdb.createTable("Kuchnia");
//        jdb.createTable("Jadalnia");
//        jdb.createTable("Lazienka");
//        jdb.createTable("Sypialnia");

//        jdb.delateTable("JADALNIA");
//        jdb.delateTable("KUCHNIA");
//        jdb.delateTable("LAZIENKA");
//        jdb.delateTable("SYPIALNIA");
        //System.out.println(jdb.listTablesHTTP());

//        jdb.createTable("Lazienka");
        String tableNejm = "Sypialnia";
//        jdb.deleteData(tableNejm);

        jdb.addData(tableNejm, nextFloat(),nextFloat());
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(),nextFloat());
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(),nextFloat());
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(),nextFloat());
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(),nextFloat());
        jdb.getData(tableNejm);

        jdb.listTables();
        jdb.closeStatements();
        jdb.closeDatabase();
    }

    private static float nextFloat()
    {
        float min =-15, max=35;
        return round(min + random.nextFloat() * (max - min),2);
    }

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
