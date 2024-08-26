package com.example.vueback.Mapper;

import com.example.vueback.pojo.Ifo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
//  获取人员的全部信息
    @Select("select * from userinfo")
    public List<Ifo> getinfo();
//  增加人物信息
    @Insert("insert into userinfo (name,age,gender,image,xueli,address) values (#{name},#{age},#{gender},#{image},#{xueli},#{address})")
    public void addinfo(Ifo ifo);

//获取有条件人物信息
   public List<Ifo> searchinfo(Integer id);
}
