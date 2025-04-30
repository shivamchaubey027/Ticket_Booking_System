package ticket_booking.Entities;
import java.util.List;


public class User {
    private String name;
    private String password;
    private String hashPassword;
    private List<Ticket> ticketsBooked;

    private String userId;

    public User(String name, String password, String hashPassword, List<Ticket> ticketsBooked, String userId){
        this.name= name;
        this.password=password;
        this.hashPassword=hashPassword;
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
        return hashPassword;
    }

    public String getPassword(){
        return password;
    }

    public void printTickets(){
        for(int i=0; i<ticketsBooked.size();i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public String getUserId(){
        return userId;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getHashedPassword() {
        return hashPassword;
    }

    public void setHashedPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked){
        this.ticketsBooked= ticketsBooked;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    
}
