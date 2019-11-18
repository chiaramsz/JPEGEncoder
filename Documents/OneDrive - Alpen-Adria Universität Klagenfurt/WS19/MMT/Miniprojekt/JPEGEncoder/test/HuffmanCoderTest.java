import at.aau.itec.emmt.jpeg.impl.Block;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.RunLevelI;
import at.aau.itec.emmt.jpeg.stud.HuffmanCoder;
import org.junit.Assert;
import org.junit.Test;

public class HuffmanCoderTest {

    @Test
    public void runLengthEncode() {

        // ---------- Test 1 ----------
        // Matrix from exercise sheet
        int[][] matrix = new int[][] {
                new int[]{32, 6, -1, 0, 0, 0, 0, 0},
                new int[]{-1, 0, 0, 0, 0, 0, 0, 0},
                new int[]{-1, 0, 1, 0, 0, 0, 0, 0},
                new int[]{-1, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0}
        };
        BlockI block = new Block(matrix);

        RunLevelI[] runLevels = new HuffmanCoder().runLengthEncode(block);
        Assert.assertEquals(runLevels.length, 7);

        Assert.assertEquals(runLevels[0].getRun(), 0);
        Assert.assertEquals(runLevels[0].getLevel(), 6);

        Assert.assertEquals(runLevels[1].getRun(), 0);
        Assert.assertEquals(runLevels[1].getLevel(), -1);

        Assert.assertEquals(runLevels[2].getRun(), 0);
        Assert.assertEquals(runLevels[2].getLevel(), -1);

        Assert.assertEquals(runLevels[3].getRun(), 1);
        Assert.assertEquals(runLevels[3].getLevel(), -1);

        Assert.assertEquals(runLevels[4].getRun(), 3);
        Assert.assertEquals(runLevels[4].getLevel(), -1);

        Assert.assertEquals(runLevels[5].getRun(), 2);
        Assert.assertEquals(runLevels[5].getLevel(), 1);

        Assert.assertEquals(runLevels[6].getRun(), 0);
        Assert.assertEquals(runLevels[6].getLevel(), 0);

        // ---------- Test 2 ----------
        // Matrix with non-zero at the end
        matrix = new int[][] {
                new int[]{32, 6, -1, 0, 0, 0, 0, 0},
                new int[]{-1, 0, 0, 0, 0, 0, 0, 0},
                new int[]{-1, 0, 1, 0, 0, 0, 0, 0},
                new int[]{-1, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 5}
        };
        block = new Block(matrix);

        runLevels = new HuffmanCoder().runLengthEncode(block);
        Assert.assertEquals(runLevels.length, 7);

        Assert.assertEquals(runLevels[0].getRun(), 0);
        Assert.assertEquals(runLevels[0].getLevel(), 6);

        Assert.assertEquals(runLevels[1].getRun(), 0);
        Assert.assertEquals(runLevels[1].getLevel(), -1);

        Assert.assertEquals(runLevels[2].getRun(), 0);
        Assert.assertEquals(runLevels[2].getLevel(), -1);

        Assert.assertEquals(runLevels[3].getRun(), 1);
        Assert.assertEquals(runLevels[3].getLevel(), -1);

        Assert.assertEquals(runLevels[4].getRun(), 3);
        Assert.assertEquals(runLevels[4].getLevel(), -1);

        Assert.assertEquals(runLevels[5].getRun(), 2);
        Assert.assertEquals(runLevels[5].getLevel(), 1);

        Assert.assertEquals(runLevels[6].getRun(), 50);
        Assert.assertEquals(runLevels[6].getLevel(), 5);

        // ---------- Test 3 ----------
        // Matrix with only zero
        matrix = new int[][] {
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 0, 0, 0, 0, 0, 0, 0}
        };
        block = new Block(matrix);

        runLevels = new HuffmanCoder().runLengthEncode(block);
        Assert.assertEquals(runLevels.length, 1);

        Assert.assertEquals(runLevels[0].getRun(), 0);
        Assert.assertEquals(runLevels[0].getLevel(), 0);

    }
}