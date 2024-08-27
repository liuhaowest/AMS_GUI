package com.student.utils;

import com.student.common.exception.MajorException;
import com.student.dao.entity.StUser;
import org.apache.shiro.SecurityUtils;

import java.util.*;

/**
 * @author 李123
 * 工具类
 */
public class MainUtils {
    /**
     * 根据年，月获取当前学年与学期
     *
     * @return map{"year":初始学年,"time":学期}
     */
    public static Map<String, Object> getStuDay(int year, int month) {
        Map<String, Object> map = new HashMap<>(2);
        if (month >= 3 && month < 9) {
            map.put("year", year - 1);
            map.put("time", "二");
        } else if (month >= 9) {
            //第一学期
            map.put("year", year);
            map.put("time", "一");
        } else {
            map.put("year", year - 1);
            map.put("time", "一");
        }
        return map;
    }

    /**
     * 构造如’2023-2024第一学期‘形式学年数据
     */
    public static List<String> initStuYear() {
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        if(user.getStuDay().equals("待完善")||user.getMajorId()==0){
            throw new MajorException("请完善自身入学日期，专业班级等信息");
        }
        String[] stuYear = user.getStuDay().split("-");
        int year = Integer.parseInt(stuYear[0]);
        int month = Integer.parseInt(stuYear[1]);
        Map<String, Object> earlyMap = getStuDay(year, month);
        int earlyYear = (int) earlyMap.get("year");
        String earlyTime = (String) earlyMap.get("time");
        //获取当前学期
        Calendar date = Calendar.getInstance();
        Map<String, Object> map = getStuDay(date.get(Calendar.YEAR), date.get(Calendar.MONTH));
        int temYear = (int) map.get("year");
        String temTime = (String) map.get("time");
        List<String> res = new LinkedList<>();
        for (int i = temYear; i >= earlyYear; i--) {
            if (i == temYear) {
                if ("二".equals(temTime)) {
                    res.add(i + "-" + (i + 1) + "第二学期");
                }
            } else {
                res.add(i + "-" + (i + 1) + "第二学期");
            }
            res.add(i + "-" + (i + 1) + "第一学期");

        }
        return res;
    }

    /**
     * 构造如’2022,一‘形式学年数据
     */
    public static List<String> initStuYearNum(){
        List<String> list = initStuYear();
        Collections.reverse(list);
        List<String> res=new LinkedList<>();
        for(String item:list){
            res.add(item.substring(0,4)+","+item.substring(10,11));
        }
        return res;
    }
}
