import java.util.Set;

public class Business {
    private String name;
    private Set<Employee> staff;
    private Set<Client> clientPortfolio;
    //Getter / Setter
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    //Functions
    public int getTotalClients(){
        return clientPortfolio.size();
    }
    public int getTotalEmployees(){
        return staff.size();
    }
}
