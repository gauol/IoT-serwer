import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Gau on 27.04.2017.
 */

public class JDB {
    public final String schemaName = "SENSORDB";
    private String framework = "embedded";
    private String protocol = "jdbc:derby:";
    private PreparedStatement psInsert;
    private Statement s;
    private Connection conn;
    private ArrayList<Statement> statements;

    JDB(){
        go();
    }

    void go()
    {
        System.out.println("Uruchamiam derby" + framework + " mode");
        conn = null;
        statements = new ArrayList<>(); // list of Statements, PreparedStatements

        try
        {
            String dbName = "derbyDB"; // the name of the database
            conn = DriverManager.getConnection(protocol + dbName
                    + ";create=false", getProperties());
            System.out.println("Connected to database " + dbName);

            conn.setAutoCommit(false);

            s = conn.createStatement();
            statements.add(s);

        }
        catch (SQLException sqle)
        {
            printSQLException(sqle);
        }
    }

    public static void printSQLException(SQLException e)
    {
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());

            e = e.getNextException();
        }
    }

    public void createSchema(String schemaName){
        try {
            // We create a table...
            s.execute("create schema : " + schemaName);
            System.out.println("Created schema : "+ schemaName);
        }catch (SQLException sqlex){
            printSQLException(sqlex);
        }
    }

    public void createTable(String tableName){
        try {
            // We create a table...
            s.execute("create table " +schemaName+"."+ tableName +" (num int, addr varchar(40))");
            System.out.println("Created table: " + tableName);
        }catch (SQLException sqlex){
            printSQLException(sqlex);
        }
    }

    public void delateTable(String tableName){
        try {
            // We create a table...
            s.execute("drop table " + schemaName+"."+tableName);
            System.out.println("Dropped table: " + tableName);
        }catch (SQLException sqlex){
            printSQLException(sqlex);
        }
    }

    public void closeDatabase(){
        try
        {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (SQLException se)
        {
            if (( (se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState()) ))) {
                System.out.println("Derby shut down normally");
            } else {
                System.err.println("Derby did not shut down normally");
                printSQLException(se);
            }
        }
    }

    private Properties getProperties(){
        Properties props = new Properties(); // connection properties

        props.put("user", "user1");
        props.put("password", "user1");
        return props;
    }

    public void getData(){
        System.out.println("Odczytuje dane z tabeli");
        try {
            ResultSet rs = s.executeQuery(
                    "SELECT num, addr FROM location ORDER BY num");
        while(rs.next()) {
            System.out.println(rs.getInt(1)+" "+rs.getString(2));
        }

            if (rs != null) {
                rs.close();
                rs = null;
            }

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public void closeStatements(){
        int i = 0;
        while (!statements.isEmpty()) {

            Statement st = statements.remove(i);
            try {
                if (st != null) {
                    st.close();
                    st = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
        }
        try {
            conn.commit();
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public void listTables() {
        try{
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet resultSet = dbmd.getTables(
                    "derbyDB", schemaName, "%", null);
            while (resultSet.next()) {
                String strTableName = resultSet.getString("TABLE_NAME");
                System.out.println("TABLE_NAME is " + strTableName +" schema - "+resultSet.getString(2));
            }
            Server.printRln("separator");
        }catch (SQLException sqlex){
            printSQLException(sqlex);
        }

    }

    public String listTablesHTTP() {
        try{
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet resultSet = dbmd.getTables(
                    "derbyDB", schemaName, "%", null);
            StringBuilder rsp = new StringBuilder();
            while (resultSet.next()) {
                String strTableName = resultSet.getString("TABLE_NAME");
                String element = "<a class=\"w3-bar-item w3-button\" href=\"?id=5\">"+strTableName+"</a>\r\n";
                //System.out.println(element);
                rsp.append(element);
            }
            //Server.printRln("separator");
            return rsp.toString();
        }catch (SQLException sqlex){
            printSQLException(sqlex);
        }
        return "<a class=\"w3-bar-item w3-button\" href=\"?id=5\">"+"Brak sensorów"+"</a>\r\n";
    }

    public void addData(String tableName){
        try {
            psInsert = conn.prepareStatement(
                    "insert into" + tableName + "values (?, ?)");
            statements.add(psInsert);

            psInsert.setInt(1, 1956);
            psInsert.setString(2, "Webster St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1956 Webster");

            psInsert.setInt(1, 1910);
            psInsert.setString(2, "Union St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1910 Union");
        }
        catch (SQLException sqle){
            printSQLException(sqle);
        }
    }

//    public Class czujnik {
//
//        ArrayList<pomiar> pomiary = new ArrayList<pomiar>;
//
//
//    }
//
//    private Class pomiar{
//        double wartosc;
//        czas czas;
//    }
}
