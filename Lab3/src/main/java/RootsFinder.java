import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Scanner;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;

/**
 * Created by Dmitriy on 5/4/2017.
 */
public class RootsFinder implements Runnable, DataProcesser {
    private InputStream in;
    private OutputStream out;
    private double lb, rb;
    private final double eps = 0.001;
    private int n;
    private double [] coefficients;
    private PolynomialFunction pf;
    private double root;

    RootsFinder(InputStream newIn, OutputStream newOut){
        in = newIn;
        out = newOut;
    }

    public void dataProcessing() throws IOException {
        while(Math.abs(lb-rb) > eps){
            if(pf.value(lb) * pf.value((lb + rb) / 2) < 0)
                rb = (lb + rb) / 2;
            else
                lb = (lb + rb) / 2;
        }
        root = (lb + rb) / 2;
    }

    public void readData() throws IOException {
        Scanner dataIn = new Scanner(in);
        dataIn.useLocale(Locale.US);
        n = dataIn.nextInt();
        coefficients = new double[n];
        for(int i = 0; i < n; i++)
            coefficients[i] = dataIn.nextDouble();
        pf = new PolynomialFunction(coefficients);
        lb = dataIn.nextDouble();
        rb = dataIn.nextDouble();
    }

    public void writeData() throws IOException {
        out.write((Double.toString(root) + ' ').getBytes());
    }

    public void run() {
        try {
            readData();
            dataProcessing();
            writeData();
            in.close();
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }
}
