package pojo;

/**
 * Created by Asus on 2016/09/02.
 */
public class Option {
    private String title;
    private boolean checked;

    public Option() {
    }

    public Option(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Option{" +
                "title='" + title + '\'' +
                ", checked=" + checked +
                '}';
    }
}
