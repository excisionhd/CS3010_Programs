import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
    public static String FILEPATH = currentDir + "/src/sys1.pts";

    public static ArrayList<Point> ReadData(String filePath){
        
        ArrayList<Point> points = new ArrayList<>();

        try(Scanner inputStream = new Scanner(new File(filePath))){
            String[] xs = inputStream.nextLine().split(" ");
            String[] ys = inputStream.nextLine().split(" ");

            int min = Math.min(xs.length, xs.length);

            for(int i = 0 ;i < min; i++){
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

    public static double[] Coeff(ArrayList<Point> points){

        double[] cs = new double[points.size()];
        int n = points.size();

        for(int i = 0; i < n; i++){
            cs[i] = points.get(i).y;
        }

        for(int j = 1; j < n; j++){
            for(int i = n-1; i > j-1; i--){
                cs[i] = (cs[i] - cs[i-1]) / (points.get(i).x - points.get(i - j).x);
            }
        }

        return cs;
    }

    public static double EvalNewton(ArrayList<Point> points, double[] cs, double z){
        int n = points.size();

        double result = cs[n-1];

        for(int i = n - 2; i>=0; i--){
            result = result * (z - points.get(i).x) + cs[i];
        }

        return result;
    }

    public static double Newton(ArrayList<Point> points, double z){

        double[] cs = Coeff(points);
        return EvalNewton(points, cs, z);
    }

    public static void main(String[] args) {

        try{
            ArrayList<Point> pts = ReadData(FILEPATH);
            System.out.printf("Successfully loaded file: %s\n",FILEPATH);
            for(Point p: pts){
                System.out.println(p.toString());
            }

            Scanner kb = new Scanner(System.in);
            double z;

            while(true){
                System.out.println("Enter a number to evaluate:");

                String response = kb.next();

                if(response.equals("q")){
                    break;
                }

                try{
                    z = Double.parseDouble(response);
                    double result = Newton(pts,z);
                    System.out.println(result);
                }
                catch(NumberFormatException e){
                    System.out.println("Please enter a number...");
                }


            }

        }
        catch(Exception e){
            e.printStackTrace();
        }





    }


}
