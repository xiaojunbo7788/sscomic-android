package com.ssreader.novel.model;

import java.util.List;

public class PayBeen {

    public String title;
    public long remain;
    public String goldRemain;
    public long silverRemain;
    public int thirdOn;
    public String tips;
    public UnitTagBean unit_tag;
    public List<ItemsBean> items;
    public List<String> about;
    public String[] pay_tips_txt;
    public List<ItemsBean.PalChannelBean> pal_channel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRemain() {
        return remain;
    }

    public void setRemain(long remain) {
        this.remain = remain;
    }

    public String getGoldRemain() {
        return goldRemain;
    }

    public void setGoldRemain(String goldRemain) {
        this.goldRemain = goldRemain;
    }

    public long getSilverRemain() {
        return silverRemain;
    }

    public void setSilverRemain(long silverRemain) {
        this.silverRemain = silverRemain;
    }

    public int getThirdOn() {
        return thirdOn;
    }

    public void setThirdOn(int thirdOn) {
        this.thirdOn = thirdOn;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public UnitTagBean getUnit_tag() {
        return unit_tag;
    }

    public void setUnit_tag(UnitTagBean unit_tag) {
        this.unit_tag = unit_tag;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public List<String> getAbout() {
        return about;
    }

    public void setAbout(List<String> about) {
        this.about = about;
    }

    public String[] getPay_tips_txt() {
        return pay_tips_txt;
    }

    public void setPay_tips_txt(String[] pay_tips_txt) {
        this.pay_tips_txt = pay_tips_txt;
    }

    public class UnitTagBean {

        private String currencyUnit;
        private String subUnit;

        public String getCurrencyUnit() {
            return currencyUnit;
        }

        public void setCurrencyUnit(String currencyUnit) {
            this.currencyUnit = currencyUnit;
        }

        public String getSubUnit() {
            return subUnit;
        }

        public void setSubUnit(String subUnit) {
            this.subUnit = subUnit;
        }
    }

    public class ItemsBean {
        /**
         * goods_id : 34
         * title : 100书币
         * price : 1
         * flag : 送1%
         * note : +1书券
         * tag : []
         * limit_num :
         * limit_style :
         * giving_price : 多送 <span>0</span> 元
         * apple_id :
         * pal_channel : [{"icon":"","title":"支付宝","channel_id":1,"channel_code":"alipay","pay_type":1,"gateway":""},{"icon":"","title":"微信","channel_id":2,"channel_code":"wechat","pay_type":1,"gateway":""},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信H5","channel_id":6,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"微信扫码","channel_id":5,"channel_code":"zysda","pay_type":2,"gateway":"http://www.baidu.com"},{"icon":"","title":"京东支付","channel_id":6,"channel_code":"xfzfpay","pay_type":2,"gateway":""},{"icon":"","title":"支付宝扫码","channel_id":7,"channel_code":"xfzfpay","pay_type":2,"gateway":""}]
         */
        public String sub_title;
        public boolean choose;
        public String goods_id;
        public String title;
        private String price;
        private String fat_price;
        private String flag;
        private String note;
        private String limit_num;
        private String limit_style;
        private String giving_price;
        private String apple_id;
        public List<BaseTag> tag;
        public List<PalChannelBean> pal_channel;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public String getFat_price() {
            return fat_price;
        }

        public void setFat_price(String fat_price) {
            this.fat_price = fat_price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getLimit_num() {
            return limit_num;
        }

        public void setLimit_num(String limit_num) {
            this.limit_num = limit_num;
        }

        public String getLimit_style() {
            return limit_style;
        }

        public void setLimit_style(String limit_style) {
            this.limit_style = limit_style;
        }

        public String getGiving_price() {
            return giving_price;
        }

        public void setGiving_price(String giving_price) {
            this.giving_price = giving_price;
        }

        public String getApple_id() {
            return apple_id;
        }

        public void setApple_id(String apple_id) {
            this.apple_id = apple_id;
        }

        public List<BaseTag> getTag() {
            return tag;
        }

        public void setTag(List<BaseTag> tag) {
            this.tag = tag;
        }

        public List<PalChannelBean> getPal_channel() {
            return pal_channel;
        }

        public void setPal_channel(List<PalChannelBean> pal_channel) {
            this.pal_channel = pal_channel;
        }

        public class PalChannelBean {
            @Override
            public String toString() {
                return "PalChannelBean{" +
                        "choose=" + choose +
                        ", icon='" + icon + '\'' +
                        ", title='" + title + '\'' +
                        ", channel_id=" + channel_id +
                        ", channel_code='" + channel_code + '\'' +
                        ", pay_type=" + pay_type +
                        ", gateway='" + gateway + '\'' +
                        '}';
            }

            /**
             * icon :
             * title : 支付宝
             * channel_id : 1
             * channel_code : alipay
             * pay_type : 1
             * gateway :
             */
            public boolean choose;
            private String icon;
            private String title;
            private int channel_id;
            private String channel_code;
            private int pay_type;
            private String gateway;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getChannel_id() {
                return channel_id;
            }

            public void setChannel_id(int channel_id) {
                this.channel_id = channel_id;
            }

            public String getChannel_code() {
                return channel_code;
            }

            public void setChannel_code(String channel_code) {
                this.channel_code = channel_code;
            }

            public int getPay_type() {
                return pay_type;
            }

            public void setPay_type(int pay_type) {
                this.pay_type = pay_type;
            }

            public String getGateway() {
                return gateway;
            }

            public void setGateway(String gateway) {
                this.gateway = gateway;
            }
        }
    }
}
