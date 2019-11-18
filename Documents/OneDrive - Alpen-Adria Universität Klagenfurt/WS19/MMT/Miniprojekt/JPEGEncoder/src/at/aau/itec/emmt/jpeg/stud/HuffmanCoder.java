package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.AbstractHuffmanCoder;
import at.aau.itec.emmt.jpeg.impl.RunLevel;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.EntropyCoderI;
import at.aau.itec.emmt.jpeg.spec.RunLevelI;

import java.util.ArrayList;
import java.util.List;

public class HuffmanCoder extends AbstractHuffmanCoder {

    //Lauflängenkoderung der AC Koeffizienten
    @Override
    public RunLevelI[] runLengthEncode(BlockI quantBlock) {
        int[][] quant = quantBlock.getData();
        int[] vector64 = new int[quant.length * quant.length];
        int[] scan = new int[vector64.length];
        List<RunLevel> runs = new ArrayList<RunLevel>();
        int index = 0;

        //Vektor mit Länge 64 erzeugen
        for (int i = 0; i < quant.length; i++) {
            for (int j = 0; j < quant[0].length; j++) {
                vector64[index] = quant[i][j];
                index++;
            }
        }

        for (int i = 0; i < vector64.length; i++) {
            scan[i] = vector64[ZIGZAG_ORDER[i]];
        }

        //Anzahl der aufeinanderfolgenden Nullen zählen
        int runLength=0;


        for(int i=1; i<scan.length;i++){
            if(scan[i] == 0){
                runLength++;

                //letztes Paar (0,0), um zu zeigen, dass nur Nullen folgen
                if(i==scan.length-1){
                    RunLevel lastRun = new RunLevel(0, 0);
                    runs.add(lastRun);
                }

            }else{ //nur Runs aus Nullen werden ersetzt
                RunLevel run = new RunLevel(runLength, scan[i]);
                runs.add(run);
                runLength=0; //wieder RunLength zurücksetzen
            }
        }

        return runs.toArray(new RunLevel[0]);

    }
}
