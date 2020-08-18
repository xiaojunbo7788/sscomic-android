package com.ssreader.novel.model;

import java.util.List;

public class VipPayBeen {

    /**
     * user : {"nickname":"泼墨泪成画","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erKMPia0icZUADCRc7ZQj8YUTpiaucmDZnPoHsbRic5sID3zxZaTwUlh2UmcsewFicl8C52V5R2TOarZVg/132","baoyue_status":0,"start_time":0,"end_time":0,"display_date":"未开通会员","expiry_date":"未开通会员","vip_desc":"开通会员免费全站畅读"}
     * thirdOn : 0
     * list : [{"goods_id":"18","title":"3个月VIP","price":100,"apple_id":"","pal_channel":[{"icon":"","title":"支付宝","channel_id":1,"channel_code":"alipay","pay_type":1,"gateway":""},{"icon":"","title":"微信","channel_id":2,"channel_code":"wechat","pay_type":1,"gateway":""},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信H5","channel_id":6,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"京东支付","channel_id":6,"channel_code":"xfzfpay","pay_type":2,"gateway":""},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"xfzfpay","pay_type":2,"gateway":""}],"note":"3个月一直无限看所有漫画！","tag":[{"tab":"热门","color":"#ff0000"},{"tab":"推荐","color":"#ff0000"}]},{"goods_id":"30","title":"1个月VIP","price":50,"apple_id":"","pal_channel":[{"icon":"","title":"支付宝","channel_id":1,"channel_code":"alipay","pay_type":1,"gateway":""},{"icon":"","title":"微信","channel_id":2,"channel_code":"wechat","pay_type":1,"gateway":""},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信H5","channel_id":6,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"京东支付","channel_id":6,"channel_code":"xfzfpay","pay_type":2,"gateway":""},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"xfzfpay","pay_type":2,"gateway":""}],"note":"1个月免费看所有漫画！","tag":[]},{"goods_id":"31","title":"半年超长VIP","price":200,"apple_id":"","pal_channel":[{"icon":"","title":"支付宝","channel_id":1,"channel_code":"alipay","pay_type":1,"gateway":""},{"icon":"","title":"微信","channel_id":2,"channel_code":"wechat","pay_type":1,"gateway":""},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信H5","channel_id":6,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"京东支付","channel_id":6,"channel_code":"xfzfpay","pay_type":2,"gateway":""},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"xfzfpay","pay_type":2,"gateway":""}],"note":"6个月内所有小说、漫画看爽爆！","tag":[]}]
     * privilege : [{"label":"会员畅读","icon":"http://open_bd.budingapp.cc/images/app_icon/baoyue-free.png","action":"","url":"","desc":""},{"label":"专属活动","icon":"http://open_bd.budingapp.cc/images/app_icon/baoyue-huodong.png","action":"","url":"","desc":""},{"label":"双倍奖励","icon":"http://open_bd.budingapp.cc/images/app_icon/baoyue-chengzhang.png","action":"","url":"","desc":""},{"label":"身份铭牌","icon":"http://open_bd.budingapp.cc/images/app_icon/baoyue-shenfen.png","action":"","url":"","desc":""}]
     * about : ["温馨提示","1.若开通免广告后10分钟内标识仍然没有点亮，请退出账号后重新登录进行尝试。","2.开通免广告会员默认您已同意布丁漫画+小说APP小说《用户协议》、《隐私协议》及《会员服务协议》。","如有其它疑问，请联系客服。"]
     * pay_set : [{"pay_key":"1","name":"微信支付","icon":"http://img3.zibenplus.com/cover/0b70399f90ebe483ba7f81d01e5afc7e.png"},{"pay_key":"2","name":"支付宝支付","icon":"http://img3.zibenplus.com/cover/75fec153165183610f7a605e06d2e3e3.png"},{"pay_key":"3","name":"米花支付","icon":"http://img3.zibenplus.com/cover/e85cc23589276d92162fa5b6f8bf2a32.png"}]
     */

    private UserBean user;
    private int thirdOn;
    private List<PayBeen.ItemsBean> list;
    private List<AcquirePrivilegeItem> privilege;
    private List<String> about;
   // private java.util.List<PaySetBean> pay_set;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getThirdOn() {
        return thirdOn;
    }

    public void setThirdOn(int thirdOn) {
        this.thirdOn = thirdOn;
    }

    public List<PayBeen.ItemsBean> getList() {
        return list;
    }

    public void setList(List<PayBeen.ItemsBean> list) {
        this.list = list;
    }

    public List<AcquirePrivilegeItem> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<AcquirePrivilegeItem> privilege) {
        this.privilege = privilege;
    }

    public List<String> getAbout() {
        return about;
    }

    public void setAbout(List<String> about) {
        this.about = about;
    }

    public  class UserBean {
        /**
         * nickname : 泼墨泪成画
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erKMPia0icZUADCRc7ZQj8YUTpiaucmDZnPoHsbRic5sID3zxZaTwUlh2UmcsewFicl8C52V5R2TOarZVg/132
         * baoyue_status : 0
         * start_time : 0
         * end_time : 0
         * display_date : 未开通会员
         * expiry_date : 未开通会员
         * vip_desc : 开通会员免费全站畅读
         */

        public String nickname;
        public String avatar;
        public int baoyue_status;
        public String display_date;
        public String expiry_date;
        public String vip_desc;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getBaoyue_status() {
            return baoyue_status;
        }

        public void setBaoyue_status(int baoyue_status) {
            this.baoyue_status = baoyue_status;
        }

        public String getDisplay_date() {
            return display_date;
        }

        public void setDisplay_date(String display_date) {
            this.display_date = display_date;
        }

        public String getExpiry_date() {
            return expiry_date;
        }

        public void setExpiry_date(String expiry_date) {
            this.expiry_date = expiry_date;
        }

        public String getVip_desc() {
            return vip_desc;
        }

        public void setVip_desc(String vip_desc) {
            this.vip_desc = vip_desc;
        }
    }
}
