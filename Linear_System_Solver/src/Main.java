import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static double[][] coeff;
    public static double[] sol;

    public static void main(String[] args) {
        try{
            String fileName;
            double[] ans;
            if (args[0].equals("--spp")) {
                fileName = args[1];
                importMatrix(fileName);
                System.out.println("Original Matrix:");
                GE.PrintMatrix(coeff, sol);

                System.out.println("Upper Triangular Form:");
                ans = GE.GaussianEliminationSPP(coeff, sol);

                GE.PrintMatrix(coeff, sol);

                System.out.println("Gaussian Scaled Partial Pivoting Solution:");
                GE.PrintAnswer(ans);
            }
            else{
                fileName = args[0];
                importMatrix(fileName);
                System.out.println("Original Matrix:");
                GE.PrintMatrix(coeff, sol);

                System.out.println("Upper Triangular Form:");
                ans = GE.NaiveGaussianElimination(coeff, sol);

                GE.PrintMatrix(coeff, sol);

                System.out.println("Naive Gaussian Solution:");
                GE.PrintAnswer(ans);
            }

            writeToFile(fileName, ans);


        }
        catch(Exception e){
            System.out.println("Incorrect input, please provide proper parameters.");
        }


    }

    //Import linear system from a file
    public static void importMatrix(String fileName){
        try{
            Scanner scan = new Scanner(new File(System.getProperty("user.dir")+ "/" + fileName));
            int size = scan.nextInt();

            coeff = new double[size][size];
            sol = new double[size];

            for(int i = 0; i<size; i++){
                for(int j = 0; j<coeff[i].length; j++){
                    coeff[i][j] = scan.nextDouble();
                }
            }

            for(int i = 0; i < size; i++){
                sol[i] = scan.nextDouble();
            }

        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    //Export to a solution file.
    public static void writeToFile(String fileName, double[] ans){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.sol", System.getProperty("user.dir"), fileName.split("\\.")[0])));
            for(int i = 0; i<ans.length; i++){
                writer.write(String.format("X%d: %.4f\n", i+1, ans[i] + 0.0));
            }

            writer.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}

class GE{

    //Print a matrix in a readable format.
    public static void PrintMatrix(double[][] matrix, double[] sol){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j<matrix[i].length; j++){
                System.out.printf(String.format("%.4f\t\t", matrix[i][j]));
            }
            System.out.printf(String.format("%.4f\t\t\n", sol[i]));
        }
    }

    //Print solution vector in readable format.
    public static void PrintAnswer(double[] ans){
        for(int i = 0; i<ans.length; i++){
            System.out.printf("X%d: %.4f\n", i+1, ans[i] + 0.0);
        }
    }

    public static double[] NaiveGaussianElimination(double[][] coeff, double[] sol){

        int size = coeff.length;

        for(int i = 0 ; i < size; i++){
            for (int k = i + 1; k < size; k++){
                double scale = -(coeff[k][i])/(coeff[i][i]);

                for(int j = i; j < size; j++){
                    if (i == j){
                        coeff[k][j] = 0;
                    }
                    else{
                        coeff[k][j] += scale * coeff[i][j];
                    }
                }

                sol[k] += scale * sol[i];
            }
        }

        return BackSubstitution(coeff, sol);
    }

    //Back substitute from bottom to top
    public static double[] BackSubstitution(double[][] coeff, double[] sol){

        double[] answer = new double[coeff.length];
        
        for (int i = coeff.length - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < coeff.length; j++) {
                sum += coeff[i][j] * answer[j];
            }
            answer[i] = (sol[i] - sum) / coeff[i][i];
        }


        return answer;
    }

    //BEGIN SCALED PARTIAL PIVOTING METHOD
    //Returns the index with the chosen pivot.
    public static int PartialPivot(double[][] coeff, double[] S, int iter, HashSet<Integer> unusedPivots, int k){

        int size  = coeff.length;
        double[] R = new double[size];

        //Compute R vector
        for(int i = 0; i<size; i++){
            R[i] = Math.abs(coeff[i][k]) / S[i];
        }

        //System.out.println("R: " + Arrays.toString(R));
        int maxIndex = 0;
        double max = 0;

        //Choose the max pivot from R that has not been chosen yet.
        for(int i = 0; i<R.length; i++){
            double num = R[i];
            if (num > max && unusedPivots.contains(i)){
                max = num;
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    //Compute the S array for assisting with pivot calculation.
    public static double[] ComputeS(double[][] coeff){
        int size = coeff.length;
        double[] S = new double[size];

        for(int i = 0; i<size; i++){
            double max = -1;

            //Obtain the max (Absolute Value) of each row
            for(int j = 0; j<size; j++){
                max = Math.max(max, Math.abs(coeff[i][j]));
            }
            S[i] = max;
        }

        return S;
    }

    //Scaled partial pivoting implemention.
    public static double[] GaussianEliminationSPP(double[][] coeff, double[] sol){
        int[] order = new int[coeff.length];
        int size = coeff.length;

        //Create hashset for unused pivots.
        HashSet<Integer> unusedPivots = new HashSet<>();

        //Initial order = 0, 1, .. n-1.
        for(int i = 0; i <order.length; i++){
            order[i] = i;
            unusedPivots.add(i);
        }

        double[] S = ComputeS(coeff);
        //System.out.println("S: " + Arrays.toString(S));

        //Iterate size - 1 times...
        for(int i = 0; i<size -1; i++){
            int iter = size - i - 1;
            int pivot = PartialPivot(coeff, S, iter, unusedPivots, i);
            //System.out.println("Pivot: " + pivot);

            //Swap the pivot with the current iteration in the I (order) array.
            swap(i, pivot, order);
            //System.out.println("Order: " + Arrays.toString(order));

            //Remove the current pivot from hashset to avoid eliminating the row.
            unusedPivots.remove(pivot);

            //Eliminate all non-pivot rows
            for(Integer row : unusedPivots){
                double scale = -(coeff[row][i]) / coeff[pivot][i];

                for(int col = i; col<coeff[row].length; col++){
                    coeff[row][col] = coeff[row][col] + coeff[pivot][col] * scale;
                }

                sol[row] = sol[row] + sol[pivot] * scale;

            }

            //GE.PrintMatrix(coeff,sol);
            //System.out.println();

        }

        return BackSubstitutionSPP(coeff, sol, order);
    }

    public static double[] BackSubstitutionSPP(double[][] coeff, double[] sol, int[] order){
        double[] answer = new double[coeff.length];
        int last = order[order.length - 1];


        //Compute last element
        answer[answer.length - 1] = sol[last]/coeff[last][coeff[last].length - 1];

        for(int i = order.length - 2; i >= 0; i--){
            double sum = 0;
            int currentRow = order[i];

            for(int j = i + 1; j < coeff[i].length; j++){
                sum += coeff[currentRow][j] * answer[j];
            }

            answer[i] = (sol[currentRow] - sum)/ coeff[currentRow][i];
        }

        return answer;
    }

    public static void swap(int i, int pivot, int[] order){

        for(int index = 0; index<order.length; index++){
            if(order[index] == pivot){
                int temp = order[i];
                order[i] = order[index];
                order[index] = temp;
                break;
            }
        }

    }
}

