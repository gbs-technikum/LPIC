package lpictraineeteacher.project.local.lpic_trainee_teacher.classes;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class Question {
    private String id;
    private String rid;
    private String frage;
    private String art;
    private String hinweis;

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", rid='" + rid + '\'' +
                ", frage='" + frage + '\'' +
                ", art='" + art + '\'' +
                ", hinweis='" + hinweis + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (!rid.equals(question.rid)) return false;
        if (!frage.equals(question.frage)) return false;
        if (!art.equals(question.art)) return false;
        return hinweis != null ? hinweis.equals(question.hinweis) : question.hinweis == null;
    }

    @Override
    public int hashCode() {
        int result = rid.hashCode();
        result = 31 * result + frage.hashCode();
        result = 31 * result + art.hashCode();
        result = 31 * result + (hinweis != null ? hinweis.hashCode() : 0);
        return result;
    }

    public String getHinweis() {

        return hinweis;
    }

    public void setHinweis(String hinweis) {
        this.hinweis = hinweis;
    }

    public Question() {
    }

    public Question(String id, String rid, String frage, String art) {
        this.id = id;
        this.rid = rid;
        this.frage = frage;

        this.art = art;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }
}
