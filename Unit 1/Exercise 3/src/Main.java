import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // Data Insertion - Authors
        Author dali = new Author();
        dali.setName("Salvador Dalí");
        dali.setNationality("Catalan");

        Author manolo = new Author();
        manolo.setName("Manuel Hugué");
        manolo.setNationality("Catalan");

        Author campeny = new Author();
        campeny.setName("Damià Campeny");
        campeny.setNationality("Catalan");

        // Data Insertion - Paintings
        Painting painting1 = new Painting();
        painting1.setTitle("The Persistence of Memory");
        painting1.setAuthor(dali);
        painting1.setType(PaintType.Oil);
        painting1.setFormat("Canvas");

        // Data Insertion - Sculptures
        Sculpture sculpture1 = new Sculpture();
        sculpture1.setTitle("Torso de Hombre");
        sculpture1.setAuthor(manolo);
        sculpture1.setMaterial(Materials.Marble);
        sculpture1.setSite(Styles.Greco_Roman);

        Sculpture sculpture2 = new Sculpture();
        sculpture2.setTitle("Lucrecia Muerta");
        sculpture2.setAuthor(campeny);
        sculpture2.setMaterial(Materials.Marble);
        sculpture2.setSite(Styles.Neoclassical);

        // Data Insertion - Galleries
        Gallery gallery1 = new Gallery();
        gallery1.setName("Modern Paintings Gallery");
        gallery1.setWorks(new ArrayList<>(Arrays.asList(painting1)));

        Gallery gallery2 = new Gallery();
        gallery2.setName("Classical Sculptures Gallery");
        gallery2.setWorks(new ArrayList<>(Arrays.asList(sculpture1, sculpture2)));

        // Data Insertion - Museum
        Museum museum = new Museum();
        museum.setName("Museo Nacional de Arte de Catalunya");
        museum.setDirection("Calle de Montjuïc 1");
        museum.setCity("Barcelona");
        museum.setCountry("Spain");
        museum.setGalleryList(new ArrayList<>(Arrays.asList(gallery1, gallery2)));

        // Data Insertion - Set galleries on museum
        gallery1.setMuseum(museum);
        gallery2.setMuseum(museum);

        // Data Insertion - Set paintings on galleries
        painting1.setGallery(gallery1);

        sculpture1.setGallery(gallery2);

        sculpture2.setGallery(gallery2);

        // Display the artworks and galleries
        museum.printMuseumAll();
    }
}
