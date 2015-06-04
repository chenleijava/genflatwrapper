//this is gen file,logic controler 

#include "LoginController.h"
LoginController*LoginController::controller=new LoginController;

/**
负责处理数据分发    处理来自服务器数据
 */
void LoginController::dispatcherMessage(char *data) {

   auto loginresponse=getRoot<LoginResponse>(data);
  //TODO::处理来自服务器数据LoginResponse

}