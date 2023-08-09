package com.mpj;


import com.fazecast.jSerialComm.SerialPort;

import java.util.HexFormat;

public class ModbusRTURegistersExample {

    public static void main(String[] args) throws InterruptedException {
        ModbusRTU modbus = new ModbusRTU();
//        for(int i=1 ; i <=9 ; i++)
//            modbus.writeSingleRegister("0002","0"+String.valueOf(2),"0002");
//        modbus.writeSingleRegister("0002","02","0002");
//        modbus.writeSingleRegister("0003","02","0001");
//        byte[] reg2Status = modbus.readSingleRegister("0002","02");
//        System.out.println(encodeHexString(reg2Status));
////         Define the Modbus RTU device parameters
        SerialPort serialPort = SerialPort.getCommPort("/dev/ttyUSB0");
        serialPort.setBaudRate(9600);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(1);
        serialPort.setParity(SerialPort.NO_PARITY);
        if (!serialPort.openPort()) {
            System.out.println("Failed to open the serial port.");
        }
        else{
            System.out.println("port opened successfully!");
        }

        HexCommandMultipleWork hexCommandMultipleWork = new HexCommandMultipleWork();
        hexCommandMultipleWork.setFuncCode(HexFormat.of().parseHex("10"));
        hexCommandMultipleWork.setSlaveId(HexFormat.of().parseHex("02"));
        hexCommandMultipleWork.setRegisterStAddr(HexFormat.of().parseHex("0001"));
        hexCommandMultipleWork.setRegistersValue(HexFormat.of().parseHex("000200020002"));
        hexCommandMultipleWork.setRegisterNum(HexFormat.of().parseHex("0003"));
        hexCommandMultipleWork.CreateHexCommandWOCRC();
        hexCommandMultipleWork.addCrcToCommand();
        System.out.println(encodeHexString(hexCommandMultipleWork.getCommandWithCrc()));
        serialPort.writeBytes(hexCommandMultipleWork.getCommandWithCrc(), hexCommandMultipleWork.getCommandWithCrc().length);
        Thread.sleep(40);
        byte[] bytebuffer= new byte[20];
        serialPort.readBytes(bytebuffer,20);
        System.out.println(encodeHexString(bytebuffer));
    }
    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) hexStringBuffer.append(byteToHex(byteArray[i]));
        return hexStringBuffer.toString();
    }
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
