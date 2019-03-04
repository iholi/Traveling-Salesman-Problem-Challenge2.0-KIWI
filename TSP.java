import java.io.*;
import java.util.*;

public class TSP {
	static ArrayList<String> f = new ArrayList<String>();
	static ArrayList<String> t = new ArrayList<String>();
	static ArrayList<String> d = new ArrayList<String>();
	static ArrayList<String> c = new ArrayList<String>();
	static ArrayList<String> airports = new ArrayList<String>();
	static ArrayList<String> airports2 = new ArrayList<String>();
	static ArrayList<String> areas = new ArrayList<String>();
	static Map<String, String> pairs = new HashMap<>();
	static ArrayList<String> officialTour = new ArrayList<String>();

	static int number_of_areas;
	static String[] arr0Str;
	static int[][] matrix;
	static int[][][] cost_matrix;
	static Scanner in = null;
	static int date, cost;

	static int totalCost = Integer.MAX_VALUE;

	static ArrayList<String> tempDestinations = new ArrayList<String>();
	static ArrayList<String> tempTour = new ArrayList<String>();
	static ArrayList<String> officialDestinations = new ArrayList<String>();

	public static void main(String[] args) {
		String oneline;
		String area, airport;

		try {
			in = new Scanner(System.in);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		oneline = in.nextLine();
		arr0Str = oneline.split(" ", 2);

		number_of_areas = Integer.parseInt(arr0Str[0]);
		System.out.println(" ");
		System.out.println("The Number of Areas is " + number_of_areas);

		/*
		 * Enter the airports and areas
		 */

		for (int i = 0; i < number_of_areas; i++) {
			area = in.nextLine();
			airport = in.nextLine();

			pairs.put(area, airport);

			airports.add(airport);
			airports2.add(airport);
			areas.add(area);

		}

		int n1 = pairs.size();
		int n2 = n1;
		cost_matrix = new int[number_of_areas + 1][n1][n2];

		/*
		 * Enter the cost between airports
		 * 
		 */

		while (in.hasNextLine()) {
			oneline = in.nextLine();
			if (oneline.length() > 2) {
				String[] arrOLine = oneline.split(" ", 4);
				f.add(arrOLine[0]);
				t.add(arrOLine[1]);
				d.add(arrOLine[2]);
				c.add(arrOLine[3]);
				fillMatrix(cost_matrix);
				System.out.println(oneline);
			}

			else {
				break;
			}

			System.out.println(" ");

		}

		System.out.println("Matrix Size" + pairs.size());

		System.out.println("Done");
		for (int i = 0; i <= number_of_areas; i++) {
			System.out.println("Matrix Cost of Day" + i);
			System.out.println(airports);
			displayMatrix(cost_matrix[i]);
			System.out.println(" ");
		}


		generateInitialTravel();
		
		System.out.println("The old tour will be ");
		System.out.println(officialDestinations);
		System.out.println(officialTour);

		totalCost = getTotalCost(officialTour);
		System.out.println("The Original Cost is " + totalCost);

		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");

		anealing(0.05, 10, cost_matrix);
		System.out.print(officialTour);

	}

	private static void fillMatrix(int[][][] cost_matrix) {
		/*
		 * matrix["AB0"]["AB1"] = c[i] airports = [...] areas = [...] A[0][1] = from AB0
		 * to AB1
		 * 
		 */

		for (int i = 0; i < cost_matrix.length; i++) {
			for (int j = 0; j < cost_matrix[1].length; j++) {
				for (int k = 0; k < cost_matrix[1][1].length; k++) {
					cost_matrix[i][j][k] = Integer.MAX_VALUE; // correspond to Infinity
				}
			}
		}

		String de, ar;
		for (int i = 0; i < f.size(); i++) {
			de = f.get(i);
			ar = t.get(i);
			date = Integer.parseInt(d.get(i));
			cost = Integer.parseInt(c.get(i));

			cost_matrix[date][airports.indexOf(de)][airports.indexOf(ar)] = cost;
		}
	}

	private static void displayMatrix(final int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print(airports.get(i) + " ");
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	/*
	 * 
	 * Simulated Annealing Method
	 * 
	 * Generating the Initial Travel
	 * 
	 */

	private static String generateInitialTravel() {
		// Start point arr0Str
		String startDeparture = arr0Str[1];
		int departure = airports.indexOf(startDeparture);
		int days = 1;
		int days0 = 0;
		int cost, cost0;
		String tcost, tcost0;
		boolean zero = d.contains("0");
		
		officialDestinations.add(airports.get(0));
		tempDestinations.add(airports.get(0));

		for (int i = 0; i < number_of_areas; i++) {
			System.out.println("i is " + i);
			System.out.println("Array size is " + airports);
			String departure1 = airports2.get(departure);
			System.out.println("Departure: " + departure1);
			System.out.println("Index:" + departure);

			// Randomize arraylist of airports
			Random airports_list = new Random();
			String arrivalName = airports.get(airports_list.nextInt(airports.size()));
			// Make the start departure airport to be the last arrival airport
			while (arrivalName.equals(startDeparture) && airports.size() != 1) {
				arrivalName = airports.get(airports_list.nextInt(airports.size()));
				if (!arrivalName.equals(startDeparture)) {
					break;
				}
			}
			if (airports.size() == 1) {
				System.out.println("hello");
				arrivalName = airports2.get(0);
			}
			// Print the arrival airport
			System.out.println("Arrival: " + arrivalName);
			int arrival = airports2.indexOf(arrivalName);
			System.out.println("Index: " + arrival);

			// Print the cost from matrix

			tcost = getCost(cost_matrix, days, departure, arrival);
			cost = cost_matrix[days][departure][arrival];
			String numberAsString = Integer.toString(cost);
			while(tcost.equals("0"))
			{
				airports_list = new Random();
				arrivalName = airports.get(airports_list.nextInt(airports.size()));
				// Make the start departure airport to be the last arrival airport
				while (arrivalName.equals(startDeparture) && airports.size() != 1) {
					arrivalName = airports.get(airports_list.nextInt(airports.size()));
					if (!arrivalName.equals(startDeparture)) {
						break;
					}
				}
				if (airports.size() == 1) {
					System.out.println("hello");
					arrivalName = airports2.get(0);
				}
				// Print the arrival airport
				System.out.println("Arrival: " + arrivalName);
				arrival = airports2.indexOf(arrivalName);
				System.out.println("Index: " + arrival);

				// Print the cost from matrix

				tcost = getCost(cost_matrix, days, departure, arrival);
				
			}
			if(!tcost.equals("0")) {
				System.out.println("Cost: " + tcost);


				// print travel
				System.out.println("Info: " + departure1 + " " + arrivalName + " " + days + " " + tcost);
				officialTour.add(airports2.get(departure) + " " + arrivalName + " " + days + " " + tcost);
				officialDestinations.add(arrivalName);
				tempTour.add(airports2.get(departure) + " " + arrivalName + " " + days + " " + tcost);
				tempDestinations.add(arrivalName);
				// Remove elements from arraylist
				airports.remove(arrivalName);
				System.out.println(airports);
				System.out.println(airports2);

				// Set the arrival to be the departure
				departure = arrival;
				
				
				
				System.out.println("The Official Destination is " + officialDestinations);

			}

			// Decrease i for loop and increase the days
						days++;

			
		}
		System.out.println("The Official Tour is ");
		for (int j = 0; j < officialTour.size(); j++) {
			System.out.println(officialTour.get(j));
		}

		return startDeparture;

	}

	/*
	 * 
	 * Swapping Cities
	 * 
	 * take 2 random cities in the traveling order swap them to see if cost reduces
	 * 
	 */
	private static void swapCities(ArrayList<String> destinations, ArrayList<String> tour, int[][][] cost) {
		// First, generate 2 random numbers in range [0, Destinations.length]

		ArrayList<Integer> randomize = new ArrayList<Integer>();

		for (int i = 1; i < destinations.size() - 1; i++) {
			randomize.add(i);
		}
		Collections.shuffle(randomize);

		int first = randomize.get(1);
		int second = randomize.get(2);

		if (first > second) {
			int temp = first;
			first = second;
			second = temp;
		}
//		
//		int first = 1;
//		int second = 4;

		System.out.println("");

		// Swap the cities based on the random numbers

		// Create a temp Destination, and Tour array to save the old route
		for (int i = 0; i < officialDestinations.size(); i++) {
			tempDestinations.set(i, officialDestinations.get(i));

		}
		for (int i = 0; i < officialTour.size(); i++) {
			tempTour.set(i, officialTour.get(i));
		}
//		tempDestinations = destinations;
//		tempTour = tour;

		// Update the new Destination and Tour according to the new route

		// Change the Destinations array
		String tempDeparture = destinations.get(first);
		destinations.set(first, destinations.get(second));
		destinations.set(second, tempDeparture);

		System.out.println("The Destinations Array has been reverted" + destinations);

		// Change the Tour array

		String date = tour.get(first).substring(8, 9);
		int intDate = Integer.parseInt(date);

		String depart = destinations.get(first);
		String arrival = destinations.get(second);

		int dep = airports2.indexOf(depart);
		int arr = airports2.indexOf(arrival);
	
		String c;

		System.out.println("The Original Travel " + tempDestinations);
		System.out.println("The New Travel " + officialDestinations);

		c = getCost(cost_matrix, intDate - 1, dep, arr);

		/*
		 * 
		 * If there is no way to swap, STOP
		 */

		if (first - second == 1 || second - first == 1) {

			if (c.equals("0")) {

				System.out.println("Can't swap");

				for (int i = 0; i < officialDestinations.size(); i++) {
					officialDestinations.set(i, tempDestinations.get(i));

				}

				for (int i = 0; i < officialTour.size(); i++) {
					tempTour.set(i, officialTour.get(i));
				}
			} else {

				/*
				 * This is the first scenario when first and second are consecutive
				 * 
				 */

				String newPath = destinations.get(first) + " " + destinations.get(second) + " " + date + " "
						+ cost[intDate - 1][dep][arr];

				String dayBefore = String.valueOf(intDate - 1);
				String previousAirport = tour.get(first - 1).substring(0, 3);
				// System.out.println("previous" + previousAirport);

				dep = airports2.indexOf(previousAirport);
				arr = airports2.indexOf(arrival);
				c = getCost(cost_matrix, intDate - 2, dep, arr);

				/*
				 * 
				 * If there is no way to swap, STOP
				 */

				if (c.equals("0")) {

					System.out.println("Can't swap");
					for (int i = 0; i < destinations.size(); i++) {
						officialDestinations.set(i, tempDestinations.get(i));

					}
					for (int i = 0; i < officialTour.size(); i++) {
						tempTour.set(i, officialTour.get(i));
					}
				} else {
					String previousPath = previousAirport + " " + depart + " " + dayBefore + " "
							+ cost[intDate - 2][dep][arr];

					String dayAfter = String.valueOf(intDate + 1);
					String nextAirport = tour.get(second).substring(4, 7);

					dep = airports2.indexOf(arrival);
					arr = airports2.indexOf(nextAirport);
					c = getCost(cost_matrix, intDate, dep, arr);

					/*
					 * 
					 * If there is no way to swap, STOP
					 */

					if (c.equals("0")) {

						System.out.println("Can't swap");
						for (int i = 0; i < officialDestinations.size(); i++) {
							officialDestinations.set(i, tempDestinations.get(i));

						}

						for (int i = 0; i < officialTour.size(); i++) {
							tempTour.set(i, officialTour.get(i));
						}
					} else {

						String nextPath = arrival + " " + nextAirport + " " + dayAfter + " "
								+ cost[intDate][airports2.indexOf(arrival)][airports2.indexOf(nextAirport)];

						tour.set(first, newPath);
						tour.set(first - 1, previousPath);
						tour.set(first + 1, nextPath);
					}
				}

			}
		} else {

			/*
			 * Need to change 4 lines of the OfficialTour
			 */

			// First line
			depart = officialTour.get(first - 1).substring(0, 3);
			arrival = officialDestinations.get(first);
			intDate = Integer.parseInt(officialTour.get(first - 1).substring(8, 9));
			dep = airports2.indexOf(depart);
			arr = airports2.indexOf(arrival);

			c = getCost(cost_matrix, intDate, dep, arr);
			if (c.equals("0")) {

				System.out.println("Can't swap");
				for (int i = 0; i < officialDestinations.size(); i++) {
					officialDestinations.set(i, tempDestinations.get(i));

				}

				for (int i = 0; i < officialTour.size(); i++) {
					tempTour.set(i, officialTour.get(i));
				}
			} else {

				String path1 = depart + " " + arrival + " " + Integer.toString(intDate) + " " + c;
				System.out.println("Path1 " + path1);
				tour.set(first - 1, path1);

				// Path 2

				intDate++;
				depart = arrival;
				arrival = officialDestinations.get(first + 1);

				dep = airports2.indexOf(depart);
				arr = airports2.indexOf(arrival);
				c = getCost(cost_matrix, intDate, dep, arr);
				if (c.equals("0")) {

					System.out.println("Can't swap");
					for (int i = 0; i < officialDestinations.size(); i++) {
						officialDestinations.set(i, tempDestinations.get(i));

					}
					for (int i = 0; i < officialTour.size(); i++) {
						tempTour.set(i, officialTour.get(i));
					}
				} else {

					String path2 = depart + " " + arrival + " " + Integer.toString(intDate) + " " + c;
					System.out.println("Path2 " + path2);

					tour.set(first, path2);

					intDate = second - 1;
					depart = officialTour.get(second - 1).substring(0, 3);
					arrival = officialDestinations.get(second);
					dep = airports2.indexOf(depart);
					arr = airports2.indexOf(arrival);
					c = getCost(cost_matrix, intDate, dep, arr);
					if (c.equals("0")) {
						System.out.println("Can't swap");
						for (int i = 0; i < officialDestinations.size(); i++) {
							officialDestinations.set(i, tempDestinations.get(i));

						}

						for (int i = 0; i < officialTour.size(); i++) {
							tempTour.set(i, officialTour.get(i));
						}
					} else {
						// System.out.println("Path 3");

						String path3 = depart + " " + arrival + " " + Integer.toString(intDate) + " " + c;
						System.out.println("Path3 " + path3);

						tour.set(second - 1, path3);

						intDate++;
						depart = arrival;
						arrival = officialDestinations.get(second + 1);

						dep = airports2.indexOf(depart);
						arr = airports2.indexOf(arrival);
						c = getCost(cost_matrix, intDate, dep, arr);
						if (c.equals("0")) {

							System.out.println("Can't swap");
							for (int i = 0; i < officialDestinations.size(); i++) {
								officialDestinations.set(i, tempDestinations.get(i));

							}
							for (int i = 0; i < officialTour.size(); i++) {
								tempTour.set(i, officialTour.get(i));
							}
						} else {
							// System.out.println("Path 4");

							String path4 = depart + " " + arrival + " " + Integer.toString(intDate) + " " + c;
							System.out.println("Path4 " + path4);

							tour.set(second, path4);
						}

					}

				}

			}

		}

		for (int i = 0; i < tour.size(); i++) {
			System.out.println(tour.get(i));
		}

	}

	/*
	 * Choose cost
	 *
	 */

	private static String getCost(int[][][] cost, int date, int dep, int arr) {

		if (cost[0][dep][arr] == Integer.MAX_VALUE && cost[date][dep][arr] == Integer.MAX_VALUE) {
			// System.out.println("Not able to calculate cost of " + airports.get(dep) + "
			// and " + airports.get(arr));
			return "0";

		}

		else {

			int c = Math.min(cost[0][dep][arr], cost[date][dep][arr]);
			// System.out.println("Cost between " + airports.get(dep) + " and " +
			// airports.get(arr) + " is " + c);
			return Integer.toString(c);
		}

	}

	/*
	 * 
	 * Revert Swap
	 * 
	 * revert the last swap if the cost is not reduced
	 * 
	 */

	private static void revertSwap() {
		for (int i = 0; i < officialDestinations.size(); i++) {
			officialDestinations.set(i, tempDestinations.get(i));

		}

		for (int i = 0; i < officialTour.size(); i++) {
			officialTour.set(i, tempTour.get(i));
		}
	}

	/*
	 * Calculate the total Travel cost
	 * 
	 */

	private static int getTotalCost(ArrayList<String> tour) {
		int tc = 0;
		int tripCost = 0;
		String tcost;
		for (int i = 0; i < tour.size(); i++) {
			tcost = tour.get(i).substring(10);
			tcost = tcost.replaceAll("\\s+", "");
			tripCost = Integer.parseInt(tcost);
			tc = tc + tripCost;
		}
		return tc;
	}

	/*
	 * Annealing Implementation
	 * 
	 */

	private static void anealing(double e, double temperature, int[][][] cost) {
		int newCost = 0;
		int oldCost = 0;
		int count=0;
		while (temperature > 0.0001) {
			temperature =  temperature - temperature * e;
			swapCities(officialDestinations, officialTour, cost);
			newCost = getTotalCost(officialTour);
			oldCost = totalCost;
			System.out.println("The old Cost now is " + oldCost);
			System.out.println("The new Cost now is " + newCost);

			if (newCost > oldCost) {
				System.out.println("Revert the previous swap");
				revertSwap();
				totalCost = oldCost;
			} else {
				totalCost = newCost;
			}
			System.out.println("The Cost now is " + totalCost);
			count++;
		}
		System.out.println("The Cost is " + totalCost + " after " + count + " iterations");
	}

}
