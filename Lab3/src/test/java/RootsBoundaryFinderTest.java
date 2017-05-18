import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Dmitriy on 5/4/2017.
 */
public class RootsBoundaryFinderTest {

    boolean isOneRootInBondaries(double [] roots, double l, double r){
        int n = 0;
        for(int i = 0; i < roots.length; i++){
            if(l - RootsBoundaryFinder.eps/2 < roots[i] && roots[i] < r + RootsBoundaryFinder.eps/2)
                n++;
        }
        if(n == 1)
            return true;
        return false;
    }

    void runTest(InputStream in, double [] roots) throws InterruptedException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RootsBoundaryFinder rbf = new RootsBoundaryFinder(in, out);
        Thread th = new Thread(rbf);
        th.start();
        Thread.currentThread().sleep(100);
        while(!rbf.isWritten()){

        }
        ByteArrayInputStream in2 = new ByteArrayInputStream(out.toByteArray());
        Scanner scan = new Scanner(in2);
        scan.useLocale(Locale.US);
        int n = scan.nextInt();
        double [] c = new double[n];
        for(int i = 0; i < n; i++)
            c[i] = scan.nextDouble();
        while(scan.hasNextDouble()){
            double l = scan.nextDouble(), r = scan.nextDouble();
            Assert.assertEquals(isOneRootInBondaries(roots, l, r), true);
        }
    }

    double [] generatePolynomial(double [] roots){
        double [] a = {1};
        double [] b = {0, 1};
        PolynomialFunction p = new PolynomialFunction(a);
        for(int i = 0; i < roots.length; i++){
            b[0] = -roots[i];
            p = p.multiply(new PolynomialFunction(b));
        }
        return p.getCoefficients();
    }

    @Test

    public void test() throws InterruptedException, IOException {
        Random r = new Random();
        for (int k = 0; k < 5; k++) {
            int n = 4 + Math.abs(r.nextInt() % 4);
            double[] roots = new double[n];
            for (int i = 0; i < n; i++) {
                roots[i] = r.nextInt() % 5;
                for (int j = 0; j < i; j++)
                    if (roots[j] == roots[i]){
                        i--;
                        break;
                    }
            }
//            double [] roots = {1, -3, 5, -7};
            n++;
            double[] c = generatePolynomial(roots);
            String s = Integer.toString(n) + ' ';
            for (int i = 0; i < n; i++) {
                s += Double.toString(c[i]) + ' ';
            }
            Arrays.sort(roots);
            s += Double.toString(roots[0] - 10) + ' ' + Double.toString(roots[n - 2] + 10);
            ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
            runTest(in, roots);
            Thread.currentThread().sleep(1000);
            in.close();
        }
    }
}
