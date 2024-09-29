import java.util.List;

public class Museum {
    private String name;
    private String direction;
    private String city;
    private String country;
    private List<Gallery> galleryList;
    private List<Work> workList;

    public Museum() {
    }

    public Museum(String name, String direction, String city, String country, List<Gallery> galleryList, List<Work> workList) {
        this.name = name;
        this.direction = direction;
        this.city = city;
        this.country = country;
        this.galleryList = galleryList;
        this.workList = workList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Gallery> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(List<Gallery> galleryList) {
        this.galleryList = galleryList;
    }

    public List<Work> getWorkList() {
        return workList;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }

    public void printMuseumAll() {
        System.out.println("Museum: " + name + ", located at: " + direction + ", " + city + ", " + country);
        for (Gallery gallery : getGalleryList()) {

            System.out.println("> Gallery: " + gallery.getName());
            boolean hasArtwork = false;

            if(gallery.getWorks().size()>1) {
                System.out.println("  Contains the following artworks:");
                hasArtwork = true;
            }else if(gallery.getWorks().size()>0){
                System.out.println("  Contains the following artwork:");
                hasArtwork = true;
            }else{
                System.out.println("  Currently does not contain any artwork");
            }

            if(hasArtwork){
                for (Work work : gallery.getWorks()) {
                    System.out.println("   - \"" + work.getTitle() + "\" by " + work.getAuthor().getName() + ", a "+work.getAuthor().getNationality()+ " artist");
                    if (work instanceof Painting) {
                        Painting painting = (Painting) work;
                        System.out.println("     Type of painting: " + painting.getType());
                        System.out.println("     Format: " + painting.getFormat());
                    } else if (work instanceof Sculpture) {
                        Sculpture sculpture = (Sculpture) work;
                        System.out.println("     Type of material: " + sculpture.getMaterial());
                        System.out.println("     Style: " + sculpture.getSite());
                    }
                }
            }
        }
    }
}
