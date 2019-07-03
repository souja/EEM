package com.byt.eemblue.model;

import com.souja.lib.models.BaseModel;

public class PageModel extends BaseModel {
    public int pageIndex;
    int pageSize = 10;

    public PageModel(int pageIndex) {
        this.pageIndex = pageIndex;
    }

}
