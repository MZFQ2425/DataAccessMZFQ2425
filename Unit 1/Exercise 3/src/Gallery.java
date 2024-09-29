import java.util.List;

public class Gallery {
    private String name;
    private List<Work> works;
    private Museum museum;

    public Gallery() {
    }

    public Gallery(String name, List<Work> works, Museum museum) {
        this.name = name;
        this.works = works;
        this.museum = museum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public Museum getMuseum() {
        return museum;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }
}
