package uk.ac.warwick.cim;

import org.junit.Test;

import uk.ac.warwick.cim.signalApp.Tone;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class ToneTest {
    @Test
    public void testDistanceIs0() {
        // Context of the app under test.
        Tone tone = new Tone();
        assertEquals(1.00, tone.getDistance(-69, -69), 1.0);
    }

    @Test
    public void testDistanceGreater0() {
        // Context of the app under test.
        Tone tone = new Tone();
        assertEquals(3.54, tone.getDistance(-80, -69), 0.25);
    }

    @Test
    public void testSetVolumeGtr0() {
        // Context of the app under test.
        Tone tone = new Tone();
        assertEquals(0.654, tone.setVolume(-80, -69), 0.25);
    }

    @Test
    public void testSetVolumeIsGreater0() {
        // Context of the app under test.
        Tone tone = new Tone();
        double vol = tone.setVolume(-120, -69);
        assertNotEquals(0.0, vol, 0.00);
        assertEquals(0.1, vol, 0.15);
    }

    @Test
    public void testSetVolumeIsNotGreater1() {
        // Context of the app under test.
        Tone tone = new Tone();
        double vol = tone.setVolume(1, -69);
        assertEquals(1.0, vol, 0.12);
    }
}
