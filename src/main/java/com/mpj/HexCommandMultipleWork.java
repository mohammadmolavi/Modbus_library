package com.mpj;

import java.util.HexFormat;

public class HexCommandMultipleWork {
    private byte[] slaveId;
    private byte[] funcCode;
    private byte[] registerStAddr;
    private byte[] registerNum;
    private byte[] registerValue;
    private byte[] commandWithOutCRC;
    private byte[] commandWithCrc;
    public byte[] getSlaveId() {
        return slaveId;
    }

    public byte[] getFuncCode() {
        return funcCode;
    }

    public byte[] getRegistersValue() {
        return registerValue;
    }
    public byte[] getCommandWithOutCRC() {
        return commandWithOutCRC;
    }

    public byte[] getCommandWithCrc() {
        return commandWithCrc;
    }

    public void setSlaveId(byte[] slaveId) {
        this.slaveId = slaveId;
    }

    public void setFuncCode(byte[] funcCode) {
        this.funcCode = funcCode;
    }

    public void setRegistersValue(byte[] registerValue) {
        this.registerValue = registerValue;
    }

    public void setCommandWithOutCRC(byte[] commandWithOutCRC) {
        this.commandWithOutCRC = commandWithOutCRC;
    }

    public void setCommandWithCrc(byte[] commandWithCrc) {
        this.commandWithCrc = commandWithCrc;
    }
    public byte[] getRegisterStAddr() {
        return registerStAddr;
    }

    public byte[] getRegisterNum() {
        return registerNum;
    }

    public void setRegisterStAddr(byte[] registerStAddr) {
        this.registerStAddr = registerStAddr;
    }

    public void setRegisterNum(byte[] registerNum) {
        this.registerNum = registerNum;
    }


    public void CreateHexCommandWOCRC(){
        String slaveId="";
        String funcCode="";
        String registerStAddr="";
        String registerNum = "";
        String registerValue="";
        String byteCount="";


        slaveId = encodeHexString(this.slaveId);

        funcCode = encodeHexString(this.funcCode);

        registerStAddr += encodeHexString(this.registerStAddr);

        registerNum += encodeHexString(this.registerNum);

        byteCount = String.valueOf(Integer.valueOf(registerNum) * 2);
        String copyByteCount=byteCount;
        byteCount="";
        while(copyByteCount.length()+byteCount.length()<2) byteCount += "0";
        byteCount+=copyByteCount;

        registerValue += encodeHexString(this.registerValue);

        String tempCommand = slaveId + funcCode + registerStAddr + registerNum+ byteCount + registerValue;
        this.commandWithOutCRC = HexFormat.of().parseHex(tempCommand);
    }
    public String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }
    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public long ModRTU_CRC(byte[] buf, int len)
    {
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


    public String decToHex(int n)
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

    public void addCrcToCommand(){
        byte[] command = this.commandWithOutCRC.clone();
        String commandString = encodeHexString(command);
        long crc = ModRTU_CRC(command,6);
        int temp = (int) (crc % 256);
        crc /= 256;
        crc += 256 * temp;
        commandString += decToHex((int)crc);
        command = HexFormat.of().parseHex(commandString);

        this.commandWithCrc = command;
    }
}
