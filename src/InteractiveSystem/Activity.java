package InteractiveSystem;

import java.io.PrintWriter;
import java.util.HashMap;

public class Activity implements Comparable<Activity> {

    private int totalNumberOfTicketAvailablePerActivity;
    private String activityName;

    Activity() {
        this.activityName = "";
        this.totalNumberOfTicketAvailablePerActivity = 0;
    }

    Activity(String activityName) {
        this.activityName = activityName;
    }

    Activity(String activityName,int totalNumberOfTicketAvailablePerActivity) {
        this.activityName = activityName;
        this.totalNumberOfTicketAvailablePerActivity = totalNumberOfTicketAvailablePerActivity;
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
        f.println(activityName);
        System.out.println(activityName);
    }

}
