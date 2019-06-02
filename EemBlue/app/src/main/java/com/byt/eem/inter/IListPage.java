package com.byt.eem.inter;

import android.support.v7.widget.RecyclerView;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

public interface IListPage {

     void setupRecyclerView(RecyclerView recyclerView);

     String getRequestUrl(int pageIndex);

     RequestParams getRequestParams();

     Class getResultClass();

     void clearList();

     <T> void addItems(ArrayList<T> data);

     void notifyAdapter();
}
