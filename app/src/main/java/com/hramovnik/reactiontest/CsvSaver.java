package com.hramovnik.reactiontest;

import android.os.Environment;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import layout.PrefActivity;

/**
 * Created by gshabalev on 12/28/2016.
 */

public class CsvSaver implements AutoCloseable{
    private CsvSaver(){}
    File sdFile = null;

    public CsvSaver(String filePath, String fileName) {
        super();
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File sdPath = Environment.getExternalStorageDirectory();
                sdPath = new File(sdPath.getAbsolutePath() + "/Goggles"
                        + "/" + PrefActivity.getProfileResult()
                        + "/" + PrefActivity.getCurrentDate()
                        + (filePath.isEmpty() ? ("/" + filePath) : ""));
                sdPath.mkdirs();

                sdFile = new File(sdPath, fileName + ".csv");
                out = new PrintWriter(sdFile, "Cp1251");
                save(PrefActivity.getProfileData());
            }
        }catch (Exception e) {
            sdFile = null;
        }
    }

    public boolean savePairs(LinkedList<Pair<String, String> > dataList){
        if (sdFile == null) return false;
        LinkedList<String> first = new LinkedList<String>();
        LinkedList<String> second = new LinkedList<String>();
        for (Pair<String,String> value :dataList ) {
            first.add(value.first);
            second.add(value.second);
        }


        StringBuilder sb = new StringBuilder();
        for (String val:first) {sb.append(val + ",");}
        sb.append("\n");
        for (String val:second) {sb.append(val + ",");}
        sb.append("\n");

        out.append(sb.toString());
        out.flush();

        return true;
    }

    public boolean save(LinkedList<String> dataList){
        if (sdFile == null) return false;

        StringBuilder sb = new StringBuilder();
        for (String val:dataList) {sb.append(val + ",");}
        sb.append("\n");

        out.append(sb.toString());
        out.flush();
        return true;

    }

    private PrintWriter out;

    @Override
    public void close() throws Exception {
        try {
            out.close();
        }catch (Exception e){

        }

    }
}
