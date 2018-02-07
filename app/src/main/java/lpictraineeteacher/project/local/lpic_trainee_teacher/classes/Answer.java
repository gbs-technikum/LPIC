package lpictraineeteacher.project.local.lpic_trainee_teacher.classes;

import static lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants.ISFALSE;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class Answer {

    private String id;
    private String fid;
    private String answer;
    private String truefalse;
    private String explanation;
    private String response;

    public Answer() {
        this.id = "";
        this.fid = "";
        this.answer = "";
        this.truefalse = Constants.ISTRUE;
        this.explanation = "";
        this.response=ISFALSE;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTruefalse() {

        return truefalse;
    }

    public void setTruefalse(String truefalse) {
        this.truefalse = truefalse;
    }

    public Answer(String id, String fid, String answer, String truefalse) {

        this.id = id;
        this.fid = fid;
        this.answer = answer;
        this.truefalse = truefalse;
    }

}
