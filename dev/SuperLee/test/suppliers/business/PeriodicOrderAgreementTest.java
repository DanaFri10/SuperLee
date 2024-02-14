package suppliers.business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PeriodicOrderAgreementTest {
    private PeriodicOrderAgreement poa;

    @BeforeEach
    void setUp() {
        //Map<Integer, Integer> products = new HashMap<>();
        poa = new PeriodicOrderAgreement(new HashMap<>());
        poa.setOrderDays(new boolean[]{false, true, false, true, true, false, false});
    }


    @Test
    void canUpdate() {
        Calendar calendar1 = new GregorianCalendar(2023,4,7);
        Calendar calendar2 = new GregorianCalendar(2023,4,23);
        Calendar calendar3 = new GregorianCalendar(2023,4,8);

        assertFalse(poa.canUpdate(calendar1));
        assertFalse(poa.canUpdate(calendar2));
        assertTrue(poa.canUpdate(calendar3));
    }
}