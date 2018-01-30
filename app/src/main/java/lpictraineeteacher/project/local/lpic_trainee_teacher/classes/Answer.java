package lpictraineeteacher.project.local.lpic_trainee_teacher.classes;

import lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData.ConstantsBD;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class Answer {

    private String id;
    private String fid;
    private String answer;
    private String truefalse;
    private String explain;

    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", fid='" + fid + '\'' +
                ", answer='" + answer + '\'' +
                ", truefalse='" + truefalse + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer1 = (Answer) o;

        if (!fid.equals(answer1.fid)) return false;
        if (!answer.equals(answer1.answer)) return false;
        if (!truefalse.equals(answer1.truefalse)) return false;
        return explain.equals(answer1.explain);
    }

    @Override
    public int hashCode() {
        int result = fid.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + truefalse.hashCode();
        result = 31 * result + explain.hashCode();
        return result;
    }

    public String getExplain() {

        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Answer() {
        this.id ="";
        this.fid ="";
        this.answer ="";
        this.truefalse= ConstantsBD.ISTRUE;
        this.explain ="";
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
