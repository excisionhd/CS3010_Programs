import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Point{
    double x,y;

    public Point(double x,double y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
public class Main {

    public static String currentDir = System.getProperty("user.dir");

    public static ArrayList<Point> ReadData(String filePath){
        
        ArrayList<Point> points = new ArrayList<>();

        try(Scanner inputStream = new Scanner(new File(filePath))){
            String[] xs = inputStream.nextLine().split(" ");
            String[] ys = inputStream.nextLine().split(" ");

            int min = Math.min(xs.length, xs.length);

            for(int i = 0 ;i < min - 1; i++){
                double x = Float.parseFloat(xs[i]);
                double y = Float.parseFloat(ys[i]);
                points.add(new Point(x,y));
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return points;

    }

    public static void Coeff(ArrayList<Point> points, double[] cs){

        int n = points.size();

        for(int i = 0; i < n; i++){
            cs[i] = points.get(i).y;
        }

        for(int j = 1; j < n; j++){
            for(int i = n; i >= j; i--){
                cs[i] = (cs[i] - cs[i-1]) / (points.get(i).x - points.get(i - j).x);
            }
        }

    }

/*    function Coeff(xs : vector(n+1), ys : vector(n+1), cs : vector(n+1))
            for i <- 0 to n
    cs[i] := ys[i]
    end for

            for j <- 1 to n
    for i <- n down to j
    cs[i] := (cs[i] - cs[i-1]) / (xs[i] - x[i-j])
    end for
    end for
    end function

    function EvalNewton(xs : vector(n+1), cs : vector(n+1), z : float)
    result := cs[n]

            for i <- (n-1) down to 0
    result := result * (z - xs[i]) + cs[i]
    end for

            return result
    end function*/

    public static double EvalNewton(ArrayList<Point> points, double[] cs, double z){
        int n = points.size();

        double result = cs[n-1];

        for(int i = n - 2; i>=0; i--){
            result = result * (z - points.get(i).x) + cs[i];
        }

        return result;
    }
    public static void main(String[] args) {

        ArrayList<Point> pts = ReadData(currentDir + "/src/sys1.pts");
        double[] cs = new double[pts.size()];
        Coeff(pts, cs);

        double result = EvalNewton(pts, cs, 10.162);
        System.out.println(Arrays.toString(cs));
        System.out.println(result);

    }


}
