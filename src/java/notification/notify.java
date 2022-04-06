/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notification;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import mainlib.tokengeneration;

/**
 *
 * @author Administrator
 */
public class notify {
     public String notifysms(String msg, String mobileno_To, String templateid, String dltId) {
        java.lang.String result = "";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

//            SMS ID :            edistrict.sms
//Signature ID :   EDISTR
//Password :        K!A6lsG%252h
//Entity ID:          1001640530000017041
// 1007161338734243262 -- BMS-OTP-- The OTP to reset your Password is {#var#}. - BMS Portal Tripura
//1007161338744086219 -- USER-REG-- You are registered successfully in bms.tripura.gov.in, Your USER ID is {#var#} and PASSWORD is {#var#} to login. Please change your password. - BMS Portal Tripura
//        
            java.lang.String gatewayuname = "edistrict.sms";
            //java.lang.String gatewaypwd = "K!A6lsG%252h";
            java.lang.String senderId = "EDISTR";//CISTHC";

            java.lang.String appId = "";

            //appId="55a9a2e7f480d5625eda7c8572d2548e923c940d0b61c7a9c7ef43af8d6114c7";
            appId = "45a8d823417508ee2152e7fa50f99e83e40d4fcfc844586f2769d65f2ad66e2d";

            java.lang.String mobileNumber = "";
            mobileNumber = "91" + mobileno_To;
            
             mainlib.tokengeneration tcg=new mainlib.tokengeneration();
             
            tcg.generatetokenwithseed4sms(mobileno_To, msg, gatewayuname, senderId, appId);
            result = sendsmsNew(tcg.getToken(), tcg.getSeed(), mobileno_To, msg, gatewayuname, senderId, appId, dltId, templateid);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String notifyemail(String mailbody, String mailto, String mailsubject) {
        java.lang.String result = "";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            java.lang.String appId = "55a9a2e7f480d5625eda7c8572d2548e923c940d0b61c7a9c7ef43af8d6114c7";
            String host = "relay.nic.in";
            String port = "25";

            //String mailFrom = "noreply.ehos-tr@gov.in";
            String mailFrom = "support.bms@tripura.gov.in";
            //Password : TrBme2021!@#

            mainlib.tokengeneration tcg = new mainlib.tokengeneration();
            tcg.generatetokenwithseed4email(mailto, mailsubject, mailbody, host, port, mailFrom, appId);
            result = sendmailNew(tcg.getToken(), tcg.getSeed(), host, port, mailFrom, mailto, mailsubject, mailbody, appId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String sendsmsNew(java.lang.String intoken, java.lang.String inseed, java.lang.String mobileno, java.lang.String smsmsg, java.lang.String username, java.lang.String senderid, java.lang.String appid, java.lang.String dltid, java.lang.String templateid) {
        main.Services_Service service = new main.Services_Service();
        main.Services port = service.getServicesPort();
        return port.sendsmsNew(intoken, inseed, mobileno, smsmsg, username, senderid, appid, dltid, templateid);
    }

    private static String sendmailNew(java.lang.String intoken, java.lang.String inseed, java.lang.String smtphost, java.lang.String smtpport, java.lang.String mailuser, java.lang.String mailto, java.lang.String mailsubject, java.lang.String mailtext, java.lang.String appid) {
        main.Services_Service service = new main.Services_Service();
        main.Services port = service.getServicesPort();
        return port.sendmailNew(intoken, inseed, smtphost, smtpport, mailuser, mailto, mailsubject, mailtext, appid);
    }

}
