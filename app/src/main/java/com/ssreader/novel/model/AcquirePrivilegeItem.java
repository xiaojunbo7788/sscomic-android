package com.ssreader.novel.model;

/**
 * Created by  on 2018/8/12.
 */
public class AcquirePrivilegeItem {

    public AcquirePrivilegeItem(int localIcon, String label) {
        this.localIcon = localIcon;
        this.label = label;
    }

    /**
     * label : 成长加速
     * icon : http://hehuan.oss-cn-beijing.aliyuncs.com//icon/3.png
     * action :
     * url :
     * desc :
     */

    public int localIcon;
    public String label;
    public String icon;
    public String action;
    public String url;
    public String desc;

    public int getLocalIcon() {
        return localIcon;
    }

    public void setLocalIcon(int localIcon) {
        this.localIcon = localIcon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
