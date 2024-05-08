/*
 * PopulateTables.java -- This program reads in the csv files given from the class
 *                        directory (ghgp_2010.csv, ghgp_2014.csv, ghgp_2018.csv, ghgp_2022.csv).
 *                        They all have the same column headers. This program will connect to an
 *                        oracle db and create / populate the tables using the given csvs.
 *
 * Author:     Ben Brewer
 * Instructor: Lester McCann
 * T/A:        Jake Bode, Pri Nayak, and Ahmad Musa
 * Course:     CSC 460 Database Design
 *
 * First Version: 01/29/2024
 * Final Version: 01/29/2024
 */
import java.sql.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/*+----------------------------------------------------------------------
 ||
 ||  Class PopulateTables
 ||
 ||         Author:  Ben Brewer
 ||
 ||        Purpose:  populates oracle SQL tables using given csv's
 ||
 ||  Inherits From:  None.
 ||
 ||     Interfaces:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  Just the default constructor; no arguments.
 ||
 ||  Class Methods:  Connection establishOracleConnection()
 ||                  void closeConnections(Connection dbConnect, Statement stmt)
 ||                  void writeBinary(ArrayList<ArrayList<String>> csvContent, String fileName)
 ||                  void addTuplesToTable(String table, Statement stmt)
 ||                  void checkAndDropTableIfExists(Statement stmt, String tableName)
 ||
 ||  Inst. Methods:  String getInitializeTableString(String tableName)
 ||                  String getAddTupleString(String line, String table)
 ||                  void setSelectToPublic(Statement stmt, String tableName)
 ||
 ++-----------------------------------------------------------------------*/
public class PopulateTables {
    
    public static void main(String[] args) {
        try {
            Connection dbConnect = establishOracleConnection(); // Connection to Oracle
            Statement stmt = dbConnect.createStatement(); // statement to execute queries

            String[] tableNames = {"ghgp_2010", "ghgp_2014", "ghgp_2018", "ghgp_2022"}; // hard coded table names from class directory

            // drop tables if they already exist in my sql database
            for (String table : tableNames) {
                checkAndDropTableIfExists(stmt, table);
            }

            // initialize the tables with column headers
            for (String table : tableNames) {
                stmt.executeQuery(getInitializeTableString(table));
            }

            // add each tuple row to the correct table
            for (String table : tableNames) {
                addTuplesToTable(table, stmt);
            }

            // grant permissions to public so TA can grade it
            for (String table : tableNames) {
                setSelectToPublic(stmt, table);
            }
            
            // program is done, close connections to oracle
            closeConnections(dbConnect, stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*---------------------------------------------------------------------
        |  Method: establishOracleConnection()
        |
        |  Purpose:  connects to oracle using my oracle credentials and DriverManager
        |
        |  Pre-condition:  The credentials provided are correct
        |
        |  Post-condition: dbConnect can be used as a Connection object
        |
        |  Parameters: None.
        |
        |  Returns: Connection dbConnect - connection created
        *-------------------------------------------------------------------*/
    private static Connection establishOracleConnection() throws SQLException {
        Connection dbConnect = null; // connection to be assigned
        try {
            dbConnect = DriverManager.getConnection(
                    "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle", 
                    "ben6brewer", "a0128");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return dbConnect;
    }

    /*---------------------------------------------------------------------
        |  Method: closeConnections(Connection dbConnect, Statement stmt)
        |
        |  Purpose:  closes oracle connection
        |
        |  Pre-condition:  A connection to oracle already exists
        |
        |  Post-condition: The established connection to oracle will be closed
        |
        |  Parameters: Connection dbConnect - connection to close
        |              Statement stmt - statement to close
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void closeConnections(Connection dbConnect, Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        if (dbConnect != null) {
            dbConnect.close();
        }
    }

    /*---------------------------------------------------------------------
        |  Method: getInitializeTableString(String tableName)
        |
        |  Purpose:  getter for String to execute sql command to initialize table.
        |
        |  Pre-condition:  All of the column names match up to the csv files to parse
        |
        |  Post-condition: None.
        |
        |  Parameters: String tableName - name of table to create sql table for
        |
        |  Returns: String - command as string to create table
        *-------------------------------------------------------------------*/
    private static String getInitializeTableString(String tableName) {
        return "CREATE TABLE " + tableName +
            " (fac_id INTEGER, " +
            "fac_name VARCHAR2(500), " +
            "city VARCHAR2(500), " +
            "state VARCHAR2(500), " +
            "zip NUMBER(20,10), " +
            "addy VARCHAR2(500), " +
            "latit NUMBER(20,10), " +
            "longit NUMBER(20,10), " +
            "total_emissions NUMBER(20,10), " +
            "co2_emissions NUMBER(20,10), " +
            "ch4_emissions NUMBER(20,10), " +
            "n2o_emissions NUMBER(20,10), " +
            "stationary_combustion NUMBER(20,10), " +
            "electricity_gen NUMBER(20,10), " +
            "PRIMARY KEY (fac_id))";
    }

    /*---------------------------------------------------------------------
        |  Method: addTuplesToTable(String table, Statement stmt)
        |
        |  Purpose: executes queries to add the tuples from the csv files to
        |           the sql tables
        |
        |  Pre-condition: Oracle connection is already established
        |
        |  Post-condition: Tuples will be added to table
        |
        |  Parameters: String table - name of table to add to
        |              Statement stmt - statement to execute query on
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void addTuplesToTable(String table, Statement stmt) {
        try {
            File csvFile = new File(table + ".csv"); // csv file
            Scanner scanner = new Scanner(csvFile); // scanner to read csv file
            scanner.nextLine(); // skips column header row
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // current tuple line of csv file 
                String addTupleString = getAddTupleString(line, table); // String of tuples to add
                if (!addTupleString.toUpperCase().contains("VALUES(NULL")) { // if it starts with null it is not a valid tuple
                    try {
                        stmt.executeQuery(addTupleString);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------
        |  Method: getAddTupleString(String line, String table)
        |
        |  Purpose: getter for the String of query to execute
        |
        |  Pre-condition: None.
        |
        |  Post-condition: None.
        |
        |  Parameters: String line - tuple line from csv file
        |              String table - name of table to add to
        |              
        |  Returns: String retval - insert into query string to execute sql command
        *-------------------------------------------------------------------*/
    private static String getAddTupleString(String line, String table) {
        List<String> values = Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")); // splits the tuple by ',' and stores them in array
        StringBuilder retval = new StringBuilder("INSERT INTO " + table + " VALUES("); // string to append to and return
        List<Integer> numericColumnsIndexes = Arrays.asList(0, 4, 6, 7, 8, 9, 10, 11, 12, 13); // columns in tuple that are numbers
        int maxValuesCount = 14; // correct number of columns, used to add NULL values
        for (int i = 0; i < values.size() && i < maxValuesCount; i++) {
            if (i > 0) {
                retval.append(", ");
            }

            String value = values.get(i).trim(); // getter for specific value in tuple

            // adds the value to string
            if (value.isEmpty()) {
                value = "NULL";
            } else if (numericColumnsIndexes.contains(i)) {
                value = value.replaceAll("[',\"]", "");
            } else if (value.startsWith("\"") && value.endsWith("\"")) {
                value = "'" + value.substring(1, value.length() - 1).replace("'", "''") + "'";
            } else if (!numericColumnsIndexes.contains(i)) {
                value = "'" + value.replace("'", "''") + "'";
            }
            retval.append(value);
        }
        int currentValuesCount = Math.min(values.size(), maxValuesCount); // gets the number of items in tuple
        // adds extra null values if needed
        for (int j = currentValuesCount; j < maxValuesCount; j++) {
            if (j > 0) {
                retval.append(", ");
            }
            retval.append("NULL");
        }
        
        retval.append(")");
        return retval.toString();
    }

    /*---------------------------------------------------------------------
        |  Method: setSelectToPublic(Statement stmt, String tableName)
        |
        |  Purpose: grants permissions of sql tables to public for TA grading
        |
        |  Pre-condition: table already exists, oracle statement is established
        |
        |  Post-condition: Tables will be public
        |
        |  Parameters: Statement stmt - statement to execute queries on
        |              String tableName - name of table to grant permissions on
        |              
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void setSelectToPublic(Statement stmt, String tableName) {
        try {
            String grantQuery = "GRANT SELECT ON " + tableName + " TO PUBLIC"; // sql command as string to execute
            stmt.executeQuery(grantQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------
        |  Method: checkAndDropTableIfExists(Statement stmt, String tableName)
        |
        |  Purpose: drops tables if they already exist within sql database. 
        |           This was needed because I had already created the tables
        |           and needed to edit / remake them.
        |
        |  Pre-condition: None.
        |
        |  Post-condition: tableName will be dropped from sql database if it exists
        |
        |  Parameters: Statement stmt - statement to execute queries on
        |              String tableName - name of table to drop
        |              
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void checkAndDropTableIfExists(Statement stmt, String tableName) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM user_tables WHERE table_name = '" + tableName.toUpperCase() + "'");
            if (rs.next()) {
                stmt.executeQuery("DROP TABLE " + tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}