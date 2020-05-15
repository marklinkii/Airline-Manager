## Airline-Manager

This program implements dijkstra's algorithm to find the lowest cost path from one airport to another for every airline. It will read from an input file (Flights.csv) and populate both Airline and Airport objects. From here an infinite loop will run, asking if you wish to continue searching for flights. 

The input file (Flights.csv) is provided with a few examples. If you wish to add to the existing file or create a new file, the format is as follows:

- File name: Flights.csv (must be this exact name and placed inside the same subdirectory as Main.java)
- File format:
  - Airline name, Departure airport, Arrival airport, Price
  - For example: Delta, ATL, MIA, 220.30

To run, download all files and navigate to the folder where Main.java is being held. Then, type "javac \*.java" to compile the code. Finally, type "java Main" to start the program. From here, enter the inputs.

- Inputs:
  - Departure airport in the same format as the input file (e.g. ATL)
  - Arrival airport in the same format as the input file (e.g. MIA)
  - Preferred airline in the same format as the input file; enter 'na' if no preference (e.g. Delta)
  - Search again (y/n)
  
If you do not specify an airline, it will show the lowest cost route for each airline from the departure airport to the arrival airport.

Here is a sample session using the data in the given input file:

>Welcome to the flight manager!
>You are guaranteed to find the lowest price for any flight!
>Initializing flight data... initialization complete.
>
>---------- Flight Manager ----------
>
>Enter departure airport: ATL
>
>Enter arrival airport: SEA
>
>Enter desired airline (NA if no preference): Delta
>
>----------------- Delta -----------------
>
>Flight itinerary from ATL to SEA:
>
>ATL --> SFO --> IAH --> SEA
>
>The total cost for this trip is: $720.0
>
>----------------------------------------.
>
>Do you wish to search again? (y/n): n
>
>Thank you for using Flight Manager!
