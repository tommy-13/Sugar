package de.tommy13.sugar.page_history;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by tommy on 28.03.2017.
 * Class to great the diagrams in the History Fragmens.
 */

class HistoryDataSet {

    private List<Data> dataList;
    private int        size;
    private float      maxValue;
    private String     unit;


    HistoryDataSet(String unit) {
        dataList      = new ArrayList<>();
        size          = 0;
        maxValue      = 0f;
        this.unit     = unit;
    }

    public void add(String label, float value, Classification classification) {
        Data data           = new Data();
        data.label          = label;
        data.value          = value;
        data.classification = classification;
        dataList.add(data);

        size++;

        if (value > maxValue) {
            maxValue = value;
        }
    }

    int getSize() {
        return size;
    }

    float getMaxValue() {
        return maxValue;
    }

    String getLabel(int pos) {
        return dataList.get(pos).label;
    }

    float getValue(int pos) {
        return dataList.get(pos).value;
    }

    Classification getClassification(int pos) {
        return dataList.get(pos).classification;
    }

    String getUnit() {
        return unit;
    }



    private class Data {
        String         label;
        float          value;
        Classification classification;
    }
}
