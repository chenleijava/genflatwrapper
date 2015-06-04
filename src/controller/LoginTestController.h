//this is gen file,don't modify it 
#ifndef LoginTestController_h
#define LoginTestController_h
#include "IController.h"
class LoginTestController:public IController{
public:
static LoginTestController *controller;
public:
/**
负责处理数据分发    处理来自服务器数据
*/
virtual void dispatcherMessage(char *data);
};
#endif //LoginTestController_h