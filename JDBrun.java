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
//        jdb.createTable("Lazieka");
//        jdb.createTable("Sypialnia");

//        jdb.delateTable("JADALNIA");
//        jdb.delateTable("KUCHNIA");
//        jdb.delateTable("LAZIEKA");
//        jdb.delateTable("SYPIALNIA");
        //System.out.println(jdb.listTablesHTTP());

//        jdb.createTable("Lazienka");
        String tableNejm = "JADALNIA";
//        jdb.deleteData(tableNejm);
        jdb.addData(tableNejm, nextFloat(-13,45));
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(-13,45));
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(-13,45));
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(-13,45));
        Thread.sleep(10000);
        jdb.addData(tableNejm, nextFloat(-13,45));
       //Thread.sleep(10000);

        jdb.getData(tableNejm);

        //jdb.listTables();
        jdb.closeStatements();
        jdb.closeDatabase();
    }

    private static float nextFloat(float min, float max)
    {
        return (min + random.nextFloat() * (max - min));
    }
}
