import java.util.ArrayList;
import java.util.List;

public class Painting extends Work{
    private PaintType type;
    private String format;

    public Painting() {
    }

    public Painting(String title, Author author, Gallery gallery, PaintType type, String format) {
        super(title, author, gallery);
        this.type = type;
        this.format = format;
    }

    public PaintType getType() {
        return type;
    }

    public void setType(PaintType type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
