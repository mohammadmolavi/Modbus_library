package com.mpj;

import java.util.HexFormat;

public class ModbusRTURegistersExample {

    public static void main(String[] args) throws InterruptedException {

//        SerialPort serialPort = SerialPort.getCommPort("/dev/ttyUSB0");
//        serialPort.setBaudRate(9600);
//        serialPort.setNumDataBits(8);
//        serialPort.setNumStopBits(1);
//        serialPort.setParity(SerialPort.NO_PARITY);
//        if (!serialPort.openPort()) {
//            System.out.println("Failed to open the serial port.");
//        }
//        else{
//            System.out.println("port opened successfully!");
//        }
//
//    }
//    public static String encodeHexString(byte[] byteArray) {
//        StringBuffer hexStringBuffer = new StringBuffer();
//        for (int i = 0; i < byteArray.length; i++) hexStringBuffer.append(byteToHex(byteArray[i]));
//        return hexStringBuffer.toString();
//    }
//    public static String byteToHex(byte num) {
//        char[] hexDigits = new char[2];
//        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
//        hexDigits[1] = Character.forDigit((num & 0xF), 16);
//        return new String(hexDigits);
//    }
        ModbusRTU modbusRTU = new ModbusRTU("/dev/ttyUSB0", 9600, 8, 1, 2);
        byte[] slaveId = HexFormat.of().parseHex("05");
        byte[] registerAddr = HexFormat.of().parseHex("001F");
        byte[] regCount = HexFormat.of().parseHex("0003");
        byte[] value = HexFormat.of().parseHex("000100010001");
        modbusRTU.writeMultipleRegister(slaveId,registerAddr,regCount,value);
    }
}
