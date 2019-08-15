package info.androidhive.supegatrab.activity;


public class View_Contacts_Model {

    private int icon;
    private String title;
    private String counter;

    private boolean isGroupHeader = false;

    public View_Contacts_Model(String title) {
        this(-1,title,null);
        isGroupHeader = true;
    }
    public View_Contacts_Model(int icon, String title, String counter) {
        super();
        this.icon = icon;
        this.title = title;
        this.counter = counter;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getCounter() {
        return counter;
    }

    public boolean isGroupHeader() {
        return false;
    }

//gettters & setters...
}