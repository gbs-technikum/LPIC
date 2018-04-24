package lpictraineeteacher.project.local.lpic_trainee_teacher.classes;

import java.util.ArrayList;

import static lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants.ISFALSE;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class Question {
    private String id;
    private String rid;
    private String frage;
    private String art;
    private String hinweis;
    private ArrayList<Answer> answers;
    private String verified;
    private String answerIsRight;

    public Question() {
        setVerified(ISFALSE);
        setAnswerIsRight(ISFALSE);
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getAnswerIsRight() {
        return answerIsRight;
    }

    public void setAnswerIsRight(String answerIsRight) {
        this.answerIsRight = answerIsRight;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public String getHinweis() {
        return hinweis;
    }

    public void setHinweis(String hinweis) {
        this.hinweis = hinweis;
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
