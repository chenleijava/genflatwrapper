import org.springframework.stereotype.Service;


@Service
public class DataController extends AbstractController {

    /**
     * spring 容器初始化 加载并初始化相应控制器处理句柄
     * 非spring环境下 可以采用静态的初始化方案
     */
    @Override
    public void PostConstruct() {
        game_controllers[DataRequest.msgID()] = this;
    }



    /**
     * 基于flatBuffers 结构数据传输
     *
     * @param data   cast subclass of table .         msgID + 游戏数据
     * @param player game session
     * @throws Exception
     */
    @Override
    public void DispatchFlatBuffer(byte[] data, PlayerInstance player) throws Exception {

    }

}