package com.example.villageplanner_teaminfiniteloop;

        import static org.junit.Assert.assertTrue;
        import org.junit.Test;
        import static org.junit.Assert.*;

        import com.example.villageplanner_teaminfiniteloop.ui.reminders.RemindersFragment;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Arrays;

public class WhiteBoxTest_ReminderCombineText {
    @Test
    public void testNotificationTime() {
        RemindersFragment tester = new RemindersFragment();
        List<String> arrivalList = Arrays.asList("18:10", "12:20");
        List<String> departureList = Arrays.asList("18:01", "12:10");
        List<String> reminderList = Arrays.asList("go to cava", "eat greenleaf");
        List<String> queueAndTravelTime = Arrays.asList("9", "10");
        ArrayList<String> remindersInString = new ArrayList<String>();
        remindersInString.add("go to cava?18:10?18:01?9");
        remindersInString.add("eat greenleaf?12:20?12:10?10");
        assertTrue(isEqual(tester.combineText(arrivalList, departureList, reminderList, queueAndTravelTime),remindersInString));
    }
    public boolean isEqual(ArrayList<String> list1, ArrayList<String> list2)
    {
        if(list1.size() != list2.size())
        {
            return false;
        }
        for(int i=0;i<list1.size();i++) {
            if(!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
