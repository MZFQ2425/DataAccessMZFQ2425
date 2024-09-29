import java.time.Duration;
import java.time.LocalDateTime;

public class Person {
    //Properties
    private String name;
    private LocalDateTime birthDate;
    //Variables
    private static final String NOT_VALID = "Invalid birthday";

    //Getters / Setters
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public LocalDateTime  getBirthDate(){
        return birthDate;
    }
    public void setBirthDate(LocalDateTime birthDate){
        if (birthDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        this.birthDate = birthDate;
    }
    //Functions
    public int getAge(){
        long days = Duration.between(birthDate, LocalDateTime.now()).toDays();
        return (int) (days / 365);
    }
}