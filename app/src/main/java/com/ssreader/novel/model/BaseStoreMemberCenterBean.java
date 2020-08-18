package com.ssreader.novel.model;

import java.util.List;

public class BaseStoreMemberCenterBean {

    private BaoyueUser user;

    private List<PublicIntent> banner;

    private List<MemberPrivilege> privilege;

    private List<BaseLabelBean> label;

    public BaoyueUser getUser() {
        return user;
    }

    public void setUser(BaoyueUser user) {
        this.user = user;
    }

    public List<PublicIntent> getBanner() {
        return banner;
    }

    public void setBanner(List<PublicIntent> banner) {
        this.banner = banner;
    }

    public List<MemberPrivilege> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<MemberPrivilege> privilege) {
        this.privilege = privilege;
    }

    public List<BaseLabelBean> getLabel() {
        return label;
    }

    public void setLabel(List<BaseLabelBean> label) {
        this.label = label;
    }

    public class MemberPrivilege{

        private String label;

        private String icon;

        private String action;

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
    }
}
