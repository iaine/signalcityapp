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

    @Test
    public void check_Full_List() {
        Repetition repetition = new Repetition();
        for (int i = 0; i<=100;i++) {
            repetition.checkRepetition("A4:54:23:cd");
        }
        Integer idx = repetition.checkRepetition("A4:54:23:cd");
        assertEquals(99, idx.intValue());
    }

    @Test
    public void insert_Full_List() {
        Repetition repetition = new Repetition();
        for (int i = 0; i<=100;i++) {
            repetition.checkRepetition("A4:54:23:cd");
        }
        repetition.checkRepetition("A4:54:23:de");
        Integer idx = repetition.checkRepetition("A4:54:23:de");
        assertEquals(99, idx.intValue());
    }
}
