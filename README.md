
###基于flatbuffers ，使用fbs文件生成对应前端代码。
###完成对后端生成代码自动修改msgID。更便于游戏生产。

```
1.使用gen Tools 生成对应客户端代码你需要严格约定
2.table命名约定 上下行消息 xxRequest xxResponse
3.前后端交互消息 存在一个消息ID；命名约定： msgID
4.命名空间 使用gen ---主要限制前端 
```
 
 ```
namespace gen;
//下行消息 xxRequest
table LoginRequest{
     msgID:int=1; //消息ID msgID
     username:string;
}
//下行消息 xxResponse
table LoginResponse{
     msgID:int=1;     //消息ID msgID
     username:string;
}
```

####如何使用:
```
该工具可以生成基于flatbuffers的代码，接口说明如下
使用命令：
use cmd：java -jar genflatMapper.jar -l cpp -c controllerpath -f fbspath
>-l ： cpp ,java ;
>-c:  game controller path or java files；
>-f: game of fbs path；
>-g:  gen flat header
###对应的工具已经在build中打包完毕，如果有疑问请联系我：qq:502959937
```

