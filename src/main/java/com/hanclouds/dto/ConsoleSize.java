package com.hanclouds.dto;

public class ConsoleSize {

    private String cols;
    private String rows;

    public ConsoleSize(String cols, String rows) {
        this.cols = cols;
        this.rows = rows;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}
