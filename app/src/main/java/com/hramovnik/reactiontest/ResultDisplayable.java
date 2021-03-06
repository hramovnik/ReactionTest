package com.hramovnik.reactiontest;

import android.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by gshabalev on 11/11/2016.
 */

public interface ResultDisplayable {
    public void displayResult(String value, SessionResultActionInterface action);
    public void displayResult(LinkedList<Pair<Integer, Integer>>dataList, int [] colors, boolean showLegend, SessionResultActionInterface action);
    public void displayResult(Pair<LinkedList<Pair<Integer, Integer> > , LinkedList<Pair<Integer, Integer> > > dataList, int [] colors, SessionResultActionInterface action);
}
