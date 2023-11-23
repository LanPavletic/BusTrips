package busTrips;

import java.util.HashMap;

// This is the main class of the program.
// Usage: java <className> <station id> <number of buses> <absolute | relative>
public class App {
    public static void main(String[] args) {
        if (args.length != 3) {
            printUsage();
            System.exit(1);
        }
        String stationId = args[0];
        int numberOfBuses = 0;

        try {
            numberOfBuses = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            handleNumberFormatExeption("The second argument must be a number.");
        }

        validateArgument(args[2]);

        boolean isAbsolute = args[2].equals("absolute");

        String stopName = "";
        HashMap<String, RouteStops> routes = new HashMap<String, RouteStops>();
        HashMap<String, String> lineStops = new HashMap<String, String>();

        try {
            stopName = GTFSParser.getStopName(stationId);
            lineStops = GTFSParser.parseTripStops(stationId);
            routes = GTFSParser.parseRoutes(lineStops, numberOfBuses);
            GTFSParser.parseRouteName(routes);

        } catch (Exception e) {
            handleException(e);
        }

        if (stopName.isEmpty()) {
            System.out.println("The station id you entered does not exist.");
            System.exit(1);
        }

        for (RouteStops rs : routes.values()) {
            System.out.println(rs.toString(isAbsolute));
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java <className> <station id> <number of buses> <absolute | relative>");
    }

    private static void handleNumberFormatExeption(String msg) {
        System.out.println(msg);
        System.exit(1);
    }

    private static void validateArgument(String arg) {
        if (!arg.equals("absolute") && !arg.equals("relative")) {
            handleNumberFormatExeption("The third argument must be either 'absolute' or 'relative'.");
        }
    }

    private static void handleException(Exception e) {
        System.out.println("An error occured while parsing the GTFS files.");
        System.out.println(e.getMessage());
        System.exit(1);
    }
}
