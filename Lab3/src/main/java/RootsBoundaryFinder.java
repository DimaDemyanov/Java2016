import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import com.sun.istack.internal.NotNull;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;

/**
 * Created by Dmitriy on 4/27/2017.
 */
public class RootsBoundaryFinder implements Runnable, DataProcesser{

    private InputStream in = System.in;
    private OutputStream out = System.out;
    public static final double eps = 0.001;
    public static final double eps2 = 0.00001;
    private int n;
    private double [] coefficients;
    private ArrayList<RootBoundaries> list = new ArrayList<RootBoundaries>();
    private double Rb, Lb;
    private PipedIOStream io;
    double [][] shturmSequence;
    private PolynomialFunction [] shturmSequencePolynomial;
    private boolean isWritten = false;

    RootsBoundaryFinder(InputStream newIn, OutputStream newOut){
        in = newIn;
        out = newOut;
    }

    private int takeCountReverse(double x){
        double last = 0;
        int count = 0;
        for(int i = 0; i < n; i++){
            if(last * shturmSequencePolynomial[i].value(x) < 0) count++;
            if(shturmSequencePolynomial[i].value(x) != 0)
                last = shturmSequencePolynomial[i].value(x);
        }
        return count;
    }

    private void getRootsBoundaries(double L, double R){
        double r = R - eps , l = L + eps;
        int rootsCnt = takeCountReverse(l) - takeCountReverse(r);
        if(rootsCnt == 0  || Math.abs(r - l) < eps) return;
        if(rootsCnt == 1)
            list.add(new RootBoundaries(l, r));
        //out.write((Double.toString(l) + ' ' + Double.toString(r) + '\n').getBytes());
        if (rootsCnt >= 2){
            if (Utils.isClose(shturmSequencePolynomial[0].value((r + l)/2), 0, eps2)){
                list.add(new RootBoundaries((r + l)/2, (r + l)/2));
            }
               //out.write((Double.toString((r + l)/2) + ' ' + Double.toString((r + l)/2) + '\n').getBytes());

            getRootsBoundaries(l, (r + l)/2);
            getRootsBoundaries((r + l) / 2, r);
        }
//            if (Utils.isClose(shturmSequencePolynomial[0].value(r), 0, eps))
//                out.write((Double.toString(r) + ' ' + Double.toString(r)).getBytes());
//            if (Utils.isClose(shturmSequencePolynomial[0].value(l), 0, eps))
//                out.write((Double.toString(l) + ' ' + Double.toString(l)).getBytes())
    }

    private void doPolynomialShturmSequence(){
        shturmSequencePolynomial = new PolynomialFunction[n];
        for(int i = 0; i < n; i++){
            shturmSequencePolynomial[i] = new PolynomialFunction(shturmSequence[i]);
        }
    }

    private void buidShturmSequence(){
        PolynomialFunction pf, sf;
        shturmSequence = new double[n][];
        shturmSequence[0] = coefficients.clone();
        pf = new PolynomialFunction(shturmSequence[0]);
        pf = pf.polynomialDerivative();
        shturmSequence[1] = pf.getCoefficients();

        int i = 2;

        for(i = 2;i < n;i++){
            sf = new PolynomialFunction(new double[]{0, 1.0 *
                    shturmSequence[i - 2][shturmSequence[i - 2].length - 1] /
                    shturmSequence[i - 1][shturmSequence[i - 1].length - 1]});
            pf = (new PolynomialFunction(shturmSequence[i - 2]))
                    .subtract(sf.multiply(new PolynomialFunction(shturmSequence[i - 1])));
            if(pf.getCoefficients().length == shturmSequence[i - 1].length) {
                sf = new PolynomialFunction(new double[]{1.0 *
                        pf.getCoefficients()[pf.getCoefficients().length - 1] /
                        shturmSequence[i - 1][shturmSequence[i - 1].length - 1]});
                pf = (pf).subtract(sf.multiply(new PolynomialFunction(shturmSequence[i - 1])));
            }
            pf = pf.negate();
            shturmSequence[i] = pf.getCoefficients();
        }
        doPolynomialShturmSequence();
    }

    public void dataProcessing() {
        buidShturmSequence();
        getRootsBoundaries(Lb, Rb);
    }

    public void readData() throws IOException {
        Scanner dataIn = new Scanner(in);
        dataIn.useLocale(Locale.US);
        n = dataIn.nextInt();
        coefficients = new double[n];
        for(int i = 0; i < n; i++)
            coefficients[i] = dataIn.nextDouble();
        Lb = dataIn.nextDouble();
        Rb = dataIn.nextDouble();
    }

    public void writeData() throws IOException {
        out.write((Integer.toString(n) + ' ').getBytes());
        out.write('\n');
        for(int i = 0; i < n; i++)
            out.write((Double.toString(coefficients[i]) + ' ').getBytes());
        out.write('\n');
        for (RootBoundaries temp: list){
            out.write((Double.toString(temp.leftBoundary) + ' ' + Double.toString(temp.rightBoundary) + '\n').getBytes());
        }
    }

    public void run() {
//        synchronized (io) {
            try {
                readData();
                dataProcessing();
                writeData();
                in.close();
                out.close();
                isWritten = true;
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
  //      }
    }

    public boolean isWritten(){return isWritten;}
}
