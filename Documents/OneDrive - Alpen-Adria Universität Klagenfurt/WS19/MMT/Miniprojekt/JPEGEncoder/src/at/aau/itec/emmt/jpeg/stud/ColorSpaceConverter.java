package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.ColorSpaceConverterI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;
import at.aau.itec.emmt.jpeg.impl.Component;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

public class ColorSpaceConverter implements ColorSpaceConverterI {

    @Override
    public YUVImageI convertRGBToYUV(Image rgbImg) {
        int currentPixel;
        int y, cb, cr, red, blue, green;
        ImageObserver o = null; //um Bild zu verändern, bevor es erzeugt wird

        int[] pixels = new int[rgbImg.getWidth(o) * rgbImg.getHeight(o)];

        PixelGrabber pGrabber = new PixelGrabber(rgbImg, 0, 0, rgbImg.getWidth(o), rgbImg.getHeight(o), pixels, 0, rgbImg.getWidth(o));

        ColorModel cm = pGrabber.getColorModel();

        int[][] yPixel = new int[rgbImg.getWidth(o)][rgbImg.getHeight(o)];
        int[][] crPixel = new int[rgbImg.getWidth(o)][rgbImg.getHeight(o)];
        int[][] cbPixel = new int[rgbImg.getWidth(o)][rgbImg.getHeight(o)];
        // Pixel aus dem Bild in ein int Array speichern.

        try {

            pGrabber.grabPixels();

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        for (int i = 0; i < rgbImg.getHeight(o); i++) {
            for (int j = 0; j < rgbImg.getWidth(o); j++) {

                currentPixel = pixels[i*rgbImg.getWidth(o)+j];

                red = cm.getRed(currentPixel);
                green = cm.getGreen(currentPixel);
                blue = cm.getBlue(currentPixel);

                y = (int) ((0.299 * red) + (0.587 * green) + (0.114 * blue));
                cb = (int) (128 - (0.1687 * red) - (0.3313 * green) + (0.5 * blue));
                cr = (int) (128 + (0.5 * red) - (0.4187 * green) - (0.0813 * blue));

                yPixel[i][j] = y;
                cbPixel[i][j] = cb;
                crPixel[i][j] = cr;
            }
        }

        Component Y = new Component(yPixel, 0);
        Component Cb = new Component(cbPixel, 1);
        Component Cr = new Component(crPixel, 2);

        return new YUVImage(Y, Cb, Cr, 0);

    }
}
