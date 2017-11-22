package com.bedrock.padder.model.tutorial;

import java.util.Comparator;

public class SyncComparator implements Comparator<Sync> {

    @Override
    public int compare(Sync t0, Sync t1) {
        // Sorting in ascending order of start offset
        return t0.start - t1.start;
    }
}
