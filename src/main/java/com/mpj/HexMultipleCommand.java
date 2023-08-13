package com.mpj;

import java.util.HexFormat;

public class HexMultipleCommand extends HexCommand{
    private String regStAddr;
    private String regCount;
    private String byteCount;
    private String values;
    private String strCommand;
    private byte[] byteArrayCommand;

    public HexMultipleCommand(String slaveId, String funcCode, String regStAddr, String regCount, String byteCount, String values) {
        this.slaveId = slaveId;
        this.funcCode = funcCode;
        this.regStAddr = regStAddr;
        this.regCount = regCount;
        this.byteCount = byteCount;
        this.values = values;
        attachAllAttr();
        long longCRC;
        if(byteCount != null && byteCount != "") {
            longCRC = Util.calculateCRC(HexFormat.of().parseHex(strCommand), 9 + Integer.valueOf(byteCount));
        }
        else{
            longCRC = Util.calculateCRC(HexFormat.of().parseHex(strCommand), 6);
        }
        this.CRC = HexFormat.of().parseHex(Util.decToStrHex((int)longCRC));
        this.byteArrayCommand = HexFormat.of().parseHex(strCommand + Util.byteArrayToStrHex(this.CRC));
    }

    public HexMultipleCommand(byte[] completeCommand) {
        String strCompleteCommand = Util.byteArrayToStrHex(completeCommand);
        char[] chars = new char[2];

        strCompleteCommand.getChars(0,2,chars,0);
        this.slaveId = String.valueOf(chars);

        strCompleteCommand.getChars(2,4,chars,0);
        this.funcCode = String.valueOf(chars);

        chars = new char[4];

        strCompleteCommand.getChars(4,8,chars,0);
        this.regStAddr = String.valueOf(chars);

        strCompleteCommand.getChars(8, 12, chars, 0);
        this.regCount = String.valueOf(chars);

        if(completeCommand.length == 8) {

            strCompleteCommand.getChars(12, 16, chars, 0);
            this.CRC = HexFormat.of().parseHex(String.valueOf(chars));
            this.byteCount = "";
            this.values = "";
            attachAllAttr();
        }
        else{
            chars = new char[2];
            strCompleteCommand.getChars(12, 14, chars, 0);
            this.byteCount = String.valueOf(chars);

            chars = new char[Integer.valueOf(this.byteCount)*2];
            strCompleteCommand.getChars(14, 14 + chars.length, chars, 0);
            this.values = String.valueOf(chars);

            chars = new char[4];
            strCompleteCommand.getChars(14 + Integer.valueOf(byteCount) * 2, 18 + Integer.valueOf(byteCount) * 2 , chars , 0);
            this.CRC = HexFormat.of().parseHex(String.valueOf(chars));

            attachAllAttr();
        }

        this.byteArrayCommand = HexFormat.of().parseHex(strCompleteCommand);

    }

    public byte[] getByteArrayCommand() {
        return byteArrayCommand;
    }
    private String attachAllAttr(){
        this.strCommand="";
        this.strCommand += slaveId + funcCode + regStAddr + regCount + byteCount + values;
        return strCommand;
    }

    private byte[] createByteCommand(){
        byte[] command = HexFormat.of().parseHex(this.strCommand);
        String commandString = Util.byteArrayToStrHex(command);
        long crc = Util.calculateCRC(command,0);
        int temp = (int) (crc % 256);
        crc /= 256;
        crc += 256 * temp;
        commandString += Util.decToStrHex((int)crc);
        command = HexFormat.of().parseHex(commandString);

        this.byteArrayCommand = command;
        return command;
    }
}
