/**
 * Created by Gau on 27.04.2017.
 */

public class JDBrun {
    public static void main(String[] args) {
        JDB jdb =  new JDB();
        //jdb.addData();
        //jdb.getData();
        //jdb.createSchema(jdb.schemaName);

//        jdb.createTable("sensor_Kuchnia");
//        jdb.createTable("sensor_Jadalnia");
//        jdb.createTable("sensor_Lazieka");
//        jdb.createTable("sensor_Sypialnia");

//        jdb.delateTable("SENSORJADALNIA");
//        jdb.delateTable("SENSORKUCHNIA");
//        jdb.delateTable("SENSORLAZIEKA");

        //System.out.println(jdb.listTablesHTTP());
        jdb.closeStatements();
        jdb.closeDatabase();
    }
}
