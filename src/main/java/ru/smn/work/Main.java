package ru.smn.work;

public class Main {
    public static void main(String[] args) {
        Fraction fr = new Fraction(2, 3);
        Fractionable num1 = Util.cache(fr);
        num1.doubleValue();  //sout сработал
        num1.doubleValue();  //sout молчит
        num1.doubleValue();  //sout молчит
        num1.setNum(5);
        num1.doubleValue();  //sout сработал
        num1.doubleValue();  //sout молчит
    }
}