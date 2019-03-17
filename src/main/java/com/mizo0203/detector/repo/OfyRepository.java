package com.mizo0203.detector.repo;

import com.googlecode.objectify.ObjectifyService;
import com.mizo0203.detector.repo.objectify.entity.Channel;

import java.io.Closeable;

public class OfyRepository implements Closeable {

  public Channel loadChannel(long id) {
    return ObjectifyService.ofy().load().type(Channel.class).id(id).now();
  }

  @SuppressWarnings("unused")
  public void saveChannel(Channel entity) {
    ObjectifyService.ofy().save().entity(entity).now();
  }

  @Override
  public void close() {}
}
