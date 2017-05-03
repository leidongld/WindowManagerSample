package com.example.leidong.windowmanagersample.database;

/**
 * Created by leidong on 2017/5/3.
 */

public class ItemColumn {
    public ItemColumn(){

    }

    //列名
    public static final String _ID = "id";
    public static final String ITEMNAME = "itemname";
    public static final String ITEMDETAIL = "itemdetail";

    //索引
    public static final int ID_COLUMN = 0;
    public static final int ITEMNAME_COLUMN = 1;
    public static final int ITEMDETAIL_COLUMN = 2;

    //查找
    public static final String[] ITEM_INFOS =
            {
                    _ID,
                    ITEMNAME,
                    ITEMDETAIL
            };
}
