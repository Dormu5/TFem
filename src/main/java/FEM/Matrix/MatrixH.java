package main.java.FEM.Matrix;


import main.java.FEM.FileOperation.DataFromFile;
import main.java.FEM.model.Grid;
import main.java.FEM.model.Node;
import main.java.FEM.model.UniversalElement;

import java.util.ArrayList;
import java.util.List;

public class MatrixH {
    private DataFromFile dataFromFile;
    private double[][] jacobian;
    private double[] detJ;
    private double[][] jacobian2D;
    private double[][] dNdX;
    private double[][] dNdY;

    private List<double[][]> dNdX2DMatrix;
    private List<double[][]> dNdY2DMatrix;
    private List<double[][]> listOf_dNdX2_and_DetJ;
    private List<double[][]> listOf_dNdY2_and_DetJ;
    private List<double[][]> partsOf_H;
    public double[][] matrixH;

    public MatrixH(DataFromFile dataFromFile) {
        this.dataFromFile = dataFromFile;
    }

    public void buildMatrixH(UniversalElement universalElement, Grid grid, int number) {
        buildJacobean(universalElement, grid, number);
        buildDetJ();
        inverseJacobean();
        calculate_dN_dX(universalElement);
        calculate_dN_dY(universalElement);
        calculate_dNdX2DMatrix_and_dNdY2DMatrix_T();
        calculate_dNdX2DMatrix__dNdY2DMatrix_T_AND_detJ();
        calculate_K_and_Matrixs();
        calculate_Matrix_H();

    }

    private void buildJacobean(UniversalElement universalElement, Grid grid, int number) {
        Integer[] tabId = grid.getElements().get(number).getId();
        Node[] nodes = {grid.getNodeByID(tabId[0]-1), grid.getNodeByID(tabId[1]-1), grid.getNodeByID(tabId[2]-1), grid.getNodeByID(tabId[3]-1)};
        double[] J_1_1 = new double[4];
        double[] J_1_2 = new double[4];
        double[] J_2_1 = new double[4];
        double[] J_2_2 = new double[4];
        double[][] fullOfJacobian = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                J_1_1[i] += universalElement.getdNdKsiValuesByID(i, j) * nodes[j].getX();
                J_1_2[i] += universalElement.getdNdKsiValuesByID(i, j) * nodes[j].getY();
                J_2_1[i] += universalElement.getdNdEtaValuesByID(i, j) * nodes[j].getX();
                J_2_2[i] += universalElement.getdNdEtaValuesByID(i, j) * nodes[j].getY();
            }
        }
        fullOfJacobian[0] = J_1_1;
        fullOfJacobian[1] = J_1_2;
        fullOfJacobian[2] = J_2_1;
        fullOfJacobian[3] = J_2_2;
        this.jacobian = fullOfJacobian;
    }

    private void buildDetJ() {
        double[] detJ = new double[4];

        for (int i = 0; i < 4; i++) {
            detJ[i] = jacobian[0][i] * jacobian[3][i] - jacobian[1][i] * jacobian[2][i];
        }
        this.detJ = detJ;
    }

    private void inverseJacobean() {
        double[] J_1_1_1 = new double[4];
        double[] J_1_1_2 = new double[4];
        double[] J_1_2_1 = new double[4];
        double[] J_1_2_2 = new double[4];
        double[][] jacobian2D = new double[4][4];
        for (int i = 0; i < 4; i++) {
            J_1_1_1[i] = jacobian[3][i] / detJ[i];
            J_1_2_1[i] = jacobian[2][i] / detJ[i];
            J_1_1_2[i] = jacobian[1][i] / detJ[i];
            J_1_2_2[i] = jacobian[0][i] / detJ[i];
        }
        jacobian2D[0] = J_1_1_1;
        jacobian2D[1] = J_1_1_2;
        jacobian2D[2] = J_1_2_1;
        jacobian2D[3] = J_1_2_2;
        this.jacobian2D = jacobian2D;
    }

    private void calculate_dN_dX(UniversalElement universalElement) {
        double[][] tmpdNdX = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmpdNdX[i][j] = jacobian2D[0][0] * universalElement.getdNdKsiValuesByID(i, j)
                        + jacobian2D[1][1] * universalElement.getdNdEtaValuesByID(i, j);
            }
        }
        this.dNdX = tmpdNdX;
    }

    private void calculate_dN_dY(UniversalElement universalElement) {
        double[][] tmpdNdY = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmpdNdY[i][j] = jacobian2D[2][2] * universalElement.getdNdKsiValuesByID(i, j)
                        + jacobian2D[3][3] * universalElement.getdNdEtaValuesByID(i, j);
            }
        }
        this.dNdY = tmpdNdY;


    }

    private void calculate_dNdX2DMatrix_and_dNdY2DMatrix_T() {
        dNdX2DMatrix = new ArrayList<>();
        dNdY2DMatrix = new ArrayList<>();
        double[] tmpFor2X;
        double[] tmpFor2Y;
        for (int p = 0; p < 4; p++) {
            tmpFor2X = dNdX[p];
            tmpFor2Y = dNdY[p];
            double[][] tmpdNdX2 = new double[4][4];
            double[][] tmpdNdY2 = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tmpdNdX2[i][j] = tmpFor2X[i] * tmpFor2X[j];
                    tmpdNdY2[i][j] = tmpFor2Y[i] * tmpFor2Y[j];
                }
            }
            dNdX2DMatrix.add(tmpdNdX2);
            dNdY2DMatrix.add(tmpdNdY2);
        }
    }

    private void calculate_dNdX2DMatrix__dNdY2DMatrix_T_AND_detJ() {
        double[][] dNdX2D;
        double[][] dNdY2D;
        listOf_dNdX2_and_DetJ = new ArrayList<>();
        listOf_dNdY2_and_DetJ = new ArrayList<>();
        for (int p = 0; p < 4; p++) {
            double[][] tmp_dNdX2_and_DetJ = new double[4][4];
            double[][] tmp_dNdY2_and_DetJ = new double[4][4];
            dNdX2D = dNdX2DMatrix.get(p);
            dNdY2D = dNdY2DMatrix.get(p);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tmp_dNdX2_and_DetJ[i][j] = dNdX2D[i][j] * detJ[i];
                    tmp_dNdY2_and_DetJ[i][j] = dNdY2D[i][j] * detJ[i];
                }
            }
            listOf_dNdX2_and_DetJ.add(tmp_dNdX2_and_DetJ);
            listOf_dNdY2_and_DetJ.add(tmp_dNdY2_and_DetJ);
        }

    }

    private void calculate_K_and_Matrixs() {
        partsOf_H = new ArrayList<>();
        for (int p = 0; p < 4; p++) {
            double[][] partMatrix = new double[4][4];
            double[][] tmpX2D = listOf_dNdX2_and_DetJ.get(p);
            double[][] tmpY2D = listOf_dNdY2_and_DetJ.get(p);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    partMatrix[i][j] = dataFromFile.getConductivity() * (tmpX2D[i][j] + tmpY2D[i][j]);
                }
            }
            partsOf_H.add(partMatrix);
        }
    }

    private void calculate_Matrix_H() {
        matrixH = new double[4][4];
        for (double[][] partmatrix : partsOf_H) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixH[i][j] += partmatrix[i][j];
                }
            }
        }

    }


    public double[] getDetJ() {
        return detJ;
    }


}
