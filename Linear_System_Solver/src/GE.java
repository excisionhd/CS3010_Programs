public class GE {

    public float[][] NaiveGaussianElimination(float[][] coeff, float[] sol){

        //TODO: Complete written assignment
        //TODO: Determine if Naive Gaussian Includes handling 0's in first row.
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

                //TODO: Compute solution vector with separate scale
            }
        }

        //TODO: Implement backward substitution.


        return null;
    }

    //TODO: Implement scaled partial pivoting method.

    //Used for partial pivoting
    public int getGreatestRow(float[][] coeff){

        int size = coeff.length;
        int maxRow = 0;
        float maxElement = 0;

        for (int i = 0; i < size; i++ ){
            if (coeff[i][0] > maxElement){
                maxElement = coeff[i][0];
                maxRow = i;
            }
        }

        return maxRow;
    }

}
