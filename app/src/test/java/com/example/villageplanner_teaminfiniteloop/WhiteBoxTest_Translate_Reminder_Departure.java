package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.villageplanner_teaminfiniteloop.ui.reminders.RemindersFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class WhiteBoxTest_Translate_Reminder_Departure {
    @Test
    public void testNotificationTime() {
        RemindersFragment tester = new RemindersFragment();
        List<String> reminderList = Arrays.asList("18:10?12:10?10", "18:10?12:10?30", "18:10?12:10?40");
        ArrayList<String> departureList = new ArrayList<String>();
        departureList.add("10");
        departureList.add("30");
        departureList.add("40");
        assertTrue(isEqual((ArrayList<String>) tester.translateReminderDeparture(reminderList),departureList));
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
