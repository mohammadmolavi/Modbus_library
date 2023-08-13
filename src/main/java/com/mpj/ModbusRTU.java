package com.mpj;

import com.fazecast.jSerialComm.SerialPort;

import java.util.HexFormat;

public class ModbusRTU {
    public String getSerialPortName() {
        return serialPortName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setSerialPortName(String serialPortName) {
        this.serialPortName = serialPortName;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    private String serialPortName = "/dev/ttyUSB0"; // Replace with the name of your serial port
    private int baudRate = 9600;                   // Replace with the baud rate of your Modbus device
    private int dataBits = 8;                      // Replace with the number of data bits (typically 8)
    private int stopBits = 1;                      // Replace with the number of stop bits (typically 1)
    private int parity = SerialPort.NO_PARITY;
    private SerialPort serialPort;

    public ModbusRTU() {
        HexCommandSingleWork hexCommand = new HexCommandSingleWork();
        // Define the Modbus RTU device parameters
        this.serialPort = SerialPort.getCommPort(this.serialPortName);
        this.serialPort.setBaudRate(this.baudRate);
        this.serialPort.setNumDataBits(this.dataBits);
        this.serialPort.setNumStopBits(this.stopBits);
        this.serialPort.setParity(this.parity);
        if (!serialPort.openPort()) {
            System.out.println("Failed to open the serial port.");
        }
        else{
            System.out.println("port opened successfully!");
        }
    }
    public void writeSingleRegister(String registerAddr, String slaveId, String value) throws InterruptedException {
        HexCommandSingleWork hexCommand = new HexCommandSingleWork();
        hexCommand.setFuncCode(HexFormat.of().parseHex("06"));
        hexCommand.setRegisterAddr(HexFormat.of().parseHex(registerAddr));
        hexCommand.setRegisterValue(HexFormat.of().parseHex(value));
        hexCommand.setSlaveId(HexFormat.of().parseHex(slaveId));
        hexCommand.CreateHexCommandWOCRC();
        hexCommand.addCrcToCommand();

        this.serialPort.writeBytes(hexCommand.getCommandWithCrc(), 8);
        Thread.sleep(40);
        byte[] bytebuffer= new byte[20];
        this.serialPort.readBytes(bytebuffer,20);

    }

    public byte[] readSingleRegister(String registerAddr, String slaveId) throws InterruptedException {
        HexCommandSingleWork hexCommand = new HexCommandSingleWork();
        hexCommand.setSlaveId(HexFormat.of().parseHex(slaveId));
        hexCommand.setFuncCode(HexFormat.of().parseHex("03"));
        hexCommand.setRegisterAddr(HexFormat.of().parseHex("0001"));
        hexCommand.setRegisterValue(HexFormat.of().parseHex("0003"));
        hexCommand.CreateHexCommandWOCRC();
        hexCommand.addCrcToCommand();
        byte[] command = hexCommand.getCommandWithCrc();
        this.serialPort.writeBytes(command, 8);
        byte[] readByte = new byte[15];
        Thread.sleep(40);
        this.serialPort.readBytes(readByte,15);
        Thread.sleep(40);
        return readByte;
    }

    public void writeMultipleRegister(String slaveId, String stRegAddr, String numOfReg, String Values) throws InterruptedException {
        HexCommandMultipleWork hexCommandMultipleWork = new HexCommandMultipleWork();
        hexCommandMultipleWork.setFuncCode(HexFormat.of().parseHex("10"));
        hexCommandMultipleWork.setSlaveId(HexFormat.of().parseHex(slaveId));
        hexCommandMultipleWork.setRegisterStAddr(HexFormat.of().parseHex(stRegAddr));
        hexCommandMultipleWork.setRegisterNum(HexFormat.of().parseHex(numOfReg));
        hexCommandMultipleWork.setRegistersValue(HexFormat.of().parseHex(Values));
        hexCommandMultipleWork.CreateHexCommandWOCRC();
        hexCommandMultipleWork.addCrcToCommand();
        System.out.println(encodeHexString(hexCommandMultipleWork.getCommandWithCrc()));
        serialPort.writeBytes(hexCommandMultipleWork.getCommandWithCrc(), hexCommandMultipleWork.getCommandWithCrc().length);
        Thread.sleep(40);
        byte[] bytebuffer= new byte[9+Values.length()/2];
        serialPort.readBytes(bytebuffer,15);
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
