package com.example.villageplanner_teaminfiniteloop;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.villageplanner_teaminfiniteloop.ui.reminders.RemindersFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class WhiteBoxTest_Translate_Reminder_Title{
    @Test
    public void testNotificationTime() {
        RemindersFragment tester = new RemindersFragment();
        List<String> reminderList = Arrays.asList("go to cava?18:10?12:10?10", "eat cherries?18:10?12:10?30", "drink milk?18:10?12:10?40");
        ArrayList<String> titleList = new ArrayList<String>();
        titleList.add("go to cava");
        titleList.add("eat cherries");
        titleList.add("drink milk");
        assertTrue(isEqual((ArrayList<String>) tester.translateReminderTitles(reminderList),titleList));
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
