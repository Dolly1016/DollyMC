package jp.dreamingpig.dollyMC.utils;

public class MathHelper {
    public static int intDivide(int num, int divisor){
        if(num >= 0) return num / divisor;
        else return (num + 1) / divisor - 1;
    }

    public static int intSurplus(int num, int divisor){
        if(num >= 0) return num % divisor;
        else return divisor - ((- num - 1) % divisor) - 1;
    }
}
