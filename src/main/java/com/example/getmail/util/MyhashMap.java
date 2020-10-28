package com.example.getmail.util;

import com.example.getmail.contentSimilarity.tokenizer.Word;

import java.util.HashMap;

public class MyhashMap extends HashMap<Word,Integer> {
    @Override
    public Integer put(Word key, Integer value) {

        //  定义一个新的value 接收 后面put的新的value值
        Integer NewVaule = value;

        //containsKey  判断这个 key  是否已经存在？
        if (containsKey(key)){

            // 获得旧的value 值
            Integer oldValue = get(key);

            //将旧值 和 后面put 的新值拼接起来
            NewVaule = oldValue +NewVaule;
        }

        // 返回拼接后的newvalue
        return super.put(key, NewVaule);
    }
}
