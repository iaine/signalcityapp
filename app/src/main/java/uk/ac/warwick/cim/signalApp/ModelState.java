package uk.ac.warwick.cim.signalApp;

/**
 * Functions to develop the model
 */
public class ModelState {

    public boolean inLoop = false;

    public boolean outLoop = false;

    public boolean distance = false;

    public boolean repetition = false;

    /**
     * Change the state of the model type
     * @param model - name of the model to be set
     * @param flag - boolean
     */
    public void changeState (boolean model, boolean flag) {
         model = flag;
    }

}
