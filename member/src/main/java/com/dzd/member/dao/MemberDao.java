package com.dzd.member.dao;

import com.dzd.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author Deng ZhengDong
 * @email 22028165@zju.edu.cn
 * @date 2021-11-02 21:25:44
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
