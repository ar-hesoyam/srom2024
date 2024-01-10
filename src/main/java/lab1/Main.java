package lab1;

import lab1.longarithmetic.*;

import java.math.BigInteger;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        BigInt16 x = new BigInt16(new HexString("764D613A939E8E98AAAED054BE8892D8"));
        BigInt16 y = new BigInt16(new HexString("764D613A939E8E9A2BDED054BE8892D8"));
        BigInt16 Mod = new BigInt16(new HexString("6B3611AC70AE0C8805A36049084BEA66"));
        BigInt16 mu = BigInt16.calculateMu(Mod);
        System.out.println(x.productMod(y, Mod, mu));
        System.out.println(x.longModPowerReduction(y, Mod));
        System.out.println(x.addMod(y, Mod));
        System.out.println(x.subMod(y, Mod));
        System.out.println(x.productMod(y, Mod, mu));
        System.out.println(x.lcm(y));
        System.out.println(x.gcd(y));
    }
}
