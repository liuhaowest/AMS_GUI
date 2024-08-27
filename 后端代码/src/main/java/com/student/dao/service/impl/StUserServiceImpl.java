package com.student.dao.service.impl;

import com.student.dao.entity.StUser;
import com.student.dao.mapper.StUserMapper;
import com.student.dao.service.IStUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liHeWei
 * @since 2022-04-29
 */
@Service
public class StUserServiceImpl extends ServiceImpl<StUserMapper, StUser> implements IStUserService {

}
