package com.yhs.onlineshopping.dao;

import com.yhs.onlineshopping.pojo.Resume;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IResumeDao {
    /**
     * 投简历
     * @param resume
     */
    public void toWorkVote(Resume resume);

    /**
     * 查询用户投递简历信息
     * @param uid
     * @return
     */
    List<Resume> toFindResumesByUid(int uid);

    /**
     * 显示投递简历
     * @return
     */
    List<Resume> toDisplayResumes();
}
