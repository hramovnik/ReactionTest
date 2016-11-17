package com.hramovnik.reactiontest;

import java.util.LinkedList;

/**
 * Created by gshabalev on 11/17/2016.
 */

public class Hand {
    private Hand(){}
    public Hand(String text){this.text = text;}
    public String text;
    public int pre = 0;
    public int post = 0;
    public LinkedList<Integer> correct = new LinkedList<Integer>();

}
