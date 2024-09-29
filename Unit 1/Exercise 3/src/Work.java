public class Work {
    private String title;
    private Author author;

    public Work() {
    }

    private Gallery gallery;

    public Work(String title, Author author, Gallery gallery) {
        this.title = title;
        this.author = author;
        this.gallery = gallery;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
