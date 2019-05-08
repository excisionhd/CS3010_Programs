import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


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



    public static void main(String[] args) {

        try{
            ArrayList<Point> pts = ReadData(FILEPATH);
            System.out.printf("Successfully loaded file: %s\n",FILEPATH);
            for(Point p: pts){
                System.out.println(p.toString());
            }
            NewtonInterpolation NI = new NewtonInterpolation(pts);

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
                    double result = NI.Evaluate(z);
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
