package uk.ac.warwick.cim.signalApp;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Unit tests for file
 */
public class ModelStateUnitTest {
    @Test
    public void init_Model() {
        ModelState modelState = new ModelState();
        assertEquals(false, modelState.distance);
    }

    @Test
    public void change_ModelState_distance() {
        ModelState modelState = new ModelState();
        modelState.changeState("distance", true);
        assertEquals(true, modelState.distance);
    }

    @Test
    public void change_ModelState_distance_testloop() {
        ModelState modelState = new ModelState();
        modelState.changeState("distance", true);
        assertFalse(modelState.inLoop);
    }
}
