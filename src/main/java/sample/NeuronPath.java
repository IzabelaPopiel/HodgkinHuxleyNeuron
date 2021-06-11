
package sample;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.stat.StatUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class storing solutions of differential equations.
 */
public class NeuronPath implements StepHandler {
    /**
     * Represents ArrayList with u values.
     */
    private ArrayList<Double> uValues = new ArrayList<>();
    /**
     * Represents ArrayList with m values.
     */
    private ArrayList<Double> mValues = new ArrayList<>();
    /**
     * Represents ArrayList with n values.
     */
    private ArrayList<Double> nValues = new ArrayList<>();
    /**
     * Represents ArrayList with h values.
     */
    private ArrayList<Double> hValues = new ArrayList<>();
    /**
     * Represents ArrayList with t values.
     */
    private ArrayList<Double> tValues = new ArrayList<>();
    /**
     * Represents ArrayList with Na ion current values.
     */
    private ArrayList<Double> iNaValues = new ArrayList<>();
    /**
     * Represents ArrayList with K ion current values.
     */
    private ArrayList<Double> iKValues = new ArrayList<>();
    /**
     * Represents ArrayList with leak current values.
     */
    private ArrayList<Double> iLValues = new ArrayList<>();
    /**
     * Represents ArrayList with m in steady state activation values.
     */
    private ArrayList<Double> mInfValues = new ArrayList<>();
    /**
     * Represents ArrayList with n in steady state activation values.
     */
    private ArrayList<Double> nInfValues = new ArrayList<>();
    /**
     * Represents ArrayList with h in steady state activation values.
     */
    private ArrayList<Double> hInfValues = new ArrayList<>();

    /**
     * Represents capacity.
     */
    private double C;
    /**
     * Represents sodium channels reversal potential.
     */
    private double eNa;
    /**
     * Represents potassium channels reversal potential.
     */
    private double eK;
    /**
     * Represents leak channels reversal potential.
     */
    private double eL;
    /**
     * Represents sodium channels conductance.
     */
    private double gNa;
    /**
     * Represents potassium channels conductance.
     */
    private double gK;
    /**
     * Represents leak channels conductance.
     */
    private double gL;

    /**
     * Represents synaptic current.
     */
    double synapticCurrent;
    /**
     * Represents time of turning synaptic current on.
     */
    double tJump;
    /**
     * Represents maximum value of spike.
     */
    private double maxPeak;
    /**
     * Represents mean value of spike.
     */
    private double peakMean;
    /**
     * Represents STD value of spike.
     */
    private double peakStDev;
    /**
     * Represents spike generation frequency.
     */
    private double peakFreq;
    /**
     * Represents spike generation frequency - 2nd calculating method.
     */
    private double peakFreq2;
    /**
     * Represents array with times between spikes.
     */
    double[] spikesTimePeriods;
    /**
     * Represents ArrayList with u values in rangw [-50, 150] mV
     */
    ArrayList<Double> uRange = new ArrayList<>();

    /**
     * Creates object of class NeuronPath and calculate steady state activation values of m, n, h for u values in range [-50, 150] mV
     *
     * @param C               capacity
     * @param eNa             sodium channels reversal potential
     * @param eK              potassium channels reversal potential
     * @param eL              leak channels reversal potential
     * @param gNa             sodium channels conductance
     * @param gK              potassium channels conductance
     * @param gL              leak channels conductance
     * @param synapticCurrent synaptic current
     * @param tJump           time of turning current on
     */
    public NeuronPath(double C, double eNa, double eK, double eL, double gNa, double gK, double gL, double synapticCurrent, double tJump) {
        this.C = C;
        this.eNa = eNa;
        this.eK = eK;
        this.eL = eL;
        this.gNa = gNa;
        this.gK = gK;
        this.gL = gL;
        this.synapticCurrent = synapticCurrent;
        this.tJump = tJump;
        calculateMNHUValues();
    }

    /**
     * Gets ArrayList with u values.
     */
    public ArrayList<Double> getuValues() {
        return uValues;
    }

    /**
     * Gets ArrayList with m values.
     */
    public ArrayList<Double> getmValues() {
        return mValues;
    }

    /**
     * Gets ArrayList with n values.
     */
    public ArrayList<Double> getnValues() {
        return nValues;
    }

    /**
     * Gets ArrayList with h values.
     */
    public ArrayList<Double> gethValues() {
        return hValues;
    }

    /**
     * Gets ArrayList with t values.
     */
    public ArrayList<Double> gettValues() {
        return tValues;
    }

    /**
     * Gets ArrayList with Na ion current values.
     */
    public ArrayList<Double> getiNaValues() {
        return iNaValues;
    }

    /**
     * Gets ArrayList with K ion current values.
     */
    public ArrayList<Double> getiKValues() {
        return iKValues;
    }

    /**
     * Gets ArrayList with leak current values.
     */
    public ArrayList<Double> getiLValues() {
        return iLValues;
    }

    /**
     * Gets maximum spike value.
     */
    public double getMaxPeak() {
        return maxPeak;
    }

    /**
     * Gets mean spike value.
     */
    public double getPeakMean() {
        return peakMean;
    }

    /**
     * Gets STD spike value.
     */
    public double getPeakStDev() {
        return peakStDev;
    }

    /**
     * Gets spike generating frequency value.
     */
    public double getPeakFreq() {
        return peakFreq;
    }

    /**
     * Gets spike generating frequency (2nd calculating method) value.
     */
    public double getPeakFreq2() {
        return peakFreq;
    }

    /**
     * Gets Array with times between spikes.
     */
    public double[] getSpikesTimePeriods() {
        return spikesTimePeriods;
    }

    /**
     * Gets ArrayList with m steady state activation value.
     */
    public ArrayList<Double> getmInfValues() {
        return mInfValues;
    }

    /**
     * Gets ArrayList with n steady state activation value.
     */
    public ArrayList<Double> getnInfValues() {
        return nInfValues;
    }

    /**
     * Gets ArrayList with h steady state activation value.
     */
    public ArrayList<Double> gethInfValues() {
        return hInfValues;
    }

    /**
     * Gets ArrayList with u steady state activation value.
     */
    public ArrayList<Double> getuRange() {
        return uRange;
    }

    /**
     * Initialize StepHandler.
     */
    @Override
    public void init(double v, double[] doubles, double v1) {

    }

    /**
     * Adds calculated values to ArrayLists.
     */
    @Override
    public void handleStep(StepInterpolator stepInterpolator, boolean b) throws MaxCountExceededException {

        double t = stepInterpolator.getCurrentTime();
        double[] u = stepInterpolator.getInterpolatedState();

        uValues.add(u[0]);
        mValues.add(u[1]);
        nValues.add(u[2]);
        hValues.add(u[3]);
        tValues.add(t);
        iNaValues.add(gNa * Math.pow(u[1], 3) * u[3] * (u[0] - eNa));
        iKValues.add(gK * Math.pow(u[2], 4) * (u[0] - eK));
        iLValues.add(gL * (u[0] - eL));
    }

    /**
     * Calculates dynamic parameters.
     */
    public void calcDymPar() {

        double[] uValuesArr = new double[uValues.size()];
        double[] tValuesArr = new double[tValues.size()];


        for (int i = 0; i < uValues.size(); i++) {
            uValuesArr[i] = uValues.get(i);
            tValuesArr[i] = tValues.get(i);

        }

        maxPeak = StatUtils.max(uValuesArr);
        System.out.println(maxPeak);


        ArrayList<Double> spikesValues = new ArrayList<>();
        ArrayList<Double> spikesTime = new ArrayList<>();

        //peak finding algortihm

        for (int i = 1; i < uValuesArr.length - 1; i++) {
            if (uValuesArr[i - 1] < uValuesArr[i] && uValuesArr[i] > uValuesArr[i + 1] && uValuesArr[i] > maxPeak / 2) {
                spikesValues.add(uValuesArr[i]);
                spikesTime.add(tValuesArr[i]);
            }

        }

        System.out.println(spikesValues);
        System.out.println(spikesTime);


        double[] spikesValuesArr = new double[spikesValues.size()];
        double[] spikesTimesArr = new double[spikesTime.size()];


        for (int i = 0; i < spikesValues.size(); i++) {
            spikesValuesArr[i] = spikesValues.get(i);
            spikesTimesArr[i] = spikesTime.get(i);
        }

        peakMean = StatUtils.mean(spikesValuesArr);
        System.out.println(peakMean);

        spikesTimePeriods = new double[spikesTimesArr.length - 1];

        for (int i = 0; i < spikesTimesArr.length - 1; i++) {
            spikesTimePeriods[i] = spikesTimesArr[i + 1] - spikesTimesArr[i];
        }
        System.out.println(Arrays.toString(spikesTimePeriods));

        peakFreq = 1 / StatUtils.mean(spikesTimePeriods);
        System.out.println(peakFreq);
//        peakFreq2 = 1/(spikesTimePeriods.length * StatUtils.sum(spikesTimePeriods));
        peakFreq2 = spikesTimePeriods.length / StatUtils.sum(spikesTimePeriods);
        System.out.println("freq2: " + peakFreq2);
        System.out.println(Math.abs(peakFreq - peakFreq2));

        peakStDev = Math.sqrt(StatUtils.variance(spikesValuesArr));
    }

    /**
     * Calculate steady state activation values of m, n, h for u values in range [-50, 150] mV
     */
    void calculateMNHUValues() {
        for (double i = -50; i <= 150; i += 0.1) {
            uRange.add(i);
        }

        for (int i = 0; i < uRange.size(); i++) {
            double alfaM = 0.1 * (25 - uRange.get(i)) / (Math.exp((25 - uRange.get(i)) / 10) - 1);
            double betaM = 4 * Math.exp(-uRange.get(i) / 18);

            double alfaN = 0.01 * (10 - uRange.get(i)) / (Math.exp((10 - uRange.get(i)) / 10) - 1);
            double betaN = 0.125 * Math.exp(-uRange.get(i) / 80);

            double alfaH = 0.07 * Math.exp(-uRange.get(i) / 20);
            double betaH = 1 / (Math.exp((30 - uRange.get(i)) / 10) + 1);


            mInfValues.add(alfaM / (alfaM + betaM));
            nInfValues.add(alfaN / (alfaN + betaN));
            hInfValues.add(alfaH / (alfaH + betaH));
        }
    }

    /**
     * Saves input parameters and dynamic parameters to file.
     */
    void save() {

        File file = new File("data.txt");
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(synapticCurrent + "\t" +
                    maxPeak + "\t" +
                    peakMean + "\t" +
                    peakStDev + "\t" +
                    peakFreq + "\t" +
                    peakFreq2 + "\t" +
                    C + "\t" +
                    eNa + "\t" +
                    eK + "\t" +
                    eL + "\t" +
                    gNa + "\t" +
                    gK + "\t" +
                    gL + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
