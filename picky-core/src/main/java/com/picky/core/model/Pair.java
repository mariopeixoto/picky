package com.picky.core.model;

public class Pair<T, K> {

    private T param1;
    private K param2;

    public Pair(T param1) {
        this.param1 = param1;
    }

    public void setParam1(T param1) {
        this.param1 = param1;
    }

    public void setParam2(K param2) {
        this.param2 = param2;
    }

    public T getParam1() {
        return this.param1;
    }

    public K getParam2() {
        return this.param2;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "param1=" + param1 +
                ", param2=" + param2 +
                '}';
    }
}
