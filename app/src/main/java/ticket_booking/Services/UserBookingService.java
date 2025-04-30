package ticket_booking.Services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.SerializationFeature;
import ticket_booking.Entities.Ticket;
import ticket_booking.Entities.Train;
import ticket_booking.Entities.User;
import ticket_booking.util.UserServiceUtil;
public class UserBookingService{

    private ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .enable(SerializationFeature.INDENT_OUTPUT);



    private List<User> userList;

    private User user;

    private final String USER_FILE_PATH = "app/src/main/java/ticket_booking/localDb/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        userList = objectMapper.readValue(new File(USER_FILE_PATH), new TypeReference<List<User>>() {});
    }



    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            boolean nameMatch = user1.getName().equals(user.getName());
            boolean passwordMatch = UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());


            return nameMatch && passwordMatch;
        }).findFirst();

        return foundUser.isPresent();
    }


    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_FILE_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBookings() {
        if (user.getTicketsBooked() == null || user.getTicketsBooked().isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            user.printTickets();
        }
    }



    public Boolean cancelBooking(String ticketId){
    
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;  
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        }else{
        System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }
        

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex){
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
            return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    Ticket ticket = new Ticket(
                            UUID.randomUUID().toString(),
                            user.getUserId(),
                            train.getStations().get(0),
                            train.getStations().get(train.getStations().size() - 1),
                            "2025-05-01",
                            train


                    );

                    user.getTicketsBooked().add(ticket);
                    saveUserListToFile();
                    return true; 
                } else {
                    return false;
                }
            } else {
                return false; 
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }
}