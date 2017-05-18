import java.io.*;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dmitriy on 4/26/2017.
 */
public class BoundaryFinder implements Runnable, DataProcesser{
    private InputStream in;
    private OutputStream out;
    private boolean isWritten = false;

    private int n;
    private double [] coefficients;
    private double Rb, Lb;

    public void setIn(InputStream inputStream){
        in = inputStream;
    }

    BoundaryFinder(InputStream newIn, OutputStream newOut)
            throws InterruptedException{
        in = newIn;
        out = newOut;
    }

    public void run() {
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
    }

    public void dataProcessing() {
        double a = coefficients[n - 1], B = coefficients[n - 2];
        int k = -1;
        for(int i = n-2; i >= 0; i--){
            if(coefficients[i] < 0 && k == -1)
                k = i;
            if(Math.abs(coefficients[i]) > B && coefficients[i] < 0)
                B = Math.abs(coefficients[i]);
        }
        a = Math.max(Math.abs(coefficients[n - 1]), B);
        //aM = Math.max(Math.abs(coefficients[n - 1]), B);

        Rb = 1 + a/Math.abs(coefficients[n - 1]);
        Lb = -(1 + a/Math.abs(coefficients[n - 1]));
    }

    public void readData() throws IOException {
        Scanner dataIn = new Scanner(in);
        dataIn.useLocale(Locale.US);
        n = dataIn.nextInt();
        coefficients = new double[n];
        for(int i = 0; i < n; i++)
            coefficients[i] = dataIn.nextDouble();
    }

    public void writeData() throws IOException {
        //OutputStream dataOut = new BufferedOutputStream(out);
        out.write((Integer.toString(n) + ' ').getBytes());
        for(int i = 0; i < n; i++)
            out.write((Double.toString(coefficients[i]) + ' ').getBytes());
        out.write((Double.toString(Lb) + ' ' + Double.toString(Rb)).getBytes());
    }

    public boolean isWritten(){return isWritten;}
}
