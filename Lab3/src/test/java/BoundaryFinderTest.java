/**
 * Created by Dmitriy on 5/4/2017.
 */
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.jetbrains.annotations.TestOnly;
import org.junit.Test;

import java.io.*;
import java.sql.Time;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

import org.junit.Assert;

public class BoundaryFinderTest {

    boolean areRootsInBoundaries(double [] roots, double l, double r){
        for(int i = 0; i < roots.length; i++){
            if (roots[i] <= l || roots[i] >= r)
                return false;
        }
        return true;
    }

    void runTest(InputStream in, double [] roots) throws InterruptedException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BoundaryFinder bf = new BoundaryFinder(in, out);
        Thread th = new Thread(bf);
        th.start();
        Thread.currentThread().sleep(100);
        while(!bf.isWritten()){

        }
        ByteArrayInputStream in2 = new ByteArrayInputStream(out.toByteArray());
        Scanner scan = new Scanner(in2);
        scan.useLocale(Locale.US);
        int n = scan.nextInt();
        double [] c = new double[n];
        for(int i = 0; i < n; i++)
            c[i] = scan.nextDouble();
        double l = scan.nextDouble(), r = scan.nextDouble();
        Assert.assertEquals(areRootsInBoundaries(roots, l, r), true);
    }

    double [] generatePolynomial(double [] roots){
        double [] a = {1};
        double [] b = {1, 0};
        PolynomialFunction p = new PolynomialFunction(a);
        for(int i = 0; i < roots.length; i++){
            b[1] = -roots[i];
            p = p.multiply(new PolynomialFunction(b));
        }
        return p.getCoefficients();
    }

    @Test

    public void test() throws InterruptedException, IOException{
        Random r = new Random();
        for (int k = 0; k < 30; k++) {
            int n = 2 + Math.abs(r.nextInt() % 25);
            double[] roots = new double[n];
            for (int i = 0; i < n; i++) {
                roots[i] = r.nextDouble();
            }
            n++;
            double[] c = generatePolynomial(roots);
            String s = Integer.toString(n) + ' ';
            for (int i = 0; i < n; i++) {
                s += Double.toString(c[i]) + ' ';
            }
            ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
            runTest(in, roots);
            in.close();
        }
    }

}
