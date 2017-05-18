import org.apache.commons.math.analysis.polynomials.PolynomialFunction;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Dmitriy on 5/4/2017.
 */
public class RootsBoundariesDistributer implements Runnable, DataProcesser{

    private PipedInputStream in;
    private PipedOutputStream out;
    private int n;
    private RootBoundaries currentBoundaries;
    private double [] coefficients;
    private ArrayList<RootBoundaries> list = new ArrayList<RootBoundaries>();

    RootsBoundariesDistributer(PipedInputStream newIn){
        in = newIn;
    }

    public void dataProcessing() throws IOException {
        for(RootBoundaries temp: list){
            out = new PipedOutputStream();
            PipedInputStream newIn = new PipedInputStream(out);
            currentBoundaries = temp;
            writeData();
            out.close();
            Thread th = new Thread(new RootsFinder(newIn, System.out));
            th.start();
        }
    }

    public void readData() throws IOException {
        Scanner dataIn = new Scanner(in);
        dataIn.useLocale(Locale.US);
        n = dataIn.nextInt();
        coefficients = new double[n];
        for(int i = 0; i < n; i++)
            coefficients[i] = dataIn.nextDouble();
        while(dataIn.hasNext()) {
            list.add(new RootBoundaries(dataIn.nextDouble(), dataIn.nextDouble()));
        }
    }

    public void writeData() throws IOException {
        out.write((Integer.toString(n) + ' ').getBytes());
        out.write('\n');
        for(int i = 0; i < n; i++)
            out.write((Double.toString(coefficients[i]) + ' ').getBytes());
        out.write('\n');
        out.write((Double.toString(currentBoundaries.leftBoundary) + ' '
                + Double.toString(currentBoundaries.rightBoundary) + '\n').getBytes());
    }

    public void run() {
        try {
            readData();
            dataProcessing();
            //writeData();
            in.close();
            out.close();
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }
}
