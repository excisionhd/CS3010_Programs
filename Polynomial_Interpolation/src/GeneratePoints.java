import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GeneratePoints {

    public static ArrayList<Point> generatePoints(int n){

        Random r = new Random();
        ArrayList<Point> points = new ArrayList<Point>();

        for(int i = 0; i < n; i++){
            double randomX = -100 + (100 - -100) * r.nextDouble();
            double randomY = -100 + (100 - -100) * r.nextDouble();
            points.add(new Point(randomX, randomY));
        }
        return points;

    }

    public static void writePointsToFile(String fileName, ArrayList<Point> points){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            StringBuilder xs = new StringBuilder();
            StringBuilder ys = new StringBuilder();

            for(Point p: points){
                String x = Double.toString(p.x) + " ";
                String y = Double.toString(p.y) + " ";
                xs.append(x);
                ys.append(y);
            }

            writer.write(xs.toString().trim());
            writer.write("\n");
            writer.write(ys.toString().trim());
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        System.out.println("How many points to generate: ");

        try{
            int n = Integer.parseInt(kb.next());
            ArrayList<Point> points = generatePoints(n);
            writePointsToFile(String.format("%d-points.pts",n), points);

        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }

    }
}
