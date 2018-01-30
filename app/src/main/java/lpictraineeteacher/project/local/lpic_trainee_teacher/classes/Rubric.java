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

    @Override
    public String toString() {
        return "Rubric{" +
                "id=" + id +
                ", kategorieID=" + kategorieID +
                ", rubrik='" + rubrik + '\'' +
                '}';
    }

    public String getKategorieID() {

        return kategorieID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rubric rubric1 = (Rubric) o;

        if (!kategorieID.equals(rubric1.kategorieID)) return false;
        return rubrik.equals(rubric1.rubrik);
    }

    @Override
    public int hashCode() {
        int result = kategorieID.hashCode();
        result = 31 * result + rubrik.hashCode();
        return result;
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
