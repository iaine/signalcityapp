package uk.ac.warwick.cim.signalApp;

import org.junit.Test;

import static org.junit.Assert.*;

public class RepetitionUnitTest {

    @Test
    public void check_Empty_Repetition() {
        Repetition repetition = new Repetition();
        Integer idx = repetition.checkRepetition("A4:54:23:cd");
        assertEquals(-1, idx.intValue());
    }
    @Test
    public void check_Repetition() {
        Repetition repetition = new Repetition();
        repetition.checkRepetition("A4:54:23:cd");
        Integer idx = repetition.checkRepetition("A4:54:23:cd");
        assertEquals(0, idx.intValue());
    }
}
