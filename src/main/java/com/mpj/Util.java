package com.mpj;

public class Util {
    public static long calculateCRC(byte[] buf, int len){
        long crc = 0xFFFF;

        for (int pos = 0; pos < buf.length; pos++) {
            crc ^= (long)buf[+pos];          // XOR byte into least sig. byte of crc

            for (int i = 8; i != 0; i--) {    // Loop over each bit
                if ((crc & 0x0001) != 0) {      // If the LSB is set
                    crc >>= 1;                    // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                }
                else                            // Else LSB is not set
                    crc >>= 1;                    // Just shift right
            }
        }
        // Note, this number has low and high bytes swapped, so use it accordingly (or swap bytes)
        return crc;
    }

    public static String decToStrHex(int n)
    {
        // Creating an array to store octal number
        int[] hexNum = new int[100];

        // counter for hexadecimal number array
        int i = 0;
        while (n != 0) {

            // Storing remainder in hexadecimal array
            hexNum[i] = n % 16;
            n = n / 16;
            i++;
        }

        // Printing hexadecimal number array
        // in the reverse order
        String result = "";
        for (int j = i - 1; j >= 0; j--) {
            if (hexNum[j] > 9)
                result += (char)(55 + hexNum[j]);

            else
                result += String.valueOf(hexNum[j]);
        }
        return result;
    }
    public static String byteArrayToStrHex(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }
    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

}
