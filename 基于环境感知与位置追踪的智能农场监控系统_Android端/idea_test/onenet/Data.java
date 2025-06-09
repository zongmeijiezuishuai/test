package com.example.idea_test.onenet;

import java.util.List;

public class Data {

    //利用List存放获取的数据流
    private int count;
    private List<Datastreams> dataatreams;
    public void setCount(int count){this.count=count;}
    public int getCount(){
        return count;
    }
    public List<Datastreams>getDatastreams(){return dataatreams;}
}
