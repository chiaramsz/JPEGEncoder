package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Component;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.SubSamplerI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;

public class SubSampler implements SubSamplerI {

    @Override
    public YUVImageI downSample(YUVImageI yuvImg, int samplingRatio) {
        if (samplingRatio == SubSampler.YUV_444) {
            return yuvImg;
        }

        int[][] cbComp = yuvImg.getComponent(YUVImageI.CB_COMP).getData();
        int[][] crComp = yuvImg.getComponent(YUVImageI.CR_COMP).getData();

        int width = cbComp[0].length;
        int height = cbComp.length;

        int[][] cbCompSub = new int[0][0];
        int[][] crCompSub = new int[0][0];

        if (samplingRatio == SubSampler.YUV_422) {
            cbCompSub = new int[height][width / 2];
            crCompSub = new int[height][width / 2];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x += 2) { //weil in den Spalten nur jeder zweite Pixe lgenommen wird
                    cbCompSub[y][x / 2] = (cbComp[y][x] + cbComp[y][x + 1]) / 2;
                    crCompSub[y][x / 2] = (crComp[y][x] + crComp[y][x + 1]) / 2;
                }
            }

        } else if (samplingRatio == SubSampler.YUV_420) {
            cbCompSub = new int[height / 2][width / 2];
            crCompSub = new int[height / 2][width / 2];

            for (int y = 0; y < height; y += 2) {
                for (int x = 0; x < width; x += 2) {
                    cbCompSub[y / 2][x / 2] = (cbComp[y][x] + cbComp[y][x + 1] + cbComp[y + 1][x] + cbComp[y + 1][x + 1]) / 4;
                    crCompSub[y / 2][x / 2] = (crComp[y][x] + crComp[y][x + 1] + crComp[y + 1][x] + crComp[y + 1][x + 1]) / 4;
                }
            }
        }

        return new YUVImage((Component) yuvImg.getComponent(yuvImg.Y_COMP),
                new Component(cbCompSub, YUVImageI.CB_COMP),
                new Component(crCompSub, YUVImageI.CR_COMP), samplingRatio);
    }


}

