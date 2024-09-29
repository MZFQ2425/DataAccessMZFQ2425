import java.util.List;

public class Author {
    private String name;
    private String nationality;
    private List<Work> workList;

    public Author() {
    }

    public Author(String name, String nationality, List<Work> workList) {
        this.name = name;
        this.nationality = nationality;
        this.workList = workList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Work> getWorkList() {
        return workList;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }
}
