package main;

import myLibrary.parser;
import myLibrary.resultCsv;

import java.util.LinkedList;
import java.util.List;

public class main {
    public static void main(String[] args)  {

        List<List> result = new LinkedList<>();

        //create parse
        parser p = new parser();
        p.create(result);

        //create CSV
        System.out.println("create CSV");
        resultCsv csv = new resultCsv(result);
        csv.countOfFeatures();
        csv.create();
    }

}