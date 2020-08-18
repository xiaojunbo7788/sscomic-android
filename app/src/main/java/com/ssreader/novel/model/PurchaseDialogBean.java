package com.ssreader.novel.model;

import java.util.List;

public class PurchaseDialogBean {

    private BaseInfoBean base_info;
    private List<PurchaseItem> buy_option;

    public BaseInfoBean getBase_info() {
        return base_info;
    }

    public void setBase_info(BaseInfoBean base_info) {
        this.base_info = base_info;
    }

    public List<PurchaseItem> getBuy_option() {
        return buy_option;
    }

    public void setBuy_option(List<PurchaseItem> buy_option) {
        this.buy_option = buy_option;
    }

    public static class BaseInfoBean {

        private long remain;
        private long gold_remain;
        private long silver_remain;
        private String unit;
        private String subUnit;
        private int chapter_id;
        private int auto_sub;

        public long getRemain() {
            return remain;
        }

        public void setRemain(long remain) {
            this.remain = remain;
        }

        public long getGold_remain() {
            return gold_remain;
        }

        public void setGold_remain(long gold_remain) {
            this.gold_remain = gold_remain;
        }

        public long getSilver_remain() {
            return silver_remain;
        }

        public void setSilver_remain(long silver_remain) {
            this.silver_remain = silver_remain;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getSubUnit() {
            return subUnit;
        }

        public void setSubUnit(String subUnit) {
            this.subUnit = subUnit;
        }

        public int getChapter_id() {
            return chapter_id;
        }

        public void setChapter_id(int chapter_id) {
            this.chapter_id = chapter_id;
        }

        public int getAuto_sub() {
            return auto_sub;
        }

        public void setAuto_sub(int auto_sub) {
            this.auto_sub = auto_sub;
        }
    }

    public static class BuyOptionBean {
        /**
         * label : 购买本章
         * total_price : 30
         * original_price : 30
         * buy_num : 1
         * discount :
         * actual_cost : {"gold_cost":0,"silver_cost":30}
         * original_cost : {"gold_cost":0,"silver_cost":30}
         */

        private String label;
        private int total_price;
        private int original_price;
        private int buy_num;
        private String discount;
        private ActualCostBean actual_cost;
        private OriginalCostBean original_cost;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(int original_price) {
            this.original_price = original_price;
        }

        public int getBuy_num() {
            return buy_num;
        }

        public void setBuy_num(int buy_num) {
            this.buy_num = buy_num;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public ActualCostBean getActual_cost() {
            return actual_cost;
        }

        public void setActual_cost(ActualCostBean actual_cost) {
            this.actual_cost = actual_cost;
        }

        public OriginalCostBean getOriginal_cost() {
            return original_cost;
        }

        public void setOriginal_cost(OriginalCostBean original_cost) {
            this.original_cost = original_cost;
        }

        public static class ActualCostBean {
            /**
             * gold_cost : 0
             * silver_cost : 30
             */

            private int gold_cost;
            private int silver_cost;

            public int getGold_cost() {
                return gold_cost;
            }

            public void setGold_cost(int gold_cost) {
                this.gold_cost = gold_cost;
            }

            public int getSilver_cost() {
                return silver_cost;
            }

            public void setSilver_cost(int silver_cost) {
                this.silver_cost = silver_cost;
            }
        }

        public static class OriginalCostBean {
            /**
             * gold_cost : 0
             * silver_cost : 30
             */

            private int gold_cost;
            private int silver_cost;

            public int getGold_cost() {
                return gold_cost;
            }

            public void setGold_cost(int gold_cost) {
                this.gold_cost = gold_cost;
            }

            public int getSilver_cost() {
                return silver_cost;
            }

            public void setSilver_cost(int silver_cost) {
                this.silver_cost = silver_cost;
            }
        }
    }
}
