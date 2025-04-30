package ticket_booking;

import ticket_booking.Entities.Train;
import ticket_booking.Entities.User;
import ticket_booking.Services.UserBookingService;
import ticket_booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        User currentUser = null;
        UserBookingService userBookingService = null;
        Train trainSelectedForBooking = null;

        while (option != 7) {
            try {
                System.out.println("\nChoose an option:");
                System.out.println("1. Sign up");
                System.out.println("2. Login");
                System.out.println("3. Fetch My Bookings");
                System.out.println("4. Search Trains");
                System.out.println("5. Book a Seat");
                System.out.println("6. Cancel My Booking");
                System.out.println("7. Exit");
                try {
                    System.out.print("Enter your choice: ");
                    if (scanner.hasNextInt()) {
                        option = scanner.nextInt();
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine(); // Clear invalid input
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Clear invalid input
                }

                switch (option) {
                    case 1: // Sign up
                        System.out.println("Enter username to sign up:");
                        String signupName = scanner.next();
                        System.out.println("Enter password to sign up:");
                        String signupPassword = scanner.next();

                        String hashedPassword = UserServiceUtil.hashPassword(signupPassword);
                        User userToSignup = new User(signupName, signupPassword, hashedPassword, new ArrayList<>(), UUID.randomUUID().toString());

                        try {
                            userBookingService = new UserBookingService(userToSignup);
                            boolean signedUp = userBookingService.signUp(userToSignup);
                            System.out.println(signedUp ? "Signup successful!" : "Signup failed.");
                        } catch (IOException e) {
                            System.out.println("Error during signup: " + e.getMessage());
                        }
                        break;

                    case 2: // Login
                        System.out.println("Enter username to login:");
                        String loginName = scanner.next();
                        System.out.println("Enter password to login:");
                        String loginPassword = scanner.next();

                        User userToLogin = new User(loginName, loginPassword, null, new ArrayList<>(), UUID.randomUUID().toString());

                        try {
                            userBookingService = new UserBookingService(userToLogin);
                            boolean isLoggedIn = userBookingService.loginUser();

                            if (isLoggedIn) {
                                System.out.println("Login successful!");
                                currentUser = userToLogin;
                            } else {
                                System.out.println("Invalid credentials.");
                                userBookingService = null;
                            }
                        } catch (IOException e) {
                            System.out.println("Error during login.");
                        }
                        break;

                    case 3: // Fetch Bookings
                        if (userBookingService != null && currentUser != null) {
                            userBookingService.fetchBookings();
                        } else {
                            System.out.println("Please login first.");
                        }
                        break;

                    case 4: 
                        if (userBookingService == null) {
                            System.out.println("Please login first.");
                            break;
                        }

                        System.out.println("Enter source station:");
                        String source = scanner.next();
                        System.out.println("Enter destination station:");
                        String dest = scanner.next();

                        List<Train> trains = userBookingService.getTrains(source, dest);
                        if (trains.isEmpty()) {
                            System.out.println("No trains found.");
                            break;
                        }

                        int idx = 1;
                        for (Train t : trains) {
                            System.out.println(idx + ". Train ID: " + t.getTrainId());
                            for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                                System.out.println("  Station: " + entry.getKey() + " | Time: " + entry.getValue());
                            }
                            idx++;
                        }

                        System.out.println("Select a train number (1-" + trains.size() + "):");
                        int choice = scanner.nextInt();
                        if (choice >= 1 && choice <= trains.size()) {
                            trainSelectedForBooking = trains.get(choice - 1);
                            System.out.println("Train selected: " + trainSelectedForBooking.getTrainId());
                        } else {
                            System.out.println("Invalid train selection.");
                        }
                        break;

                    case 5: 
                        if (userBookingService == null || trainSelectedForBooking == null) {
                            System.out.println("Please login and select a train first.");
                            break;
                        }

                        List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                        System.out.println("Available Seats:");
                        for (int i = 0; i < seats.size(); i++) {
                            for (int j = 0; j < seats.get(i).size(); j++) {
                                System.out.print(seats.get(i).get(j) + " ");
                            }
                            System.out.println();
                        }

                        System.out.println("Enter seat row:");
                        int row = scanner.nextInt();
                        System.out.println("Enter seat column:");
                        int col = scanner.nextInt();

                        System.out.println("Booking your seat...");
                        boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                        if (booked) {
                            System.out.println("Seat booked successfully. Enjoy your journey!");
                        } else {
                            System.out.println("Seat booking failed. Try another one.");
                        }
                        break;

                    case 6:
                        System.out.println("Cancel booking feature not implemented yet.");
                        break;

                    case 7: // Exit
                        System.out.println("Thanks for using Train Booking System.");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        scanner.close();
    }
}
