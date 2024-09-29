public class Student {
    //Properties
    private String name;
    private Integer grade;

    //Getters setters
    public String getName(){
        return this.name;
    }

    public void setName(String nombre){
        this.name = nombre;
    }

    public Integer getGrade(){
        return this.grade;
    }
    public void setGrade(Integer nota){
        if(nota >= 0 && nota <= 10){
            this.grade = nota;
        }
    }

    //Functions
    public Boolean hasPassed(){
        if(this.grade>=5){
            return true;
        }
        return false;
    }
}