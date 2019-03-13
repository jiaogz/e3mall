package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;

    @Override
    public E3Result getUserByToken(String token) {
        String json = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)) {
            return E3Result.build(400,"用户登录已过期");
        }
        return E3Result.ok(JsonUtils.jsonToPojo(json,TbUser.class));
    }
}
