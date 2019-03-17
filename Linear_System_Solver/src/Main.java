import java.util.Arrays;
import java.util.HashSet;

public class Main {
    //Two classes:
    //1. Naive Gaussian Elimination
    //2. Scaled Partial Pivoting

    static float[][] coeff = { {3, 4, 3}, {1, 5, -1}, {6, 3, 7} };
    static float[][] coeff2 = { {3, 4, 3}, {1, 5, -1}, {6, 3, 7} };
    static float[] sol = { 10, 7, 15 };
    static float[] sol2 = { 10, 7, 15 };

    static double[][] coeff3 = { {0.0001, -5.0300, 5.8090, 7.8320}, {2.2660, 1.9950, 1.2120, 8.0080}, {8.8500, 5.6810, 4.5520, 1.3020}, {6.7750, -2.2530, 2.9080, 3.9700} };
    static double[][] coeff4 = { {0.0001, -5.0300, 5.8090, 7.8320}, {2.2660, 1.9950, 1.2120, 8.0080}, {8.8500, 5.6810, 4.5520, 1.3020}, {6.7750, -2.2530, 2.9080, 3.9700} };








    public static void main(String[] args) {
        float[] ans = GE.NaiveGaussianElimination(coeff, sol);
        GE.PrintMatrix(coeff, sol);
        GE.PrintAnswer(ans);


        float[] ans2 = GE.GaussianEliminationSPP(coeff2, sol2);
        GE.PrintMatrix(coeff2, sol2);
        GE.PrintAnswer(ans2);


    }
}

class GE{

    public static void PrintMatrix(float[][] matrix, float[] sol){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j<matrix[i].length; j++){
                System.out.printf(String.format("%.2f\t", matrix[i][j]));
            }
            System.out.println(sol[i]);
        }
    }

    public static void PrintAnswer(float[] ans){
        for(int i = 0; i<ans.length; i++){
            System.out.printf("X%d: %.2f\n", i+1, ans[i] + 0.0);
        }
    }

    public static float[] NaiveGaussianElimination(float[][] coeff, float[] sol){

        int size = coeff.length;

        //Loop for each row
        for(int i = 0 ; i < size; i++){
            for (int k = i + 1; k < size; k++){
                float scale = -(coeff[k][i])/(coeff[i][i]);

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

    public static float[] BackSubstitution(float[][] coeff, float[] sol){
        float[] answer = new float[coeff.length];
        int size = coeff.length-1;

        //Compute last element
        answer[size] = sol[size]/coeff[size][size];

        for(int i = coeff.length - 1; i >= 0; i--)
        {
            float sum = sol[i];
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
    public static int PartialPivot(float[][] coeff, float[] S, int iter, HashSet<Integer> unusedPivots){

        int size  = coeff.length;
        float[] R = new float[size];

        for(int i = 0; i<size; i++){
            R[i] = Math.abs(coeff[i][iter]) / S[i];
        }

        int maxIndex = 0;
        float max = 0;

        for(int i = 0; i<R.length; i++){
            float num = R[i];
            if (num > max && unusedPivots.contains(i)){
                max = num;
                maxIndex = i;
            }
        }

        //System.out.println("R: " + Arrays.toString(R));

        return maxIndex;
    }

    public static float[] ComputeS(float[][] coeff){
        int size = coeff.length;
        float[] S = new float[size];

        for(int i = 0; i<size; i++){
            float max = -1;

            for(int j = 0; j<size; j++){
                max = Math.max(max, Math.abs(coeff[i][j]));
            }
            S[i] = max;
        }

        return S;
    }

    public static float[] GaussianEliminationSPP(float[][] coeff, float[] sol){
        int[] order = new int[coeff.length];
        int size = coeff.length;
        HashSet<Integer> unusedPivots = new HashSet<>();

        //Initial order = 0, 1, .. n-1.
        for(int i = 0; i <order.length; i++){
            order[i] = i;
            unusedPivots.add(i);
        }

        float[] S = ComputeS(coeff);


        //Iterate size - 1 times...
        for(int i = 0; i<size -1; i++){
            int iter = size - i - 1;
            int pivot = PartialPivot(coeff, S, iter, unusedPivots);
            swap(i, pivot, order);

            unusedPivots.remove(pivot);

            for(Integer row : unusedPivots){
                float scale = -(coeff[row][i]) / coeff[pivot][i];

                for(int col = i; col<coeff[row].length; col++){
                    coeff[row][col] = coeff[row][col] + coeff[pivot][col] * scale;
                }

                sol[row] = sol[row] + sol[pivot] * scale;

            }

        }

        return BackSubstitutionSPP(coeff, sol, order);
    }

    public static float[] BackSubstitutionSPP(float[][] coeff, float[] sol, int[] order){
        float[] answer = new float[coeff.length];
        int last = order[order.length - 1];

        //Compute last element
        answer[last] = sol[last]/coeff[last][coeff[last].length - 1];

        //[2, 1, 0]
        for(int i = order.length - 2; i >= 0; i--){
            float sum = 0;
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

