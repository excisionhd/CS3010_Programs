import java.util.Arrays;
import java.util.HashSet;

public class Main {
    //Two classes:
    //1. Naive Gaussian Elimination
    //2. Scaled Partial Pivoting

    static float[][] coeff = { {3, 4, 3}, {1, 5, -1}, {6, 3, 7} };

    static float[] sol = { 10, 7, 15 };

    public static void main(String[] args) {
        //float[] ans = GE.NaiveGaussianElimination(coeff, sol);
        //GE.PrintMatrix(coeff, sol);
        //GE.PrintAnswer(ans);


        GE.NaiveGaussianEliminationSPP(coeff, sol);


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
    //TODO: Implement scaled partial pivoting method.

    //Returns the index with the chosen pivot.
    public static int PartialPivot(float[][] coeff, float[] sol, float[] S, int[] order, int iter){

        int size  = coeff.length;
        float[] R = new float[size];

        for(int i = 0; i<size; i++){
            R[i] = Math.abs(coeff[i][iter]) / S[i];
        }

        HashSet<Integer> toCheck = new HashSet<>();
        for(int i = iter; i< order.length;i++){
            toCheck.add(order[i]);
        }

        int maxIndex = 0;
        float max = 0;

        for(int i = 0; i<R.length; i++){
            float num = R[i];
            if (num > max && toCheck.contains(i)){
                max = num;
                maxIndex = i;
            }
        }

        System.out.println(Arrays.toString(R));

        return maxIndex;
    }

    public static float[] ComputeS(float[][] coeff, float[] sol){
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

    public static float[] NaiveGaussianEliminationSPP(float[][] coeff, float[] sol){
        int[] order = new int[coeff.length];
        int size = coeff.length;

        //Initial order = 0, 1, .. n-1.
        for(int i = 0; i <order.length; i++){
            order[i] = i;
        }

        float[] S = ComputeS(coeff, sol);
        //Iterate size - 1 times...
        for(int i = 0; i<size - 1; i++){
            int pivot = PartialPivot(coeff, sol, S,order, size - i - 1);
            System.out.println(pivot);
        }




        return null;
    }

    public static void swap(int i, int j, int[] order){
        int copy = order[i];
        order[i] = j;
        order[j] = copy;
    }
}

