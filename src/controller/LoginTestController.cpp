//this is gen file,logic controler 

#include "LoginTestController.h"
LoginTestController*LoginTestController::controller=new LoginTestController;

/**
负责处理数据分发    处理来自服务器数据
 */
void LoginTestController::dispatcherMessage(char *data) {

   auto logintestresponse=getRoot<LoginTestResponse>(data);
  //TODO::处理来自服务器数据LoginTestResponse

}