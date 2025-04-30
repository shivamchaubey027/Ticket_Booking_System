package ticket_booking.Entities;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class User {

    @JsonProperty("name")
    private String name;

    @JsonProperty("password")
    private String password;

    @JsonProperty("hashed_password")
    private String hashedPassword;

    @JsonProperty("tickets_booked")
    private List<Ticket> ticketsBooked;

    @JsonProperty("user_id")
    private String userId;
    public User() {

    }
    public User(String name, String password, String hashPassword, List<Ticket> ticketsBooked, String userId){
        this.name= name;
        this.password=password;
        this.hashedPassword=hashPassword;
        this.ticketsBooked=ticketsBooked;
        this.userId=userId;
    }

    public String getName(){
        return name;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public String getHashPassword(){
        return hashedPassword;
    }

    public String getPassword(){
        return password;
    }

    public void printTickets() {
        if (ticketsBooked == null || ticketsBooked.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Ticket ticket : ticketsBooked) {
            System.out.println(ticket.getTicketInfo());
        }
    }


    public String getUserId(){
        return userId;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashPassword) {
        this.hashedPassword = hashPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked){
        this.ticketsBooked= ticketsBooked;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    
}
