package com.bedrock.padder.model;

import com.bedrock.padder.helper.FileHelper;

public class Timing {

    private int timing[];

    private int index = 0;

    public Timing(String path, FileHelper fileHelper) {
        this.timing = getTimingFromFile(path, fileHelper);
    }

    int[] getTimingFromFile(String path, FileHelper fileHelper) {
        String file = fileHelper.getStringFromFile(path);
        String lines[] = file.split("\\n");
        int timing[] = new int[lines.length];

        for (int i = 0; i < lines.length; i++) {
            // parse the millisecond string to int
            timing[i] = Integer.parseInt(lines[i]);
        }
        return timing;
    }

    public int getDelay() {
        index++;
        if (index == 1) {
            // first delay
            return timing[index - 1];
        } else if (index < timing.length) {
            return timing[index] - timing[index - 1];
        } else {
            return -1;
        }
    }
}
