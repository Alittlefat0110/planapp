package com.example.getmail.bean;

public class CronConfig {
    private int cron_id;
    private String cron;

    public int getCron_id() {
        return cron_id;
    }

    public void setCron_id(int cron_id) {
        this.cron_id = cron_id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public String toString() {
        return "CronConfig{" +
                "cron_id=" + cron_id +
                ", cron='" + cron + '\'' +
                '}';
    }
}
