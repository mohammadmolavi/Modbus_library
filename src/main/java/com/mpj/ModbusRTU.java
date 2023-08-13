package com.mpj;

import com.fazecast.jSerialComm.SerialPort;

import java.util.HexFormat;

public class ModbusRTU {


    private String serialPortName = "/dev/ttyUSB0"; // Replace with the name of your serial port
    private int baudRate;                  // Replace with the baud rate of your Modbus device
    private int dataBits;                     // Replace with the number of data bits (typically 8)
    private int stopBits;                      // Replace with the number of stop bits (typically 1)
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

    public ModbusRTU() {
        this.serialPortName = "/dev/ttyUSB0"; // Replace with the name of your serial port
        this.baudRate = 9600;                   // Replace with the baud rate of your Modbus device
        this.dataBits = 8;                      // Replace with the number of data bits (typically 8)
        this.stopBits = 1;                      // Replace with the number of stop bits (typically 1)
        this.parity = SerialPort.NO_PARITY;
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
        Thread.sleep(40);
        byte[] bytebuffer= new byte[30];
        this.serialPort.readBytes(bytebuffer,30);
    }

    public byte[] readSingleRegister(byte[] slaveId, byte[] registerAddr) throws InterruptedException {

        HexSingleCommand hexSingleCommand = new HexSingleCommand(Util.byteArrayToStrHex(slaveId), "03"
                                            , Util.byteArrayToStrHex(registerAddr),"0001");
        byte[] command = hexSingleCommand.getByteArrayCommand();
        this.serialPort.writeBytes(command, 8);
        byte[] readByte = new byte[8];
        Thread.sleep(40);
        this.serialPort.readBytes(readByte,8);
        return readByte;
    }

    public void writeMultipleRegister(byte[] slaveId, byte[] stRegAddr, byte[] numOfReg, byte[] values) throws InterruptedException {
        String byteCount = Util.decToStrHex(values.length);
        if(byteCount.length()<2) byteCount = "0" + byteCount;
        HexMultipleCommand hexCommand = new HexMultipleCommand(Util.byteArrayToStrHex(slaveId),"10"
                                        ,Util.byteArrayToStrHex(stRegAddr),Util.byteArrayToStrHex(numOfReg)
                                        , byteCount ,Util.byteArrayToStrHex(values));
        System.out.println(Util.byteArrayToStrHex(hexCommand.getByteArrayCommand()));
        serialPort.writeBytes(hexCommand.getByteArrayCommand(), hexCommand.getByteArrayCommand().length);
        Thread.sleep(40);
        byte[] bytebuffer= new byte[9+values.length];
        serialPort.readBytes(bytebuffer,bytebuffer.length);
        System.out.println(Util.byteArrayToStrHex(bytebuffer));
    }
}
