import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static String currentDir = System.getProperty("user.dir");

    public static ArrayList<Point> ReadData(String filePath) throws FileNotFoundException{
        
        ArrayList<Point> points = new ArrayList<>();

        Scanner inputStream = new Scanner(new File(filePath));

        String[] xs = inputStream.nextLine().split(" ");
        String[] ys = inputStream.nextLine().split(" ");

        int min = Math.min(xs.length, xs.length);

        for(int i = 0 ;i < min; i++){
            double x = Float.parseFloat(xs[i]);
            double y = Float.parseFloat(ys[i]);
            points.add(new Point(x,y));
        }



        return points;

    }



    public static void main(String[] args) {

        if(args.length == 0){
            System.out.println("Please specify a file.");
            System.exit(0);
        }

        String FILEPATH = currentDir + "/" + args[0];

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

                    //Start timer
                    long startTime = System.currentTimeMillis();

                    //Begin computing
                    NewtonInterpolation NI = new NewtonInterpolation(pts);
                    double result = NI.Evaluate(z);

                    //Stop timer
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;

                    System.out.println(String.format("%d points took time: %d ms", NI.points.size(), elapsedTime));
                    System.out.println(result + "\n");
                }
                catch(NumberFormatException e){
                    System.out.println("Please enter a number...");
                }


            }

        }
        catch(FileNotFoundException e){
            System.out.println("Could not find file: " + FILEPATH);
            System.exit(0);
        }


    }


}
