
package com.teotigraphix.caustk.core.midi;

import java.util.ArrayList;
import java.util.List;

public final class ScaleMatrixUtils {

    public static List<Integer> getIntervals(ScaleReference scaleReference, int rootPitch,
            int numColumns, int numRows) {
        List<Integer> result = new ArrayList<Integer>();
        List<Integer> matrix = createScale(scaleReference, numColumns, numRows, 0);
        for (int i = 0; i < numRows; i++) {
            result.add(rootPitch + matrix.get(i));
        }
        return result;
    }

    public static List<Integer> getNotes(ScaleReference scaleReference, int rootPitch,
            int numColumns, int numRows) {
        List<Integer> result = new ArrayList<Integer>();
        List<Integer> matrix = createScale(scaleReference, numColumns, numRows, 0);
        for (int i = 0; i < numRows; i++) {
            result.add(matrix.get(i));
        }
        return result;
    }

    static List<Integer> createScale(ScaleReference scaleReference, int numColumns, int numRows,
            int shift) {
        int[] intervales = scaleReference.getIntervals();

        int len = intervales.length;
        List<Integer> matrix = new ArrayList<Integer>();
        List<Integer> chromatic = new ArrayList<Integer>();

        boolean isUp = true;//this.orientation == Scales.ORIENT_UP;
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                int y = isUp ? row : column;
                int x = isUp ? column : row;
                int offset = y * shift + x;
                matrix.add((int)((Math.floor(offset / len)) * 12 + intervales[offset % len]));
                chromatic.add(y * (shift == numRows ? numRows : intervales[shift % len]) + x);
            }
        }
        return matrix;
    }
}

/*

Scales.prototype.createScale = function (scale)
{
    var len = scale.notes.length;
    var matrix = [];
    var chromatic = [];
    var isUp = this.orientation == Scales.ORIENT_UP;
    for (var row = 0; row < this.numRows; row++)
    {
        for (var column = 0; column < this.numColumns; column++)
        {
            var y = isUp ? row : column;
            var x = isUp ? column : row;
            var offset = y * this.shift + x;
            matrix.push ((Math.floor (offset / len)) * 12 + scale.notes[offset % len]);
            chromatic.push (y * (this.shift == this.numRows ? this.numRows : scale.notes[this.shift % len]) + x);
        }
    }
    return { name: scale.name, matrix: matrix, chromatic: chromatic };
};

*/
