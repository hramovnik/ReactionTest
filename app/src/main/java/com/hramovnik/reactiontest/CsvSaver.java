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

public class CsvSaver {
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

        try {
            PrintWriter out = new PrintWriter(sdFile);
            StringBuilder sb = new StringBuilder();
            for (String val:first) {sb.append(val + ",");}
            sb.append("\n");
            for (String val:second) {sb.append(val + ",");}
            sb.append("\n");

            out.write(sb.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean save(LinkedList<String> dataList){
        if (sdFile == null) return false;
        try {
            PrintWriter out = new PrintWriter(sdFile);
            StringBuilder sb = new StringBuilder();
            for (String val:dataList) {sb.append(val + ",");}
            sb.append("\n");

            out.write(sb.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
