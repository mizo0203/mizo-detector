package com.mizo0203.detector.repo;

import com.linecorp.bot.model.ReplyMessage;

import java.io.Closeable;

public class LineMessagingClient implements Closeable {
  private final String mChannelToken;
  private final LineRepository mLineRepository;

  private LineMessagingClient(String channelToken) {
    mChannelToken = channelToken;
    mLineRepository = new LineRepository();
  }

  public static Builder builder(String channelToken) {
    return new Builder(channelToken);
  }

  @Override
  public void close() {
    mLineRepository.destroy();
  }

  /** 応答メッセージを送る */
  public void replyMessage(ReplyMessage accountLinkEvent) {
    mLineRepository.replyMessage(mChannelToken, accountLinkEvent);
  }

  public static class Builder {
    private final String mChannelToken;

    private Builder(String channelToken) {
      mChannelToken = channelToken;
    }

    public LineMessagingClient build() {
      return new LineMessagingClient(mChannelToken);
    }
  }
}
