/**
 * Created by Gau on 27.04.2017.
 */

public class JDBrun {
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

        jdb.deleteData("KUCHNIA");
        jdb.addData("KUCHNIA", (float) 13.2);
        Thread.sleep(10000);
        jdb.addData("KUCHNIA", (float) 23.3);
        Thread.sleep(10000);
        jdb.addData("KUCHNIA", (float) 21.1);
        Thread.sleep(10000);
        jdb.addData("KUCHNIA", (float) 2.0);
        Thread.sleep(10000);
        jdb.addData("KUCHNIA", (float) 12.9);
        Thread.sleep(10000);

        jdb.getData("KUCHNIA");

        //jdb.listTables();
        jdb.closeStatements();
        jdb.closeDatabase();
    }
}
