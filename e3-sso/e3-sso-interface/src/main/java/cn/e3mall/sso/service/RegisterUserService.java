package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterUserService {

    E3Result checkUserData(String date,int type);

    E3Result registerUser(TbUser user);
}
