package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户注册
 */
@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    @Autowired
    private TbUserMapper userMapper;

    /**
     * 校验用户是否已存在
     * @param date 参数
     * @param type 类型 1：用户名  2：手机号 3：邮箱
     * @return 存在返回false否则返回true
     */
    @Override
    public E3Result checkUserData(String date, int type) {
        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(date);
        } else if (type ==2 ) {
            criteria.andPhoneEqualTo(date);
        } else if (type == 3) {
            criteria.andEmailEqualTo(date);
        } else {
            return E3Result.build(400,"校验类型不合法");
        }
        List<TbUser> tbUsers = userMapper.selectByExample(userExample);
        if (tbUsers != null && tbUsers.size()>0) {
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public E3Result registerUser(TbUser user) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
            return E3Result.build(400,"有效数据不能为空");
        }

        E3Result e3Result = checkUserData(user.getUsername(), 1);
        if ((boolean)e3Result.getData()) {
            return E3Result.build(400,"用户名已存在");
        }
        e3Result = checkUserData(user.getPhone(), 2);
        if ((boolean)e3Result.getData()) {
            return E3Result.build(400,"手机号");
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());

        String md5pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5pass);

        userMapper.insert(user);
        return E3Result.ok();
    }
}
