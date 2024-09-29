import java.util.Set;
public class Executive extends Employee {
    //Properties
    private String category;
    private Set<Employee> supervises;
    //Getter / Setter
    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    //Functions
    public int countSubordinates(){
        return supervises.size();
    }
}
