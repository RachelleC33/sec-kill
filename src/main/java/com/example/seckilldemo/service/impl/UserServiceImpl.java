package com.example.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckilldemo.exception.GlobalException;
import com.example.seckilldemo.mapper.UserMapper;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.service.IUserService;
import com.example.seckilldemo.utils.CookieUtil;
import com.example.seckilldemo.utils.MD5Util;
import com.example.seckilldemo.utils.UUIDUtil;
import com.example.seckilldemo.vo.LoginVo;
import com.example.seckilldemo.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.example.seckilldemo.vo.RespBeanEnum;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2024-01-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo) {
        return null;
    }



    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response)  {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
////        参数校验
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
//
//        if (!ValidatorUtil.isMobile(mobile)) {
//            RespBean response1 = RespBean.error(RespBeanEnum.MOBILE_ERROR);
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //判断密码是否正确
        if (!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
            //throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
       // RespBean response = RespBean.success();
        //生成Cookie
        String userTicket = UUIDUtil.uuid();
       // request.getSession().setAttribute(userTicket,user);
        //将用户信息存入redis
        redisTemplate.opsForValue().set("user:" + userTicket, user);

//        request.getSession().setAttribute(userTicket, user);
        CookieUtil.setCookie(request, response, "userTicket", userTicket);
        //return RespBean.success(userTicket);

        return RespBean.success(userTicket);

    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }


    /**
     * renew password
     * @param userTicket
     * @param id
     * @param password
     * @return
     */

    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSalt()));
        int result = userMapper.updateById(user);
        if (1 == result) {
            //删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }


//    @Override
//    public RespBean updatePassword(String userTicket, Long id, String password) {
//        User user = userMapper.selectById(id);
//        if (user == null) {
//            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
//        }
//        user.setPassword(MD5Util.inputPassToDbPass(password, user.getSalt()));
//        int result = userMapper.updateById(user);
//        if (1 == result) {
////删除Redis
//            redisTemplate.delete("user:" + userTicket); return RespBean.success();
//        }
//        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
//    }

//    @Override
//    public RespBean doLongin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
//        return null;
//    }
}
