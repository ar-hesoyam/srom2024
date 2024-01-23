package lab1.longarithmetic;
import java.util.Arrays;

public class BigInt16 {
    private final int w = 16;
    private final long b = 65535L;
    public long[] arr;
    public BigInt16(HexString hex) {
        // 2 symbols - 1 byte
        arr = new long[2048 / w + 1];
        String[] hexArray = hex.splitString(4);
        int i = hexArray.length - 1;
        for (String s : hexArray) {
            arr[i] = Long.parseLong(s, 16);
            --i;
        }
    }
    public BigInt16() {
        this.arr = new long[2048 / w + 1];
    }
    public BigInt16(BigInt16 b) {
        this.arr = Arrays.copyOf(b.arr, b.arr.length);
    }
    public BigInt16(long c) {
        // 0 <= c <= 2^16 - 1
        this.arr = new long[2048/ w + 1];
        this.arr[0] = c;
    }
    public BigInt16 add(BigInt16 b) {
        long carry = 0;
        int x = this.arr.length;
        BigInt16 c = new BigInt16();
        c.arr = new long[x];
        for (int i = 0; i <= x - 1; i++) {
            long tmp = this.arr[i] + b.arr[i] + carry;
            c.arr[i] = tmp & 65535L;
            carry = tmp >> w;
        }
        return c;
    }
    public int size() {
        int cZeros = 0;
        for (int i = 2048 / w ; i >= 0; i--) {
            if (this.arr[i] != 0) {
                break;
            }
            cZeros++;
        }
        return 2048 / w + 1 - cZeros;
    }
    public BigInt16 sub(BigInt16 b) {
        long borrow = 0;
        int x = this.arr.length;
        BigInt16 c = new BigInt16();
        for (int i = 0; i <= x - 1; i++) {
            long tmp = this.arr[i] - b.arr[i] - borrow;
            if (tmp >= 0 ) {
                c.arr[i] = tmp;
                borrow = 0;
            }
            else {
                c.arr[i] = 65536 + tmp;
                borrow = 1;
            }
        }
        return c;
    }
    public short longCmp(BigInt16 b) {
        int i = this.arr.length - 1;
        if (this.arr.length > b.arr.length) {
            BigInt16 newB = new BigInt16();
            newB.arr = new long[this.arr.length];
            for (int j = 0; j < b.arr.length; j++) {
                newB.arr[j] = b.arr[j];
            }
            b = newB;
        }
        while (this.arr[i] == b.arr[i]) {
            i -= 1;
            if (i == -1) {
                return 0;
            }
        }if (i == -1) {
            return 0;
        }
        else {
            if (this.arr[i] > b.arr[i]) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
    public BigInt16 longShiftDigitsToHigh(int n) {
        if (this.size() < n) {
            return new BigInt16();
        }
        BigInt16 c = new BigInt16();
        int i = n;
        for (int j = 0; j < this.size(); j++) {
            c.arr[i] = this.arr[j];
            i++;
        }
        return c;
    }
    public BigInt16 longMulOneDigit(long b) {
        long carry = 0;
        long tmp;
        BigInt16 c = new BigInt16();
        c.arr = new long[2048 / w + 1];
        for (int i = 0; i < 2048/ w + 1; i++) {
            tmp = this.arr[i] * b + carry;
            c.arr[i] = tmp & 65535L;
            carry = tmp >> w;
        }
        if (c.size() < 2048 / w + 1) {
            c.arr[c.size()] = carry;
        }
        return c;
    }
    public BigInt16 longMul(BigInt16 b) {
        BigInt16 c = new BigInt16();
        BigInt16 tmp ;
        for (int i = 0; i < 2048/w + 1; i++) {
            tmp = this.longMulOneDigit(b.arr[i]);
            c = c.add(tmp.longShiftDigitsToHigh(i));
        }
        return c;
    }
    public int bitLength() {
        if (this.size() == 0) {
            return 1;  // Для нуля битовая длина равна 1
        }
        int i = this.size() - 1;
        int bitLength = i * 16;
        long tmp = this.arr[i];
        while (tmp != 0) {
            tmp = tmp >> 1;
            bitLength++;
        }
        return bitLength;
    }
    public BigInt16 longShiftBitsToLow(int k) {
        BinaryString bs = new BinaryString(this);
        if (bs.binary.length() <= k) {
            return this;
        }
        bs.binary = bs.binary.substring(k);
        return bs.bsToBi16();
    }

    public BigInt16 longShiftBitsToHigh(int k) {
        if (k <= 0 || k > 128 * 16) {
            return this;
        }
        int x = this.arr.length;
        int numOfShifts = k / 16;
        int shift = k % 16;
        long carry = 0;
        BigInt16 C = new BigInt16();
        C.arr = new long[x];
        BigInt16 out = new BigInt16();
        out.arr = new long[x];
        if (shift != 0) {
            for (int i = 0; i < x; i++) {
                long tmp = (this.arr[i] << shift) + carry;
                C.arr[i] = tmp & 65535L;
                carry = this.arr[i] >> (16 - shift);
            }
            for (int j = numOfShifts; j < x; j++) {
                out.arr[j] = C.arr[j - numOfShifts];
            }
            return out;
        } else {
            for (int i = 0; i < x; i++) {
                C.arr[i] = this.arr[i];
            }
            for (int j = numOfShifts; j < x; j++) {
                out.arr[j] = C.arr[j - numOfShifts];
            }
            return out;
        }
    }
    public BigInt16 longDivMod(BigInt16 B, String s) {
        int k = B.bitLength();
        BigInt16 R = new BigInt16(this);
        BigInt16 Q = new BigInt16();
        Q.arr = new long[this.arr.length];
        while (R.longCmp(B) != -1) {
            int t = R.bitLength();
            BigInt16 C = B.longShiftBitsToHigh(t-k);
            if (R.longCmp(C) == -1) {
                t -= 1;
                C = B.longShiftBitsToHigh(t-k);
            }
            R = R.sub(C);
            BigInt16 a = new BigInt16(1);
            a = a.longShiftBitsToHigh(t-k);
            Q = Q.add(a);
        }
        if (s == "q") {
            return Q;
        }
        else {
            return R;
        }
    }
    public BigInt16 longDiv(BigInt16 b) {
        return this.longDivMod(b, "q");
    }
    public BigInt16 longMod(BigInt16 b) {
        return  this.longDivMod(b, "r");
    }
    public BigInt16 toSquare(){
        return this.longMul(this);
    }
    public BigInt16 longPower(BigInt16 B) {
        BigInt16 C = new BigInt16(1);
        BigInt16 A = new BigInt16(this);
        String Bb = new StringBuilder((new BinaryString(B).toString())).reverse().toString();
        for (int i = Bb.length() - 1; i >= 0; i--) {
            if (Bb.charAt(i) == '1') {
                C = A.longMul(C);
            }
            if (i > 0) {
                C = C.longMul(C);
            }
        }
        return C;
    }
    //lab2
    public BigInt16 killLastDigits(int k) {
        if (this.size() < k) {
            return new BigInt16();
        }
        BigInt16 C = new BigInt16();
        int j = 0;
        for (int i = k; i < this.size(); i++) {
            C.arr[j] = this.arr[i];
            j++;
        }
        return C;
    }
    public BigInt16 gcd(BigInt16 b) {
        BigInt16 zeroConst = new BigInt16();
        BigInt16 A = new BigInt16(this);
        BigInt16 B = new BigInt16(b);
        if (A.longCmp(zeroConst) == 0) {
            return B;
        }
        if (B.longCmp(zeroConst) == 0) {
            return A;
        }
        while (B.longCmp(zeroConst) != 0) {
            BigInt16 res = A.longMod(B);
            A = B;
            B = res;
        }
        return A;
    }
    public BigInt16 lcm(BigInt16 b) {
        return (this.longMul(b).longDiv(this.gcd(b)));
    }
    public BigInt16 barretReduction(BigInt16 n, BigInt16 mu) {
        int k = n.size();
        BigInt16 A = new BigInt16(this);
        if (A.longCmp(n) == -1) {
            return A;
        }
        BigInt16 q= A.killLastDigits(k-1);
        q = q.longMul(mu);
        q = q.killLastDigits(k+1);
        BigInt16 r = A.sub(q.longMul(n));
        while (r.longCmp(n) != - 1) {
            r = r.sub(n);
        }
        return r;
    }
    public static BigInt16 calculateMu(BigInt16 beta) {
        int k = beta.size();
        BigInt16 x = new BigInt16();
        x.arr = new long[2048 / 16 + 1];
        x.arr[0] = 1;
        x = x.longShiftBitsToHigh(32*k );
        return x.longDiv(beta);
    }
    public BigInt16 addMod(BigInt16 b, BigInt16 mod) {
        BigInt16 mu = calculateMu(mod);
        BigInt16 C = this.add(b);
        return C.barretReduction(mod, mu);
    }
    public BigInt16 subMod(BigInt16 b, BigInt16 mod) {
        BigInt16 mu = calculateMu(mod);
        b = b.barretReduction(mod, mu);
        BigInt16 c = this.sub(b);
        c = c.barretReduction(mod, mu);
        return c;
    }
    public BigInt16 productMod(BigInt16 b, BigInt16 mod, BigInt16 mu) {
        BigInt16 A = new BigInt16(this);
        BigInt16 B = new BigInt16(b);
        while (A.longCmp(mod) == 1) {
            A = A.sub(mod);
        }
        while (B.longCmp(mod) == 1) {
            B = B.sub(mod);
        }
        BigInt16 C = A.longMul(B);
        return C.barretReduction(mod, mu);
    }
    
    public BigInt16 longModPowerReduction(BigInt16 B, BigInt16 N, BigInt16 mu) {
        BigInt16 A = new BigInt16(this);
        A = A.barretReduction(N, mu);
        System.out.println(A);
        BigInt16 C = new BigInt16(1);
        String BinaryB = (new StringBuilder((new BinaryString(B).toString())).reverse()).toString();
        for (int i = BinaryB.length() - 1; i >= 0; i--) {
            System.out.println(C);
            if (BinaryB.charAt(i) == '1') {
                C = C.productMod(A, N , mu);
            }
            if (i > 0) {
                C = C.productMod(C, N, mu);
            }
        }
        return C;
    }
    @Override
    public String toString() {
        String hex = "";
        for (int i = this.size() - 1; i >= 0; --i) {
            String longHex = Long.toHexString(this.arr[i]);
            while (longHex.length() != 4) {
                longHex = "0" + longHex;
            }
            hex += longHex;
        }
        if (hex == "") {
            return "0000";
        }
        while (hex.charAt(0) == '0') {
            hex = hex.replaceFirst("0", "");
        }
        return hex;
    }
}
