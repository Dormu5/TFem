package main.java.FEM.Matrix;

public class GaussianElimination {

    public double[] gaussElimination(int nodesCount, double[][] globalMatrixH, double[] globalVectorP) {

        double m, s, e;
        e = Math.pow(10, -12);
        double[] tabResult = new double[nodesCount];

        double[][] tabAB = new double[nodesCount][nodesCount + 1];
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                tabAB[j][i] = globalMatrixH[j][i];
            }
        }

        for (int i = 0; i < nodesCount; i++) {
            tabAB[i][nodesCount] = globalVectorP[i];
        }

        for (int i = 0; i < nodesCount - 1; i++) {
            for (int j = i + 1; j < nodesCount; j++) {
                if (Math.abs(tabAB[i][i]) < e) {
                    System.err.println("dzielnik rowny 0");
                    break;
                }

                m = -tabAB[j][i] / tabAB[i][i];
                for (int k = 0; k < nodesCount + 1; k++) {
                    tabAB[j][k] += m * tabAB[i][k];
                }
            }
        }

        for (int i = nodesCount - 1; i >= 0; i--) {
            s = tabAB[i][nodesCount];
            for (int j = nodesCount - 1; j >= 0; j--) {
                s -= tabAB[i][j] * tabResult[j];
            }
            if (Math.abs(tabAB[i][i]) < e) {
                System.err.println("dzielnik rowny 0");
                break;
            }
            tabResult[i] = s / tabAB[i][i];
        }
        return tabResult;
    }
}