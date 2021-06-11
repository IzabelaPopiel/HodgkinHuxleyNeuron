package sample;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
/**
 * Class solving differential equations.
 */
public class Neuron implements FirstOrderDifferentialEquations {

    /**
     * Represents capacity.
     */
    private double c;
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
     * Creates object of class Neuron.
     * @param c capacity
     * @param eNa sodium channels reversal potential
     * @param eK potassium channels reversal potential
     * @param eL leak channels reversal potential
     * @param gNa sodium channels conductance
     * @param gK potassium channels conductance
     * @param gL leak channels conductance
     */
    public Neuron(double c, double eNa, double eK, double eL, double gNa, double gK, double gL, double synapticCurrent, double tJump) {
        this.c = c;
        this.eNa = eNa;
        this.eK = eK;
        this.eL = eL;
        this.gNa = gNa;
        this.gK = gK;
        this.gL = gL;
        this.synapticCurrent = synapticCurrent;
        this.tJump = tJump;
    }

    /**
     * Represents synaptic current.
     */
    double synapticCurrent;
    /**
     * Represents time of turning synaptic current on.
     */
    double tJump;

    /**
     * Gets dimension of array with integration results.
     */
    @Override
    public int getDimension() {
        return 4;
    }

    /**
     * Calculates derivatives.
     * @param t time
     * @param u array [u, m, n, h]
     * @param dudt array [du, dm, dn, dh]
     */
    @Override
    public void computeDerivatives(double t, double[] u, double[] dudt) throws MaxCountExceededException, DimensionMismatchException {
        int currentOn = 0;
        if (t > tJump) {
            currentOn = 1;
        }

        //u[0]=u
        //u[1]=m
        //u[2]=n
        //u[3]=h

        double alfaM = 0.1 * (25 - u[0]) / (Math.exp((25 - u[0]) / 10) - 1);
        double betaM = 4 * Math.exp(-u[0] / 18);

        double alfaN = 0.01 * (10 - u[0]) / (Math.exp((10 - u[0]) / 10) - 1);
        double betaN = 0.125 * Math.exp(-u[0] / 80);

        double alfaH = 0.07 * Math.exp(-u[0] / 20);
        double betaH = 1 / (Math.exp((30 - u[0]) / 10) + 1);


        dudt[0] = (-(gNa * Math.pow(u[1], 3) * u[3] * (u[0] - eNa) + gK * Math.pow(u[2], 4) * (u[0] - eK) + gL * (u[0] - eL)) + synapticCurrent * currentOn) / c;
        dudt[1] = alfaM * (1 - u[1]) - betaM * u[1];
        dudt[2] = alfaN * (1 - u[2]) - betaN * u[2];
        dudt[3] = alfaH * (1 - u[3]) - betaH * u[3];
    }
}
