package com.github.chen0040.matrix.solvers;

import com.github.chen0040.matrix.DoubleUtils;
import com.github.chen0040.matrix.Matrix;
import com.github.chen0040.matrix.Vector;


/**
 * Created by xschen on 10/11/2015 0011.
 */
public class BackwardSubstitution {
    /** 
     * @brief solve x such that R * x = c
     *
     * @param[Matrix] R a upper triangular matrix
     * @param[Vector] c
     */
    public static Vector solve(Matrix R, Vector c)
    {
        int n = R.getRowCount();
        Vector x = new Vector(n);
        for (int r = n - 1; r >= 0; --r)
        {
            if (!DoubleUtils.isZero(R.get(r, r)))
            {
                x.set(r, (c.get(r) - R.getRow(r).multiply(x)) / R.get(r, r));
            }
        }
        return x;
    }
}
