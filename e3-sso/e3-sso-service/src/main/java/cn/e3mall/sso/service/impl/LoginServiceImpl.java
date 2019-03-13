package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${TOKEN_EXPIRE}")
    private Integer TOKEN_EXPIRE;

    @Override
    public E3Result Login(String uname, String password) {
        //1.查询数据库验证用户是否存在
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(uname);
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (tbUsers == null && tbUsers.size() == 0) {
            return E3Result.build(400,"用户名或密码错误");
        }
        if (!tbUsers.get(0).getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return E3Result.build(400,"用户名或密码错误");
        }
        //存在，生成token信息保存到redis中
        String token = UUID.randomUUID().toString();
        tbUsers.get(0).setPassword(null);
        jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(tbUsers.get(0)));
        jedisClient.expire("SESSION:"+token,TOKEN_EXPIRE);
        return E3Result.ok(token);
    }
}
