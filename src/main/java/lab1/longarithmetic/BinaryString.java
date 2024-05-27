package lab1.longarithmetic;

public class BinaryString {
    public String binary;
    public BinaryString(String binary) {
        this.binary = binary;
    }
    public BinaryString(BigInt16 bi16) {
        String binStr = "";
        String bin;
        for (int i = 0; i <= bi16.size() - 1; i++) {
            bin = "";
            bin += Long.toBinaryString(bi16.arr[i]);
            while ((bin.length() % 16) != 0) {
                bin = "0" + bin;
            }
            binStr = bin + binStr;
        }
        this.binary = binStr;
    }
    private String[] splitString(String s, int chunkSize) {
        int subarrayCount = ( s.length() + chunkSize - 1) / chunkSize;
        String[] subarrays = new String[subarrayCount];
        for (int i = subarrayCount - 1; i >= 0; --i) {
            int startIndex = i * chunkSize;
            int endIndex = Math.min(startIndex + chunkSize,  s.length());
            subarrays[i] = s.substring(startIndex, endIndex);
        }
        return subarrays;
    }
    public int size() {
        int size = 0;
        String b = this.binary;
        while (b.charAt(0) == '0') {
            b = b.replaceFirst("0", "");
        }
        return b.length();
    }
    public BigInt16 bsToBi16() {
        String[] subarrays = splitString(this.binary, 16);
        BigInt16 b = new BigInt16();
        int i =0;
        for (String s : subarrays) {
            b.arr[i] = Long.parseLong(s,2);
            i++;
        }
        return b;
    }
    private void removeZeros() {
        if (this.binary != "") {
            while (this.binary.charAt(0) == '0') {
                this.binary = this.binary.replaceFirst("0", "");
            }
        }
    }
    @Override
    public String toString() {
        this.removeZeros();
        return this.binary;
    }
}
