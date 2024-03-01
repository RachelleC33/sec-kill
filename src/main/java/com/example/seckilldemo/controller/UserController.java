package com.example.seckilldemo.controller;


import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.rabbitmq.MQSender;
import com.example.seckilldemo.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>

 * 了字节
 * @author zhoubin
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    //@RequestMapping(value = "/info", method = RequestMethod.GET)
    @RequestMapping(value = "/info")
    @ResponseBody
    // @ApiOperation("返回用户信息")
    public RespBean info(User user) {
        return RespBean.success(user);

    }

    /**
     * 测试发送RabbitMQ的消息
     */
    @RequestMapping(value = "/mq", method = RequestMethod.GET)
    @ResponseBody
    public void mq() {
        mqSender.send("Hello");
    }

    @RequestMapping(value = "/mq/fanout", method = RequestMethod.GET)
    @ResponseBody
    public void mqFanout() {
        mqSender.send("Hello");
    }

    @RequestMapping(value = "/mq/direct01", method = RequestMethod.GET)
    @ResponseBody
    public void mqDirect01() {
        mqSender.send01("Hello Red");
    }

    @RequestMapping(value = "/mq/direct02", method = RequestMethod.GET)
    @ResponseBody
    public void mqDirect02() {
        mqSender.send02("Hello Green");
    }


    @RequestMapping(value = "/mq/topic01", method = RequestMethod.GET)
    @ResponseBody
    public void mqtopic01() {
        mqSender.send03("Hello Red");
    }

    @RequestMapping(value = "/mq/topic02", method = RequestMethod.GET)
    @ResponseBody
    public void mqtopic02() {
        mqSender.send04("Hello Green");
    }
//
//
//    @RequestMapping(value = "/mq/header01", method = RequestMethod.GET)
//    @ResponseBody
//    public void mq06() {
//        mqSender.send05("Hello 01");
//    }
//
//    @RequestMapping(value = "/mq/header02", method = RequestMethod.GET)
//    @ResponseBody
//    public void mq07() {
//        mqSender.send06("Hello 02");
//
//    }
}
