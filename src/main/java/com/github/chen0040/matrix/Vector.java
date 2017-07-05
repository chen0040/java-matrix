package com.github.chen0040.matrix;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class Vector implements Serializable, Cloneable {
    private HashMap<Integer, Double> data = new HashMap<Integer, Double>();
    private int dimension;
    private int id = -1;



    public Vector(int dimension){
        this.dimension = dimension;
    }

    public Vector(int dimension, Map<Integer, Double> data){
        this.dimension = dimension;

        for(Map.Entry<Integer, Double> entry : data.entrySet()){
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object clone(){
        Vector clone = new Vector(dimension);
        clone.copy(this);
        return clone;
    }

    public void copy(Vector rhs){
        dimension = rhs.dimension;
        id = rhs.id;

        data.clear();
        for(Map.Entry<Integer, Double> entry : rhs.data.entrySet()){
            data.put(entry.getKey(), entry.getValue());
        }
    }

    public void set(int i, double value){
        if(i >= dimension){
            throw new RuntimeException("dimension exceeded at index " + i + ", given dimension is only " + dimension);
        }

        if(DoubleUtils.isZero(value)){
            data.remove(i);
            return;
        }

        data.put(i, value);

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double get(int i){
        return data.getOrDefault(i, 0.0);
    }

    @Override
    public boolean equals(Object rhs){
        if(rhs != null && rhs instanceof Vector){
            Vector rhs2 = (Vector)rhs;
            if(dimension != rhs2.dimension){
                return false;
            }

            if(data.size() != rhs2.data.size()){
                return false;
            }

            for(Integer index : data.keySet()){
                if(!rhs2.data.containsKey(index)) return false;
                if(!DoubleUtils.equals(data.get(index), rhs2.data.get(index))){
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int getDimension(){
        return dimension;
    }


    public HashMap<Integer, Double> getData(){
        return data;
    }

    public Vector projectOrthogonal(Iterable<Vector> vlist) {
        Vector b = this;
        for(Vector v : vlist)
        {
            b = b.minus(b.projectAlong(v));
        }

        return b;
    }

    public Vector projectOrthogonal(List<Vector> vlist, Map<Integer, Double> alpha) {
        Vector b = this;
        for(int i = 0; i < vlist.size(); ++i)
        {
            Vector v = vlist.get(i);
            double norm_a = v.multiply(v);

            if (DoubleUtils.isZero(norm_a)) {
                return new Vector(dimension);
            }
            double sigma = b.multiply(v) / norm_a;
            Vector v_parallel = v.multiply(sigma);

            alpha.put(i, sigma);

            b = b.minus(v_parallel);
        }

        return b;
    }

    public Vector projectAlong(Vector rhs)
    {
        double norm_a = rhs.multiply(rhs);

        if (DoubleUtils.isZero(norm_a)) {
            return new Vector(dimension);
        }
        double sigma = multiply(rhs) / norm_a;
        return rhs.multiply(sigma);
    }

    public Vector multiply(double rhs){
        Vector clone = (Vector)this.clone();
        for(Integer i : data.keySet()){
            clone.data.put(i, rhs * data.get(i));
        }
        return clone;
    }

    public double multiply(Vector rhs)
    {
        double productSum = 0;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            productSum += entry.getValue() * rhs.get(entry.getKey());
        }

        return productSum;
    }

    public Vector pow(double scalar)
    {
        Vector result = new Vector(dimension);
        for (Map.Entry<Integer, Double> entry : data.entrySet())
        {
            result.data.put(entry.getKey(), Math.pow(entry.getValue(), scalar));
        }
        return result;
    }

    public Vector add(Vector rhs)
    {
        Vector result = new Vector(dimension);
        int index;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            index = entry.getKey();
            result.data.put(index, entry.getValue() + rhs.data.get(index));
        }
        for(Map.Entry<Integer, Double> entry : rhs.data.entrySet()){
            index = entry.getKey();
            if(result.data.containsKey(index)) continue;
            result.data.put(index, entry.getValue() + data.get(index));
        }

        return result;
    }

    public Vector minus(Vector rhs)
    {
        Vector result = new Vector(dimension);
        int index;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            index = entry.getKey();
            result.data.put(index, entry.getValue() - rhs.data.get(index));
        }
        for(Map.Entry<Integer, Double> entry : rhs.data.entrySet()){
            index = entry.getKey();
            if(result.data.containsKey(index)) continue;
            result.data.put(index, data.get(index) - entry.getValue());
        }

        return result;
    }

    public double sum(){
        double sum = 0;

        for(Map.Entry<Integer, Double> entry : data.entrySet()){
            sum += entry.getValue();
        }

        return sum;
    }

    public boolean isZero(){
        return DoubleUtils.isZero(sum());
    }

    public double norm(int level)
    {
        if (level == 1)
        {
            double sum = 0;
            for (Double val : data.values())
            {
                sum += Math.abs(val);
            }
            return sum;
        }
        else if (level == 2)
        {
            double sum = multiply(this);
            return Math.sqrt(sum);
        }
        else
        {
            double sum = 0;
            for (Double val : this.data.values())
            {
                sum += Math.pow(Math.abs(val), level);
            }
            return Math.pow(sum, 1.0 / level);
        }
    }

    public Vector normalize()
    {
        double norm = norm(2); // L2 norm is the cartesian distance
        if (DoubleUtils.isZero(norm))
        {
            return new Vector(dimension);
        }
        Vector clone = new Vector(dimension);

        for (Integer k : data.keySet())
        {
            clone.data.put(k, data.get(k) / norm);
        }
        return clone;
    }
}
