package com.mpj;

import java.util.HexFormat;
import java.util.stream.IntStream;

public class HexSingleCommand extends HexCommand{
    private String regAddr;
    private String strCommand;
    private String value;
    private byte[] byteArrayCommand;

    public byte[] getByteArrayCommand() {
        return byteArrayCommand;
    }

    public HexSingleCommand(String slaveId, String funcCode, String regAddr,String value) {
        this.slaveId = slaveId;
        this.funcCode = funcCode;
        this.regAddr = regAddr;
        this.value = value;
        attachAllAttr();
        long longCRC = Util.calculateCRC(HexFormat.of().parseHex(this.strCommand),6);
        this.CRC = HexFormat.of().parseHex(Util.decToStrHex((int)longCRC));
        this.createByteCommand();
    }

    public HexSingleCommand(byte[] CompleteCommand) {
        String strCompleteCommand = Util.byteArrayToStrHex(CompleteCommand);
        char[] chars = new char[2];

        strCompleteCommand.getChars(0,2,chars,0);
        this.slaveId = String.valueOf(chars);

        strCompleteCommand.getChars(2,4,chars,0);
        this.funcCode = String.valueOf(chars);

        chars = new char[4];

        strCompleteCommand.getChars(4,8,chars,0);
        this.regAddr = String.valueOf(chars);

        strCompleteCommand.getChars(8,12,chars,0);
        this.value = String.valueOf(chars);

        strCompleteCommand.getChars(12,16,chars,0);
        this.CRC = HexFormat.of().parseHex(String.valueOf(chars));

        this.strCommand = slaveId + funcCode + regAddr + value;

        this.byteArrayCommand = HexFormat.of().parseHex(strCompleteCommand);
    }

    private String attachAllAttr(){
           this.strCommand="";
           this.strCommand += slaveId + funcCode + regAddr + value;
           return strCommand;
    }

    private byte[] createByteCommand(){
        byte[] command = HexFormat.of().parseHex(this.strCommand);
        String commandString = Util.byteArrayToStrHex(command);
        long crc = Util.calculateCRC(command,6);
        int temp = (int) (crc % 256);
        crc /= 256;
        crc += 256 * temp;
        commandString += Util.decToStrHex((int)crc);
        command = HexFormat.of().parseHex(commandString);

        this.byteArrayCommand = command;
        return command;
    }

}
