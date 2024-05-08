/*
 * Prog3.java -- This program creates a frontend view for the user
 *               to pull data from an sql database. It promps the user
 *               to enter information. With this information a sql
 *               query is ran and data is collected to be presented
 *               to the user in the terminal.
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
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;

/*+----------------------------------------------------------------------
 ||
 ||         Class: Prog3
 ||
 ||         Author:  Ben Brewer
 ||
 ||        Purpose:  Gathers and displays data from SQL queries based on user input.
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
 ||                  void calculateFacilitiesQuantityChange(Connection dbConnect)
 ||                  void calculateGreatCircleNavigation(Connection dbConnect)
 ||                  void calculateTopTenEmissions(Connection dbConnect)
 ||                  void calculateCustomQuery(Connection dbConnect)
 ||                  void closeConnections(Connection dbConnect, Statement stmt)
 ||
 ||  Inst. Methods:  int getUserChoiceQuery()
 ||                  double[] getFacilityCoords(Connection dbConnect, int year, int facId)
 ||                  
 ||
 ++-----------------------------------------------------------------------*/
public class Prog3 {

    public static void main(String[] args) {
        try {
            Connection dbConnect = establishOracleConnection(); // Connection to Oracle
            Scanner scanner = new Scanner(System.in); // statement to execute queries

            while (true) {
                int choice = getUserChoiceQuery(); // choice from user input

                switch (choice) {
                    case 1:
                        calculateFacilitiesQuantityChange(dbConnect);
                        break;
                    case 2:
                        calculateGreatCircleNavigation(dbConnect);
                        break;
                    case 3:
                        calculateTopTenEmissions(dbConnect);
                        break;
                    case 4:
                        calculateCustomQuery(dbConnect);
                        break;
                    case 5:
                        System.out.println("Exiting.");
                        closeConnections(dbConnect, null);
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------
        |  Method: getUserChoiceQuery()
        |
        |  Purpose: gets users choice for query to execute
        |
        |  Pre-condition: None.
        |
        |  Post-condition: None.
        |
        |  Parameters: None.
        |
        |  Returns: int choice - choice 1-5 of user
        *-------------------------------------------------------------------*/
    private static int getUserChoiceQuery() {
        Scanner scanner = new Scanner(System.in); // scanner to read user input
        int choice = 0; // user choice to be reassigned
        while (true) {
            try {
                System.out.println();
                System.out.println("Select an option:");
                System.out.println("1. Calculate facilities quantity percentage change");
                System.out.println("2. Calculate great circle navigation between two facilities");
                System.out.println("3. Calculate top ten emissions for a year");
                System.out.println("4. Calculate a specific emission threshold for a year");
                System.out.println("5. Exit");
                System.out.print("Choice: ");
                choice = scanner.nextInt();
                System.out.println();

                if (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5) {
                    break;
                } else {
                    System.out.println();
                    System.out.println("Please enter a number 1-5.");
                }
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("Invalid input. Please enter a number 1-5.");
                scanner.next();
            }
        }
        return choice;
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
        |  Method: calculateFacilitiesQuantityChange(Connection dbConnect)
        |
        |  Purpose: calculates the change in the quantity of facilities between the 4 years
        |
        |  Pre-condition: Oracle SQL database tables exist and are populated,
        |                 Oracle Connection is valid
        |
        |  Post-condition: Data is gathered and displayed in terminal
        |
        |  Parameters: Connection dbConnect - oracle connection
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void calculateFacilitiesQuantityChange(Connection dbConnect) {
        int[] years = {2010, 2014, 2018, 2022}; // 4 years of tables

        for (int i = 0; i < years.length - 1; i++) {
            int year1 = years[i]; // first year to compare from
            int year2 = years[i + 1]; // second year to compare to
            String table1Name = "ghgp_" + year1; // table 1 name
            String table2Name = "ghgp_" + year2; // table 2 name

            try (Statement stmt = dbConnect.createStatement()) {
                ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) AS count FROM " + table1Name); // gets number of facilities in table1
                rs1.next();
                int count1 = rs1.getInt("count"); // number of facilities in table1
                
                ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) AS count FROM " + table2Name); // gets number of facilities in table2
                rs2.next();
                int count2 = rs2.getInt("count"); // number of facilities in table2

                double percentageChange = ((double) count2 - count1) / count1 * 100; // calculates the change in percent between the two tables
                System.out.printf("From %d to %d, the quantity of facilities changed by %.2f%%\n", year1, year2, percentageChange);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*---------------------------------------------------------------------
        |  Method: calculateGreatCircleNavigation(Connection dbConnect)
        |
        |  Purpose: calculates distance between to facilities using given equation
        |
        |  Pre-condition: Oracle SQL database tables exist and are populated,
        |                 Oracle Connection is valid
        |
        |  Post-condition: Data is gathered and displayed in terminal
        |
        |  Parameters: Connection dbConnect - oracle connection
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void calculateGreatCircleNavigation(Connection dbConnect) {
        Scanner scanner = new Scanner(System.in); // scanner to read user input
        int year = 0; // Initialize year with a default value to be reassigned
        
        while (true) {
            try {
                System.out.println("Enter the year (2010, 2014, 2018, or 2022):");
                year = scanner.nextInt();

                if (year == 2010 || year == 2014 || year == 2018 || year == 2022) {
                    break;
                } else {
                    System.out.println("Invalid year entered. Please enter year 2010, 2014, 2018, or 2022");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid year entered. Please enter year 2010, 2014, 2018, or 2022");
                scanner.next();
            }
        }

        String facIdStr1; // intialize fac_id1 string to be reassigned
        int facId1= 0; // intialize fac_id1 int to be reassigned
        boolean validIdEntered = false; // used to check if an id is valid

        while (!validIdEntered) {
            System.out.println("Enter the first facility ID:");
            facIdStr1 = scanner.next();

            // checks if id is valid
            if (facIdStr1.length() == 7 && facIdStr1.matches("\\d+")) {
                validIdEntered = true;
                facId1 = Integer.parseInt(facIdStr1);
            } else {
                System.out.println("Invalid ID entered. The ID must exist and be exactly 7 digits long.");
            }
        }

        String facIdStr2; // intialize fac_id2 string to be reassigned
        int facId2 = 0; // intialize fac_id2 int to be reassigned
        validIdEntered = false; // update value to false and do same operations on fac_id2

        while (!validIdEntered) {
            System.out.println("Enter the second facility ID:");
            facIdStr2 = scanner.next();

            // checks if id is valid
            if (facIdStr2.length() == 7 && facIdStr2.matches("\\d+")) {
                validIdEntered = true;
                facId2 = Integer.parseInt(facIdStr2);
            } else {
                System.out.println("Invalid ID entered. The ID must exist and be exactly 7 digits long.");
            }
        }
        
        // gets coords based on facID
        double[] fac1Coords = getFacilityCoords(dbConnect, year, facId1); // facId1 coords
        double[] fac2Coords = getFacilityCoords(dbConnect, year, facId2); // facId2 coords

        // converts to radians
        if (fac1Coords != null && fac2Coords != null) {
            double lat1 = Math.toRadians(fac1Coords[0]); // radians of facId1 latitude
            double lon1 = Math.toRadians(fac2Coords[1]); // radians of facId1 longitude
            double lat2 = Math.toRadians(fac2Coords[0]); // radians of facId2 latitude
            double lon2 = Math.toRadians(fac2Coords[1]); // radians of facId2 longitude

            // Great circle distance calculation given in project spec
            double dist = Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                                    Math.cos(lat1) * Math.cos(lat2) *
                                    Math.cos(lon1 - lon2)); // calcultes the angle
            double value = Math.toDegrees(dist) * 60; // converts back to degrees
            BigDecimal bd = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP); // takes value to nearest 4 decimal places
            double roundedValue = bd.doubleValue(); // converts value to a double
            System.out.println("The great circle navigation distance is: " + roundedValue + " nautical miles.");
        } else {
            System.out.println("One or both facilities could not be found.");
        }
    }

    /*---------------------------------------------------------------------
        |  Method: getFacilityCoords(Connection dbConnect, int year, int facId)
        |
        |  Purpose: helper function of calculateGreatCircleNavigation, getter for facility coords
        |
        |  Pre-condition: Oracle SQL database tables exist and are populated,
        |                 Oracle Connection is valid,
        |                 facility exists
        |
        |  Post-condition: None.
        |
        |  Parameters: Connection dbConnect - oracle connection
        |              int year - year of data to gather
        |              int facId - facility ID to gather data on
        |
        |  Returns: double[] {lat, lon} - latitude and longitude of facility ID
        *-------------------------------------------------------------------*/
    private static double[] getFacilityCoords(Connection dbConnect, int year, int facId) {
        String query = String.format("SELECT latit, longit FROM ghgp_%d WHERE fac_id = ?", year); // query to build
        try (PreparedStatement stmt = dbConnect.prepareStatement(query)) {
            stmt.setInt(1, facId);
            ResultSet rs = stmt.executeQuery(); // result of query

            if (rs.next()) {
                double lat = rs.getDouble("latit"); // latitude of facility ID
                double lon = rs.getDouble("longit"); // longitude of facility ID
                return new double[]{lat, lon};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*---------------------------------------------------------------------
        |  Method: calculateTopTenEmissions(Connection dbConnect)
        |
        |  Purpose: calcultes the top 10 Emissions for all the years
        |
        |  Pre-condition: Oracle SQL database tables exist and are populated,
        |                 Oracle Connection is valid
        |
        |  Post-condition: Data is displayed in terminal.
        |
        |  Parameters: Connection dbConnect - oracle connection
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void calculateTopTenEmissions(Connection dbConnect) {
        int[] years = {2010, 2014, 2018, 2022}; // all years of tables
        
        for (int year : years) {
            System.out.println();
            System.out.println("Year: " + year);
            System.out.println("Top 10 Total Reported Direct Emission Facilities");
            System.out.println("------------------------------------------------");
            System.out.println("Facility Name\t\tState\tTotal Emissions");
            
            String query = String.format("SELECT fac_name, state, total_emissions " +
                        "FROM ghgp_%s " +
                        "ORDER BY total_emissions DESC", year); // query to execute
                        
            try (PreparedStatement stmt = dbConnect.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery(); // result of query
                
                int count = 0; // count used to only get the top 10 as they are sorted in descending order
                while (rs.next() && count < 10) {
                    String name = rs.getString("fac_name"); // name of facility
                    String state = rs.getString("state"); // state of facility
                    double totalEmissions = rs.getDouble("total_emissions"); // number of total emissions
                    System.out.printf("%s\t\t%s\t%.2f\n", name, state, totalEmissions);
                    count++;
                }
                System.out.println("------------------------------------------------\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*---------------------------------------------------------------------
        |  Method: calculateCustomQuery(Connection dbConnect)
        |
        |  Purpose: Calcultes the my own custom query. This query acts as a
        |           search for an emission on a year above a threshold for that
        |           emission.
        |
        |  Pre-condition: Oracle SQL database tables exist and are populated,
        |                 Oracle Connection is valid
        |
        |  Post-condition: Data is displayed in terminal.
        |
        |  Parameters: Connection dbConnect - oracle connection
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void calculateCustomQuery(Connection dbConnect) {
        Scanner scanner = new Scanner(System.in); //  scanner to read data
        String emissionType = ""; // initialize emission type to be reassigned
        boolean validEmissionTypeEntered = false; // boolean state to check if a valid emission type was entered

        // gets emission type from user: CO2, CH4, or N2O
        while (!validEmissionTypeEntered) {
            System.out.println("Enter emission type (CO2, CH4, or N2O):");
            emissionType = scanner.next().toUpperCase();
            if (emissionType.equals("CO2") || emissionType.equals("CH4") || emissionType.equals("N2O")) {
                validEmissionTypeEntered = true;
            } else {
                System.out.println("Invalid emission type entered. Please enter CO2, CH4, or N2O.");
            }
        }

        // gets emissions threshold
        System.out.println("Enter the minimum emissions threshold:");
        while (!scanner.hasNextDouble()) {
            System.out.println("That's not a valid number. Please enter a valid decimal number.");
            scanner.next();
            System.out.println("Enter the minimum emissions threshold:");
        }

        double threshold = scanner.nextDouble(); // emissions theshold entered by user
        int[] years = {2010, 2014, 2018, 2022}; // all 4 of the years we have tables for
        
        String emissionColumn = switch (emissionType) { // sets emissionColumn to match the name in sql table based on user input
            case "CO2" -> "CO2_emissions";
            case "CH4" -> "CH4_emissions";
            case "N2O" -> "N2O_emissions";
            default -> "";
        };

        for (int year : years) {
            String tableName = "ghgp_" + year; // table name to do search on
            String query = "SELECT fac_name, state, " + emissionColumn +
                        " FROM " + tableName +
                        " WHERE " + emissionColumn + " > ?" +
                        " ORDER BY " + emissionColumn + " DESC"; // query to execute

            try (PreparedStatement stmt = dbConnect.prepareStatement(query)) {
                stmt.setDouble(1, threshold);
                ResultSet rs = stmt.executeQuery(); // result set from query

                int count = 0; // Counter to keep track of the number of results printed
                boolean hasResults = false; // boolean state to make sure results were gathered
                while (rs.next() && count < 5) { // Iterate over the results and limit to top 5
                    if (!hasResults) {
                        System.out.println();
                        System.out.println("Year " + year + " - Top 5 Facilities with " + emissionType + "_emissions greater than " + threshold + ":");
                        System.out.println("------------------------------------------------");
                        System.out.println("Facility Name\tState\t" + emissionType + " Emissions");
                        hasResults = true;
                    }
                    String facilityName = rs.getString("fac_name"); // getter for facility name
                    String state = rs.getString("state"); // getter for facility state
                    double emissions = rs.getDouble(emissionColumn); // getter for emissions amount
                    
                    System.out.printf("%s\t%s\t%.2f\n", facilityName, state, emissions);
                    count++;
                }
                
                if (!hasResults) {
                    System.out.println("No facilities found for year " + year + " with " + emissionType + " emissions greater than " + threshold + ".");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
}