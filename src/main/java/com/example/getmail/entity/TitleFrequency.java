package com.example.getmail.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TitleFrequency {
    private int id;
    private String words;
    private Integer frequency;
}
