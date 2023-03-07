package uk.ac.warwick.cim.signalApp;

/**
 * Functions to develop the model
 */
public class ModelState {


    public boolean inLoop = false;

    public boolean outLoop = false;

    public boolean distance = false;

    public boolean repetition = false;

    public boolean covid = false;

    /**
     * Change the state of the model type
     * @param model - string for the state to be set
     * @param flag - boolean
     */
    public void changeState (String model, boolean flag) {
        switch (model) {
            case "inloop":
                this.inLoop = flag;
                break;
            case "outloop":
                this.outLoop = flag;
                break;
            case "distance":
                this.distance = flag;
                break;
            case "repetition":
                this.repetition = flag;
                break;
            case "covid":
                this.covid = flag;
                break;
            default:
                break;
        }
    }

}
