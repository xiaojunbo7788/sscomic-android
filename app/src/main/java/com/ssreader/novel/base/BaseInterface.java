package com.ssreader.novel.base;

public interface  BaseInterface {
   
    /**
     * 配置布局文件
     */
     int initContentView();

    /**
     * 初始化各个视图
     */
     void initView();

    /**
     * 发起网络请求
     */
     void initData();

    /**
     * 处理网络请求数据
     */
     void initInfo(String json);


    /**
     * 网络加载失败
     */
     void errorInfo(String json) ;
}
