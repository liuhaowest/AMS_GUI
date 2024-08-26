package com.example.vueback.Service.impl;

import com.example.vueback.Mapper.StudentMapper;
import com.example.vueback.pojo.Ifo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.vueback.Service.Student;
@Service
public class Studentimpl implements Student {
    @Autowired
    private StudentMapper familyMapper;
//  获取全部信息
    @Override
    public List<Ifo> getallinfo() {
       return familyMapper.getinfo();
    }


    @Override
    public void addinfo(Ifo ifo) {
        familyMapper.addinfo(ifo);
    }

    @Override
    public List<Ifo> searchinfo(Integer id) {
        List<Ifo> searchinfo = familyMapper.searchinfo(id);
        return searchinfo;
    }
}
