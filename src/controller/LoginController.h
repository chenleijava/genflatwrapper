//this is gen file,don't modify it 
#ifndef LoginController_h
#define LoginController_h
#include "IController.h"
class LoginController:public IController{
public:
static LoginController *controller;
public:
/**
负责处理数据分发    处理来自服务器数据
*/
virtual void dispatcherMessage(char *data);
};
#endif //LoginController_h