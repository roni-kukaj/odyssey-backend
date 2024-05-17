package com.odyssey.plans;

import com.odyssey.locations.Location;
import com.odyssey.user.User;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PlanTest {

    @Test
    void testPlanGettersAndSetters() throws ParseException {
        User user = new User();
        Location location = new Location();
        LocalDate date = LocalDate.now();
        Plan plan = new Plan();
        plan.setId(1);
        plan.setUser(user);
        plan.setLocation(location);
        plan.setVisitDate(date);

        assertEquals(1, plan.getId());
        assertEquals(user, plan.getUser());
        assertEquals(location, plan.getLocation());
        assertEquals(date, plan.getVisitDate());

    }
}