package com.tsmrachel.cdcslots;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rachel on 24/5/2015.
 */
public class Group {
    public String string;
    public final List<String> children = new ArrayList<String>();

    public Group(String string) {
        this.string = string;
    }
}
