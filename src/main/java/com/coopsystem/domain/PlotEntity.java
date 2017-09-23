package com.coopsystem.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dariusz Ł on 29.04.2017.
 */
public class PlotEntity implements Serializable {
    public Date time;
    public Object value;

    public PlotEntity(Date time, Object value) {
        this.time = time;
        this.value = value;
    }


}
