package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Block;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.QuantizationI;

public class Quantizer implements QuantizationI {

    private int qualityFactor;

    public Quantizer(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

    @Override
    public int[] getQuantumLuminance() {
        return getScaledQuantum(QUANTUM_LUMINANCE, getScaleFactor());
    }

    private int[] getScaledQuantum(int[] sQi_table, int scaleFactor) {
        int[] output = new int[sQi_table.length];

        for(int i=0; i<sQi_table.length; i++) {
            output[i] = Math.min(255, Math.max(1, (sQi_table[i]* scaleFactor + 50)/100));
        }
        return output;
    }

    @Override
    public int[] getQuantumChrominance() {
        return getScaledQuantum(QUANTUM_CHROMINANCE, getScaleFactor());
    }

    @Override
    public BlockI quantizeBlock(DCTBlockI dctBlock, int compType) {
        int[][] output = new int[dctBlock.getData().length][dctBlock.getData()[0].length];
        int[] sQi = getYComp(compType);


        for (int i = 0; i < dctBlock.getData().length; i++) {
            for (int j = 0; j < dctBlock.getData()[i].length; j++) {
                output[i][j] = (int) Math.round(dctBlock.getData()[i][j] / sQi[i * dctBlock.getData().length + j]);
            }
        }
        return new Block(output);
    }

    @Override
    public void setQualityFactor(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }


    public int getScaleFactor() {
        if(qualityFactor < 50) {
            return 5000/qualityFactor;
        } else return 200 - 2 * qualityFactor;
    }

    public int[] getYComp(int ch) {
        if(ch == YUVImage.Y_COMP) {
            return getQuantumLuminance();
        }
        return getQuantumChrominance();
    }

}
