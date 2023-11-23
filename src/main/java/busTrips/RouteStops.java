package busTrips;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

// This class stores the information about a specific route
// and the stop times on a specific station.
public class RouteStops {
    private String routeId;
    private String routeName;
    private List<String> stops;

    public RouteStops(String routeId) {
        this.routeId = routeId;
        this.stops = new ArrayList<String>();
    }

    public String toString(boolean isAbsolute) {
        StringBuilder sb = new StringBuilder();
        sb.append(routeName + ": ");
        for (int i = 0; i < stops.size(); i++) {
            String stop = stops.get(i);

            // How we format the time depends on the argument passed to the program
            if (isAbsolute) {
                LocalTime time = LocalTime.parse(stop);
                stop = time.truncatedTo(ChronoUnit.MINUTES).toString();
            } else {
                LocalTime currentTime = LocalTime.now();
                LocalTime stopTime = LocalTime.parse(stop);
                long minutesDifference = currentTime.until(stopTime, ChronoUnit.MINUTES);
                stop = Long.toString(minutesDifference) + "min";
            }

            sb.append(stop);
            if (i != stops.size() - 1) {
                sb.append(", ");
            }
        }

        if (stops.size() == 0) {
            sb.append("No buses in the next 2 hours");
        }

        return sb.toString();
    }

    // This method will sort the stops by time and remove stops that are more than 2
    // hours away from now.
    // Also it will only return the first numberOfBuses stops.
    public void filterTimes(int numberOfBuses) {
        Collections.sort(stops, new TimeComparator());

        // we need to parse this time and check if its withn 2 hourse from now.
        LocalTime now = LocalTime.now();

        Iterator<String> iterator = stops.iterator();

        while (iterator.hasNext()) {
            LocalTime arrivalTime = LocalTime.parse(iterator.next());
            // Calculate the time difference in hours
            long minutesDifferance = ChronoUnit.MINUTES.between(now, arrivalTime);

            // Remove times more than 2 hours away from the current time
            if (minutesDifferance >= 120 || minutesDifferance < 0) {
                iterator.remove();
            }
        }

        // Get only the first number of requested buses
        stops = stops.subList(0, numberOfBuses);
    }

    static class TimeComparator implements Comparator<String> {
        @Override
        public int compare(String time1, String time2) {
            LocalTime localTime1 = LocalTime.parse(time1);
            LocalTime localTime2 = LocalTime.parse(time2);
            return localTime1.compareTo(localTime2);
        }
    }

    public String getRouteId() {
        return routeId;
    }

    public List<String> getStops() {
        return stops;
    }

    public void addStop(String stopId) {
        stops.add(stopId);
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    public int size() {
        return stops.size();
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

}
