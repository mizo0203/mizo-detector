package com.mizo0203.detector.util;

import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtil {

  private static final Logger LOG = Logger.getLogger(HttpUtil.class.getName());

  public static void post(
      URL url, Map<String, String> reqProp, @Nonnull String body, Callback callback) {
    LOG.info("post url:     " + url);
    LOG.info("post reqProp: " + reqProp);
    LOG.info("post body:    " + body);
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      for (String key : reqProp.keySet()) {
        connection.setRequestProperty(key, reqProp.get(key));
      }
      connection.setRequestProperty(
          "Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));
      connection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
      connection.getOutputStream().flush();
      LOG.info("getResponseCode():    " + connection.getResponseCode());
      LOG.info("getResponseMessage(): " + connection.getResponseMessage());
      if (connection.getErrorStream() != null) {
        LOG.severe(
            "getErrorStream(): "
                + IOUtils.toString(connection.getErrorStream(), StandardCharsets.UTF_8));
      }
      if (callback != null) {
        callback.response(connection.getResponseCode());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public interface Callback {

    void response(int responseCode);
  }
}
