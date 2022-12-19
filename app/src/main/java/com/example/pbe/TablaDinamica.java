package com.example.pbe;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TablaDinamica {

    private TableLayout tableLayout;
    private Context context;
    private String[]header;
    private ArrayList<String[]>data;
    private TableRow tableRow;
    private TextView txtCell;
    private int IndexC;
    private int IndexR;


    public TablaDinamica(TableLayout tableLayout, Context context) {
        this.tableLayout=tableLayout;
        this.context = context;
    }
    public void addHeader(String[]header){
        this.header=header;
        createHeader();
    }
    public void addData(ArrayList<String[]>data){
        this.data=data;
        createDataTable();
    }
    private void newRow(){
        tableRow=new TableRow(context);
    }
    private void newCell(){
        txtCell=new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(25);
    }
    private void createHeader(){
        IndexC = 0;
        newRow();
        while(IndexC<header.length){
            newCell();
            txtCell.setText(header[IndexC++]);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }
    private void createDataTable(){
        String info;
        for(IndexR=1;IndexR<=header.length;IndexR++){
            newRow();
            for(IndexC=0;IndexC<=header.length;IndexC++){
                newCell();
                String[] colums=data.get(IndexR-1);
                info=(IndexC<colums.length)?colums[IndexC]:"";
                txtCell.setText(info);
                tableLayout.addView(txtCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    public TableRow.LayoutParams newTableRowParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }


}
