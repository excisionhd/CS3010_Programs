import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    //Two classes:
    //1. Naive Gaussian Elimination
    //2. Scaled Partial Pivoting

    public static double[][] coeff;
    public static double[] sol;

    public static void main(String[] args) {

        try{
            String fileName;
            if (args[0].equals("--spp")) {
                fileName = args[1];
                importMatrix(fileName);
                System.out.println("Original Matrix:");
                GE.PrintMatrix(coeff, sol);

                System.out.println("Upper Triangular Form:");
                double[] ans = GE.GaussianEliminationSPP(coeff, sol);

                GE.PrintMatrix(coeff, sol);
                GE.PrintAnswer(ans);
            }
            else{
                fileName = args[0];
                importMatrix(fileName);
                System.out.println("Original Matrix:");
                GE.PrintMatrix(coeff, sol);

                System.out.println("Upper Triangular Form:");
                double[] ans = GE.NaiveGaussianElimination(coeff, sol);

                GE.PrintMatrix(coeff, sol);
                GE.PrintAnswer(ans);
            }
        }
        catch(Exception e){
            System.out.println("Incorrect input, please provide proper parameters.");
        }


    }

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
}

class GE{

    public static void PrintMatrix(double[][] matrix, double[] sol){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j<matrix[i].length; j++){
                System.out.printf(String.format("%.4f\t\t", matrix[i][j]));
            }
            System.out.printf(String.format("%.4f\t\t\n", sol[i]));
        }
    }

    public static void PrintAnswer(double[] ans){
        for(int i = 0; i<ans.length; i++){
            System.out.printf("X%d: %.4f\n", i+1, ans[i] + 0.0);
        }
    }

    public static double[] NaiveGaussianElimination(double[][] coeff, double[] sol){

        int size = coeff.length;

        //Loop for each row
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

    public static double[] BackSubstitution(double[][] coeff, double[] sol){
        double[] answer = new double[coeff.length];
        int size = coeff.length-1;

        //Compute last element
        answer[size] = sol[size]/coeff[size][size];

        for(int i = coeff.length - 1; i >= 0; i--)
        {
            double sum = sol[i];
            for(int j = i + 1; j <= coeff.length - 1; j = j + i + 1)
            {
                sum = sum - coeff[i][j] * answer[j];
            }
            answer[i] = sum/coeff[i][i];
        }

        return answer;
    }

    //BEGIN SCALED PARTIAL PIVOTING METHOD
    //Returns the index with the chosen pivot.
    public static int PartialPivot(double[][] coeff, double[] S, int iter, HashSet<Integer> unusedPivots){

        int size  = coeff.length;
        double[] R = new double[size];

        for(int i = 0; i<size; i++){
            R[i] = Math.abs(coeff[i][iter]) / S[i];
        }

        int maxIndex = 0;
        double max = 0;

        for(int i = 0; i<R.length; i++){
            double num = R[i];
            if (num > max && unusedPivots.contains(i)){
                max = num;
                maxIndex = i;
            }
        }

        //System.out.println("R: " + Arrays.toString(R));

        return maxIndex;
    }

    public static double[] ComputeS(double[][] coeff){
        int size = coeff.length;
        double[] S = new double[size];

        for(int i = 0; i<size; i++){
            double max = -1;

            for(int j = 0; j<size; j++){
                max = Math.max(max, Math.abs(coeff[i][j]));
            }
            S[i] = max;
        }

        return S;
    }

    public static double[] GaussianEliminationSPP(double[][] coeff, double[] sol){
        int[] order = new int[coeff.length];
        int size = coeff.length;
        HashSet<Integer> unusedPivots = new HashSet<>();

        //Initial order = 0, 1, .. n-1.
        for(int i = 0; i <order.length; i++){
            order[i] = i;
            unusedPivots.add(i);
        }

        double[] S = ComputeS(coeff);


        //Iterate size - 1 times...
        for(int i = 0; i<size -1; i++){
            int iter = size - i - 1;
            int pivot = PartialPivot(coeff, S, iter, unusedPivots);
            swap(i, pivot, order);

            unusedPivots.remove(pivot);

            for(Integer row : unusedPivots){
                double scale = -(coeff[row][i]) / coeff[pivot][i];

                for(int col = i; col<coeff[row].length; col++){
                    coeff[row][col] = coeff[row][col] + coeff[pivot][col] * scale;
                }

                sol[row] = sol[row] + sol[pivot] * scale;

            }

        }

        return BackSubstitutionSPP(coeff, sol, order);
    }

    public static double[] BackSubstitutionSPP(double[][] coeff, double[] sol, int[] order){
        double[] answer = new double[coeff.length];
        int last = order[order.length - 1];


        //Compute last element
        answer[answer.length - 1] = sol[last]/coeff[last][coeff[last].length - 1];

        //[2, 1, 0]
        for(int i = order.length - 2; i >= 0; i--){
            double sum = 0;
            int currentRow = order[i];

            for(int j = i + 1; j < coeff[i].length; j++){
                sum += coeff[currentRow][j] * answer[j];
            }

            answer[i] = (sol[currentRow] - sum)/ coeff[currentRow][i];
            //System.out.println(Arrays.toString(answer));
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

