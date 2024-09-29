import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client extends Person {
    //Properties
    private String telephone;
    private Set<Business> isClientOf;
    //Variables
    private static final String NOT_VALID = "Invalid telephone";
    //Getters / Setters
    public String getTelephone(){
        return this.telephone;
    }
    public void setTelephone(String telephone) {
        Pattern pattern = Pattern.compile("^((\\+|00)\\d{2,3})?\\d{9}$");
        Matcher matcher = pattern.matcher(telephone);
        if(matcher.find()){
            this.telephone = telephone;
        }else{
            throw new IllegalArgumentException(NOT_VALID);
        }
    }
}
