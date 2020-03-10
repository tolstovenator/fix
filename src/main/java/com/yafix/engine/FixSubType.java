package com.yafix.engine;

public enum FixSubType {

    Char(FixType.Char),
    Boolean(FixType.Char),

    Data(FixType.Data),

    Float(FixType.Float),
    Amt(FixType.Float),
    Percentage(FixType.Float),
    Price(FixType.Float),
    PriceOffset(FixType.Float),
    Qty(FixType.Float),

    Int(FixType.Int),
    DayOfMonth(FixType.Int),
    Length(FixType.Int),
    NumInGroup(FixType.Int),
    SeqNum(FixType.Int),
    TagNum(FixType.Int),

    String(FixType.String),
    Country(FixType.String),
    Currency(FixType.String),
    //todo
    UTCTimestamp(FixType.String);

    FixType parent;

    FixSubType(FixType parent) {
        this.parent = parent;
    }
}
