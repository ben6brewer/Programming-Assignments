import java.sql.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
import java.util.List;

class testing {
    public static void main(String[] args) {
        String csvLine = ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
        String query = getAddTupleString(csvLine, "table");
        System.out.println(query);
    }

    private static String getAddTupleString(String line, String table) {
        List<String> values = Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
        StringBuilder retval = new StringBuilder("INSERT INTO " + table + " VALUES(");

        // Adjusted to consider the first column (index 0) as numeric along with others
        List<Integer> numericColumnsIndexes = Arrays.asList(0, 4, 6, 7, 8, 9, 10, 11, 12, 13); // Assuming these are the numeric columns
        int maxValuesCount = 14; // Maximum number of values allowed
        for (int i = 0; i < values.size() && i < maxValuesCount; i++) {
            if (i > 0) {
                retval.append(", ");
            }

            String value = values.get(i).trim();

            if (value.isEmpty()) {
                value = "NULL";
            } else if (numericColumnsIndexes.contains(i)) {
                // For numeric columns, remove quotes and commas, and leave it unquoted if it's numeric
                value = value.replaceAll("[',\"]", "");
            } else if (value.startsWith("\"") && value.endsWith("\"")) {
                // Handle strings that are enclosed in quotes properly
                value = "'" + value.substring(1, value.length() - 1).replace("'", "''") + "'";
            } else if (!numericColumnsIndexes.contains(i)) {
                // For non-numeric columns not enclosed in quotes, ensure proper handling of internal quotes
                value = "'" + value.replace("'", "''") + "'";
            }

            retval.append(value);
        }

        // Fill the rest with NULLs if less than 14 values are present
        int currentValuesCount = Math.min(values.size(), maxValuesCount);
        for (int j = currentValuesCount; j < maxValuesCount; j++) {
            if (j > 0) {
                retval.append(", ");
            }
            retval.append("NULL");
        }

        retval.append(")");

        return retval.toString();
    }




}