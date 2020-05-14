import java.util.*;

public class Airline {

    private int numAirports;
    private ArrayList<Airport> airports;
    private String name;

    /*
     * Constructor for airline object. Takes in the airline's name and number of airports
     */
    Airline(String name, int numAirports){
        this.name = name;
        this.numAirports = numAirports;
        this.airports = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getNumAirports(){
        return this.numAirports;
    }

    public void addAirport(Airport airport){
        airports.add(airport);
        numAirports += 1;
    }

    public Airport getAirportByName(String name){
        for(Airport a: airports){
            if(a.getName().equalsIgnoreCase(name)) return a;
        }
        return null;
    }

    /*
     * Member function that implements dijkstra's algorithm to print the lowest cost route from one airport to another
     * Returns true if a route can be formed and false if not
     */
    public boolean dijkstras(Airport source, Airport destination){
        Queue<Airport> toVisit = new LinkedList<>();
        ArrayList<Airport> toRemove = new ArrayList<>();
        //initialize the queue of airports to scan
        for(Map.Entry next: source.getRoutes().entrySet()){
            String nextName = (String) next.getKey();
            Airport nextVisit = getAirportByName(nextName);
            toVisit.add(nextVisit);
        }
        Airport cur = source;
        Airport next = null;
        double cost = 0;
        cur.setRelativeCost(cost);
        //goes until no airports are left to visit (all known)
        while(!toVisit.isEmpty()){
            //cost of the previous vertex
            cost = cur.getRelativeCost();
            double lowestCost = Double.MAX_VALUE;
            //check all connecting flights and choose lowest cost flight to be next vertex if not known
            for(Airport connection: toVisit){
                //airport is connected to previous airport
                if(cur.getRoutes().containsKey(connection.getName())) {
                    //if cost of previous airport to this one is less than the total cost so far of routes to that airport
                    if(cur.getRoutes().get(connection.getName()) < connection.getRelativeCost()) {
                        connection.setRelativeCost(cost + cur.getRoutes().get(connection.getName()));
                        connection.setPath(cur.getName());
                    }
                    //finds the lowest cost route from the previous airport to determine which airport to visit next
                    if (cur.getRoutes().get(connection.getName()) < lowestCost && !connection.isKnown()) {
                        lowestCost = cur.getRoutes().get(connection.getName());
                        next = connection;
                    }
                    else if(!connection.isKnown()){
                        continue;
                    }
                    else{
                        toRemove.add(connection);
                    }
                }
                //airport is not connected to previous airport
                else{
                    toRemove.add(connection);
                    //last in queue, make it the next airport to visit
                    if(toVisit.isEmpty()) next = connection;
                }
            }
            for(Airport a: toRemove){
                toVisit.remove(a);
            }
            toRemove = new ArrayList<>();
            toVisit.remove(next);
            next.setKnown(true);
            //if the next airport to visit has routes available
            if(!next.getRoutes().isEmpty()) {
                //loop through the available routes
                for (Map.Entry add : next.getRoutes().entrySet()) {
                    String nextName = (String) add.getKey();
                    Airport nextVisit = getAirportByName(nextName);
                    //ensure we aren't backtracking to the original airport
                    if(!nextName.equalsIgnoreCase(source.getName())) {
                        //airport already visited or in the queue
                        if (toVisit.contains(nextVisit) || nextVisit.isKnown()) {
                            //only update if cost is lower
                            if (next.getRoutes().get(nextVisit.getName()) + next.getRelativeCost() < nextVisit.getRelativeCost()) {
                                nextVisit.setPath(next.getName());
                                nextVisit.setRelativeCost(next.getRoutes().get(nextVisit.getName()) + next.getRelativeCost());
                            }
                        }
                        //airport hasn't been visited and isn't in queue
                        else {
                            nextVisit.setRelativeCost(next.getRoutes().get(nextVisit.getName()) + next.getRelativeCost());
                            toVisit.add(nextVisit);
                        }
                    }
                }
            }
            cur = next;

            //nothing in queue, make sure all airports are known
            if(toVisit.isEmpty()){
                for(Airport a: airports){
                    for (Map.Entry add : a.getRoutes().entrySet()) {
                        String nextName = (String) add.getKey();
                        Airport nextVisit = getAirportByName(nextName);
                        //unknown airport, add to queue and set previous to airport that goes there
                        if(!nextVisit.isKnown()){
                            toVisit.add(nextVisit);
                            cur = a;
                        }
                    }
                }
            }
        }

        //dijkstra's path finished, find/print path
        ArrayList<String> pathToDest = new ArrayList<>();
        Airport iterAir = destination;

        //no path from source to destination
        if(iterAir.getPath().equalsIgnoreCase("null")){
            return false;
        }

        //backtrack the path and add airport names to list
        while(!iterAir.getPath().equalsIgnoreCase("null")){
            pathToDest.add(0, iterAir.getName());
            iterAir = getAirportByName(iterAir.getPath());
        }

        //add the source to the front
        pathToDest.add(0, source.getName());

        //creates/fills the itinerary string
        String result = "";
        for(String path: pathToDest){
            if(path.equalsIgnoreCase(destination.getName())) result += path;
            else result += path + " --> ";
        }
        printHeader();
        System.out.println("Flight itinerary from " + source.getName() + " to " + destination.getName() + ":");
        System.out.println(result);
        System.out.println("The total cost for this trip is: $"+destination.getRelativeCost());
        if(name.length()%2==1) System.out.println("-----------------------------------------");
        else System.out.println("----------------------------------------");
        return true;
    }

    /*
     * Member function clean; resets all of the airports' dijkstra identifiers
     * In order to successfully run dijkstra's on more than one occasion per airline.
     */
    public void clean(){
        for(Airport airport: airports){
            airport.setPath("null");
            airport.setKnown(false);
            airport.setRelativeCost(Double.MAX_VALUE);
        }
    }

    @Override
    public String toString(){
        return "Name: " + name + ", Number of airports: " + numAirports;
    }

    /*
     * Prints a consistent header for all airlines based on length
     * of the airline's name
     */
    private void printHeader(){
        int dashNum = (40-name.length())/2;
        for(int i = 0; i < dashNum; i++){
            System.out.print("-");
        }
        if(name.length()%2==1) System.out.print(" ");
        System.out.print(name);
        if(name.length()%2==1) System.out.print(" ");
        for(int i = 0; i < dashNum; i++){
            System.out.print("-");
        }
        System.out.println();
    }

}

