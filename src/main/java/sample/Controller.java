package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import java.util.Arrays;

/**
 * Controls application window.
 */
public class Controller {

    /**
     * Represents LineChart function of m,n,h parameters in steady state activation depending on u potential in range [-50, 150mV].
     */
    @FXML
    private LineChart<Number, Number> chartXU;
    /**
     * Represents LineChart time function of u potential.
     */
    @FXML
    private LineChart<Number, Number> uChart;
    /**
     * Represents LineChart time function of ion currents.
     */
    @FXML
    private LineChart<Number, Number> iChart;
    /**
     * Represents LineChart time function of m,n and h parameters.
     */
    @FXML
    private LineChart<Number, Number> mhnChart;
    /**
     * Represents TextArea with statistics parameters.
     */
    @FXML
    private TextArea txtAreaStats;
    /**
     * Represents Button showing charts ans stats.
     */
    @FXML
    private Button btnShow;
    /**
     * Represents Button clearing window.
     */
    @FXML
    private Button btnClear;
    /**
     * Represents Button setting parameters to default values.
     */
    @FXML
    private Button btnDefault;
    /**
     * Represents TextField with start time.
     */
    @FXML
    private TextField txtTStart;
    /**
     * Represents TextField with stop time.
     */
    @FXML
    private TextField txtTStop;
    /**
     * Represents TextField with integration step.
     */
    @FXML
    private TextField txtDT;
    /**
     * Represents TextField with value of current.
     */
    @FXML
    private TextField txtI;
    /**
     * Represents Label with capacity value.
     */
    @FXML
    private Label lblC;
    /**
     * Represents Label with sodium channels reversal potential.
     */
    @FXML
    private Label lblENa;
    /**
     * Represents Label with potassium channels reversal potential.
     */
    @FXML
    private Label lblEK;
    /**
     * Represents Label with leak channels reversal potential.
     */
    @FXML
    private Label lblEL;
    /**
     * Represents Label with sodium channels conductance.
     */
    @FXML
    private Label lblGNa;
    /**
     * Represents Label with potassium channels conductance.
     */
    @FXML
    private Label lblGK;
    /**
     * Represents Label with leak channels conductance.
     */
    @FXML
    private Label lblGL;
    /**
     * Represents TextField with capacity value.
     */
    @FXML
    private TextField txtC;
    /**
     * Represents TextField with sodium channels reversal potential.
     */
    @FXML
    private TextField txtENa;
    /**
     * Represents TextField with potassium channels reversal potential.
     */
    @FXML
    private TextField txtEK;
    /**
     * Represents TextField with leak channels reversal potential.
     */
    @FXML
    private TextField txtEL;
    /**
     * Represents TextField with sodium channels conductance.
     */
    @FXML
    private TextField txtGNa;
    /**
     * Represents TextField with potassium channels conductance.
     */
    @FXML
    private TextField txtGK;
    /**
     * Represents TextField with leak channels conductance.
     */
    @FXML
    private TextField txtGL;
    /**
     * Represents object of class used to integrate equations.
     */
    private FirstOrderDifferentialEquations neuron;
    /**
     * Represents object of class NeuronPath storing integration results.
     */
    private NeuronPath neuronPath;

    /**
     * Pressing button show recalls calculating method, displays charts and stats and saves results to file.
     */
    @FXML
    void btnShowPressed(ActionEvent event) {
        calculate();
        displayCharts();
        neuronPath.calcDymPar();
        showStats();
        neuronPath.save();
    }

    /**
     * Sets TextFiled with equations parameters to default values.
     */
    @FXML
    void btnDefaultPressed(ActionEvent event) {
        txtC.setText("1.0");
        txtENa.setText("115.0");
        txtEK.setText("-12.0");
        txtEL.setText("10.6");
        txtGNa.setText("120.0");
        txtGK.setText("36.0");
        txtGL.setText("0.3");
    }

    /**
     * Clears charts and stats.
     */
    @FXML
    void btnClearPressed(ActionEvent event) {
        chartXU.getData().clear();
        uChart.getData().clear();
        iChart.getData().clear();
        mhnChart.getData().clear();
        txtAreaStats.clear();
    }

    /**
     * Calculate values using classes solving differential equations.
     */
    public void calculate() {

        double tStart = Double.parseDouble(txtTStart.getText());
        double tStop = Double.parseDouble(txtTStop.getText());
        double dt = Double.parseDouble(txtDT.getText());
        double synapticCurrent = Double.parseDouble(txtI.getText());
        double tDelay = 0.1 * (tStop - tStart) + tStart;

        double c = Double.parseDouble(txtC.getText());
        double eNa = Double.parseDouble(txtENa.getText());
        double eK = Double.parseDouble(txtEK.getText());
        double eL = Double.parseDouble(txtEL.getText());
        double gNa = Double.parseDouble(txtGNa.getText());
        double gK = Double.parseDouble(txtGK.getText());
        double gL = Double.parseDouble(txtGL.getText());

        neuron = new Neuron(c, eNa, eK, eL, gNa, gK, gL, synapticCurrent, tDelay);
        FirstOrderIntegrator rkIntegrator = new ClassicalRungeKuttaIntegrator(dt);
        neuronPath = new NeuronPath(c, eNa, eK, eL, gNa, gK, gL, synapticCurrent, tDelay);
        rkIntegrator.addStepHandler(neuronPath);

        double u0 = 0.0;
        double alfaM0 = 0.1 * (25 - u0) / (Math.exp((25 - u0) / 10) - 1);
        double betaM0 = 4 * Math.exp(-u0 / 18);

        double alfaN0 = 0.01 * (10 - u0) / (Math.exp((10 - u0) / 10) - 1);
        double betaN0 = 0.125 * Math.exp(-u0 / 80);

        double alfaH0 = 0.07 * Math.exp(-u0 / 20);
        double betaH0 = 1 / (Math.exp((30 - u0) / 10) + 1);

        double m0 = alfaM0 / (alfaM0 + betaM0);
        double n0 = alfaN0 / (alfaN0 + betaN0);
        double h0 = alfaH0 / (alfaH0 + betaH0);

        double[] yStart = {u0, m0, n0, h0};
        double[] yAfter = {0, 0, 0, 0};

        rkIntegrator.integrate(neuron, tStart, yStart, tStop, yAfter);
    }

    /**
     * Displays charts.
     */
    public void displayCharts() {

        XYChart.Series<Number, Number> muSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> nuSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> huSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> uSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> iNaSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> iKSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> iLSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> mSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> nSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> hSeries = new XYChart.Series<>();


        muSeries.setName("m");
        nuSeries.setName("n");
        huSeries.setName("h");
        uSeries.setName("u");
        iNaSeries.setName("iNa");
        iKSeries.setName("iK");
        iLSeries.setName("iL");
        mSeries.setName("m");
        nSeries.setName("n");
        hSeries.setName("h");

        for (int i = 0; i < neuronPath.gettValues().size(); i++) {
            uSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.getuValues().get(i)));
            iNaSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.getiNaValues().get(i)));
            iKSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.getiKValues().get(i)));
            iLSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.getiLValues().get(i)));
            mSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.getmValues().get(i)));
            nSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.getnValues().get(i)));
            hSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.gettValues().get(i), neuronPath.gethValues().get(i)));
        }

        for (int i = 0; i < neuronPath.getuRange().size(); i++) {
            muSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.getuRange().get(i), neuronPath.getmInfValues().get(i)));
            nuSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.getuRange().get(i), neuronPath.getnInfValues().get(i)));
            huSeries.getData().add(new XYChart.Data<Number, Number>(neuronPath.getuRange().get(i), neuronPath.gethInfValues().get(i)));
        }

        chartXU.getData().addAll(muSeries);
        chartXU.getData().addAll(nuSeries);
        chartXU.getData().addAll(huSeries);

        uChart.getData().addAll(uSeries);

        iChart.getData().addAll(iNaSeries);
        iChart.getData().addAll(iKSeries);
        iChart.getData().addAll(iLSeries);

        mhnChart.getData().addAll(mSeries);
        mhnChart.getData().addAll(nSeries);
        mhnChart.getData().addAll(hSeries);

    }

    /**
     * Shows statistics in TextArea.
     */
    void showStats() {
        txtAreaStats.appendText(
                "Maksmalna wartosc iglicy: " + neuronPath.getMaxPeak() +
                        "\n" + "Sredni potencjal iglicy: " + neuronPath.getPeakMean() +
                        "\n" + "Odchylenie standardowe potencjal iglicy: " + neuronPath.getPeakStDev() +
                        "\n" + "Czestotliwosc generowania iglicy: " + neuronPath.getPeakFreq() +
                        "\n" + "Czasy miedzy iglicami: " + Arrays.toString(neuronPath.getSpikesTimePeriods())
        );
    }


}