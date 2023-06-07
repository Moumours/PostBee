import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Poste {

    public Poste (String title, String text) {
        this.title = title;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.text = text;
        this.status = "en attente";
    }

    private String title;
    private String date;
    private String text;
    private String status;

    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public String getText() {
        return text;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
