package org.rj.homectl.status.hue;

import org.rj.homectl.hue.model.Light;
import org.rj.homectl.status.events.StatusEventContent;

import java.util.HashMap;

public class HueStatusData extends HashMap<String, Light>
                          implements StatusEventContent { }
