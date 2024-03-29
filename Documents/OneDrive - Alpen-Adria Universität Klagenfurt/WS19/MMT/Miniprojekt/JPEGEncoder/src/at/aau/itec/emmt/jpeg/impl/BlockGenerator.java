package at.aau.itec.emmt.jpeg.impl;

import at.aau.itec.emmt.jpeg.spec.BlockGeneratorI;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.ComponentI;
import at.aau.itec.emmt.jpeg.spec.MinimumCodedUnitI;
import at.aau.itec.emmt.jpeg.spec.RegionI;
import at.aau.itec.emmt.jpeg.spec.SubSamplerI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;

import java.util.Vector;


/**
 * Pattern implementation of interface BlockGeneratorI.
 *
 * @author Roland Tusch
 * @author Christian Timmerer
 * @version 1.0
 */

public class BlockGenerator implements BlockGeneratorI {

    public MinimumCodedUnitI[] generateMinimumCodedUnits(YUVImageI yuvImg) {
        ComponentI Y = yuvImg.getComponent(YUVImageI.Y_COMP);
        if ((yuvImg.getSamplingRatio() == SubSamplerI.YUV_444 &&
                Y.getSize().width % 8 == 0 && Y.getSize().height % 8 == 0) ||
                (yuvImg.getSamplingRatio() != SubSamplerI.YUV_444 &&
                        Y.getSize().width % 16 == 0 && Y.getSize().height % 16 == 0)) {
            // image has correct dimensions for MCU generation
            switch (yuvImg.getSamplingRatio()) {
                case SubSamplerI.YUV_444:
                    return generate444MCUs(yuvImg);
                case SubSamplerI.YUV_422:
                    return generate422MCUs(yuvImg);
                default:
                    return generate420MCUs(yuvImg);
            }
        }
        // illegal image dimensions
        return null;
    }

    /**
     * Note: MCU's with 4 Y_COMP, 4 CB_COMP and 4 CR_COMP are not allowed by the
     * JPEG specification. Therefore 1:1:1 MCU's are constructed.
     */
    protected MinimumCodedUnitI[] generate444MCUs(YUVImageI yuvImg) {
        ComponentI Y = yuvImg.getComponent(YUVImageI.Y_COMP);
        ComponentI Cb = yuvImg.getComponent(YUVImageI.CB_COMP);
        ComponentI Cr = yuvImg.getComponent(YUVImageI.CR_COMP);
        int[][] YData = Y.getData();
        int[][] CbData = Cb.getData();
        int[][] CrData = Cr.getData();
        Vector<MinimumCodedUnit> mcuVec = new Vector<MinimumCodedUnit>();
        // all components are of same size
        for (int y = 0; y < Y.getSize().height; y += 8) {
            for (int x = 0; x < Y.getSize().width; x += 8) {
                BlockI[] YBlocks = new Block[1];
                BlockI[] CbBlocks = new Block[1];
                BlockI[] CrBlocks = new Block[1];
                YBlocks[0] = new Block(getBlockData(YData, x, y));
                CbBlocks[0] = new Block(getBlockData(CbData, x, y));
                CrBlocks[0] = new Block(getBlockData(CrData, x, y));
                RegionI[] mcuRegions = new RegionI[3];
                mcuRegions[0] = new Region(YBlocks, YUVImageI.Y_COMP);
                mcuRegions[1] = new Region(CbBlocks, YUVImageI.CB_COMP);
                mcuRegions[2] = new Region(CrBlocks, YUVImageI.CR_COMP);
                mcuVec.addElement(new MinimumCodedUnit(mcuRegions));
            }
        }
        MinimumCodedUnitI[] mcuArr = new MinimumCodedUnitI[mcuVec.size()];
        mcuVec.toArray(mcuArr);
        return mcuArr;
    }

    /**
     * Returns a block of data (8 x 8) from the given component data matrix,
     * starting with point (startX,startY) as the block's top-left corner.
     *
     * @param compData the component data of a YUV image
     * @param startX   the x-coordinate of the top-left point of the block
     * @param startY   the y-coordinate of the top-left point of the block
     */
    protected int[][] getBlockData(int[][] compData, int startX, int startY) {
        int[][] blockData = new int[BlockI.N][BlockI.N];
        for (int i = 0; i < blockData.length; i++) {
            for (int j = 0; j < blockData[i].length; j++) {
                blockData[i][j] = compData[startY + i][startX + j];
            }
        }
        return blockData;
    }

    protected MinimumCodedUnitI[] generate422MCUs(YUVImageI yuvImg) {
        ComponentI Y = yuvImg.getComponent(YUVImageI.Y_COMP);
        ComponentI Cb = yuvImg.getComponent(YUVImageI.CB_COMP);
        ComponentI Cr = yuvImg.getComponent(YUVImageI.CR_COMP);
        int[][] YData = Y.getData();
        int[][] CbData = Cb.getData();
        int[][] CrData = Cr.getData();
        Vector<MinimumCodedUnit> mcuVec = new Vector<MinimumCodedUnit>();
        // chrominance components are only half of the width of the luminance component
        for (int y = 0; y < Y.getSize().height; y += 16) {
            for (int x = 0; x < Y.getSize().width; x += 16) {
                BlockI[] YBlocks = new Block[4];
                BlockI[] CbBlocks = new Block[2];
                BlockI[] CrBlocks = new Block[2];
                int index = 0;
                for (int i = 0; i < 2; i++) {       // 2 blocks vertical for each component
                    for (int j = 0; j < 2; j++) {   // 2 blocks horizontal for Y_COMP, 1 block for CB_COMP and CR_COMP
                        YBlocks[index] = new Block(getBlockData(YData, x + j * BlockI.N, y + i * BlockI.N));
                        if (j == 0) {           // get also CB_COMP and CR_COMP blocks
                            CbBlocks[index / 2] = new Block(getBlockData(CbData, x / 2 + j * BlockI.N, y + i * BlockI.N));
                            CrBlocks[index / 2] = new Block(getBlockData(CrData, x / 2 + j * BlockI.N, y + i * BlockI.N));
                        }
                        index++;
                    }
                }
                RegionI[] mcuRegions = new RegionI[3];
                mcuRegions[0] = new Region(YBlocks, YUVImageI.Y_COMP);
                mcuRegions[1] = new Region(CbBlocks, YUVImageI.CB_COMP);
                mcuRegions[2] = new Region(CrBlocks, YUVImageI.CR_COMP);
                mcuVec.addElement(new MinimumCodedUnit(mcuRegions));
            }
        }
        MinimumCodedUnitI[] mcuArr = new MinimumCodedUnitI[mcuVec.size()];
        mcuVec.toArray(mcuArr);
        return mcuArr;
    }

    protected MinimumCodedUnitI[] generate420MCUs(YUVImageI yuvImg) {
        ComponentI Y = yuvImg.getComponent(YUVImageI.Y_COMP);
        ComponentI Cb = yuvImg.getComponent(YUVImageI.CB_COMP);
        ComponentI Cr = yuvImg.getComponent(YUVImageI.CR_COMP);
        int[][] YData = Y.getData();
        int[][] CbData = Cb.getData();
        int[][] CrData = Cr.getData();
        Vector<MinimumCodedUnit> mcuVec = new Vector<MinimumCodedUnit>();
        // chrominance components are only half of the size (width and height)
        // of the luminance component
        for (int y = 0; y < Y.getSize().height; y += 16) {
            for (int x = 0; x < Y.getSize().width; x += 16) {
                BlockI[] YBlocks = new Block[4];
                BlockI[] CbBlocks = new Block[1];
                BlockI[] CrBlocks = new Block[1];
                int index = 0;
                for (int i = 0; i < 2; i++) {       // 2 blocks vertical for Y_COMP, 1 block for CB_COMP and CR_COMP
                    for (int j = 0; j < 2; j++) {   // 2 blocks horizontal for Y_COMP, 1 block for CB_COMP and CR_COMP
                        YBlocks[index] = new Block(getBlockData(YData, x + j * BlockI.N, y + i * BlockI.N));
                        if ((i == 0) && (j == 0)) { // get also CB_COMP and CR_COMP blocks
                            CbBlocks[index] = new Block(getBlockData(CbData, x / 2 + j * BlockI.N, y / 2 + i * BlockI.N));
                            CrBlocks[index] = new Block(getBlockData(CrData, x / 2 + j * BlockI.N, y / 2 + i * BlockI.N));
                        }
                        index++;
                    }
                }
                RegionI[] mcuRegions = new RegionI[3];
                mcuRegions[0] = new Region(YBlocks, YUVImageI.Y_COMP);
                mcuRegions[1] = new Region(CbBlocks, YUVImageI.CB_COMP);
                mcuRegions[2] = new Region(CrBlocks, YUVImageI.CR_COMP);
                mcuVec.addElement(new MinimumCodedUnit(mcuRegions));
            }
        }
        MinimumCodedUnitI[] mcuArr = new MinimumCodedUnitI[mcuVec.size()];
        mcuVec.toArray(mcuArr);
        return mcuArr;
    }
}