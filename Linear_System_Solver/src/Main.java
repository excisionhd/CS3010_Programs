public class Main {
    //Two classes:
    //1. Naive Gaussian Elimination
    //2. Scaled Partial Pivoting

    static float[][] coeff = { {3, 4, 3}, {1, 5, -1}, {6, 3, 7} };

    static float[] sol = { 10, 7, 15 };

    public static void main(String[] args) {
        GE ge = new GE();

        ge.NaiveGaussianElimination(coeff, sol);

        printMatrix(coeff, sol);
    }

    public static void printMatrix(float[][] matrix, float[] sol){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j<matrix[i].length; j++){
                System.out.printf(String.format("%.2f\t", matrix[i][j]));
            }
            System.out.println(sol[i]);
        }
    }
}
