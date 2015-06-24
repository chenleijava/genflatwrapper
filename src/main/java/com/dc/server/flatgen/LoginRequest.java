// automatically generated, do not modify

package com.dc.server.flatgen;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

public class LoginRequest extends Table {
  public static LoginRequest getRootAsLoginRequest(ByteBuffer _bb) { return getRootAsLoginRequest(_bb, new LoginRequest()); }
  public static LoginRequest getRootAsLoginRequest(ByteBuffer _bb, LoginRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public LoginRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public static int msgID(){return 1;}
  public String username() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer usernameAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }

  public static int createLoginRequest(FlatBufferBuilder builder,
      int msgID,
      int username) {
    builder.startObject(2);
    LoginRequest.addUsername(builder, username);
    LoginRequest.addMsgID(builder, msgID);
    return LoginRequest.endLoginRequest(builder);
  }

  public static void startLoginRequest(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addMsgID(FlatBufferBuilder builder, int msgID) { builder.addInt(0, msgID, 1); }
  public static void addUsername(FlatBufferBuilder builder, int usernameOffset) { builder.addOffset(1, usernameOffset, 0); }
  public static int endLoginRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

