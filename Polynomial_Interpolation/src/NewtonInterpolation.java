import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;

public class NewtonInterpolation {

    public static ArrayList<Point> points;
    public static double[] cs;

    public NewtonInterpolation(ArrayList<Point> points){
        this.points = points;
        this.cs = new double[points.size()];

        //Initialize CS
        Coeff();
    }

    public static void Coeff(){

        int n = points.size();

        for(int i = 0; i < n; i++){
            cs[i] = points.get(i).y;
        }

        for(int j = 1; j < n; j++){
            for(int i = n-1; i > j-1; i--){
                cs[i] = (cs[i] - cs[i-1]) / (points.get(i).x - points.get(i - j).x);
            }
        }
    }

    public double Evaluate(double z){

        int n = points.size();

        double result = cs[n-1];

        for(int i = n - 2; i>=0; i--){
            result = result * (z - points.get(i).x) + cs[i];
        }

        return result;
    }

}
