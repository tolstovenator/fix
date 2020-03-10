package com.yafix.engine;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class FixTags {

    private static final Int2ObjectMap<FixTag> tagMap = new Int2ObjectArrayMap<>();

    static {
        for (FixTag value : FixTag.values()) {
            tagMap.put(value.tag, value);
        }
    }

    public static FixTag getByNumber(int tag) {
        return tagMap.get(tag);
    }
}
