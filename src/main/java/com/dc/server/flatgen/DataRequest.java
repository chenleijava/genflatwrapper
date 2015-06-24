// automatically generated, do not modify

package com.dc.server.flatgen;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

public class DataRequest extends Table {
  public static DataRequest getRootAsDataRequest(ByteBuffer _bb) { return getRootAsDataRequest(_bb, new DataRequest()); }
  public static DataRequest getRootAsDataRequest(ByteBuffer _bb, DataRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public DataRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public static int msgID(){return 45;}

  public static int createDataRequest(FlatBufferBuilder builder,
      int msgID) {
    builder.startObject(1);
    DataRequest.addMsgID(builder, msgID);
    return DataRequest.endDataRequest(builder);
  }

  public static void startDataRequest(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addMsgID(FlatBufferBuilder builder, int msgID) { builder.addInt(0, msgID, 45); }
  public static int endDataRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

