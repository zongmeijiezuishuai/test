package com.example.idea_test.onenet;

import java.util.List;

public class Datastreams {
    //获取数据流
    private List<Datapoints> datapoints;
    private String id;
    public void setDatapoints(List<Datapoints> datapoints){this.datapoints=datapoints;}
    public List<Datapoints> getDatapoints(){return datapoints;}
    //public String getId(){return  id;}
}
