package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.vo.LoginVo;
import com.example.seckilldemo.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2024-01-03
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo);


    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    //根据cookie获取用户
    //User getUserByCookie(String userTicket);
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    //renew password
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);


}
