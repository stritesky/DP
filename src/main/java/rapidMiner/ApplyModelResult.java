package rapidMiner;

/**
 * Created by radek on 3.4.17.
 */
public class ApplyModelResult {

    public final String ATTRIBUTE_PREDICTION = "prediction(att1)";
    public final String ATTRIBUTE_CONFIDENCE_0 = "prediction(att1)";
    public final String ATTRIBUTE_CONFIDENCE_1 = "prediction(att1)";

    private double prediction;
    private double confidence0;
    private double confidence1;

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }

    public double getConfidence0() {
        return confidence0;
    }

    public void setConfidence0(double confidence0) {
        this.confidence0 = confidence0;
    }

    public double getConfidence1() {
        return confidence1;
    }

    public void setConfidence1(double confidence1) {
        this.confidence1 = confidence1;
    }
}
