package info.androidhive.easypago.activity;


public class View_MySell_Model {

    private int icon;
    private String title;
    private String text;

    private boolean isGroupHeader = false;

    public View_MySell_Model(String title) {
        this(-1,title,null);
        isGroupHeader = true;
    }
    public View_MySell_Model(int icon, String title, String text) {
        super();
        this.icon = icon;
        this.title = title;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public boolean isGroupHeader() {
        return false;
    }

//gettters & setters...
}