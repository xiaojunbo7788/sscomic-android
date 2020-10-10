package com.ssreader.novel.model;

import java.util.List;

/**
 * 软件属性
 */
public class AppUpdate {

    public VersionUpdateBean version_update;
    public PublicIntent start_page;
    public SystemSettingBean system_setting;
    public AdStatusSettingBean ad_status_setting;
    public List<PushPassagewayBean> push_passageway;
    public String web_view_url;
    private List<WebURLBean>web_view_urlist;
    private ProtocolBean protocol_list;
    private String system_notice;

    public String getSystem_notice() {
        if (system_notice == null) {
            return "";
        }
        return system_notice;
    }

    public void setSystem_notice(String system_notice) {
        this.system_notice = system_notice;
    }

    public List<WebURLBean> getWeb_view_urlist() {
        return web_view_urlist;
    }

    public void setWeb_view_urlist(List<WebURLBean> web_view_urlist) {
        this.web_view_urlist = web_view_urlist;
    }

    public VersionUpdateBean getVersion_update() {
        return version_update;
    }

    public void setVersion_update(VersionUpdateBean version_update) {
        this.version_update = version_update;
    }

    public PublicIntent getStart_page() {
        return start_page;
    }

    public void setStart_page(PublicIntent start_page) {
        this.start_page = start_page;
    }

    public SystemSettingBean getSystem_setting() {
        return system_setting;
    }

    public void setSystem_setting(SystemSettingBean system_setting) {
        this.system_setting = system_setting;
    }

    public AdStatusSettingBean getAd_status_setting() {
        return ad_status_setting;
    }

    public void setAd_status_setting(AdStatusSettingBean ad_status_setting) {
        this.ad_status_setting = ad_status_setting;
    }

    public List<PushPassagewayBean> getPush_passageway() {
        return push_passageway;
    }

    public void setPush_passageway(List<PushPassagewayBean> push_passageway) {
        this.push_passageway = push_passageway;
    }

    public ProtocolBean getProtocol_list() {
        return protocol_list;
    }

    public void setProtocol_list(ProtocolBean protocol_list) {
        this.protocol_list = protocol_list;
    }

    public static class WebURLBean {
        private String play_title;
        private String play_url;

        public String getPlay_title() {
            if (play_title == null) {
                return "";
            }
            return play_title;
        }

        public void setPlay_title(String play_title) {
            this.play_title = play_title;
        }

        public String getPlay_url() {
            if (play_url == null) {
                play_url = "";
            }
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }
    }

    public static class VersionUpdateBean {

        private int status;
        private String msg;
        private String url;
        private String new_url;

        public String getNew_url() {
            return new_url;
        }

        public void setNew_url(String new_url) {
            this.new_url = new_url;
        }


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class SystemSettingBean {

        private String currencyUnit;
        private String subUnit;
        private int ad_switch;
        private int vip_send_switch;
        private int check_status;
        private int audio_switch;
        private int ai_switch;
        private List<String> site_type;
        public int monthly_ticket_switch;
        public int novel_reward_switch;

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

        public int getAd_switch() {
            return ad_switch;
        }

        public void setAd_switch(int ad_switch) {
            this.ad_switch = ad_switch;
        }

        public int getVip_send_switch() {
            return vip_send_switch;
        }

        public int getAudio_switch() {
            return audio_switch;
        }

        public void setAudio_switch(int audio_switch) {
            this.audio_switch = audio_switch;
        }

        public void setVip_send_switch(int vip_send_switch) {
            this.vip_send_switch = vip_send_switch;
        }

        public int getCheck_status() {
            return check_status;
        }

        public void setCheck_status(int check_status) {
            this.check_status = check_status;
        }

        public int getAi_switch() {
            return ai_switch;
        }

        public void setAi_switch(int ai_switch) {
            this.ai_switch = ai_switch;
        }

        public List<String> getSite_type() {
            return site_type;
        }

        public void setSite_type(List<String> site_type) {
            this.site_type = site_type;
        }
    }

    public class AdStatusSettingBean {

        private int chapter_read_end;
        private int chapter_read_bottom;
        private int comic_read_end;
        private int video_ad_switch;
        private int ad_free_time;
        private String video_ad_text;

        public int getChapter_read_end() {
            return chapter_read_end;
        }

        public int getAd_free_time() {
            return ad_free_time;
        }

        public void setAd_free_time(int ad_free_time) {
            this.ad_free_time = ad_free_time;
        }

        public void setChapter_read_end(int chapter_read_end) {
            this.chapter_read_end = chapter_read_end;
        }

        public int getVideo_ad_switch() {
            return video_ad_switch;
        }

        public void setVideo_ad_switch(int video_ad_switch) {
            this.video_ad_switch = video_ad_switch;
        }

        public String getVideo_ad_text() {
            return video_ad_text;
        }

        public void setVideo_ad_text(String video_ad_text) {
            this.video_ad_text = video_ad_text;
        }


        public int getChapter_read_bottom() {
            return chapter_read_bottom;
        }

        public void setChapter_read_bottom(int chapter_read_bottom) {
            this.chapter_read_bottom = chapter_read_bottom;
        }

        public int getComic_read_end() {
            return comic_read_end;
        }

        public void setComic_read_end(int comic_read_end) {
            this.comic_read_end = comic_read_end;
        }
    }

    public class PushPassagewayBean {

        private String p_key;
        private String app_id;
        private String app_key;
        private String app_secret;

        public String getP_key() {
            return p_key;
        }

        public void setP_key(String p_key) {
            this.p_key = p_key;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getApp_key() {
            return app_key;
        }

        public void setApp_key(String app_key) {
            this.app_key = app_key;
        }

        public String getApp_secret() {
            return app_secret;
        }

        public void setApp_secret(String app_secret) {
            this.app_secret = app_secret;
        }
    }

    public class ProtocolBean{

        private String notify, privacy, logoff, user, vip_service;

        public String getNotify() {
            return notify;
        }

        public void setNotify(String notify) {
            this.notify = notify;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }

        public String getLogoff() {
            return logoff;
        }

        public void setLogoff(String logoff) {
            this.logoff = logoff;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getVip_service() {
            return vip_service;
        }

        public void setVip_service(String vip_service) {
            this.vip_service = vip_service;
        }
    }
}




