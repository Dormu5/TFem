package main.java;

import main.java.FEM.FileOperation.DataFromFile;
import main.java.FEM.Matrix.*;
import main.java.FEM.model.Grid;
import main.java.FEM.model.Node;
import main.java.FEM.model.UniversalElement;

public class Application {

    public static void main(String[] args) {
        int[] localId = {1, 2, 3, 4};
        double[][] globalMatrixH = new double[16][16];
        double[][] globalMatrixC = new double[16][16];
        double[][] globalMatrixHBC = new double[16][16];
        double[] globalVectorP = new double[16];

        GaussianElimination gaussianElimination = new GaussianElimination();
        DataFromFile dataFromFile = new DataFromFile();
        UniversalElement universalElement = new UniversalElement();
        Grid grid = new Grid();
        grid.generateNodes(dataFromFile);
        grid.showNodes(dataFromFile);
        AreaGenerator areaArray = new AreaGenerator();
        areaArray.areaStatusGenerator(grid, dataFromFile);
        double[] endTemp;

        for (double l = 50; l <= dataFromFile.getTau(); l=l+dataFromFile.getDtau()) {
            System.out.println("\n\n---------------------------------------------------------------------------------------------   After:    "+l + " [s]    ----------------------------------------------------------------------------------------------\n");
            for (int elementNumber = 0; elementNumber < 9; elementNumber++) {
                int[] globalId = new int[4];
                for (int i = 0; i < 4; i++) {
                    globalId[i] = grid.getElements().get(elementNumber).getId()[i];
                }

                MatrixH matrixH = new MatrixH(dataFromFile);
                matrixH.buildMatrixH(universalElement, grid, elementNumber);

                MatrixC matrixC = new MatrixC(dataFromFile, matrixH);
                matrixC.buildMatrixC(universalElement);

                MatrixHBC matrixHBC = new MatrixHBC();
                matrixHBC.buildMatrixHBC(dataFromFile, grid, elementNumber, areaArray.getListBoarderConditionForElement().get(elementNumber),
                        localId, globalId, globalVectorP);

                arrayToGlobal(localId, globalId, matrixC.matrixC, globalMatrixC);
                arrayToGlobal(localId, globalId, matrixH.matrixH, globalMatrixH);
                arrayToGlobal(localId, globalId, matrixHBC.getMatrixHBC(), globalMatrixHBC);

            }

            globalMatrixH = globalMatrixHCalculation(globalMatrixH, globalMatrixC, dataFromFile.getDtau(), globalMatrixHBC);
            globalVectorPOperation(globalVectorP, globalMatrixC, dataFromFile.getDtau(), grid);

            endTemp = gaussianElimination.gaussElimination(grid.getNodes().size(), globalMatrixH, globalVectorP);
            for (int i = 0; i < 16; i++) {
                grid.getNodes().get(i).setT(endTemp[i]);
                for (int j = 0; j < 16; j++) {
                    globalMatrixH[i][j] = 0;
                    globalMatrixC[i][j] = 0;
                    globalMatrixHBC[i][j] = 0;
                }
                globalVectorP[i] = 0;
            }
            grid.showNodes(dataFromFile);
        }


    }

    private static void arrayToGlobal(int[] localId, int[] globalId, double[][] localArray, double[][] globalArray) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                int globalX = globalId[localId[j] - 1];
                int globalY = globalId[localId[i] - 1];

                globalArray[globalX - 1][globalY - 1] += localArray[i][j];
            }
        }
    }



    private static void globalVectorPOperation(double[] globalVectorP, double[][] globalMatrixC, double dTau, Grid grid) {

        for (int i = 0; i < 16; i++) {
            Node node = grid.getNodes().get(i);
            for (int j = 0; j < 16; j++) {
                globalMatrixC[i][j] = globalMatrixC[i][j] / dTau;
                globalMatrixC[j][i] = globalMatrixC[j][i] * node.getT();

            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalVectorP[i] += globalMatrixC[i][j];
            }
        }
    }

    private static double[][] globalMatrixHCalculation(double[][] matrixH, double[][] matrixC, double dTau, double[][] matrixHBC2D) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                matrixH[i][j] += matrixC[i][j] / dTau + matrixHBC2D[i][j];
            }
        }
        return matrixH;
    }


}


