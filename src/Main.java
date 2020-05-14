// File: Main.java
// Author(s): Mark Link
// Date: 05/12/2020
// Description:
// This program will compute the lowest cost to get from one airport to another, and has an option to show only results
// for one specific airline or all airlines.

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        System.out.println("Welcome to the flight manager!");
        System.out.println("You are guaranteed to find the lowest price for any flight!");
        System.out.print("Initializing flight data... ");

        ArrayList<Airline> all_airlines = new ArrayList<>();

        Scanner infile;
        //try opening the input file
        try {
            infile = new Scanner(new FileReader("Flights.csv"));
        } catch (FileNotFoundException e) {
            //file unable to open
            System.out.println("Unable to open file!");
            return;
        }

        //skip header
        infile.nextLine();

        int lineCounter = 1;

        //loop through data, line by line
        while(infile.hasNextLine()) {
            boolean foundAirline = false;
            boolean routeFound = false;
            lineCounter += 1;
            String airline = "";
            String departure = "";
            String arrival = "";
            double cost = 0;
            //split the line into an array
            String line = infile.nextLine();
            String[] lineArray = line.split(",");
            if(lineArray.length != 4){
                System.out.println("Error: Invalid amount of arguments on line " + lineCounter + " of Flights.csv");
                return;
            }
            airline = lineArray[0].strip();
            departure = lineArray[1].strip();
            arrival = lineArray[2].strip();
            cost = Double.parseDouble(lineArray[3]);

            Airline curLine = null;
            //creates new airline if necessary
            if(!all_airlines.isEmpty()){
                for(Airline airline1: all_airlines){
                    //airline matches
                    if(airline1.getName().equalsIgnoreCase(airline)){
                        foundAirline = true;
                        curLine = airline1;
                    }
                }
                //airline match not found, make new airline
                if(!foundAirline){
                    curLine = new Airline(airline, 0);
                    all_airlines.add(curLine);
                }
            }
            //base case, create new airline
            else{
                curLine = new Airline(airline, 0);
                all_airlines.add(curLine);
            }

            //airport not yet in airline
            if(curLine.getAirportByName(departure) == null){
                Airport ap = new Airport(departure);
                ap.addRoute(arrival, cost);
                curLine.addAirport(ap);
            }
            //airport is in airline, check if flight is in routes
            else{
                Airport ap = curLine.getAirportByName(departure);
                //loop through routes
                for(Map.Entry e: ap.getRoutes().entrySet()){
                    String arr = (String) e.getKey();
                    double c = (double) e.getValue();
                    //route already in airline
                    if(arr.equalsIgnoreCase(arrival)){
                        //price is lower, replace
                        if(c > cost){
                            ap.getRoutes().replace(arrival, cost);
                            routeFound = true;
                        }
                    }
                }
                //did not find route, add it to list
                if(!routeFound){
                    ap.addRoute(arrival, cost);
                }
            }
            //airport not yet in airline
            if(curLine.getAirportByName(arrival) == null){
                Airport ap = new Airport(arrival);
                curLine.addAirport(ap);
            }
        }

        System.out.println("initialization complete.");
        String go_again = "y";

        //Main loop
        while(go_again.equalsIgnoreCase("y")) {
            System.out.println();
            System.out.println("---------- Flight Manager ----------");
            Scanner inputs = new Scanner(System.in);
            System.out.print("Enter departure airport: ");
            String initial_airport = inputs.nextLine().strip();
            System.out.print("Enter arrival airport: ");
            String destination_airport = inputs.nextLine().strip();
            System.out.print("Enter desired airline (NA if no preference): ");
            String preferred_airline = inputs.nextLine().strip();
            System.out.println();

            //TODO: add option for max number of connections

            boolean airlineFound = false;
            boolean flightsFound = false;
            Airline search_flights = null;

            //Only look for flights on one airline.
            if (!preferred_airline.equalsIgnoreCase("NA")) {
                for (Airline line : all_airlines) {
                    //Airline found
                    if (line.getName().equalsIgnoreCase(preferred_airline)) {
                        search_flights = line;
                        airlineFound = true;
                    }
                }
                //No such airline exists
                if (!airlineFound) {
                    System.out.println("Error: Could not find desired airline.");
                    System.out.println();
                }
                //Airline found, searching for flights
                else {
                    Airport src = search_flights.getAirportByName(initial_airport);
                    Airport dest = search_flights.getAirportByName(destination_airport);
                    //Departure airport non-existent
                    if (src == null) {
                        System.out.println("Error: Could not find the airport " + initial_airport + " on " + preferred_airline + ".");
                        System.out.println();
                    }
                    //Arrival airport non-existent
                    else if (dest == null) {
                        System.out.println("Error: Could not find the airport " + destination_airport + " on " + preferred_airline + ".");
                        System.out.println();
                    }
                    //Both airports found
                    else {
                        //If no possibles routes from one airport to the other
                        if (!search_flights.dijkstras(src, dest)) {
                            System.out.println("There are no flights from " + initial_airport + " to " + destination_airport + " on " + preferred_airline + ".");
                            System.out.println();
                        } else System.out.println();
                    }
                }
            }
            //Searches all airlines
            else {
                for (Airline line : all_airlines) {
                    Airport src = line.getAirportByName(initial_airport);
                    Airport dest = line.getAirportByName(destination_airport);
                    //Both airports found in current airline
                    if (src != null && dest != null) {
                        //Route between them found
                        if (line.dijkstras(src, dest)) {
                            flightsFound = true;
                            System.out.println();
                        }
                    }
                }
                //No routes found on any airline
                if (!flightsFound) {
                    System.out.println("There are no flights from " + initial_airport + " to " + destination_airport + " on any airline.");
                    System.out.println();
                }
            }
            System.out.print("Do you wish to search again? (y/n): ");
            go_again = inputs.nextLine().strip();
            //Reset dijkstra's identifiers for all airlines
            for(Airline line: all_airlines){
                line.clean();
            }
        }
        System.out.println();
        System.out.println("Thank you for using Flight Manager!");
        return;
    }

}
