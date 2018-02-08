package lpictraineeteacher.project.local.lpic_trainee_teacher.classes;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class Rubric {

    private String id;
    private String kategorieID;
    private String rubrik;

    public Rubric() {
    }

    public Rubric(String kategorieID) {
        this.kategorieID = kategorieID;
    }

    public Rubric(String id, String kategorieID, String rubrik) {
        this.id = id;
        this.kategorieID = kategorieID;
        this.rubrik = rubrik;
    }

    public String getKategorieID() {

        return kategorieID;
    }

    public void setKategorieID(String kategorieID) {
        this.kategorieID = kategorieID;
    }

    public String getRubrik() {
        return rubrik;
    }

    public void setRubrik(String rubrik) {
        this.rubrik = rubrik;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
