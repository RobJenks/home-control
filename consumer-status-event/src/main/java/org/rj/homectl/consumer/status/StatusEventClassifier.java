package org.rj.homectl.consumer.status;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.kafka.common.header.Headers;
import org.rj.homectl.consumer.status.awair.AwairStatusEvent;
import org.rj.homectl.consumer.status.hue.HueStatusEvent;
import org.rj.homectl.consumer.status.st.StStatusEvent;
import org.rj.homectl.consumer.status.unknown.UnknownStatusEvent;

import java.util.Arrays;

public class StatusEventClassifier {
    private static final byte[] KEY_HUE = "hue".getBytes();
    private static final byte[] KEY_ST = "st".getBytes();
    private static final byte[] KEY_AWAIR = "awair".getBytes();

    private static final JavaType TYPE_HUE = TypeFactory.defaultInstance().constructType(HueStatusEvent.class);
    private static final JavaType TYPE_ST = TypeFactory.defaultInstance().constructType(StStatusEvent.class);
    private static final JavaType TYPE_AWAIR = TypeFactory.defaultInstance().constructType(AwairStatusEvent.class);
    private static final JavaType TYPE_UNKNOWN = TypeFactory.defaultInstance().constructType(UnknownStatusEvent.class);

    public static JavaType byKey(String topic, byte[] data, Headers headers) {
        if      (hasKey(data, KEY_HUE)) return TYPE_HUE;
        else if (hasKey(data, KEY_ST)) return TYPE_ST;
        else if (hasKey(data, KEY_AWAIR)) return TYPE_AWAIR;

        else return TYPE_UNKNOWN;
    }

    private static boolean hasKey(final byte[] data, final byte[] key) {
        return Arrays.equals(data, key);
    }
}
