package InteractiveSystem;

import java.io.PrintWriter;

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

    public int getNoOfTicketAvailableForActivity() {
        return totalNumberOfTicketAvailablePerActivity;
    }

    public void setNoOfTicketAvailableForActivity(int totalNumberOfTicketAvailablePerActivity) {
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

    public void printDetails(PrintWriter f) {
        f.println("Activity name : "+activityName +", Total number of ticket available = "+totalNumberOfTicketAvailablePerActivity);
        System.out.println("Activity name : "+activityName +", Total number of ticket available = "+totalNumberOfTicketAvailablePerActivity);
    }

}
