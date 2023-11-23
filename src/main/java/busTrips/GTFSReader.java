package busTrips;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

// This is a simple class that can read a GTFS file (or any CSV file)
// and allow you to access the data by column name instead of index.
// GTFSReader is more or less a wrapper around BufferedReader.
class GTFSReader {
    BufferedReader reader;

    // This maps column names to indices so we can access the data by column name
    HashMap<String, Integer> columnIndices;

    public GTFSReader(String filePath) throws FileNotFoundException, IOException {
        reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
        columnIndices = new HashMap<String, Integer>();
    }

    public void readHeaders() throws IOException {
        String header;
        byte[] headerBytes = reader.readLine().getBytes();

        // This bug took me way to long to figure out.
        // The BOM is present in the GTFS files so we need to check for it
        // and remove it.
        if (headerBytes.length >= 3 &&
                headerBytes[0] == (byte) 0xEF &&
                headerBytes[1] == (byte) 0xBB &&
                headerBytes[2] == (byte) 0xBF) {

            // Remove the BOM by creating a new string with the offset of 3 bytes
            header = new String(headerBytes, 3, headerBytes.length - 3, StandardCharsets.UTF_8);
        } else {
            header = new String(headerBytes, StandardCharsets.UTF_8);
        }

        String[] headers = header.split(",");

        for (int i = 0; i < headers.length; i++) {
            columnIndices.put(headers[i], i);
        }
    }

    // Just like BufferedReader.readLine() but returns a HashMap instead of a String
    // The HashMaps keys are the column names and the values are the values in the
    // column
    public HashMap<String, String> nextLine() throws IOException {
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        String[] tokens = line.split(",", -1);

        HashMap<String, String> map = new HashMap<String, String>();

        for (String columnName : columnIndices.keySet()) {
            map.put(columnName, tokens[columnIndices.get(columnName)]);
        }

        return map;
    }

    public String GetHeaders() {
        return columnIndices.toString();
    }
}
