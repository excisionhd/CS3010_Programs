import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Point{
    float x,y;

    public Point(float x,float y){
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
                float x = Float.parseFloat(xs[i]);
                float y = Float.parseFloat(ys[i]);
                points.add(new Point(x,y));
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return points;

    }
    public static void main(String[] args) {

        ArrayList<Point> pts = ReadData("src/sys1.pts");
        float[] cs = new float[pts.size()];
        Coeff(pts, cs);
        float result = EvalNewton(pts, cs, 2);
        System.out.println(Arrays.toString(cs));
        System.out.println(result);

    }

    public static void Coeff(ArrayList<Point> points, float[] cs){

        int n = points.size() - 1;

        for(int i = 0; i < n; i++){
            cs[i] = points.get(i).y;
        }

        for(int j = 1; j < n; j++){
            for(int i = n; i >= j; i--){
                cs[i] = (cs[i] - cs[i-1]) / (points.get(i).x - points.get(i - j).x);
            }
        }

    }

    public static float EvalNewton(ArrayList<Point> points, float[] cs, float z){
        float result = cs[points.size() -1];

        for(int i = points.size() - 2; i>=0; i--){
            result = result * (z - points.get(i).x) + cs[i];
        }

        return result;
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
        end function
        */
