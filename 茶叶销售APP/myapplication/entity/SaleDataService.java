package com.example.myapplication.entity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SaleDataService {
    public static List<SaleProduct> getProducts(){
        List<SaleProduct> saleProduct = new ArrayList<>();
        saleProduct.add(new SaleProduct(1030,"汉唐清茗霍山黄茶焖黄轻发酵茶叶特级2024新茶安徽大别山特产150g",69,59, R.drawable.huoshanhuangcha));
        saleProduct.add(new SaleProduct(1031, "六堡茶梧州广西特级熟茶2016年陈黑茶金花六宝茶叶礼盒送长辈朋友",150,138, R.drawable.liubaocha));
        saleProduct.add(new SaleProduct(1032,"黑茶湖南正宗安化黑茶天尖茶陈年荒野高档礼盒装野生安华天尖茶叶",388,298, R.drawable.tianjiancha));
        saleProduct.add(new SaleProduct(1033,"品茗桂香 高山白茶福鼎大白茶明前花香白牡丹饼2023整箱48饼",1888,1780, R.drawable.baicha));
        saleProduct.add(new SaleProduct(1034,"芋泥青团奶绿",12,10, R.drawable.qingtuan));
        saleProduct.add(new SaleProduct(1035,"布蕾脆脆奶芙",16,14, R.drawable.bulei));
        saleProduct.add(new SaleProduct(1036,"伯牙绝玄",20,16, R.drawable.boya));
        saleProduct.add(new SaleProduct(1037,"奈雪果茶",68,47, R.drawable.naixue));

        return saleProduct;
    }
}
