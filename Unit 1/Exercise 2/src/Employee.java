public class Employee extends Person {
    //Properties
    private Double grossSalary;
    //Variables
    private static final String NOT_VALID = "Invalid salary";
    //Getter / Setter
    public Double getGrossSalary() {
        return this.grossSalary;
    }
    public void setGrossSalary(Double grossSalary){
        if(grossSalary < 0){
            throw new IllegalArgumentException(NOT_VALID);
        }else{
            this.grossSalary = grossSalary;
        }
    }
}
