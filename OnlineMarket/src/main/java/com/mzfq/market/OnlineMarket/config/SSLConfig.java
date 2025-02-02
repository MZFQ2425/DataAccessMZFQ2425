package com.mzfq.market.OnlineMarket.config;


import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SSLConfig {
    private static SSLContext getSSLContext()
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                   String authType) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                   String authType) {}
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }}, null);
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        return sslContext;
    }
}
