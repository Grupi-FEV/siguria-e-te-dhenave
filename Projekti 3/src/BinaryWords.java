public class BinaryWords {

    public String toBinary(String s) {
        String binaryString = "";
        for (int i = 0; i < s.length(); i++) {
            int asciiValue = (int) s.charAt(i);
            String binaryValue = Integer.toBinaryString(asciiValue);
            while (binaryValue.length() < 8) {
                binaryValue = "0" + binaryValue;
            }
            binaryString += binaryValue;
        }
        return binaryString;
    }

    public String toString(String bits) {
        while(bits.length() % 8 !=0){
            bits = "0"+bits;
        }
        String s = "";
        for (int i = 0; i < bits.length(); i += 8) {
            String bitChunk = bits.substring(i, i + 8);
            int decimalValue = Integer.parseInt(bitChunk, 2);
            s += (char) decimalValue;
        }
        return s;
    }
}