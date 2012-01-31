package net.gtamps.android.graphics.test.listview;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:06
 */
public class ActivityModel extends ListModel {
    
    private final String packageName;
    private final String className;

    public ActivityModel(String title, String packageName, String className) {
        super(title);
        this.packageName = packageName;
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }
}
