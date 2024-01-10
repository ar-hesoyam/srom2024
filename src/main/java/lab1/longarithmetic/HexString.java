package lab1.longarithmetic;

public class HexString {
    private String hex;

    public HexString(String hex) {
        this.hex = hex;
    }

    private String reverse(String s) {
        String new_s = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            new_s += s.charAt(i);
        }
        return new_s;
    }
    public String[] splitString(int chunkSize) {
        int length = this.hex.length();
        int subarrayCount = (length + chunkSize - 1) / chunkSize;
        String[] subarrays = new String[subarrayCount];

        for (int i = 0; i < subarrayCount; ++i) {
            int startIndex = Math.max(length - (i + 1) * chunkSize, 0);
            int endIndex = length - i * chunkSize;
            subarrays[i] = this.hex.substring(startIndex, endIndex);
        }
        // Reverse the array
        return reverseArray(subarrays);
    }

    private String[] reverseArray(String[] array) {
        int length = array.length;
        String[] reversedArray = new String[length];
        for (int i = 0; i < length; i++) {
            reversedArray[i] = array[length - i - 1];
        }
        return reversedArray;
    }

}
