package uk.ac.warwick.cim;

import org.junit.Test;

import static org.junit.Assert.*;

import android.location.Location;
import android.location.LocationManager;

import uk.ac.warwick.cim.signalApp.ModelState;

/**
 * Unit tests for file
 */
public class ModelStateUnitTest {
    @Test
    public void test_initModel() {
        ModelState modelState = new ModelState();
        assertEquals(false, modelState.distance);
    }

    @Test
    public void test_changeModelState() {
        ModelState modelState = new ModelState();
        modelState.changeState(modelState.distance, true);
        assertEquals(true, modelState.distance);
    }
}
