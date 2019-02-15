package main.java.FEM.Matrix;

import main.java.FEM.FileOperation.DataFromFile;
import main.java.FEM.model.UniversalElement;

import java.util.ArrayList;
import java.util.List;

public class MatrixC {

    private DataFromFile dataFromFile;
    private double[] detJ;

    private double[][] fukcjaKsztaltu;
    public double[][] matrixC;
    private List<double[][]> listOfPointIntegrals;

    public MatrixC(DataFromFile dataFromFile, MatrixH matrixH) {
        this.dataFromFile = dataFromFile;

        this.detJ = matrixH.getDetJ();
    }

    public void buildMatrixC(UniversalElement universalElement) {

        calculateFunkcjaKsztaltu(universalElement);
        calculateIntegralPoints();
        calculateMatrixC();

    }
    private void calculateFunkcjaKsztaltu(UniversalElement universalElement) {
        double[] ksiValues = universalElement.getKsiValueTable();
        double[] etaValues = universalElement.getEtaValueTable();
        double[][] tmpFunkcjaKsztaltu = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 - ksiValues[j]) * (1 - etaValues[j]);
                }
                if (i == 1) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 + ksiValues[j]) * (1 - etaValues[j]);
                }
                if (i == 2) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 + ksiValues[j]) * (1 + etaValues[j]);
                }
                if (i == 3) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 - ksiValues[j]) * (1 + etaValues[j]);
                }
            }
        }
        this.fukcjaKsztaltu = tmpFunkcjaKsztaltu;
    }

    private void calculateIntegralPoints() {
        listOfPointIntegrals = new ArrayList<>();
        for (int p = 0; p < 4; p++) {
            double[][] integralPoints = new double[4][4];
            double[] tmpFunkcjaKsztaltu = fukcjaKsztaltu[p];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    integralPoints[i][j] = dataFromFile.getSpecHeat() * dataFromFile.getDensity() * tmpFunkcjaKsztaltu[i] * tmpFunkcjaKsztaltu[j] * detJ[p];
                }
            }
            listOfPointIntegrals.add(integralPoints);
        }
    }

    private void calculateMatrixC() {
        matrixC = new double[4][4];
        for (double[][] partMatrix : listOfPointIntegrals) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixC[i][j] += partMatrix[i][j];
                }
            }
        }
    }


}
