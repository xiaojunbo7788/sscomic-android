package com.ssreader.novel.model;

import java.util.Arrays;
import java.util.List;

public class TaskCenter {

    private List<TaskCenter2> task_menu;
    public Sign_info sign_info;

    public static class Sign_info {
        @Override
        public String toString() {
            return "Sign_info{" +
                    "sign_days=" + sign_days +
                    ", max_award=" + max_award +
                    ", sign_status=" + sign_status +
                    ", unit='" + unit + '\'' +
                    ", sign_rules=" + Arrays.toString(sign_rules) +
                    '}';
        }

        public int getSign_days() {
            return sign_days;
        }

        public void setSign_days(int sign_days) {
            this.sign_days = sign_days;
        }

        public int getMax_award() {
            return max_award;
        }

        public void setMax_award(int max_award) {
            this.max_award = max_award;
        }

        public int getSign_status() {
            return sign_status;
        }

        public void setSign_status(int sign_status) {
            this.sign_status = sign_status;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String[] getSign_rules() {
            return sign_rules;
        }

        public void setSign_rules(String[] sign_rules) {
            this.sign_rules = sign_rules;
        }

        public int sign_days;//连续签到的天数
        public int max_award;//获取的奖励
        public int sign_status;//签到状态0未签到 1 已签到
        public String unit;//
        public String sign_rules[];
    }

    public List<TaskCenter2> getTask_menu() {
        return task_menu;
    }

    public void setTask_menu(List<TaskCenter2> task_menu) {
        this.task_menu = task_menu;
    }

    public static class TaskCenter2 {

        private String task_title;
        private List<Taskcenter> task_list;

        public String getTask_title() {
            return task_title;
        }

        public void setTask_title(String task_title) {
            this.task_title = task_title;
        }

        public List<Taskcenter> getTask_list() {
            return task_list;
        }

        public void setTask_list(List<Taskcenter> task_list) {
            this.task_list = task_list;
        }

        public static class Taskcenter {

            private String task_award;
            private String task_desc;
            private String task_id;
            private String task_action;
            private String task_label;
            private int task_state; //完成状态0未完成1已完成

            @Override
            public String toString() {
                return "Taskcenter{" +
                        "task_award='" + task_award + '\'' +
                        ", task_desc='" + task_desc + '\'' +
                        ", task_id='" + task_id + '\'' +
                        ", task_action='" + task_action + '\'' +
                        ", task_label='" + task_label + '\'' +
                        ", task_state=" + task_state +
                        '}';
            }

            public String getTask_action() {
                return task_action;
            }

            public void setTask_action(String task_action) {
                this.task_action = task_action;
            }

            public String getTask_award() {
                return task_award;
            }

            public void setTask_award(String task_award) {
                this.task_award = task_award;
            }

            public String getTask_desc() {
                return task_desc;
            }

            public void setTask_desc(String task_desc) {
                this.task_desc = task_desc;
            }

            public String getTask_id() {
                return task_id;
            }

            public void setTask_id(String task_id) {
                this.task_id = task_id;
            }

            public String getTask_label() {
                return task_label;
            }

            public void setTask_label(String task_label) {
                this.task_label = task_label;
            }

            public int getTask_state() {
                return task_state;
            }

            public void setTask_state(int task_state) {
                this.task_state = task_state;
            }
        }
    }
}
