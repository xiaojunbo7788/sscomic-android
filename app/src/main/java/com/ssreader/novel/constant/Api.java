package com.ssreader.novel.constant;

public class Api {

    //新版启动接口
    public static final String check_setting = "/service/check-setting";

    public static final String read_log = "/user/read-log";
    // 删除阅读历史
    public static final String del_read_log = "/user/del-read-log";
    // 新增阅读历史
    public static final String add_read_log = "/user/add-read-log";
    // 分享成功增加金币
    public static final String ShareAddGold = "/user/share-reward";

    public static final String APP_SHARE = "/user/app-share";

    public static final String task_read = "/user/task-read";

    public static final String mBookStoreUrl = "/book/store";
    // 注销
    public static final String CANCELACCOUNT = "/user/cancel-account";
    // 作品详情url
    public static final String mBookInfoUrl = "/book/info";
    // 关于我们
    public static final String aBoutUs = "/service/about";
    // 任务中心
    public static final String taskcenter = "/task/center";
    // 签到
    public static final String sIgninhttp = "/user/sign";
    // 排行榜作品列表
    public static final String mRankListUrl = "/rank/book-list";
    // 完本作品列表
    public static final String mFinishUrl = "/book/finish";
    // 分类详情首页
    public static final String mCategoryIndexUrl = "/book/category-index";
    // 限免作品列表
    public static final String mFreeTimeUrl = "/book/free";
    // 作品章节目录
    public static final String mChapterCatalogUrl = "/chapter/new-catalog";
    // 旧作品章节目录
    public static final String mChapterCatalogUrl_old = "/chapter/catalog";
    // 作品评论列表
    public static final String mCommentListUrl = "/comment/list";
    // 发布作品评论
    public static final String mCommentPostUrl = "/comment/post";

    // 发现页接口
    public static final String mDiscoveryUrl = "/book/new-featured";

    // 个人中心首页接口
    public static final String mUserCenterUrl = "/user/index";

    // 发送短信获取验证码
    public static final String mMessageUrl = "/message/send";

    // 手机号登录
    public static final String mMobileLoginUrl = "/user/mobile-login";

    // 设备号登录
    public static final String VLOGIN_device = "/user/device-login";

    // 微信登录
    public static final String login_wechat = "/user/app-login-wechat";

    // 微信绑定
    public static final String bind_wechat = "/user/app-bind-wechat";

    // QQ登录
    public static final String login_qq = "/user/qq-login";

    // QQ绑定
    public static final String bind_qq = "/user/bind-qq";

    // 包月购买页
    public static final String mPayBaoyueCenterUrl = "/pay/baoyue-center";

    // 包月库作品库首页
    public static final String mBaoyueIndexUrl = "/book/baoyue-index";

    // 推荐更多
    public static final String mRecommendUrl = "/book/recommend";

    public static final String free_time = "/book/free-time";

    // 添加书架作品
    public static final String mBookAddCollectUrl = "/user/collect-add";

    // 搜索页接口
    public static final String mSearchUrl = "/book/search";

    // 搜索首页
    public static final String mSearchIndexUrl = "/book/search-index";

    // 书架作品
    public static final String mBookDelCollectUrl = "/user/collect-del";

    // 用户书架
    public static final String mBookCollectUrl = "/user/book-collect";

    // 排行榜首页
    public static final String mRankUrl = "/rank/index";

    // 章节下载
    public static final String mChapterDownUrl = "/chapter/down";

    public static final String auto_sub = "/user/auto-sub";

    public static final String chapter_text = "/chapter/text";

    // 消费充值记录
    public static final String mPayGoldDetailUrl = "/pay/gold-detail";

    // 查询用户个人资料
    public static final String USER_DATA = "/user/data";

    // 用户设置头像
    public static final String mUserSetAvatarUrl = "/user/set-avatar";

    // 修改昵称
    public static final String mUserSetNicknameUrl = "/user/set-nickname";

    // 修改性别
    public static final String mUserSetGender = "/user/set-gender";

    // 绑定手机号
    public static final String mUserBindPhoneUrl = "/user/bind-mobile";

    // 充值页面
    public static final String mPayRechargeCenterUrl = "/pay/center";

    // 微信下单接口
    public static final String mWXPayUrl = "/pay/wxpay";

    // 支付宝下单接口
    public static final String mAlipayUrl = "/pay/alipay";

    // 更新deviceId接口
    public static final String mSyncDeviceIdUrl = "/user/sync-device";

    // 章节购买
    public static final String mChapterBuy = "/chapter/buy";

    // 章节购买预览
    public static final String mChapterBuyIndex = "/chapter/buy-index";

    public static final String start_recommend = "/user/start-recommend";

    // 换一换
    public static final String book_refresh = "/book/refresh";

    // 漫画书架
    public static final String COMIC_SHELF = "/fav/index";

    // 漫画新增书架
    public static final String COMIC_SHELF_ADD = "/fav/add";

    // 漫画删除书架
    public static final String COMIC_SHELF_DEL = "/fav/del";

    // 漫画书城
    public static final String COMIC_home_stock = "/comic/home-stock";

    // 漫画换一换
    public static final String COMIC_home_refresh = "/comic/refresh";

    // 漫画发现
    public static final String COMIC_featured = "/comic/featured";

    // 漫画详情
    public static final String COMIC_info = "/comic/info";

    // 漫画目录
    public static final String COMIC_catalog = "/comic/catalog";

    // 漫画章节
    public static final String COMIC_chapter = "/comic/chapter";

    // 漫画吐槽
    public static final String COMIC_tucao = "/comic/tucao";

    // 漫画分类
    public static final String COMIC_list = "/comic/list";

    // 漫画排行首页
    public static final String COMIC_rank_index = "/rank/comic-index";

    /**
     * 漫画 排行 列表
     */
    public static final String COMIC_rank_list = "/rank/comic-list";

    // 漫画搜索列表
    public static final String COMIC_search = "/comic/search";

    // 漫画搜索首页
    public static final String COMIC_search_index = "/comic/search-index";

    // 漫画评论类表
    public static final String COMIC_comment_list = "/comic/comment-list";

    // WODE漫画评论类表
    public static final String COMIC_comment_list_MY = "/user/comic-comments";

    // 我的评论
    public static final String mUserCommentsUrl = "/user/comments";

    /**
     * 漫画发评论
     */
    public static final String COMIC_sendcomment = "/comic/post";

    // 漫画下载选项
    public static final String COMIC_down_option = "/comic/down-option";

    // 漫画下载
    public static final String COMIC_down = "/comic/down";

    // 漫画购买预览
    public static final String COMIC_buy_index = "/comic-chapter/buy-index";

    // 漫画购买
    public static final String COMIC_buy_buy = "/comic-chapter/buy";

    // 漫画 获取阅读历史
    public static final String COMIC_read_log = "/user/comic-read-log";

    // 漫画删除阅读历史
    public static final String COMIC_read_log_del = "/user/del-comic-read-log";

    // 漫画新增阅读历史
    public static final String COMIC_read_log_add = "/user/add-comic-read-log";

    // 漫画限免
    public static final String COMIC_free_time = "/comic/free";

    // 漫画 完本
    public static final String COMIC_finish = "/comic/finish";

    // 漫画 会员列表
    public static final String COMIC_baoyue_list = "/comic/baoyue-list";

    // 漫画 查看更多
    public static final String COMIC_recommend = "/comic/recommend";

    // 小说下载
    public static final String DOWN_OPTION = "/chapter/down-option";

    // 小说下载选项
    public static final String DOWN_URL = "/chapter/down-url";

    // 广告控制接口
    public static final String ADVERT_INFO = "/advert/info";

    //广告点击
    public static final String ACTOR_ADVERT_CLICK = "/advert/click";
    // 通知开关查询
    public static final String push_state = "/user/push-state";

    // 通知开关查询
    public static final String push_state_up = "/user/update-push-state";

    //帮助问题列表
    public static final String FaceBcakAnswer = "/answer/list";

    //帮助问题列表
    public static final String PostFaceBcakContent = "/answer/post-feedback";

    //用户反馈列表
    public static final String AnswerFaceBcakList = "/answer/feedback-list";

    //上传图片/upload/image
    public static final String UPLoadImage = "/upload/image";

    //删除图片
    public static final String DeleteImage = "/upload/delete-image";

    // 有声书架
    public static final String AUDIO_FAV_USER_FAV = "/audio-fav/user-fav";
    // 有声新增书架
    public static final String AUDIO_FAV_ADD = "/audio-fav/add";
    // 有声删除书架
    public static final String AUDIO_FAV_DEL = "/audio-fav/del";
    // 有声详情
    public static final String AUDIO_INFO = "/audio/info";
    // 有声目录
    public static final String AUDIO_CATALOG = "/audio/catalog";
    // 有声章节详情
    public static final String AUDIO_CHAPTER_INFO = "/audio-chapter/info";
    // 有声推荐
    public static final String AUDIO_INFO_RECOMMEND = "/audio/info-recommend";
    //听书首页
    public static final String AUDIO_INDEX = "/audio/index";
    //听书榜单
    public static final String AUDIO_TOP_INDEX = "/audio/top-index";
    //榜单详情
    public static final String AUDIO_TOP_List = "/audio/top-list";
    //免费
    public static final String AUDIO_FREE = "/audio/free";
    //完结
    public static final String AUDIO_FINISHED = "/audio/finished";
    //包月列表
    public static final String AUDIO_BAOYUE_LIST = "/audio/baoyue-list";
    //分类
    public static final String AUDIO_CATEGORY_INDEX = "/audio/category-index";
    //听书换一换
    public static final String AUDIO_REFRESH = "/audio/refresh";
    //听书更多
    public static final String AUDIO_RECOMMEND = "/audio/recommend";
    //听书限时免费
    public static final String AUDIO_FREE_TIME = "/audio/free-time";
    //听书阅读历史
    public static final String AUDIO_READ_LOG = "/user/audio-read-log";
    //听书阅读历史删除
    public static final String AUDIO_DEL_READ_LOG = "/user/audio-del-read-log";
    //听书我的评论
    public static final String AUDIO_COMMENT = "/user/audio-comment";
    //听书发现
    public static final String AUDIO_NEW_FEATURED = "/audio/new-featured";
    //增加历史记录接口
    public static final String AUDIO_ADD_READ_LOG = "/audio/add-read-log";
    //章节列表
    public static final String AUDIO_CHAPTER_COMMENT_LIST = "/audio-chapter/comment-list";
    //章节评论
    public static final String AUDIO_CHAPTER_COMMENT_POST = "/audio-chapter/comment-post";
    //听书下载
    public static final String audio_chapter_down = "/audio-chapter/down";
    //听书购买选项
    public static final String audio_chapter_buyoption = "/audio-chapter/buy-option";
    //听书购买
    public static final String audio_chapter_buy = "/audio-chapter/buy";
    // AI推荐
    public static final String AUDIO_AI_RECOMMEND = "/book/detail";
    // 有声推荐
    public static final String AUDIO_RECOMMEND_COMMENT = "/audio-chapter/detail";
    //有声搜索列表
    public static final String AUDIO_search = "/audio/search";
    // 有声搜索首页
    public static final String AUDIO_search_index = "/audio/search-index";

    // 评论详情
    public static final String COMMENT_INFO = "/user/comment-info";

    // 会员中心接口
    public static final String MEMBER_CENTER = "/user/vip-center";

    // 漫画弹幕
    public static final String COMIC_BARRAGE = "/comic/barrage";

    //打赏礼物给作品
    public static final String REWARD_GIFT_SEND = "/reward/gift-send";
    //打赏记录
    public static final String REWARD_GIFT_LOG = "/reward/gift-log";
    //打赏首页
    public static final String REWARD_LIST = "/reward/gift-option";
    //投月票
    public static final String REWARD_TICKET_VOTE = "/reward/ticket-vote";
    //月票记录
    public static final String REWARD_TICKET_LOG = "/reward/ticket-log";
    //月票首页
    public static final String REWARD_TICKET_OPTION = "/reward/ticket-option";
    // 新增书籍末尾推荐
    public static final String READ_END_RECOMMEND = "/book/end-of-recommend";
    // 书籍页猜你喜欢
    public static final String BOOK_GUESS_LIKE = "/book/guess-like";
    // 书架页推荐书籍
    public static final String SHELF_RECOMMEND = "/novel/shelf-recommend";
    //作品分享接口
    public static final String NOVEL_SHARE = "/novel/share-novel";
}
