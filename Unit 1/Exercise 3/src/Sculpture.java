public class Sculpture extends Work{
    private Materials material;
    private Styles site;

    public Sculpture(String title, Author author, Gallery gallery, Materials material, Styles site) {
        super(title, author, gallery);
        this.material = material;
        this.site = site;
    }

    public Sculpture() {
    }

    public Materials getMaterial() {
        return material;
    }

    public void setMaterial(Materials material) {
        this.material = material;
    }

    public Styles getSite() {
        return site;
    }

    public void setSite(Styles site) {
        this.site = site;
    }
}
