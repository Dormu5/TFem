package main.java.FEM.Matrix;

import main.java.FEM.model.Point;

class VectorP {

    double[] buildVectorP(Point point, double detJ, double ambT, double alfa) {

        detJ=detJ/2.0;

        double[] vectorP1 = new double[4];
        vectorP1[0] = alfa * ambT * 0.25 * (1 - point.getX()) * (1 - point.getY()) * detJ;
        vectorP1[1] = alfa * ambT * 0.25 * (1 + point.getX()) * (1 - point.getY()) * detJ;
        vectorP1[2] = alfa * ambT * 0.25 * (1 + point.getX()) * (1 + point.getY()) * detJ;
        vectorP1[3] = alfa * ambT * 0.25 * (1 - point.getX()) * (1 + point.getY()) * detJ;
        return vectorP1;

    }
}



