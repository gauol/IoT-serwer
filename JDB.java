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

    JDB() {
        go();
    }

    void go() {
        Server.print("Uruchamiam derby " + framework + " mode");
        conn = null;
        statements = new ArrayList<>(); // list of Statements, PreparedStatements

        try {
            String dbName = "derbyDB"; // the name of the database
            conn = DriverManager.getConnection(protocol + dbName
                    + ";create=false", getProperties());
            Server.print("Connected to database " + dbName);

            conn.setAutoCommit(false);

            s = conn.createStatement();
            statements.add(s);

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public static void printSQLException(SQLException e) {
        while (e != null) {
            Server.print("\n----- SQLException -----");
            Server.print("  SQL State:  " + e.getSQLState());
            Server.print("  Error Code: " + e.getErrorCode());
            Server.print("  Message:    " + e.getMessage());

            e = e.getNextException();
        }
    }

    public void createSchema(String schemaName) {
        try {
            // We create a table...
            s.execute("create schema : " + schemaName);
            Server.print("Created schema : " + schemaName);
        } catch (SQLException sqlex) {
            printSQLException(sqlex);
        }
    }

    public void createTable(String tableName) {
        try {
            s.execute("create table " + schemaName + "." + tableName + " (temp1 float, temp2 float, Czas time, dzien int, miesiac int, rok int)");
            Server.print("Created table: " + tableName);
        } catch (SQLException sqlex) {
            printSQLException(sqlex);
        }
    }

    public void delateTable(String tableName) {
        try {
            // We create a table...
            s.execute("drop table " + schemaName + "." + tableName);
            Server.print("Dropped table: " + tableName);
        } catch (SQLException sqlex) {
            printSQLException(sqlex);
        }
    }

    public void closeDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            if (((se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState())))) {
                Server.print("Derby shut down normally");
            } else {
                System.err.println("Derby did not shut down normally");
                printSQLException(se);
            }
        }
    }

    private Properties getProperties() {
        Properties props = new Properties(); // connection properties

        props.put("user", "user1");
        props.put("password", "user1");
        return props;
    }

    public void closeStatements() {
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
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet resultSet = dbmd.getTables(
                    "derbyDB", schemaName, "%", null);
            while (resultSet.next()) {
                String strTableName = resultSet.getString("TABLE_NAME");
                Server.print("TABLE_NAME is " + strTableName + " schema - " + resultSet.getString(2));
            }
            Server.printRln("separator");
        } catch (SQLException sqlex) {
            printSQLException(sqlex);
        }

    }

    public ArrayList<String> listTablesToArray() {
        ArrayList<String> rsp = new ArrayList<>();
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet resultSet = dbmd.getTables(
                    "derbyDB", schemaName, "%", null);

            while (resultSet.next()) {
                rsp.add(resultSet.getString("TABLE_NAME"));
            }
            return rsp;
        } catch (SQLException sqlex) {
            printSQLException(sqlex);
            rsp.add("Brak sensorów!");
            return rsp;
        }

    }

    public void addData(String tableName, float value1, float value2) {
        try {
            Czas t = new Czas();
            psInsert = conn.prepareStatement(
                    "insert into " + schemaName + "." + tableName + " values (?, ?, ?, ?, ?, ?)");    //(temp1 float, temp2 float, Czas time, dzien int, miesiac int, rok int)
            statements.add(psInsert);

            psInsert.setFloat(1, value1);
            psInsert.setFloat(2, value2);
            psInsert.setTime(3, new Time(t.getMsTime()));
            psInsert.setInt(4, t.getDzien());
            psInsert.setInt(5, t.getMonth());
            psInsert.setInt(6, t.getYear());
            psInsert.executeUpdate();

            Server.print("Zapisano odczyt z sensora: " + tableName + " : " + value1 + " : " + value2);
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public void getData(String tableName) {
        try {
            ResultSet rs = s.executeQuery(
                    "SELECT * FROM "+ schemaName + "." + tableName + " ORDER BY rok, miesiac, dzien, Czas");            //(temp float, Czas time, dzien int, miesiac int, rok int)
            while (rs.next()) {
                Server.print(rs.getFloat(1) + " " + rs.getFloat(2) + " : " + rs.getString(3));
            }

            if (rs != null) {
                rs.close();
                rs = null;
            }

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public String getDataHTML(String tableName) {
        StringBuilder str = new StringBuilder();
        try {
            ResultSet rs = s.executeQuery(
                    "SELECT * FROM "+ schemaName + "." + tableName + " ORDER BY rok, miesiac, dzien, Czas");            //(temp float, Czas time, dzien int, miesiac int, rok int)
            while (rs.next()) {
                String eventDate = rs.getTime(3).toString() + " " + rs.getInt(4) + "-" + rs.getInt(5) + "-" + rs.getInt(6);
                str.append( ",\r\n['" + eventDate + "', " + rs.getFloat(1) +", "+rs.getFloat(2) +"]");
            }

            if (rs != null) {
                rs.close();
                rs = null;
            }

            return str.toString();

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
        return "FATAL ERROR";
    }

    public void deleteData(String tableName) {
        try {
            ResultSet rs = s.executeQuery(
                    "DELETE FROM "+ schemaName + "." + tableName);            //(temp float, Czas time, dzien int, miesiac int, rok int)

            if (rs != null) {
                rs.close();
                rs = null;
            }

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public void executeQuery(String query) {
        try {
            ResultSet rs = s.executeQuery(
                    query);            //(temp float, Czas time, dzien int, miesiac int, rok int)
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }
}
