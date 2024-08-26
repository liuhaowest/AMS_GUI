package com.example.vueback.Service;

import com.example.vueback.pojo.Ifo;

import java.util.List;


public interface Student {
//   获取全部信息
    public List<Ifo> getallinfo();
//   增加信息
    public void addinfo(Ifo ifo);
//  搜查信息
    public List<Ifo> searchinfo(Integer id);
}
