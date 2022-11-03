package InteractiveSystem;

import java.io.PrintWriter;
import java.util.HashMap;

public class Activity implements Comparable<Activity> {

    private int totalNumberOfActivity;
    private int totalNumberOfTicketAvailablePerActivity;
    private String activityName;

    Activity() {
        this.activityName = "";
        this.totalNumberOfActivity = 0;
        this.totalNumberOfTicketAvailablePerActivity = 0;
    }

    Activity(int totalNumberOfActivity, int totalNumberOfTicketAvailablePerActivity, String activityName) {
        this.totalNumberOfTicketAvailablePerActivity = totalNumberOfTicketAvailablePerActivity;
        this.totalNumberOfActivity = totalNumberOfActivity;
        this.activityName = activityName;
    }

    public int getTotalNumberOfActivity() {
        return totalNumberOfActivity;
    }

    public void setTotalNumberOfActivity(int totalNumberOfActivity) {
        this.totalNumberOfActivity = totalNumberOfActivity;
    }

    public int getTotalNumberOfTicketAvailablePerActivity() {
        return totalNumberOfTicketAvailablePerActivity;
    }

    public void setTotalNumberOfTicketAvailablePerActivity(int totalNumberOfTicketAvailablePerActivity) {
        this.totalNumberOfTicketAvailablePerActivity = totalNumberOfTicketAvailablePerActivity;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }


    @Override
    public int compareTo(Activity activity) {
        return activityName.compareTo(activity.activityName);
    }

    public void printName(PrintWriter f) {
        f.println(activityName + " -> " + totalNumberOfActivity);
        System.out.println(activityName + " -> " + totalNumberOfActivity);
    }
}
