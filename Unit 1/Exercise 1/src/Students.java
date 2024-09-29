import java.util.ArrayList;

public class Students {
    //Properties
    private ArrayList<Student> studentsList;

    //Functions
    public void addStudent(Student student){
        studentsList.add(student);
    }

    public Student getStudent(int pos){
        if(pos >= 0 && pos <= studentsList.size()){
            return studentsList.get(pos);
        }
        return null;
    }

    public Float getAverage(){
        if(studentsList.size() == 0){
            return 0f;
        }else{
            Float avg = 0f;
            for(int i = 0; i < studentsList.size(); i++){
                avg += studentsList.get(i).getGrade();
            }
            return (avg / studentsList.size());
        }
    }
}
