#被窝阅读接口文档

##通用申明

###接口地址
	http://open.beiwo.com

###签名方法
>将请求参数按照k的升序排序，按照规则拼接后md5生产签名串，sign不参与验签。  
paramsStr = 按 k 的自然排序，按如下格式拼接 a=xxx&b=xxx&c=xxx 值等于空也参与，sign不参与验签，如果值为数组，将数组的值以英文逗号拼接成一个字符串参与签名  
sign = md5(appKey + paramsStr + appSecret)  
iOS appKey: 
Android appKey: 
secretKey:  

###状态码
>&nbsp;&nbsp;&nbsp;&nbsp;0 成功  
103 签名过期  
104 签名无效  
301 未登录 需要跳转登录  
802 金币不足 需要跳转充值页面  
其他 按提示显示错误

###请求方式
>json post  
udid、token、osType、ver、time、sign、sysVer 、procut全局必传

	ver:    客户端版本号  
	osType：系统， 1：iOS，  2：android  
	time：  10位时间戳,注意不同os获取到的时间戳精确度可能不同
	sign：  签名
	appId： 当前app分配的唯一标识id
	udid：  设备唯一标识
	token： 登录授权token，未登录时传空
	sysVer：系统版本
	product： 产品线 1app 2公众号 3小程序
	packageName： 包名
	marketChannel：市场渠道

###检测更新接口 （OK）


>	Request: POST /service/checkver   

>	About:启动时调用，弹窗提示用户更新客户端版本。有普通更新和强制更新2种。
	
	Params: 
	{
		...其他全局必传参数 
	}
>  

    Response:
    {
	    "code": 0, //0:正常, >1:其它错误按msg信息提示
	    "msg": "success",
	    "data": {
	        "update": 2, //0:不提示, 1:提示, 2强制无取消按钮
	        "msg": "有更新哦...", //String弹窗提示文案 
	        "url": "https://wap.baidu.com", //String安卓使用
	        "setting": { //系统常量配置
	            "money_unit": "金币" //资产单位：例：书豆、阅读币等
	        }
	        "start_page": {
                "skip_type": 1, // 1-APP内部跳转，2-外部链接跳转
                "image": "http://dlreader.oss-cn-hangzhou.aliyuncs.com/b85b064a2bf8d34ee8c1539a0beb0865.jpeg?x-oss-process=image%2Fresize%2Cw_1242%2Ch_2208%2Cm_lfit",  // 启动图
                "skip_url": "", // 外部链接地址
                "book_id": 1277 // 书籍ID
            }
	    }
	}
	
	
###过审开关 （OK）


>	Request: POST /service/check-data   

>	About:安卓和iOS是否为过审版本接口
	
	Params: 
	{
		channel: "oppo", //渠道名称
		...其他全局必传参数 
	}
>  

    Response:
    {
	    "code": 0, //0:正常, >1:其它错误按msg信息提示
	    "msg": "success",
	    "data": {
	        "service": 0, //开关状态，0为关，1为开
	    }
	}
	
###关于我们接口 （OK）


>	Request: POST /service/about   

>	
	
	Params: 
	{
		...其他全局必传参数 
	}
>  

    Response:
    {
	    "code": 0,
	    "msg": "success",
	    "data": { //没有值时不需展示该信息项
	        "telphone": "010-12312312312", //联系电话
	        "email": "77676182@qq.com", //联系邮箱
	        "qq": "77676182" //联系QQ
	    }
	}

##用户接口

###手机号登陆[OK]

>	Request:POST	/user/mobile-login

> 

	Params:
	{
		mobile: 13800000000 //手机号
		code: 1234 //验证码
	}
	
>	Response:

	{
	    "code": 0, //105，验证码错误
	    "msg": "success",
	    "data": {
			"uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "sign_days": 2,//int 连续签到天数
		    "sign_status": 1, //int 当天的签到状态 1-已签到 0-未签到
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}
	

###设备号登录
>	Request:POST	/user/device-login

> 

	Params:
	{
		udid, //iOS: udid ,安卓 imei
		其他通用参数
	}
	
>	Response:

	{
	    "code": 0, //105，验证码错误
	    "msg": "success",
	    "data": {
			"uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "sign_days": 2,//int 连续签到天数
		    "sign_status": 1, //int 当天的签到状态 1-已签到 0-未签到
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}


###账号密码登陆[OK]

>	Request:POST	/user/account-login

>  

	Params:
	{
		 user_name: 1234@qq.com, //用户名
	    password: 123456, //密码
	}

>  Response:

	{
	    "code": 0, //300账号不存在，303密码错误
	    "msg": "success",
	    "data": {
			"uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "sign_days": 2,//int 连续签到天数
		    "sign_status": 1, //int 当天的签到状态 1-已签到 0-未签到
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}


###微信绑定[OK]

>	Request:POST	/user/bind-wechat

>  

	Params:
	{
		info, //必填，微信返回的用户json信息串
		token, //必填
	}

> 	Response:

	{
	    "code": 0, //失败为其他code
	    "msg": "success",
	    "data": {
			"uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "sign_days": 2,//int 连续签到天数
		    "sign_status": 1, //int 当天的签到状态 1-已签到 0-未签到
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}
	
###同步device信息
>	Request:POST	/user/sync-device

>  

	Params:
	{
		udid,	//iOS:udid, Android:imei
    	device_id, //阿里推送设备device_id 
		sysVer, //系统版本
		token, //登录授权token，未登录时传空
	}

>	

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {}
	}

###微信登陆[OK]

>	Request:POST	/user/login-wechat

>  

	Params:
	{
		info, //必填，微信返回的用户json信息串
		token,
	}

> 	Response:

	{
	    "code": 0, //失败为其他code
	    "msg": "success",
	    "data": {
			"uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "sign_days": 2,//int 连续签到天数
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}


###小程序登陆[OK]

>	Request:POST	/user/login-mini

>  

	Params:
	{
		code, //必填wx.login返回的code
		info, //必填，微信返回的用户json信息串
	}

> 	Response:

	{
	    "code": 0, //失败为其他code
	    "msg": "success",
	    "data": {
			"uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "sign_days": 2,//int 连续签到天数
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}

	
###用户设置头像[OK]
>	Request: POST /user/set-avatar

>  

	Params:
	{
		avatar, //相片base数据流
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {}
	}

###更新deviceId(启动和登录调用)[OK]
>	Request: POST /user/sync-device

>  

	Params:
	{
		udid, //iOS: udid ,安卓 imei
		deviceId, 阿里sdk的设备号
		token, 可以传空。不登录情况
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {}
	}


###查询用户个人资料
>	Request: POST /user/info

>  

	Params:
	{
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    "mobile": 13800000000, //手机号
		    "user_token": jsldfjlsdjfsldkjf, //token
		    "is_vip": 1, //是否为VIP ，0否 1是
		    "gender": 1, //性别 1女 2男
		    "remain": 100, //余额
		    "sign_days": 2,//int 连续签到天数
		    "unit":"阅读币", //金币单位
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ]
	    }
	}
	
###修改个人昵称[OK]
>	Request: POST /user/set-nickname

>  

	Params:
	{
		nickname, //string 昵称 必填
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {}
	}
	
###绑定手机号[OK]
>	Request: POST /user/bind-mobile

>  

	Params:
	{
		mobile, //string 必传 手机号
		code, //string 必传 验证码
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {}
	}

###获取用户余额
>	Request: POST /user/remain

>  

	Params:
	{
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {
			"remain": 700, //余额
			"nickname": "小b"
		}
	}
	
###用户签到 （OK）
>	Request: POST /user/sign   

>	About: 用户签到接口，支持重复调用，不会重复签到。  
	返回错误码 311时，即用户已签到过，可忽略。  
	用户未登录时，请求接口 不会报错误码，data 返回的是空对象。  

	Params: 
	{
		token,
		...其他全局必传参数 
	}
>  

    Response:
    {
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "sign_days": "已经连续签到3天", //文案， 已经连续签到x天
	        "award": "+109金币", // 金币icon +20
	        "tomorrow_award": "明日获得159金币", //明日签到奖励 +40
	        "book_list": [
	            {
	                "book_id": 39,
	                "name": "鬼妻艳无双",
	                "cover": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/f4efe640928056dc48049e2adbcbe844.jpg?x-oss-process=image%2Fresize%2Cw_143%2Ch_200%2Cm_lfit",
	                "finished": "连载中",
	                "flag": "免费"
	            },
	            {
	                "book_id": 57,
	                "name": "恐怖高校",
	                "cover": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/534194611ceac401aa5b6f92ab3f7b5f.jpg?x-oss-process=image%2Fresize%2Cw_143%2Ch_200%2Cm_lfit",
	                "finished": "连载中",
	                "flag": "热门"
	            },
	            {
	                "book_id": 80,
	                "name": "天荒战纪",
	                "cover": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/ba5b51d3e120b1fab8ea0c4c6a247a0f.jpg?x-oss-process=image%2Fresize%2Cw_143%2Ch_200%2Cm_lfit",
	                "finished": "连载中",
	                "flag": "独家"
	            }
	        ]
	    }
	}
    
    未登录时返回：
    {
	    "code": 0,
	    "msg": "success",
	    "data": {} //签到信息为空
	}
	
###签到中心

>	Request: POST /user/sign-center  

>	

	Params: 
	{
		token,
		...其他全局必传参数 
	}
>  

    Response:
    {
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "sign_info": { //签到基本信息
	            "sign_status": 0, //签到状态 0未签到 1已签到
	            "tomorrow_award": "明天获得59金币" //明天签到获取到的奖励
	        },
	        "sign_calendar": [ //签到日历
	            {
	                "award": "+59", //奖励
	                "sign_status": 0 //签到状态 0未签到 1已签到
	            },
	            {
	                "award": "+79",
	                "sign_status": 0
	            },
	            {
	                "award": "+109",
	                "sign_status": 0
	            },
	            {
	                "award": "+159",
	                "sign_status": 0
	            },
	            {
	                "award": "+209",
	                "sign_status": 0
	            },
	            {
	                "award": "+309",
	                "sign_status": 0
	            },
	            {
	                "award": "+509",
	                "sign_status": 0
	            }
	        ],
	        "sign_rules": [ //签到规则
	            "1、连续签到送金币",
	            "2、连续签到天数越多，赠送越多"
	        ]
	    }
	}
    
    未登录时返回：
    {
	    "code": 0,
	    "msg": "success",
	    "data": {} //签到信息为空
	}  
	
###积分明细
>	Request: POST /user/score-detail

>  

	Params:
	{
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [ //明细列表
	            {
	                "article": "签到增加", //string 明细说明
	                "detail": "+500积分", //string 明细
	                "date": "10月29日 19:08", //string 日期
	                "detail_type": 1 //int 明细类型 1-增加 2-扣减
	            },
	            {
	                "article": "违规刷分",
	                "detail": "-50积分",
	                "date": "10月28日 15:08",
	                "detail_type": 2
	            },
	            {
	                "article": "购买章节",
	                "detail": "+50积分",
	                "date": "10月28日 13:13",
	                "detail_type": 2
	            },
	            {
	                "article": "购买章节",
	                "detail": "+50积分",
	                "date": "10月25日 19:30",
	                "detail_type": 2
	            },
	            {
	                "article": "签到增加",
	                "detail": "+50元宝",
	                "date": "10月20日 19:30",
	                "detail_type": 1
	            }
	        ]
	    }
	}
	
###用户书架[OK]
> 	Request:POST	/user/book-collect


>  

	Params:
	{
		token, // 必传
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"list": [
	    		{
	    			"book_id": 22,
	    			"name": "我是真校草",
	    			"cover": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/9dbcb07321a05ac61cac3a8aec1f0067.jpg",
	    			"author": "周星星",
	    			"total_chapter": 50
	    		},
	    		{
	    			"book_id": 96,
	    			"name": "混沌天兵",
	    			"cover": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/49f1986242e76876ac69c183cddf86de.jpg",
	    			"author": "凌天",
	    			"total_chapter": 100
	    		},
	    		...
	    	],
	    	"announce": [
	    		{
	    			"title": "再来一条公告",
		    		"content": "这是一个公告",
	    		},
	    		{
	    			"title": "第二条公告",
		    		"content": "这是一个公告",
	    		}
	    	]
	    }
	}

###添加书架作品[OK]
> 	Request:POST	/user/collect-add

>  

	Params:
	{
		book_id, //批量添加时以 英文逗号拼接，例： 10086,10010
		token,
	}

>  

	Response:
	{
	    "code": 0, //0成功，客户端只需要提示成功或失败
	    "msg": "success",
	    "data": {}
	}

###删除书架作品[OK]
>	Request:POST	/user/collect-del

>  

	Params:
	{
		book_id, //批量添加时以 英文逗号拼接，例： 10086,10010
		token,
	}

>  

	Response:
	{
	    "code": 0, //0成功，客户端只需要提示成功或失败
	    "msg": "success",
	    "data": {}
	}

	
###自动订阅开关[OK]
>	Request: POST /user/auto-sub

>  

	Params:
	{
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {
			"auto_sub": 0, //自动订阅开关，0：关，1：开
		}
	}

###获取用户openid[OK]
>	Request: POST /user/get-openid

>  

	Params:
	{
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
		"code": 0,
		"msg": "成功",
		"data": {
			"openid":  ofPMI0t2R2aaHR0sAtUhLI1KjlM0 //open id
		}
	}
	
###增加用户阅读记录[OK]
>	Request: POST /user/add-read-log

>  

    Params:
    {
        book_id, 书籍ID
        chapter_id, 章节ID
        token,
        ...其他全局必传参数
    }

>  

    Response:
    {
        "code": 0,
        "msg": "success",
        "data": true
    }
	
###获取用户阅读记录[OK]
>	Request: POST /user/read-log

>  

    Params:
    {
        token,
        ...其他全局必传参数
    }

>  

    Response:
    {
        "code": 0,
        "msg": "success",
        "data": {
            "total_page": 1,
            "current_page": 1,
            "page_size": 10,
            "total_count": 1,
            "list": [
                {
                    "log_id": 776,  // 记录ID
                    "chapter_id": 6252, // 章节ID
                    "name": "我是真校草",   //书籍名称
                    "chapter_title": "第二章 校草的真正含义", // 章节标题
                    "book_id": 22,  // 书籍ID
                    "cover": "http://dlreader.oss-cn-hangzhou.aliyuncs.com/cover/9dbcb07321a05ac61cac3a8aec1f0067.jpg?x-oss-process=image%2Fresize%2Cw_143%2Ch_200%2Cm_lfit",   // 书籍封面图
                    "total_chapter": 64,    // 更新章节
                    "last_chapter_time": "更新于10天前"  // 更新时间
                    "ad_type": 0    // 0-阅读记录，大于0：1-穿山甲，2-百度，3-腾讯
                }
            ]
        }
    }
    
###删除用户阅读记录[OK]
>	Request: POST /user/del-read-log

>  

    Params:
    {
        log_id, 记录id
        token,
        ...其他全局必传参数
    }

>  

    Response:
    {
        "code": 0,
        "msg": "success",
        "data": []
    }
    
###用户分享结果查询接口[OK]
>	Request: POST /user/share-reward

>  

    Params:
    {
        token,
        ...其他全局必传参数
    }

>  

    Response:
    {
        "code": 0,
        "msg": "success",
        "data": {
            "tip": "分享成功+100金币" //提示消息
        }
    }
    
###邮箱登录/注册[OK]
>	Request: POST /user/email-login

>  

    Params:
    {
        email, 邮箱（必传）
        $password,  密码（必传）
        $code,  验证码（非必传）
        ...其他全局必传参数
    }

>  

    {
        "code": 0, //105，验证码错误
        "msg": "success",
        "data": {
            "uid": 10086 //用户唯一id,
            "nickname": "涛子", //昵称
            'mobile': 13800000000, //手机号
            'user_token': jsldfjlsdjfsldkjf, //token
            'is_vip': 1, //是否为VIP ，0否 1是
            'gender': 1, //性别 1女 2男
            'remain': 100, //余额
            "sign_days": 2,//int 连续签到天数
            "sign_status": 1, //int 当天的签到状态 1-已签到 0-未签到
            "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
            "auto_sub": 0, //自动订阅开关，0：关，1：开
            "bind_list": [ //绑定关系列表
                {
                    "label": "手机号", //绑定类型
                    "action": "bindPhone", //绑定操作
                    "status": 0, //绑定状态 0-未绑定 1-已绑定
                    "display": "未绑定" //展示绑定内容
                },
                {
                    "label": "微信",
                    "action": "bindWechat",
                    "status": 1,
                    "display": "好多鱼儿"
                }
            ]
        }
    }


##短信接口
###发送短信[OK]

>	Request:POST	/message/send

>  

	Params:
	{
		mobile: 13800000000
	}


>  Response:

	{
	    "code": 0, //202发送失败
	    "msg": "success",
	    "data": {}
	}

###检验验证码[OK]

>	Request:POST	/message/check

>  

	Params:
	{
		mobile: 13800000000,
		code: 1234,  //验证码4位
	}


>  Response:

	{
	    "code": 0, //105验证错误
	    "msg": "success",
	    "data": {}
	}
	
###发送邮件验证码[OK]
>	Request: POST /message/send-email

>  

    Params:
    {
        email, 邮箱
        ...其他全局必传参数
    }

>  

    Response:
    {
        "code": 0,
        "msg": "success",
        "data": ""
    }

##书城接口

###发现页[OK]

>	Request:POST	/book/featured

> 

	Params:
	{
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"label": [
		        {
		        	"recommend_id": 13,
		        	"label": "包月推荐",
		        	"list": [
						{
			                "book_id": 100, //作品id
			                "name": "爱你一万年", //作品名
			                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg", //封面
			                "finished": "连载中", //连载状态
			                "flag": "" //作品角标
			            },
			            {
			                "book_id": 100,
			                "name": "爱你一万年",
			                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
			                "finished": "已完结",
			                "flag": ""
			            },
			            ...
		        	],
		        },
		        {
		        	"recommend_id": 14,
		        	"label": "限时免费",
		        	"list": [
						{
			                "book_id": 100,
			                "name": "爱你一万年",
			                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
			                "finished": "连载中",
			                "flag": ""
			            },
			            {
			                "book_id": 100,
			                "name": "爱你一万年",
			                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
			                "finished": "已完结",
			                "flag": ""
			            },
			            ...
		        	],
		        },
		        {
		        	"label": "猜你喜欢",
		        	"list": [
						{
			                "book_id": 100,
			                "name": "爱你一万年",
			                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
			                "finished": "连载中",
			                "flag": ""
			            },
			            {
			                "book_id": 100,
			                "name": "爱你一万年",
			                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
			                "finished": "已完结",
			                "flag": ""
			            },
			            ...
		        	],
		        }
			]
		}
	}
	

###书城首页接口[OK]
> 	Request:POST /book/store

>  
	1.男生,女生  
	2.banner  
	3.label
	4.推荐列表(小编推荐， 限时特价...)

>  
	
	Params:
	{
		channel, //频道 1-男频 2-女频
		token,
		...其他全局必传参数
	}
	
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "banner": [ //banner列表
	            {
	                "book_id": 10086, //作品id
	                "name": "哈哈哈", //作品名
	                "image": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg" //banner图地址
	            },
	            {
	                "book_id": 10086,
	                "name": "呵呵呵",
	                "image": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg"
	            },
	            {
	                "book_id": 10087,
	                "name": "呀呀呀",
	                "image": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg"
	            }
	        ],
	        "label": [ //推荐位列表
	            {
	                "recommend_id": 1, //推荐位id
	                "label": "大神推荐", //推荐位名称
	                "icon": "http://pics.sc.chinaz.com/Files/pic/icons128/6859/f1.png", //推荐位图标
	                "total": 15, //推荐位总作品数
	                "list": [ //推荐作品列表
	                    {
	                        "book_id": 100, //作品名
	                        "name": "爱你一万年", //作品名
	                        "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg", //封面
	                        "description": "这是一本言情小说", //作品简介
	                        "author": "小茗同学", //作者
                        	"tag": [ //作品标签 没有标签时为空数组
					            {
					                "tab": "恐怖",
					                "color": "#2ae0c8 "
					            },
					            {
					                "tab": "盗墓",
					                "color": "#acf6ef"
					            },
					            {
					                "tab": "完结",
					                "color": "#fbb8ac"
					            }
					        ],
	                        "finished": "已完结", //连载状态
	                        "flag": "免费" //作品角标
	                    },
	                    {
	                        "book_id": 100,
	                        "name": "爱你一万年",
	                        "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
	                        "description": "这是一本言情小说",
	                        "author": "小茗同学",
	                        "tag": [
                            	{
					                "tab": "恐怖",
					                "color": "#2ae0c8 "
					            },
					            {
					                "tab": "盗墓",
					                "color": "#acf6ef"
					            },
					            {
					                "tab": "完结",
					                "color": "#fbb8ac"
					            }
	                        ],
	                        "finished": "已完结",
	                        "flag": "免费"
	                    }
	                ]
	            },
	            {
	                "recommend_id": 2,
	                "label": "宅男最爱",
	                "icon": "http://pics.sc.chinaz.com/Files/pic/icons128/6859/f1.png",
	                "total": 12,
	                "list": [
	                    {
	                        "book_id": 100,
	                        "name": "爱你一万年",
	                        "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
	                        "description": "这是一本言情小说",
	                        "author": "小茗同学",
	                        "tag": [
		                       	{
					                "tab": "恐怖",
					                "color": "#2ae0c8 "
					            },
					            {
					                "tab": "盗墓",
					                "color": "#acf6ef"
					            },
					            {
					                "tab": "完结",
					                "color": "#fbb8ac"
					            }
	                        ],
	                        "finished": "已完结",
	                        "flag": "独家"
	                    },
	                    {
	                        "book_id": 100,
	                        "name": "爱你一万年",
	                        "cover": "https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9a504fc2d56285351b6e601d92ef76c6a7ef6304.jpg",
	                        "description": "这是一本言情小说",
	                        "author": "小茗同学",
	                        "tag": [],
	                        "finished": "已完结",
	                        "flag": "限免"
	                    }
	                ]
	            }
	        ]
	    }
	}	
	
###推荐更多[OK]
> 	Request:POST /book/recommend  
> 推荐位标题和推荐位书籍信息

>  

	Params:
	{
		recommend_id, //必传 推荐位id
		page_num, //页码 可不传，默认第一页
		page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"recomment": {
	    		"title": 精品推荐
	    	},
	        "book": {  
	        	"total_page": 3, //总页数
		        "current_page": 2, //当前页码
		        "page_size": 2, //每页显示条数
		        "list": [ //作品列表
		            {
		                "book_id": 100, //作品id
		                "name": "重生千金：国民女神归来", //作品名
		                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180", //封面
		                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......", //简介
		                "author": "渊絮雅", //作者
		                "tag": [ //Tag list
		                    {
		                        "tab": "浪漫青春", //tag名称
		                        "color": "#ff0000" //tag颜色
		                    },
		                    {
		                        "tab": "青春校园",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "虐爱",
		                        "color": "#ff0000"
		                    }
		                ],
		                "finished": "连载中",
		                "flag": "免费"
		            },
		            {
		                "book_id": 100,
		                "name": "重生学霸千金：首席校草，别犯规",
		                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
		                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
		                "author": "风子_",
		                "tag": [
		                    {
		                        "tab": "连载",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "青春言情",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "虐爱",
		                        "color": "#FFEFDB"
		                    }
		                ],
		                "finished": "连载中",
		                "flag": "付费"
		            }
		        ]
	        }
	    }
	}
	
	
###作品详情接口[OK]

> 	Request:POST /book/info

>  
	
	Params:
	{
		book_id, //作品id
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "book": { //作品基本信息
	            "book_id": 1006, //作品id
	            "name": "舞动乾坤", //作品名
	            "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg", //封面
	            "author": "小茗同学", //作者
	            "description": "天地不仁以万物为刍狗", //作品简介
	            "display_label": "连载中 | 总裁高干 | 241万字", //展示label
	            "tag": [ //标签
	                {
	                    "tab": "恐怖", //标签名
	                    "color": "#2ae0c8 " //色值
	                },
	                {
	                    "tab": "盗墓",
	                    "color": "#acf6ef"
	                },
	                {
	                    "tab": "完结",
	                    "color": "#fbb8ac"
	                }
	            ],
	            "finished": "已完结", //连载状态
	            "flag": "独家", //角标
	            "total_words": "5千字", //总字数
	            "total_comment": 200, //总评论数
	            "last_chapter_time": "更新于两天前", //最新章节更新时间
	            "last_chapter": "第201章：野猪会上树" //最新章节名
	        },
	        "comment": [ //评论
	            {
	                "comment_id": 2, //评论id
	                "avatar": "http://imgsrc.baidu.com/forum/w=580/sign=1588b7c5d739b6004dce0fbfd9503526/7bec54e736d12f2eb97e1a464dc2d56285356898.jpg", //评论人头像
	                "uid": 58, //评论人uid
	                "time": "3分钟前", //评论时间
	                "nickname": "路边的小草", //评论人昵称
	                "like_num": 10, //点赞数
	                "content": "寂寞的大鹏", //评论内容
	                "reply_info": "" //回复的评论内容
	                'is_vip': 1, //是否为VIP ，0否 1是
	            },
	            {
	                "comment_id": 10,
	                "avatar": "http://imgsrc.baidu.com/forum/w=580/sign=1588b7c5d739b6004dce0fbfd9503526/7bec54e736d12f2eb97e1a464dc2d56285356898.jpg",
	                "uid": 68,
	                "time": "03月24日 09:42",
	                "nickname": "吃饭的松鼠",
	                "like_num": 10,
	                "content": "你寂寞么",
	                "reply_info": "回复 路边的小草：寂寞的大鹏"
	                'is_vip': 1, //是否为VIP ，0否 1是
	            }
	        ],
	        "label": [
	            {
	                "label": "大家都在看", //label名称
	                "list": [ //作品list
	                    {
	                        "book_id": 100,
	                        "name": "爱你一万年",
	                        "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
	                        "finished": "已完结",
	                        "flag": "免费"
	                    },
	                    ...
	                ]
	            },
	            {
	                "label": "猜你喜欢", //label名称
	                "list": [ //作品list
	                    {
	                        "book_id": 100,
	                        "name": "爱你一万年",
	                        "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg",
	                        "finished": "已完结",
	                        "flag": "免费"
	                    },
	                    ...
	                ]
	            }
	        ],
	        "is_collect": 1 //是否收藏，1：是
	        "about": { //作品补充说明
	            "title": "更多书籍信息",
	            "content": "本书由阅文集团授权 搜狗阅读进行d电子制作与发行\n 版权所有 侵权必究"
	        }
	    }
	}
	
###分类列表接口[OK]
> 	Request:POST /book/category

>  
	1.男生频道分类列表  
	2.女生频道分类列表  

>  
	
	Params:
	{
		token,
		...其他全局必传参数
	}
	

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": [
	        {
	            "channel": "男生频道", //频道名
	            "categories": [ //频道分类
	                {
	                    "cat1": 1, //一级分类id
	                    "cat_name": "奇幻玄幻", //分类名
	                    "icon": "http://beiwo.oss-cn-beijing.aliyuncs.com/bookcat_icon/1251f6b24ca35ffa689cce8dd731f256.jpeg", //分类icon
	                    "sub_cat": [	//子分类列表
	                        "热血",
	                        "复仇",
	                        "重生",
	                        "废柴"
	                    ]
	                },
	                {
	                    "cat1": 2,
	                    "cat_name": "武侠修仙",
	                    "icon": "http://beiwo.oss-cn-beijing.aliyuncs.com/bookcat_icon/1251f6b24ca35ffa689cce8dd731f256.jpeg",
	                    "sub_cat": [
	                        "奇遇",
	                        "人鬼奇谈",
	                        "丹药",
	                        "凡人"
	                    ]
	                }
	            ]
	        },
	        {
	            "channel": "女生频道",
	            "categories": [
	                {
	                    "cat1": 3,
	                    "cat_name": "总裁高干",
	                    "icon": "http://beiwo.oss-cn-beijing.aliyuncs.com/bookcat_icon/1251f6b24ca35ffa689cce8dd731f256.jpeg",
	                    "sub_cat": [
	                        "豪门",
	                        "霸宠",
	                        "契约",
	                        "军婚"
	                    ]
	                },
	                {
	                    "cat1": 4,
	                    "cat_name": "穿越架空",
	                    "icon": "http://beiwo.oss-cn-beijing.aliyuncs.com/bookcat_icon/1251f6b24ca35ffa689cce8dd731f256.jpeg",
	                    "sub_cat": [
	                        "异世",
	                        "翻身",
	                        "女强",
	                        "王爷"
	                    ]
	                }
	            ]
	        }
	    ]
	}
	

###分类详情首页[OK]
> 	Request:POST /book/category-index


>  
	
	Params:
	{
		cat1, //一级分类id
		token,
		...其他全局必传参数
	}
	

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"category": [ //一级分类信息
	    		"cat1": 1,
	    		"name": 热血,
	    		"total_page": 2, //总页数
	    	],
	        "search_box": [ //搜索tab
	            {
	                "label": "分类", //tab名
	                "field": "cat2", //字段名 请求分类作品接口时作为参数使用
	                "list": [ //可选筛选条件list
	                    {
	                        "display": "全部", //筛选条件名
	                        "value": 0 //参数值
	                    },
	                    {
	                        "display": "都市",
	                        "value": 1
	                    },
	                    {
	                        "display": "言情",
	                        "value": 2
	                    },
	                    {
	                        "display": "盗墓",
	                        "value": 3
	                    }
	                ]
	            },
	            {
	                "label": "状态",
	                "field": "finished",
	                "list": [
	                    {
	                        "display": "全部",
	                        "value": 0
	                    },
	                    {
	                        "display": "连载",
	                        "value": 1
	                    },
	                    {
	                        "display": "完结",
	                        "value": 2
	                    }
	                ]
	            },
	            {
	                "label": "属性",
	                "field": "flag",
	                "list": [
	                    {
	                        "display": "全部",
	                        "value": ""
	                    },
	                    {
	                        "display": "免费",
	                        "value": "free"
	                    },
	                    {
	                        "display": "付费",
	                        "value": "vip"
	                    },
	                    {
	                        "display": "包月",
	                        "value": "baoyue"
	                    }
	                ]
	            }
	        ],
	        "book_list": [ //作品列表
	            {
	                "book_id": 100, //作品id
	                "name": "爱你一万年", //作品名
	                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg", //封面
	                "description": "这是一本言情小说", //描述
	                "author": "小茗同学", //作者
	                "tag": [ //标签
	                    {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "finished": "已完结", //连载状态
	                "flag": "独家" //角标
	            },
	            {
	                "book_id": 100,
	                "name": "爱你一万年",
	                "cover": "https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9a504fc2d56285351b6e601d92ef76c6a7ef6304.jpg",
	                "description": "这是一本言情小说",
	                "author": "小茗同学",
	                "tag": [
		                {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "finished": "已完结",
	                "flag": "限免"
	            }
	        ]
	    }
	}
	
###分类作品列表[OK]
> 	Request:POST /book/category-list
>  
	
	Params: //参数值按照分类详情首页接口返回值传参即可
	{
		cat1,//必传 一级分类
		cat2, //选填 二级分类id 
		finished, //选填 完结状态
		flag, //选填 属性
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}
	
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页码
	        "current_page": 2, //当前页码
	        "page_size": 2, //每页数据条数
	        "list": [ //作品list
	            {
	                "book_id": 100, //作品id
	                "name": "重生千金：国民女神归来", //作品名
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180", //封面
	                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......", //描述
	                "author": "渊絮雅", //作者
	                "tag": [ //标签
	                    {
	                        "tab": "浪漫青春", //标签名
	                        "color": "#ff0000" //颜色值
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "finished": "连载中", //连载状态
	                "flag": "免费" //角标
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
	                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
	                "author": "风子_",
	                "tag": [
	                    {
	                        "tab": "连载",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春言情",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#FFEFDB"
	                    }
	                ],
	                "finished": "连载中",
	                "flag": "付费"
	            }
	        ]
	    }
	}
	
###作品章节目录[Pending，是否可读，tag规则待定]
>	Request:POST /chapter/catalog

>  

	Params:
	{
		book_id, //必传 作品id
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "book_id": 1006, //作品id
	        "name": "斗破苍穹", //作品名
	        "total_chapter": 1006, //总章节数
	        "chapter_list": [ //章节列表
	            {
	                "chapter_id": 1, //章节id
	                "chapter_title": "第一章 亢龙有悔", //章节名
	                "words": 1001, //章节字数
	                "is_vip": 0, //是否vip 0不是 1是
	                "can_read": 1, //当前用户是否可读
	                "tag": [ //标签
	                    {
	                        "tab": "免费",
	                        "color": "#ff0000"
	                    }
	                ]
	            },
	            {
	                "chapter_id": 2,
	                "chapter_title": "第二章 飞龙在天",
	                "is_vip": 1,
	                "words": 1201,
	                "can_read": 0,
	                "tag": [
	                    {
	                        "tab": "vip",
	                        "color": "#ff0000"
	                    }
	                ]
	            },
	            {
	                "chapter_id": 3,
	                "chapter_title": "第三章 无法无天",
	                "words": 1003,
	                "is_vip": 1,
	                "can_read": 1,
	                "tag": [
	                    {
	                        "tab": "已订阅",
	                        "color": "#ff0000"
	                    }
	                ]
	            },
	            {
	                "chapter_id": 4,
	                "chapter_title": "第四章 未来日记",
	                "words": 1212,
	                "is_vip": 1,
	                "can_read": 1,
	                "tag": [
	                    {
	                        "tab": "已包月",
	                        "color": "#ff0000"
	                    }
	                ]
	            }
	        ]
	    }
	}


###章节阅读[OK]
>  
	
	About:获取章节内容接口
	1、免费章节直接获取内容返回
	2、付费章节需要判断用户是否可读（订阅可读、限免可读、包月可读），不可读返回预览章节，可读返回完整内容

>	Request:POST /chapter/text

>  
	
	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		token,
		...其他全局必传参数
	}
	
> 
	
	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "chapter_title": "第一章 哈哈哈", //章节名
	        "content": "记着，某月某日我很失落，失落时我总喜欢站在山上看夕阳西下的一瞬间，转过头对今天的辛酸苦辣一笑而过。\\n  还记着，某月某日她对我说：你太痴情，一个在乎你的人你不珍惜，偏偏珍惜一个快渐渐遗忘了你的人。\\n  我说：或许是吧！也许是我太傻太笨，我已对她立下了誓言，给下了承诺。", //章节内容
	        "is_preview": 1, //是否预览内容 1-是 0-不是
	        "last_chapter": 11, //上一章节id 没有上一章时返回0
	        "next_chapter": 13 //下一章id 没有下一章时返回0
	    }
	}
	

###章节下载
>	Request:POST /chapter/down

>	
	
	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		num, //下载chapter_id后的num个章节内容
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": [
	        {
	            "chapter_id": 1, //章节id
	            "chapter_title": "第二十章不公平的运动会", //章节名称
	            "content": "记着，某月某日我很失落，失落时我总喜欢站在山上看夕阳西下的一瞬间，转过头对今天的辛酸苦辣一笑而过。\\n  还记着，某月某日她对我说：你太痴情，一个在乎你的人你不珍惜，偏偏珍惜一个快渐渐遗忘了你的人。\\n  我说：或许是吧！也许是我太傻太笨，我已对她立下了誓言，给下了承诺。", //章节内容
	            "is_preview": 0 //是否预览章节 1是 0不是
	        },
	        {
	            "chapter_id": 2,
	            "chapter_title": "第二十章不公平的运动会", //章节名称
	            "content": "记着，某月某日我很失落，失落时我总喜欢站在山上看夕阳西下的一瞬间，转过头对今天的辛酸苦辣一笑而过。\\n  还记着，某月某日她对我说：你太痴情，一个在乎你的人你不珍惜，偏偏珍惜一个快渐渐遗忘了你的人。\\n  我说：或许是吧！也许是我太傻太笨，我已对她立下了誓言，给下了承诺。",
	            "is_preview": 0
	        },
	        {
	            "chapter_id": 4,
	            "chapter_title": "第二十章不公平的运动会", //章节名称
	            "content": "记着，某月某日我很失落，失落时我总喜欢站在山上看夕阳西下的一瞬间，转过头对今天的辛酸苦辣一笑而过。\\n  还记着，某月某日她对我说：你太痴情，一个在乎你的人你不珍惜，偏偏珍惜一个快渐渐遗忘了你的人。\\n  我说：或许是吧！也许是我太傻太笨，我已对她立下了誓言，给下了承诺。",
	            "is_preview": 1
	        }
	    ]
	}

###章节批量下载【OK】
>	Request:POST /chapter/down-url

>  
	
	About:批量下载章节，返回一个文件名，再请求down-load/file，get方法，参数file_name，下载完后文件则自动删除
>	
	
	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		num, //下载chapter_id后的num个章节内容
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0, //没有购买则会返回802
	    "msg": "success",
	    "data": [
	        'file_name': '880df9b62956e334fa93c4cf29f4df15.txt'
	    ]
	}

###章节购买预览
>	Request:POST /chapter/buy-index

>  

	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 购买章节id
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "base_info": { //基础信息
	            "remain": 500, //账号余额
	            "chapter_id": 70 //当前章节id
	            "auto_sub": 0 //自动订阅开关
	        },
	        "buy_option": [ //购买选项
	            {
	                "label": "购买本章", //label名
	                "total_price": 500, //总价格
	                "original_price": 600, //原价
	                "buy_num": 1, //购买章节数
	                "discount": "" //折扣信息
	            },
	            {
	                "label": "后10章",
	                "total_price": 1200,
	                "original_price": 1400,
	                "buy_num": 10,
	                "discount": "9.5折"
	            },
	            {
	                "label": "后50章",
	                "total_price": 5000,
	                "original_price": 8000,
	                "buy_num": 50,
	                "discount": "9折"
	            },
	            {
	                "label": "后面全部章节",
	                "total_price": 15000,
	                "original_price": 40000,
	                "buy_num": 108,
	                "discount": "8折"
	            }
	        ]
	    }
	}

###章节购买
>	Request:POST /chapter/buy

>  

	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		num, //必传 单章时传1
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0, //0成功，余额不足错误码，直接跳转充值页面
	    "msg": "success",
	    "data": {}
	}
	
	
###章节购买项详情
>	Request:POST /chapter/buy-option

>  

	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		num, //必传 章节数
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "base_info": { //用户基础信息
	            "remain": 788225, //用户总余额
	            "gold_remain": 787745, //金币余额
	            "silver_remain": 480, //银币余额
	            "unit": "书币", //主货单位
	            "subUnit": "书券", //二级货币单位
	            "chapter_id": 50240, //章节id
	            "auto_sub": 1 //自动订阅开关，0：关，1：开
	        },
	        "buy_option": { //购买项详情
	            "label": "第003章  谁都有重要的人（后1章）", //展示标题
	            "total_price": 0, //总价
	            "original_price": 0, //原价
	            "buy_num": 1, //购买章节数
	            "discount": "", //折扣
	            "actual_cost": { //区分金币银币下的 总价
	                "gold_cost": 0, //需消费金币
	                "silver_cost": 0 //需消费银币
	            },
	            "original_cost": { //区分金币银币下的 原价
	                "gold_cost": 0, //需消费金币
	                "silver_cost": 0 //需消费银币
	            }
	        }
	    }
	}
	
	
###章节下载选项预览
>	Request:POST /chapter/down-option

>  

	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "down_option": [
	            {
	                "label": "1—20章", //展示标题
	                "tag": "免费", //标签 值为空时不展示
	                "s_chapter": "110594", //开始章节id 调用下载接口时传参使用
	                "down_num": 20, //下载章节数 调用下载接口时传参使用
	                "file_name": "3e12ab01cc34e38be8c3cf58f9be3250.txt", //下载文件名
	                "start_order": "1", //下载章节 起始序号
	                "end_order": "20" //下载章节的 结束序号
	            },
	            {
	                "label": "后50章",
	                "tag": "",
	                "down_num": 50,
	                "file_name": "052f48ad4544bed547f73da20b247d44.txt",
	                "s_chapter": "110620",
	                "start_order": "27",
	                "end_order": "76"
	            },
	            {
	                "label": "后100章",
	                "tag": "",
	                "down_num": 100,
	                "file_name": "3460667470e200392b96aab2c3fe0f11.txt",
	                "s_chapter": "110620",
	                "start_order": "27",
	                "end_order": "126"
	            },
	            {
	                "label": "下载后全部章节",
	                "tag": "",
	                "down_num": 1947,
	                "file_name": "b090c28156abd4cd3464698bf8e8869d.txt",
	                "s_chapter": "110620",
	                "start_order": "27",
	                "end_order": "1973"
	            }
	        ]
	    }
	}
	
###获取章节下载地址

>	Request:POST /chapter/down-url


> 
	
	About:根据传的作品id和章节id，获取章节下载链接，接口返回 701错误码时，需要请求/chapter/buy-option 接口，渲染购买页面

>  

	Params:
	{
		book_id, //必传 作品id
		chapter_id, //必传 章节id
		num, //必传 单章时传1
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success", //0成功 错误码为需要购买时跳转，购买选项详情页
	    "data": {
	        "file_url": "http://api.beiwo-xiaoshuo.com/uploads/052f48ad4544bed547f73da20b247d44.txt", //文件下载地址
	        "file_name": "052f48ad4544bed547f73da20b247d44.txt" //文件名，全局唯一
	    }
	}

	
###排行榜首页[Pending，图标、描述规则待定]
> 	Request:POST /rank/index

>  
	
	About:排行榜首页列出来了当前的所有排行类型，和榜单的描述信息，榜单类型是固定的

>  

	Params:
	{
		channel, //频道 1-男频 2-女频
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": [
	        {
	            "rank_type": "collect", //排行榜类型
	            "list_name": "综合人气榜", //榜单名
	            "description": "《最强狂兵》上升2名", //榜单简介
	            "icon": [ //榜单图标
	                "https://qidian.qpic.cn/qdbimg/349573/1010399782/180",
	                "https://qidian.qpic.cn/qdbimg/349573/1010828991/150",
	                "https://qidian.qpic.cn/qdbimg/349573/c_9568258804975003/90"
	            ]
	        },
	        {
	            "rank_type": "yuepiao",
	            "list_name": "原创月票榜",
	            "description": "阿里原创作品风云角逐",
	            "icon": [
	                "https://qidian.qpic.cn/qdbimg/349573/1011453073/90",
	                "https://qidian.qpic.cn/qdbimg/349573/c_9121092004885203/90",
	                "https://qidian.qpic.cn/qdbimg/349573/c_10147373103984303/90"
	            ]
	        },
	        {
	            "rank_type": "finish",
	            "list_name": "完结榜",
	            "description": "经典完结数",
	            "icon": [
	                "https://qidian.qpic.cn/qdbimg/349573/1010840760/180",
	                "https://qidian.qpic.cn/qdbimg/349573/1004608738/150",
	                "https://qidian.qpic.cn/qdbimg/349573/1010468795/150"
	            ]
	        },
	        {
	            "rank_type": "newbook",
	            "list_name": "新书榜",
	            "description": "百万书友投票推荐",
	            "icon": [
	                "https://qidian.qpic.cn/qdbimg/349573/1010191960/180",
	                "https://qidian.qpic.cn/qdbimg/349573/1005401501/150",
	                "https://qidian.qpic.cn/qdbimg/349573/1003354631/150"
	            ]
	        }
	    ]
	}
	

###排行榜作品列表[OK]
> 	Request:POST /rank/book-list

>  
	
	Params:
	{
		channel, //频道 1-男频 2-女频
		rank_type, //榜单类型
		period, //榜单周期 总榜-all 月榜-month 周榜-week 
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页码
	        "page_size": 2, //每页条数
	        "list": [
	            {
	                "book_id": 100, //作品id
	                "name": "重生千金：国民女神归来", //作品名
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180", //作品封面
	                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......", //描述
	                "author": "渊絮雅", //作者
	                "tag": [ //标签
	                    {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    }
	                ],
	                "is_vip": 0, //是否vip作品 0不是 1是
	                "is_baoyue": 1, //是否包月作品 0不是 1是
	                "is_finished": 1, //是否完结 0未完结 1完结
	                "finished": "连载中", //连载状态
	                "flag": "免费" //角标
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
	                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
	                "author": "风子_",
	                "tag": [
	                    {
	                        "tab": "连载",
	                        "color": "#ff0000"
	                    }
	                    {
	                        "tab": "虐爱",
	                        "color": "#FFEFDB"
	                    }
	                ],
	                "is_vip": 0,
	                "is_baoyue": 1,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "付费"
	            }
	        ]
	    }
	}
	

###包月首页[OK]
> 	Request:POST /book/baoyue

>  

	Params:
	{
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "user": { //用户信息
	            "nickname": "小b", //昵称
	            "avatar": "http://img3.duitang.com/uploads/item/201505/26/20150526002859_c2yKG.thumb.700_0.jpeg", //头像
	            "baoyue_status": 1, //包月状态 0未开通包月 1已开通包月
	            "start_time": 1528381654, //包月有效期开始时间
	            "end_time": 1528481654, //包月有效期结束时间
	            "expiry_date": "2018年3月10~2018年5月20" //包月有效期
	        },
	        "book_list": [ //作品列表
	            {
	                "book_id": 100, //作品id
	                "name": "爱你一万年", //作品名
	                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg", //封面
	                "description": "这是一本言情小说", //描述
	                "author": "小茗同学", //作者
	                "tag": [ //tab
		                {
			                "tab": "浪漫青春",
			                "color": "#ff0000"
			            },
			            {
			                "tab": "青春校园",
			                "color": "#ff0000"
			            }
	                ],
	                "is_vip": 1, //是否vip作品 1是 0不是
	                "is_baoyue": 0, //是否包月作品 1是 0不是
	                "is_finished": 1, //是否完结 1已完结 0未完结
	                "finished": "已完结", //连载状态
	                "flag": "独家" //角标
	            },
	            {
	                "book_id": 100,
	                "name": "爱你一万年",
	                "cover": "https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9a504fc2d56285351b6e601d92ef76c6a7ef6304.jpg",
	                "description": "这是一本言情小说",
	                "author": "小茗同学",
	                "tag": [],
	                "is_vip": 0,
	                "is_baoyue": 1,
	                "is_finished": 1,
	                "finished": "已完结",
	                "flag": "限免"
	            }
	        ]
	    }
	}


###包月库作品库首页[OK]
> 	Request:POST /book/baoyue-index

>  

	Params:
	{
		channel, //必传参数 1男频 2女频
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "search_box": [ //包月作品检索条件
	            {
	                "label": "分类",
	                "field": "cat1",
	                "list": [
	                    {
	                        "display": "全部",
	                        "value": 0
	                    },
	                    {
	                        "display": "都市",
	                        "value": 1
	                    },
	                    {
	                        "display": "言情",
	                        "value": 2
	                    },
	                    {
	                        "display": "盗墓",
	                        "value": 3
	                    }
	                ]
	            },
	            {
	                "label": "字数",
	                "field": "words",
	                "list": [
	                    {
	                        "display": "全部",
	                        "value": 0
	                    },
	                    {
	                        "display": "30万字以下",
	                        "value": 1
	                    },
	                    {
	                        "display": "30-100万字",
	                        "value": 2
	                    },
	                    {
	                        "display": "100万字以上",
	                        "value": 3
	                    }
	                ]
	            },
	            {
	                "label": "类型",
	                "field": "type",
	                "list": [
	                    {
	                        "display": "全部",
	                        "value": ""
	                    },
	                    {
	                        "display": "热度",
	                        "value": "hot"
	                    },
	                    {
	                        "display": "更新",
	                        "value": "new"
	                    },
	                    {
	                        "display": "完结",
	                        "value": "finish"
	                    }
	                ]
	            }
	        ],
	        "book_list": [ //作品列表
	             {
		                "book_id": 100, //作品id
		                "name": "爱你一万年", //作品名
		                "cover": "https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3dec01d955990eef01f3a29795b.jpg", //封面
		                "description": "这是一本言情小说", //描述
		                "author": "小茗同学", //作者
		                "tag": [ //tab标签
		                    {
		                        "tab": "连载",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "青春言情",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "虐爱",
		                        "color": "#FFEFDB"
		                    }
		                ],
		                "is_vip": 1, //是否vip 0不是 1是 
		                "is_baoyue": 0, //是否包月 0不是 1是
		                "is_finished": 1, //是否完结 0未完结 1已完结
		                "finished": "已完结", //完结状态
		                "flag": "独家" //角标
	            	},
	                {
		                "book_id": 100,
		                "name": "爱你一万年",
		                "cover": "https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9a504fc2d56285351b6e601d92ef76c6a7ef6304.jpg",
		                "description": "这是一本言情小说",
		                "author": "小茗同学",
		                "tag": [
		                    {
		                        "tab": "连载",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "青春言情",
		                        "color": "#ff0000"
		                    },
		                    {
		                        "tab": "虐爱",
		                        "color": "#FFEFDB"
		                    }
		                ],
		                "is_vip": 0,
		                "is_baoyue": 1,
		                "is_finished": 1,
		                "finished": "已完结",
		                "flag": "限免"
	            	}
	        ]
	    }
	}

###包月作品列表[OK]
> 	Request:POST /book/baoyue-list

>  

	Params:
	{
		channel, //频道
		cat1, //一级分类
		words, //字数
		type, //类型
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [
	            {
	                "book_id": 100,
	                "name": "重生千金：国民女神归来",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180",
	                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......",
	                "author": "渊絮雅",
	                "tag": [
	                    {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "免费"
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
	                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
	                "author": "风子_",
	                "tag": [
	                    {
	                        "tab": "连载",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春言情",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#FFEFDB"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "付费"
	            }
	        ]
	    }
	}
	
###完本作品列表[OK]
> 	Request:POST /book/finish

>  

	Params:
	{
		channel, //频道 1-男频 2-女频
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [
	            {
	                "book_id": 100,
	                "name": "重生千金：国民女神归来",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180",
	                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......",
	                "author": "渊絮雅",
	                "tag": [
	                    {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "免费"
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
	                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
	                "author": "风子_",
	                "tag": [
	                    {
	                        "tab": "连载",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春言情",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#FFEFDB"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "付费"
	            }
	        ]
	    }
	}
###限免作品列表[OK]
> 	Request:POST /book/free-time

>  

	Params:
	{
		channel, //频道 1-男频 2-女频
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [
	            {
	                "book_id": 100,
	                "name": "重生千金：国民女神归来",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180",
	                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......",
	                "author": "渊絮雅",
	                "tag": [
	                    {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "免费"
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
	                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
	                "author": "风子_",
	                "tag": [
	                    {
	                        "tab": "连载",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春言情",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#FFEFDB"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "付费"
	            }
	        ]
	    }
	}

###作品评论列表[OK]
> 	Request:POST /comment/list

>  

	Params:
	{
		book_id, //必传 作品id
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}

>  
	
	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [
	            {
	                "comment_id": 2, //评论id
	                "avatar": "http://imgsrc.baidu.com/forum/w=580/sign=1588b7c5d739b6004dce0fbfd9503526/7bec54e736d12f2eb97e1a464dc2d56285356898.jpg", //头像
	                "uid": 58, //用户uid
	                "time": "3分钟前", //评论时间
	                "nickname": "路边的小草", //昵称
	                "like_num": 10, //点赞数
	                "content": "寂寞的大鹏", //评论内容
	                "reply_info": "" //回复的评论内容
	                'is_vip': 1, //是否为VIP ，0否 1是
	            },
	            {
	                "comment_id": 10,
	                "avatar": "http://imgsrc.baidu.com/forum/w=580/sign=1588b7c5d739b6004dce0fbfd9503526/7bec54e736d12f2eb97e1a464dc2d56285356898.jpg",
	                "uid": 68,
	                "time": "03月24日 09:42",
	                "nickname": "吃饭的松鼠",
	                "like_num": 10,
	                "content": "你寂寞么",
	                "reply_info": "回复 路边的小草：寂寞的大鹏"
	                'is_vip': 1, //是否为VIP ，0否 1是
	            }
	        ]
	    }
	}

###发布作品评论[OK]
> 	Request:POST /comment/post

>  

	Params:
	{
		book_id, //必传 作品id
		comment_id, //非必传 回复评论的id
		content, //必传 内容
		token,
		...其他全局必传参数
	}

>  
	
	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"status": 1, 		// 1评论成功，0评论失败
	    	"comment_id": 7184, // 评论成功时返回新增评论的ID
	    }
	}


###作品评论点赞[OK]
> 	Request:POST /comment/like

>  

	Params:
	{
		comment_id, //必传 评论的id
		token,
		...其他全局必传参数
	}

>  
	
	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"status": 1,	// 1点赞成功，0点赞失败
	    	"like_num": 37,	// 点赞完成后更新的点赞数
	    }
	}
	
## 我的接口
###我的

####个人中心首页[OK]
>	Request:POST /user/center

>  

	Params:
	{
		token, //必传
	}
	
> 
	
	Response: 
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
            "uid": 10086 //用户唯一id,
	        "nickname": "涛子", //昵称
		    'mobile': 13800000000, //手机号
		    'user_token': jsldfjlsdjfsldkjf, //token
		    'is_vip': 1, //是否为VIP ，0否 1是
		    'gender': 1, //性别 1女 2男
		    'remain': 100, //余额
		    "goldRemain": 192,  // 书币余额
            "silverRemain": 385,// 书券余额
            "canExtract": 3,    // 可提现回馈
            "unit": "书豆",      // 货币一级单位
            "subUnit": "书券",   // 货币二级单位
	        "avatar": "http://hehuan.oss-cn-beijing.aliyuncs.com/photos/23/3a1a04b7aad03bd26ad647f269ae362a.jpg", //头像
	        "auto_sub": 0, //自动订阅开关，0：关，1：开
	        "bind_list": [ //绑定关系列表
	            {
	                "label": "手机号", //绑定类型
	                "action": "bindPhone", //绑定操作
	                "status": 0, //绑定状态 0-未绑定 1-已绑定
	                "display": "未绑定" //展示绑定内容
	            },
	            {
	                "label": "微信",
	                "action": "bindWechat",
	                "status": 1,
	                "display": "好多鱼儿"
	            }
	        ],
	        "task_list": {
                "mission_num": 9,   // 任务完成数
                "finish_num": 5     // 任务总数
            }
        }
	}

####我的书评[OK]
>	Request:POST /user/comments

>  

	Params:
	{
		token, //必传
	}
	
> 
	
	Response: 
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"total_page": 259,
	    	"current_page": 1,
	    	"page_size": 10,
	    	"total_count": 2589,
	    	"list": [
	    		{
	    			"book_id": 1,
	    			"book_name": "无敌九零后",
	    			"nickname": "大爷来玩啊",
	    			"avatar": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/ba663db0c7b26fe5734b30def034e448.jpg",
	    			"time": "7天前",
	    			"like_num": 0,
	    			"content": "test"
	    		},
	    		{
	    			"book_id": 51,
	    			"book_name": "吹尸人",
	    			"nickname": "大爷来玩啊",
	    			"avatar": "http://beiwo.oss-cn-beijing.aliyuncs.com/cover/ba663db0c7b26fe5734b30def034e448.jpg",
	    			"time": "28天前",
	    			"like_num": 994,
	    			"content": "前一世，一剑破苍穹，只手挡万神，却不料在渡天劫时被小人所害。可苍天不负有心人，他重生在小城败落家族叶家之中，成了废柴少爷叶云。一世剑神，注定崛起，前世破苍穹挡万神，叱咤风云。这一世斩妖魔诛邪神，笑看沧海！"
	    		},
	    		...
	    	]
        }
	}


####提交意见反馈[OK]
>	Request:POST /user/post-feedback

>  

	Params:
	{
		token,   // 用户token, 必传
		content, // 反馈内容, 必传, 不超过100个字
	}
	
> 
	
	Response: 
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	"status": 1, // 状态，1成功0失败
        }
	}

####充值页面
>	Request:POST /pay/center

>  

	Params:
	{
		token: sdkfjsdkfdjs; //非必传	}
	
>  
 
	Response: 
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	 "title":"元宝充值", //页面标题
	        "tips": "阅读币充值（1元=10阅读币）", //提示
	        "thirdOn": 1, //是否开第三方支付 1-开启 0-不开启
	        "items": [ //商品列表
	            {
	                "goods_id": 1, //商品id 下单时的必传参数
	                "title": "60阅读币", //商品名
	                "price": "6元", //价格
	                "flag": "", //角标
	                "note": [], //商品注解
	                "apple_id": "com.aizaihehuan.test1", //商品对应苹果id 非苹果支付时不需要care
	                "pay_channel": [ //支持的支付渠道 苹果支付不需要care
	                    "alipay",
	                    "weixin",
	                    "apple"
	                ]
	            },
	            {
	                "goods_id": 2,
	                "title": "300阅读币",
	                "price": "30元",
	                "flag": "送10%",
	                "note": [
	                    "+30代金券"
	                ],
	                "apple_id": "com.aizaihehuan.test2",
	                "channel": [
	                    "alipay",
	                    "weixin",
	                    "apple"
	                ]
	            },
	            {
	                "goods_id": 3,
	                "title": "500阅读币",
	                "price": "50元",
	                "flag": "送12%",
	                "note": [
	                    "+60代金券"
	                ],
	                "apple_id": "",
	                "channel": [
	                    "alipay",
	                    "weixin"
	                ]
	            },
	            {
	                "goods_id": 5,
	                "title": "1080阅读币",
	                "price": "108元",
	                "flag": "送14%",
	                "note": [
	                    "+280代金券"
	                ],
	                "apple_id": "",
	                "channel": [
	                    "alipay"
	                ]
	            }
	        ],
	        "about": [ //说明
	            "1、通过App Store充值阅读币，可以查看充值引导帮助。",
	            "2、苹果政策规定iOS上的书豆不能在其他终端上使用。"
	        ]
	    }
	}

####支付宝下单接口(OK)
>	Request: POST /pay/alipay

>  

	About:支付宝下单接口,充值金币和购买vip都使用该接口生成订单。订单返回的是一个签名好的数据串。只有code = 0时为正常请求。

>  

	Params:
	{
		goods_id, //int 商品id,必传参数
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "order_str": "alipay_sdk=alipay-sdk-php-20161101&amp;app_id=2088721867698833&amp;biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0VIP%E5%B9%B4%E8%B4%B9%E4%BC%9A%E5%91%98%22%2C%22subject%22%3A+%22%E7%88%B1%E5%9C%A8%E5%90%88%E6%AC%A2%E4%BA%A7%E5%93%81%E8%B4%AD%E4%B9%B0%22%2C%22out_trade_no%22%3A+%222017120316385724186455%22%2C%22timeout_express%22%3A+%2230m%22%2C%22total_amount%22%3A+%22998%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%22passback_params%22%3A%22%7B%22goods_id%22%3A%2221%22%7D%22%7D&amp;charset=UTF-8&amp;format=json&amp;method=alipay.trade.app.pay&amp;notify_url=http%3A%2F%2Fwww.aizaihehuan.com%2Fapi%2Fpay%2Falipay-notify&amp;sign_type=RSA2&amp;timestamp=2017-12-03+16%3A38%3A57&amp;version=1.0&amp;sign=Kcc7oAT4yqjl3y6cpCX%2BVx7PqL9N8bMI5eqnDk9h21ie%2Fao1jFhPA4aXo5%2FLBo1LnfiL1AnV14B6ZK283Iwsw10hl7o0kFBfe5uoeqd5Q2%2BCHbFXDa20ylXj85J2AYUStN893KzkoT%2B5gkV8ZaBfRdpRVvrIASLpLc6yEAJyNQs%3D"
	    }
	}
	
####微信下单接口(OK)
>	Request: POST /pay/wxpay

>  

	About:微信下单接口,充值金币和购买vip都使用该接口生成订单。订单返回的是一个签名好的数据串。只有code = 0时为正常请求。其他提示错误

>  

	Params:
	{
		goods_id, //int 商品id,必传参数
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "prepay_id": "wx20171203163248e517892acf0826367713"
	    }
	}

####微信公众号下单接口(OK)
>	Request: POST /pay/mppay

>  

	About:微信下单接口,充值金币和购买vip都使用该接口生成订单。订单返回的是一个签名好的数据串。只有code = 0时为正常请求。其他提示错误

>  

	Params:
	{
		goods_id, //int 商品id,必传参数
		openid, //string 必传 用户公众号下唯一id
		token,
		...其他全局必传参数
	}
	
>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "appId": "wx589c0288fc0a7410", //公众号id
	        "timeStamp": 1532246915, //时间戳
	        "nonceStr": "REupV0npNsMrVSYf", //随机字符串
	        "package": "prepay_id=wx22160657875520fb79a880983068892491", //订单详情扩展字符串
	        "signType": "MD5", //签名方式
	        "paySign": "EAB6EDDCD32CC51C563BE9643A5F3D2C" //签名
	    }
	}

####苹果支付确认接口(OK)
>	Request: POST /pay/applepay

>  

	Params:
	{
		receipt, //string 苹果交易凭证,必传参数
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {}
	}
	异常时：
	{
	    "code": 107,
	    "msg": "参数[receipt]无效",
	    "data": {}
	}
	
	
####通用下单接口(OK)
>	Request: POST /pay/create-order

>  
	About：提供通用的下单接口，通过该接口可以进行下单操作

>  

	Params:
	{
		goods_id, //int 商品id,必传参数
		token,
		...其他全局必传参数
	}

>  

	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "orderId": "20180912010225481936", //订单号
	        "orderName": "6元", //商品名称
	        "userId": 36, //用户uid
	        "extension": "goods_id-1", //扩展字段
	        "amount": "600" //金额，单位：分
	    }
	}
	异常时：
	{
	    "code": 107,
	    "msg": "商品无效",
	    "data": {}
	}
	
	
####包月购买页
>	Request:POST	/pay/baoyue-center

>  

	Params:
	{
		token,
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "user": { //用户信息
	            "nickname": "小b", //string 昵称
	            "avatar": "http://img3.duitang.com/uploads/item/201505/26/20150526002859_c2yKG.thumb.700_0.jpeg", //string 头像
	            "baoyue_status": 1, //string 是否包月用户  1是 0不是
	            "start_time": 1528381654, //int 包月开始时间
	            "end_time": 1528481654, //int 包月到期时间
	            "display_date": "2018年3月10~2018年5月20" //string 有效期格式化显示
	        },
	        "list": [
	            {
	                "goods_id": 21, //int 商品id，支付宝和微信支付时，下单必传参数，苹果支付不care
	                "title": "1个月", //string 商品名
	                "price": 18, //int 商品展示价格 单位元
	                "note": "18元/月", //string 商品注解
	                "apple_id": "", //string 苹果商品id，苹果支付时传参使用
	                "tag": [], //array  标签
	                "channel": [ //array 支持的支付渠道 ios无需关注
	                    "alipay",
	                    "weixin"
	                ]
	            },
	            {
	                "goods_id": 22,
	                "title": "3个月",
	                "price": 40,
	                "note": "省14元，13.3元/月",
	                "apple_id": "com.aizaihehuan.test5",
	                "tag": [
	                    {
	                        "tab": "推荐",
	                        "color": "#ff0000"
	                    }
	                ],
	                "channel": [
	                    "alipay",
	                    "weixin",
	                    "apple"
	                ]
	            },
	            {
	                "goods_id": 23,
	                "title": "6个月",
	                "price": 98,
	                "note": "省30元，13元/月",
	                "apple_id": "com.aizaihehuan.test5",
	                "tag": [],
	                "channel": [
	                    "weixin",
	                    "apple"
	                ]
	            },
	            {
	                "goods_id": 24,
	                "title": "12个月",
	                "price": 98,
	                "note": "省88元，10.7元/月",
	                "apple_id": "com.aizaihehuan.test5",
	                "tag": [
	                    {
	                        "tab": "年费vip",
	                        "color": "#ff0000"
	                    }
	                ],
	                "channel": [
	                    "weixin",
	                    "apple"
	                ]
	            }
	        ],
	        "privilege": [ //特权说明
	            {
	                "label": "包月免费读", //特权
	                "icon": "http://hehuan.oss-cn-beijing.aliyuncs.com//icon/1.png", //icon
	                "action": "", //动作
	                "url": "", //string 跳转的url
	                "desc": "" //string 描述
	            },
	            {
	                "label": "折扣特权",
	                "icon": "http://hehuan.oss-cn-beijing.aliyuncs.com//icon/2.png",
	                "action": "",
	                "url": "",
	                "desc": ""
	            },
	            {
	                "label": "成长加速",
	                "icon": "http://hehuan.oss-cn-beijing.aliyuncs.com//icon/3.png",
	                "action": "",
	                "url": "",
	                "desc": ""
	            }
	        ]
	    }
	}

####充值消费记录
>	Request: POST /pay/gold-detail

>  

	Params:
	{
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,
		...其他全局必传参数
	}

>  

	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [ //明细列表
	            {
	                "article": "支付宝充值", //string 明细说明
	                "detail": "+500元宝", //string 明细
	                "date": "10月29日 19:08", //string 日期
	                "detail_type": 1 //int 明细类型 1-充值 2-消费
	            },
	            {
	                "article": "购买章节",
	                "detail": "-50元宝",
	                "date": "10月28日 15:08",
	                "detail_type": 2
	            },
	            {
	                "article": "购买章节",
	                "detail": "-50元宝",
	                "date": "10月28日 13:13",
	                "detail_type": 2
	            },
	            {
	                "article": "购买章节",
	                "detail": "-50元宝",
	                "date": "10月25日 19:30",
	                "detail_type": 2
	            },
	            {
	                "article": "签到获取",
	                "detail": "+50元宝",
	                "date": "10月20日 19:30",
	                "detail_type": 1
	            }
	        ]
	    }
	}


#### 消费记录
>1.书评（同评论二级页面）


#### 设置页面
>1.2G/3G同步书架，wifi自动下载，清缓存（大鹏牛逼）  
>2.好评，关于（可H5）

#### 积分记录
>1.积分记录

#### 积分等级说明，H5页面

#### 阅读币余额
>1.剩余阅读币  
>2.充值按钮（点击跳转充值页面）

### 搜索
#### 搜索首页
>	Request:POST /book/search-index  
> 	用于搜索首页显示信息，热词hot_word共10个，已经排好序，list热搜榜，共6本书

>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	    	 "hot_word" : [
	    	 		"测试1",
	    	 		"测试2",
	    	 ],
	        "list": [
	            {
	                "book_id": 100,
	                "name": "重生千金：国民女神归来",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180"
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180"	            }
	        ]
	    }
	}

#### 搜索页接口
>	Request:POST /book/search  
>	用于关键词搜索

>  

	Params:
	{
		keyword, //必传，关键词
		page_num, //页码 可不传，默认第一页
    	page_size, //每页条数 可不传，默认10条
		token,

	}
	
> 
 
	Response:
	{
	    "code": 0,
	    "msg": "success",
	    "data": {
	        "total_page": 3, //总页数
	        "current_page": 2, //当前页数
	        "page_size": 2, //每页条数
	        "list": [
	            {
	                "book_id": 100,
	                "name": "重生千金：国民女神归来",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_9329485903287903/180",
	                "description": "上一世她是国民女神，豪门千金，拥有神赐的天籁之音，坐拥粉丝无数，因遭渣男贱女陷害重生回到高中。这一世，她不仅要夺回属于自己的荣耀，更要活出一个人人羡慕的精彩人生。当学霸，虐渣打脸白莲花，重回歌坛，影后天后各种奖项拿到手软。只是，某次颁奖礼，人称最接近神的男人却当众亲吻了她并宣布是她的未婚夫。她勾唇轻笑，我说，某位男神大人，说好的高冷禁欲呢......",
	                "author": "渊絮雅",
	                "tag": [
	                    {
	                        "tab": "浪漫青春",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春校园",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#ff0000"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "免费"
	            },
	            {
	                "book_id": 100,
	                "name": "重生学霸千金：首席校草，别犯规",
	                "cover": "https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180",
	                "description": "重生前她受尽排挤，名声尽毀，保送名额被夺，亲生母亲因自己而死，自己更是受尽凌辱被迫害而亡。意外重生，她成为时家千金时予初。继母毒姐机关算尽，毁她声名，欲顶替她正牌千金身份，夺她继承权；校园重遇心机绿茶，过去再现。那好，新仇旧帐一起算，她发誓定让残害她的人血债血偿。曾经不起眼的时家千金锋芒渐起，校园做学霸女神，校外她是神秘的金融女王，更是国际首席音乐大师关门弟子，享万丈光芒，美男环绕。终于某冰山醋意漫天，忍无可忍，将她掠回家中。星空璀璨，月光正好，她明知故问：“校草大人想做什么？”某冰山挑眉，十分耿直：“让你成为我的人。”护你周全，无人再敢动你分毫。",
	                "author": "风子_",
	                "tag": [
	                    {
	                        "tab": "连载",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "青春言情",
	                        "color": "#ff0000"
	                    },
	                    {
	                        "tab": "虐爱",
	                        "color": "#FFEFDB"
	                    }
	                ],
	                "is_vip": 1,
	                "is_baoyue": 0,
	                "is_finished": 1,
	                "finished": "连载中",
	                "flag": "付费"
	            }
	        ]
	    }
	}



##小程序过审
###是否为过审
>	Request:POST /mini/report 


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		     "time": 1, //忽略
		     "status": 1, //是否为过审 ，0不是，1是
	         "ver": 1, //忽略
	         "session": "02831a90706987d76175cedc26b1c465", //忽略
	    }
	}

###书城首页
>	Request:POST /mini/index 


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		     "list": [
		     	{
		     		"id": 1,
		     		"title": "乔布斯传",
		     		"cover": "封面"
		     	},
		     	{
		     		"id": 1,
		     		"title": "乔布斯传",
		     		"cover": "封面"
		     	},
		     ]
	    }
	}


###书籍详情
>	Request:POST /mini/info 


>  

	Params:
	{
		token,
		id, //必传，书籍id
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		     "id": 1,
		     "title": "乔布斯传",
		     "cover": "封面",
		     "publish":"清华大学出版社",
		     "publish_time": "2018-09-16",
		     "language": "英语",
		     "status":true, //是否收藏
	    }
	}

###关于我们
>	Request:POST /mini/about 


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		     "about": "温暖被窝主要用于书友之间分享名著传记，通过小程序的传播，共同学习进步。",
		     "copyright": "©2018 北京万象云智科技有限公司"
	    }
	}

###联系我们
>	Request:POST /mini/connect 


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		     "tel": "010-52894572",
		     "qq": "1067385597",
		     "copyright": "©2018 北京万象云智科技有限公司"
	    }
	}

###书架
>	Request:POST /mini/shelf 


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		     "list": [
		     	{
		     		"id": 1,
		     		"title": "乔布斯传",
		     		"cover": "封面"
		     	},
		     	{
		     		"id": 1,
		     		"title": "乔布斯传",``
		     		"cover": "封面"
		     	},
		     ]
	    }
	}
###书籍添加和删除和之前的一样


##分享跳转
>	Request:GET /site/share 

>  

	Params:
	{
		uid,//必传，用户唯一标识，没有的时候传空
		book_id, //必传，分享的书籍id，在书籍阅读和详情页传book_id，其余处传空，
					分享有书籍和应用两种情况，book_id为空时分享的是应用
		osType：系统， 1：iOS，  2：android 
		product： 产品线 1app 2公众号 3小程序
	}

##任务系统

###任务中心
>	Request:POST /task/center 


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {
		      [
		      		{
		      			"task_title": "日常任务",
					    "task_list": [
					    {
						     "task_id" => 10  
                             "task_award" => 金币20
                             "task_desc" => 分享APP
                             "task_label" => 分享APP
                             "task_state" => 1
						},
						 "task_list": [
					    {
						     "task_id" => 11  
                             "task_award" => 金币20
                             "task_desc" => 分享APP
                             "task_label" => 分享APP
                             "task_state" => 1
						}
						]
					  }
		      		},
		      		{
		      		   "task_title": "新手任务",
					   "task_list": [
							 {
						     "task_id" => 12  
                             "task_award" => 金币20
                             "task_desc" => 分享APP
                             "task_label" => 分享APP
                             "task_state" => 1
						},
						 "task_list": [
					    {
						     "task_id" => 13  
                             "task_award" => 金币20
                             "task_desc" => 分享APP
                             "task_label" => 分享APP
                             "task_state" => 1
						}	
						]
					  }
		      		},
		      ],
			
			  "user_info": {
				   "avatar": "www.xxxxxxxxxxxx", // 头像
				   "is_vip": 1, // 是否是会员 0 不是 1是
				   "nickname": "Andrew", // 昵称
				   "unit": "金币" // 货币单位
				   "mission_num": 9  //任务总数
				   "finish_num":4  //任务已完成数
			   }	     
		  }
	}

###完成任务阅读两本书
	
>	Request:POST /user/task-read


>  

	Params:
	{
		token,
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {}
	}	
	

###保存用户推荐
	
>	Request:POST /user/save-recommend


>  

	Params:
	{
		token,
		cat1: 1,2, //分类，用,拼接
		gender:1 //性别
	}
	
> 
 
	Response:
	{
		"code": 0,
	    "msg": "success",
	    "data": {}
	}	
	
