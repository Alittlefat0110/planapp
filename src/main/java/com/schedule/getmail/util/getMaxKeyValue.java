package com.schedule.getmail.util;

import com.schedule.getmail.contentSimilarity.tokenizer.Word;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class getMaxKeyValue {
    public static Object getMaxValue(Map<Word, Integer> map) {
        if (map == null) return null;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

    public static Object getMaxKey(Map<Word, Integer> map) {
        if (map == null) return null;
        Set<Word> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

}
