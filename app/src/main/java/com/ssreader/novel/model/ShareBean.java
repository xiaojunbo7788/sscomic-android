package com.ssreader.novel.model;

import java.util.List;

public class ShareBean {
    public String title;
    public String desc;
    public String link;
    public String imgUrl;
    public String invite_code;
    public String bind_user;
    public String bind_code;
    public InviteInfo inviteInfo;
    public static class InviteInfo {
        public String count;
        public List<InviteUserItem>userList;
    }

    public static class InviteUserItem {
        private String nickname;
        private String created_at;
        private String userHead;

        public String getUserHead() {
            return userHead;
        }

        public void setUserHead(String userHead) {
            this.userHead = userHead;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }


}
