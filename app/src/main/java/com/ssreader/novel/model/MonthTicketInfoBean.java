package com.ssreader.novel.model;

public class MonthTicketInfoBean {
    public long book_id;
    public int user_remain;//用户月票余额
    public String name;
    public String cover;
    public String current_month_get; //本月投票
    public String last_distance;//距离上一名投票数
    public String rank_tips;//排行名次
    public String ticket_rule; //月票规则
    public int can_vote;//是否可投票 1可投 0不可投
    public String monthly_tips;  //本月投票说明

}
