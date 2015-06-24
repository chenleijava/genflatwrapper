// automatically generated, do not modify

package gen;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

public class DataRequest extends Table {
  public static DataRequest getRootAsDataRequest(ByteBuffer _bb) { return getRootAsDataRequest(_bb, new DataRequest()); }
  public static DataRequest getRootAsDataRequest(ByteBuffer _bb, DataRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public DataRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int msgID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 4; }

  public static int createDataRequest(FlatBufferBuilder builder,
      int msgID) {
    builder.startObject(1);
    DataRequest.addMsgID(builder, msgID);
    return DataRequest.endDataRequest(builder);
  }

  public static void startDataRequest(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addMsgID(FlatBufferBuilder builder, int msgID) { builder.addInt(0, msgID, 4); }
  public static int endDataRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

