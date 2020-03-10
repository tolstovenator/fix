package com.yafix.engine;

public enum FixTag {

    BeginString(8, FixSubType.String),
    BodyLength(9, FixSubType.Length),
    CheckSum(10, FixSubType.String),
    MsgSeqNum(34, FixSubType.SeqNum),
    MsgType(35, FixSubType.String),
    SenderCompID(49, FixSubType.String),
    SendingTime(52, FixSubType.UTCTimestamp),
    TargetCompID(56, FixSubType.String),
    EncryptMethod(98, FixSubType.Int),
    HeartBtInt(108, FixSubType.Int);


    int tag;
    FixSubType fixSubType;

    FixTag(int tag, FixSubType fixType) {
        this.tag = tag;
        this.fixSubType = fixType;
    }


}
