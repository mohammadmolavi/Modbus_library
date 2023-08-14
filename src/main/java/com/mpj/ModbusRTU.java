package com.mpj;

import com.fazecast.jSerialComm.SerialPort;

import java.util.HexFormat;

public class ModbusRTU {


    private String serialPortName = "/dev/ttyUSB0";
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;
    private SerialPort serialPort;


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
        serialPort.closePort();
        serialPort.openPort();
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
        serialPort.closePort();
        serialPort.openPort();
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
        serialPort.closePort();
        serialPort.openPort();
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
        serialPort.closePort();
        serialPort.openPort();
    }

    public void setParity(int parity) {
        this.parity = parity;
        serialPort.closePort();
        serialPort.openPort();
    }

    public ModbusRTU() {
        this.serialPortName = "/dev/ttyUSB0";
        this.baudRate = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = SerialPort.NO_PARITY;
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

    public ModbusRTU(String serialPortName, int baudRate, int dataBits, int stopBits, int parity) {
        this.serialPortName = serialPortName;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.serialPort = SerialPort.getCommPort(this.serialPortName);
        this.serialPort.setBaudRate(this.baudRate);
        this.serialPort.setNumDataBits(this.dataBits);
        this.serialPort.setNumStopBits(this.stopBits);
        this.serialPort.setParity(this.parity);
        if (!serialPort.openPort()) {
            System.out.println("Failed to open the serial port.");
        }
        else {
            System.out.println("port opened successfully!");
        }
    }

    public void writeSingleRegister(byte[] slaveId, byte[] registerAddr, byte[] value) throws InterruptedException {
        HexSingleCommand hexCommand = new HexSingleCommand(Util.byteArrayToStrHex(slaveId), "06"
                                    , Util.byteArrayToStrHex(registerAddr), Util.byteArrayToStrHex(value));

        this.serialPort.writeBytes(hexCommand.getByteArrayCommand(), 8);
        Thread.sleep(30);
        byte[] bytebuffer= new byte[30];
        this.serialPort.readBytes(bytebuffer,30);
    }

    public byte[] readSingleRegister(byte[] slaveId, byte[] registerAddr) throws InterruptedException {

        HexSingleCommand hexSingleCommand = new HexSingleCommand(Util.byteArrayToStrHex(slaveId), "03"
                                            , Util.byteArrayToStrHex(registerAddr),"0001");
        byte[] command = hexSingleCommand.getByteArrayCommand();
        this.serialPort.writeBytes(command, 8);
        byte[] readByte = new byte[8];
        Thread.sleep(30);
        this.serialPort.readBytes(readByte,8);
        return readByte;
    }

    public void writeMultipleRegister(byte[] slaveId, byte[] stRegAddr, byte[] numOfReg, byte[] values) throws InterruptedException {
        String byteCount = Util.decToStrHex(values.length);
        if(byteCount.length()<2) byteCount = "0" + byteCount;
        HexMultipleCommand hexCommand = new HexMultipleCommand(Util.byteArrayToStrHex(slaveId),"10"
                                        ,Util.byteArrayToStrHex(stRegAddr),Util.byteArrayToStrHex(numOfReg)
                                        , byteCount ,Util.byteArrayToStrHex(values));
        serialPort.writeBytes(hexCommand.getByteArrayCommand(), hexCommand.getByteArrayCommand().length);
        Thread.sleep(60);
        byte[] bytebuffer= new byte[40];
        serialPort.readBytes(bytebuffer,40);
    }

    public byte[] readMultipleRegister(byte[] slaveId, byte[] stRegAddr, byte[] numOfReg) throws InterruptedException {
        byte[] bytebuffer= new byte[60];
        serialPort.readBytes(bytebuffer,bytebuffer.length);
        HexMultipleCommand hexCommand = new HexMultipleCommand(Util.byteArrayToStrHex(slaveId),"03"
                ,Util.byteArrayToStrHex(stRegAddr),Util.byteArrayToStrHex(numOfReg)
                , "" ,"");
        System.out.println(Util.byteArrayToStrHex(hexCommand.getByteArrayCommand()));
        serialPort.writeBytes(hexCommand.getByteArrayCommand(), hexCommand.getByteArrayCommand().length);
        Thread.sleep(40);
        bytebuffer= new byte[5 + 2 * numOfReg[1]];
        serialPort.readBytes(bytebuffer,bytebuffer.length);
        return bytebuffer;
    }
}
