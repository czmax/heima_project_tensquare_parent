package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ProblemDao extends JpaRepository<Problem, String>, JpaSpecificationExecutor<Problem> {
    /**
     * 根据标签ID查询最新问题列表
     *
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem ,tb_pl WHERE id = problemid AND labelid=?1 ORDER BY replytime DESC", nativeQuery = true)
    public Page<Problem> findNewListByLabelId(String labelId, Pageable pageable);

    /**
     * 根据标签ID查询热门问题列表
     *
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem ,tb_pl WHERE id = problemid AND labelid=?1 ORDER BY reply DESC", nativeQuery = true)
    public Page<Problem> findHotListByLabelId(String labelId, Pageable pageable);

    /**
     * 根据标签ID查询等待回答列表
     *
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem ,tb_pl WHERE id = problemid AND labelid=?1 AND reply=0 ORDER BY createtime DESC", nativeQuery = true)
    public Page<Problem> findWaitListByLabelId(String labelId, Pageable pageable);
}
