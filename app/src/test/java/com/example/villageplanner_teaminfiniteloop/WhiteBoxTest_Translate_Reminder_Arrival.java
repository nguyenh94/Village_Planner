package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.villageplanner_teaminfiniteloop.ui.reminders.RemindersFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class WhiteBoxTest_Translate_Reminder_Arrival {
    @Test
    public void testNotificationTime() {
        RemindersFragment tester = new RemindersFragment();
        List<String> reminderList = Arrays.asList("18:10?12:10?10", "18:10?13:10?30", "18:10?14:10?40");
        ArrayList<String> arrivalList = new ArrayList<String>();
        arrivalList.add("12:10");
        arrivalList.add("13:10");
        arrivalList.add("14:10");
        assertTrue(isEqual((ArrayList<String>) tester.translateReminderArrival(reminderList),arrivalList));
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
