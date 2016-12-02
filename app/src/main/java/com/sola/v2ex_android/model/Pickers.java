package com.sola.v2ex_android.model;

import java.io.Serializable;

/**
 * Created by wei on 2016/12/2.
 */

public class Pickers implements Serializable {

    public String title;
    public String id;

    public Pickers(String title ,String id ){
        this.title = title;
        this.id = id;
    }

}
