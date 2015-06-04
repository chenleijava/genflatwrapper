//warn this is gen file,don't modify it 
#ifndef _ICONTROLLER_H
#define _ICONTROLLER_H
#include <map>
#include "cocos2d.h"
#include "flatgen/Game_generated.h"
using  namespace cocos2d;
using namespace gen;
using  namespace std;
#ifndef delete_array
#define delete_array(p)     do { if(p) { delete[] (p); (p) = nullptr; } } while(0)
#endif //delete_array
class IController {

public:
  IController() { }
 /**
  注册消息ID 和对应处理controller的关系
  以便于快速索引处理
  */
  static std::map<int, IController *> controller_map;
  /**
  负责处理数据分发
  */
  virtual void dispatcherMessage(char *data);
  /**
  转化对应消息为flatbuffers实体
  data+4: 消息ID 占用4 字节
  */
  template <typename  T> const  T* getRoot(char* data);
 /**
  * @Author 石头哥哥, 15-06-01 23:06:26
  *
  * @brief  注册controoler and mapper
  */
  static void registerMapperController();
};
template<typename T>
inline const T *IController::getRoot(char *data) {
    return flatbuffers::GetRoot<T>(data+4);
}
#endif//_ICONTROLLER_H