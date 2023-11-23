package busTrips;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

// This class is responsible for parsing the GTFS files.
// Right now its contains 4 methods that are specific to the assignment.
class GTFSParser {

    // Given a stop id, return the name of the stop.
    // Reads through the "stops.txt" file in the "gtfs" folder.
    // if the stop id does not exist, return an empty string.
    public static String getStopName(String stopId) throws FileNotFoundException, IOException {
        String fileName = "gtfs/stops.txt";
        GTFSReader reader = new GTFSReader(fileName);
        reader.readHeaders();

        HashMap<String, String> line;

        while ((line = reader.nextLine()) != null) {
            if (line.get("stop_id").equals(stopId)) {
                return line.get("stop_name");
            }
        }
        return "";
    }

    // Given a stop id, return a HashMap that maps trip id to arrival time.
    // Reads through the "stop_times.txt" file in the "gtfs" folder.
    public static HashMap<String, String> parseTripStops(String stopId) throws IOException, ParseException {
        String fileName = "gtfs/stop_times.txt";
        GTFSReader reader = new GTFSReader(fileName);
        HashMap<String, String> TripStopTime = new HashMap<String, String>();

        reader.readHeaders();
        HashMap<String, String> line;

        while ((line = reader.nextLine()) != null) {
            // if the stop id does not match the one we are looking for we can skip it
            if (line.get("stop_id").equals(stopId)) {
                TripStopTime.put(line.get("trip_id"), line.get("arrival_time"));
            }
        }

        return TripStopTime;
    }

    // Given a HashMap that maps trip id to arrival time, return a HashMap that
    // maps route id to a RouteStops object. The RouteStops object contains all the
    // stop times for that route.
    // Reads through the "trips.txt" file in the "gtfs" folder.
    public static HashMap<String, RouteStops> parseRoutes(HashMap<String, String> TripStopTime, int numberOfBuses)
            throws IOException {
        String fileName = "gtfs/trips.txt";
        GTFSReader reader = new GTFSReader(fileName);

        // This maps routeId to a RouteStops object where we store all the stop times.
        HashMap<String, RouteStops> routes = new HashMap<String, RouteStops>();

        reader.readHeaders();
        HashMap<String, String> line;

        while ((line = reader.nextLine()) != null) {
            // if trip does not stop at the station we are looking for we can skip it
            if (!TripStopTime.containsKey(line.get("trip_id"))) {
                continue;
            }

            // When we find a route that already exists.
            if (routes.containsKey(line.get("route_id"))) {
                RouteStops currentRoute = routes.get(line.get("route_id"));
                String tripStopTime = TripStopTime.get(line.get("trip_id"));
                currentRoute.addStop(tripStopTime);

                // If the route does not exist, we want to create a new route
            } else {
                String routeId = line.get("route_id");
                RouteStops route = new RouteStops(routeId);
                String tripStopTime = TripStopTime.get(line.get("trip_id"));
                route.addStop(tripStopTime);
                routes.put(routeId, route);
            }
        }

        for (String routeId : routes.keySet()) {
            RouteStops currentRoute = routes.get(routeId);
            currentRoute.filterTimes(numberOfBuses);
        }

        return routes;
    }

    // Assigns a route name to each RouteStops object by reading through the
    // "routes.txt" file in the "gtfs" folder.
    public static void parseRouteName(HashMap<String, RouteStops> routes) throws IOException {
        String fileName = "gtfs/routes.txt";
        GTFSReader reader = new GTFSReader(fileName);

        reader.readHeaders();
        HashMap<String, String> line;

        while ((line = reader.nextLine()) != null) {
            if (routes.containsKey(line.get("route_id"))) {
                RouteStops currentRoute = routes.get(line.get("route_id"));
                currentRoute.setRouteName(line.get("route_short_name"));
            }
        }
    }
}
