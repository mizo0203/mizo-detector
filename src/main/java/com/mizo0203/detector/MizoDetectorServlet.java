package com.mizo0203.detector;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.servlet.LineBotCallbackException;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.mizo0203.detector.repo.LineMessagingClient;
import com.mizo0203.detector.repo.OfyRepository;
import com.mizo0203.detector.repo.objectify.entity.Channel;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class MizoDetectorServlet extends HttpServlet {

  @SuppressWarnings("unused")
  private static final Logger LOG = Logger.getLogger(MizoDetectorServlet.class.getName());

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      onLineWebhook(req, resp);
    } catch (LineBotCallbackException e) {
      e.printStackTrace();
    }
  }

  private void onLineWebhook(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, LineBotCallbackException {

    OfyRepository ofyRepository = new OfyRepository();
    Channel channel =
        Objects.requireNonNull(
            ofyRepository.loadChannel(1555539889L)); // Channel ID - チャネルを区別するための識別子です。

    LineMessagingClient client = LineMessagingClient.builder(channel.getToken()).build();

    LineBotCallbackRequestParser parser =
        new LineBotCallbackRequestParser(
            new LineSignatureValidator(channel.getSecret().getBytes()));
    CallbackRequest callbackRequest = parser.handle(req);
    for (Event event : callbackRequest.getEvents()) {
      LOG.info("Sender Id: " + event.getSource().getSenderId());
      LOG.info("User Id:   " + event.getSource().getUserId());
      LOG.info("Timestamp: " + event.getTimestamp().toString());
      if (event instanceof AccountLinkEvent) {
        LOG.info("AccountLinkEvent");
      } else if (event instanceof BeaconEvent) {
        LOG.info("BeaconEvent");
        client.replyMessage(
            new ReplyMessage(((BeaconEvent) event).getReplyToken(), new TextMessage("近くにみぞがいます")));
      } else if (event instanceof FollowEvent) {
        LOG.info("FollowEvent");
        client.replyMessage(
            new ReplyMessage(
                ((FollowEvent) event).getReplyToken(),
                Arrays.asList(
                    new TextMessage[] {
                      new TextMessage("スマートフォンのBluetoothがオンになっていることを確認します。"),
                      new TextMessage("LINEで［設定］>［プライバシー管理］の順に選択し、［LINE Beacon］チェックボックスをオンにします。"),
                    })));
      } else if (event instanceof JoinEvent) {
        LOG.info("JoinEvent");
      } else if (event instanceof LeaveEvent) {
        LOG.info("LeaveEvent");
      } else if (event instanceof MessageEvent) {
        LOG.info("MessageEvent");
      } else if (event instanceof PostbackEvent) {
        LOG.info("PostbackEvent");
      } else if (event instanceof ThingsEvent) {
        LOG.info("ThingsEvent");
      } else if (event instanceof UnfollowEvent) {
        LOG.info("UnfollowEvent");
      } else if (event instanceof UnknownEvent) {
        LOG.info("UnknownEvent");
      }
    }
    // ボットアプリのサーバーに webhook から送信される HTTP POST リクエストには、ステータスコード 200 を返す必要があります。
    // https://developers.line.me/ja/docs/messaging-api/reference/#anchor-99cdae5b4b38ad4b86a137b508fd7b1b861e2366
    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
