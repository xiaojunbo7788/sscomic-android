package com.ssreader.novel.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.constant.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 常用工具类
 */
public class UserUtils {

    private static String versionName;

    public static String getAppVersionName(Context context) {
        if (TextUtils.isEmpty(versionName)) {
            versionName = BuildConfig.VERSION_NAME;
        }
        return versionName;
    }

    /**
     * md5加密
     */
    public static String MD5(String params) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(params.getBytes("utf-8"));
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前手机系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取平台版本
     */
    public static String getOsType() {
        return "2";
    }

    /**
     * 获取产品线 1：app 2：公众号 3：小程序
     */
    public static String getProduct() {
        return "1";
    }

    /**
     * 获取设备UUID
     */
    private static String ServiceUUID;

    public static final String getUUID(Context context) {
        if (TextUtils.isEmpty(ServiceUUID)) {
            ServiceUUID = new DeviceUuidFactory(context).getDeviceUuid();
        }
        return ServiceUUID;
    }

    /**
     * 获取用户token
     */
    private static String UserToken;

    public static String getToken(Context context) {
        if (TextUtils.isEmpty(UserToken) && context != null) {
            UserToken = ShareUitls.getUserString(context, "UserStringToken", "");
        }
        return UserToken;//"03c1e3ac7c3049b69a9bb76b784ab54e";
    }

    public static void putToken(Context context, String UserStringToken) {
        UserToken = UserStringToken;
        ShareUitls.putUserString(context, "UserStringToken", UserStringToken);
    }

    /**
     * 获取用户的唯一标识
     */
    private static String UserUID;

    public static String getUID(Context context) {
        if (TextUtils.isEmpty(UserUID) && context != null) {
            UserUID = ShareUitls.getUserString(context, "UserStringUid", "");
        }
        return UserUID;
    }

    public static String putUID(Context context, String UserStringUserUID) {
        UserUID = UserStringUserUID;
        ShareUitls.putUserString(context, "UserStringUid", UserStringUserUID);
        return UserUID;
    }

    /**
     * 判断用户是否登录
     */
    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getToken(context));
    }

    /**
     * 获取设备IMEI。
     */

    private static String imei;

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        if (TextUtils.isEmpty(imei)) {
            try {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if ((imei = tm.getDeviceId()) == null) {
                    imei = "";
                }
            } catch (Exception e) {
            }
            if (TextUtils.isEmpty(imei)) {
                imei = "";
            }
        }

        return imei;
    }

    /**
     * 获取macaddress
     */
    public static String getMac(Context context) {
        String strMac = null;
        if (Build.VERSION.SDK_INT < 23) {
            strMac = getLocalMacAddressFromWifiInfo(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < 24 && Build.VERSION.SDK_INT >= 23) {
            strMac = getMacAddress(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= 24) {
            if (!TextUtils.isEmpty(getMacAddress(context))) {
                strMac = getMacAddress(context);
                return strMac;
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                strMac = getMachineHardwareAddress();
                return strMac;
            } else {
                strMac = getLocalMacAddressFromBusybox();
                return strMac;
            }
        }

        return "02:00:00:00:00:00";
    }

    /**
     * 根据wifi信息获取本地mac
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     */
    public static String getMacAddress(Context context) {

        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return macSerial;
    }

    /**
     * Check whether accessing wifi state is permitted
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 根据IP地址获取MAC地址
     */
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备HardwareAddress地址
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }


    /**
     * 根据busybox获取本地Mac
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //获取当前 的 渠道号
    private static String channelName;

    public static String getChannelName(Context activity) {
        if (channelName == null) {
            channelName = ShareUitls.getUserString(activity, "UserChannelName", "");
            if (TextUtils.isEmpty(channelName)) {
                try {
                    PackageManager packageManager = activity.getPackageManager();
                    if (packageManager != null) {
                        //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                        ApplicationInfo applicationInfo = packageManager.
                                getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
                        if (applicationInfo != null) {
                            if (applicationInfo.metaData != null) {
                                channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                channelName = (channelName == null || channelName.equals("null")) ? "none" : channelName;
                ShareUitls.putUserString(activity, "UserChannelName", channelName);
            }
        }
        return channelName;
    }

    public static boolean NoLoginToLogin(Activity activity) {
        if (isLogin(activity)) {
            return true;
        } else {
            return false;
        }
    }
}
