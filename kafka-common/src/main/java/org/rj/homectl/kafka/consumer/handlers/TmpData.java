package org.rj.homectl.kafka.consumer.handlers;

import java.util.ArrayList;
import java.util.List;

public class TmpData {
    private static List<String> data = new ArrayList<>();

    public static List<String> getData() { return data; }

    public static void recordData(String item) {
        data.add(item);
    }

}
