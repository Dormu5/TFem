package main.java.FEM.Matrix;

import main.java.FEM.FileOperation.DataFromFile;
import main.java.FEM.model.Grid;

import java.util.ArrayList;
import java.util.List;

public class AreaGenerator {

    private List<int[]> listBoarderConditionForElement;

    public void areaStatusGenerator(Grid grid, DataFromFile fileData) {
        listBoarderConditionForElement=new ArrayList<>();
        for (int i = 0; i < fileData.getN_B() - 1; ++i)
            for (int j = 0; j < fileData.getN_H() - 1; ++j) {
                int[] areaStatus = new int[4];
                if (grid.getNodes().get(j + i * fileData.getN_H()).isBoarderCondition() && grid.getNodes().get(j + (i + 1) * fileData.getN_H()).isBoarderCondition()) {
                    areaStatus[0] = 1;
                }
                if (grid.getNodes().get(j + (i + 1) * fileData.getN_H()).isBoarderCondition() && grid.getNodes().get(j + (i + 1) * fileData.getN_H() + 1).isBoarderCondition()) {
                    areaStatus[1] = 1;
                }
                if (grid.getNodes().get(j + (i + 1) * fileData.getN_H() + 1).isBoarderCondition() && grid.getNodes().get(j + 1 + i * fileData.getN_H()).isBoarderCondition()) {
                    areaStatus[2] = 1;
                }
                if (grid.getNodes().get(j + 1 + i * fileData.getN_H()).isBoarderCondition() && grid.getNodes().get(j + i * fileData.getN_H()).isBoarderCondition()) {
                    areaStatus[3] = 1;
                }

                listBoarderConditionForElement.add(areaStatus);

            }
    }

    public List<int[]> getListBoarderConditionForElement() {
        return listBoarderConditionForElement;
    }
}