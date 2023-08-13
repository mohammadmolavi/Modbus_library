package com.mpj;

public class HexCommand {
    protected String slaveId;
    protected String funcCode;
    protected byte[] CRC;

    public void setSlaveId(String slaveId) {
        this.slaveId = slaveId;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public void setCRC(byte[] CRC) {
        this.CRC = CRC;
    }

    public String getSlaveId() {
        return slaveId;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public byte[] getCRC() {
        return CRC;
    }

    public void addCRCToCommand(){

    }
}
