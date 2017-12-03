import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.BitSet;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.ThreadLocalRandom;

public class EOPI2 {
        
    public static class Problem_6_25 {
        
        //Assume the board is n x m.  We go row by row.  Assume we have filled in correctly k rows.  For row k+1:
        //If there is a rook on row k+1, we fill it with 0's obviously.  If there is no rook on row k+1, we need to check
        //for each square on row k+1 whether there is a rook in a corresponding column above.  If there is a rook,
        //then all squares abovethe given square will be 0's (since the first k rows are already processed).  We can check the
        //square directly above.  If it is 0, then either there is a rook in the column, or there is a rook in row k (previous row).
        //To avoid checking the whole column, we can keep count of consequitive rooks in rows ending with row k.  If that count is c,
        //then instead of checking previous row, we can check row k-c.  The remaining case we need to handle is if all processed rows
        //have rooks on them, but none may be in the current column.
        
        //Oops.  Ok, that's a problem.
        //Another idea for the solution: find a row that has no rooks on it (if there is no such row, fill everything with 0's).
        //Find column that has no rooks on it (if there is no such column, fill everything with 0's).  Use these row and column
        //to map the rooks (by storing 0's in squares of that row if corresponding column has a rook, and by storing 0's in
        //corresponding squares of that column if corresponding row has a rook).  Then we can fill the rest of the matrix by using this map.
        //Note that there is no problem with intersection of our found row and column, since there is no rook in corresponding column and row,
        //so the intersection square will remain 1.
        //This solution will require several passes over the matrix: 1 to find the row, 1 to find the column, 1 to map out rooks using those
        //row and column and 1 to update the rest of the matrix according to the map, 4 passes in total.
        //Is there a a solution with fewer passes?
        
        //Let's first write out the solution with 4 passes:
        static void fillRookPos(int [][] b)
        {
            int nrRow = -1, nrCol = -1;
            
            //identify row with no rooks
            for(int i = 0; i < b.length; i++) {
                boolean hasRooks = false;
                for(int j = 0; j < b[0].length; j++)
                    if (b[i][j] == 0) {
                        hasRooks = true;
                        break;
                    }
                if (!hasRooks) {
                    nrRow = i;
                    break;
                }
            }
            
            //identify column with no rooks
            if (nrRow != -1)
                for(int i = 0; i < b[0].length; i++) {
                    boolean hasRooks = false;
                    for(int j = 0; j < b.length; j++)
                        if (b[j][i] == 0) {
                            hasRooks = true;
                            break;
                        }
                    if (!hasRooks) {
                        nrCol = i;
                        break;
                    }
                }
            
            //if either doesn't exist, fill with 0s and return
            if (nrRow == -1 || nrCol == -1) {
                for(int i = 0; i < b.length; i++)
                    for(int j = 0; j < b[0].length; j++)
                        b[i][j] = 0;
                return;
            }
            
            //map out the rooks using found rwRow and rwCol
            for(int i = 0; i < b.length; i++) {
                if (i == nrRow)
                    continue;
                for(int j = 0; j < b[0].length; j++)
                    if (j != nrCol && b[i][j] == 0) {
                        b[nrRow][j] = 0;
                        b[i][nrCol] = 0;
                    }
            }
            
            //fill out the rest of the matrix using the mapped information
            for(int i = 0; i < b.length; i++) {
                if (i == nrRow)
                    continue;
                for(int j = 0; j < b[0].length; j++) {
                    if (j == nrCol)
                        continue;
                    if (b[nrRow][j] == 0 || b[i][nrCol] == 0)
                        b[i][j] = 0;
                }
            }
                        
        }
        
        public static void test() throws Exception
        {
            int [][] b = EOPI.readInt2D();
            fillRookPos(b);
            EOPI.printInt2D(b);
        }
    }
    
    public static void main(String [] args) throws Exception
    {
        Problem_6_25.test();
    }
    
}
