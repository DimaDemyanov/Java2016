/**
 * Created by Dmitriy on 4/28/2017.
 */
public class Utils {
    public static boolean isClose(double a, double b, double eps){
        if(Math.abs(a - b) < eps) return true;
        return false;
    }
}
