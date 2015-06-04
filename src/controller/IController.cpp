//this is gen file,don't modify it 
#include "IController.h"
#include "LoginController.h"
#include "LoginTestController.h"

map<int, IController *> IController::controller_map;
void IController::dispatcherMessage(char *data) {}
/**
 * ×¢²ácontroller mapper
 */
void IController::registerMapperController() {
  log("%s", "registerMapperController¿ªÊ¼×¢²ácontroller... ...");
  controller_map[LoginResponse::msgID()]=LoginController::controller;
  controller_map[LoginTestResponse::msgID()]=LoginTestController::controller;
}
