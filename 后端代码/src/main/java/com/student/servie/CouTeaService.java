package com.student.servie;


import com.student.common.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 李123
 * 学生课堂的接口类
 */
public interface CouTeaService {
    /**
     * 更新成绩
     * @param grade 成绩
     * @param roomId 课堂id
     * @param stuId 学生id
     * @return su/fa
     */
    Result updateGrade(int grade,Long roomId,Long stuId);

    /**
     * 教师强制退课
     * @param roomId 课堂id
     * @param stuId  学生id
     * @return sc/fa
     */
    Result exitRoom(Long roomId,Long stuId,int couType);

    /**
     * 上传excel文件更新成绩
     * @param file 文件
     * @return sc/fa
     */
    Result upGrade(MultipartFile file,Long roomId) throws IOException;
}
