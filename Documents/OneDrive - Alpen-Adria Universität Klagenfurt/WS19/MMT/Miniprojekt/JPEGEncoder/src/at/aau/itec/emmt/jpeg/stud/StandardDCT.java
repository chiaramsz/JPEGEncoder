package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.DCTBlock;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.DCTI;

public class StandardDCT implements DCTI {

    @Override
    public DCTBlockI forward(BlockI b) {
        double[][] frequenz_matrix = new double[b.getData().length][b.getData()[0].length];

        for (int v = 0; v < frequenz_matrix.length; v++) {
            for (int u = 0; u < frequenz_matrix[v].length; u++) {
                double sum = 0;

                for (int j = 0; j < b.getData().length; j++) {
                    for (int i = 0; i < b.getData()[j].length; i++) {
                        sum += Math.cos(((2 * i + 1) * u * Math.PI) / 16) * Math.cos(((2 * j + 1) * v * Math.PI) / 16) * (b.getData()[j][i] - 128);
                    }
                }
                double temp = getC(u) * getC(v) / 4;
                frequenz_matrix[v][u] = sum * temp ;
            }
        }

        return new DCTBlock(frequenz_matrix);
    }

    public double getC(int i) {
        if (i == 0) {
            return 1d / Math.sqrt(2);
        }
        return 1;
    }

}
