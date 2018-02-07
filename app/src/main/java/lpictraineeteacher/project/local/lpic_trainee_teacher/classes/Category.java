package lpictraineeteacher.project.local.lpic_trainee_teacher.classes;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class Category {

    private String id;
    private String category;

    public Category() {
    }

    public Category(String category) {
        this.category = category;
    }

    public Category(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
