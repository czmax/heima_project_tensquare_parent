package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签的业务层
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有标签
     *
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     * 保存
     *
     * @param label
     */
    public void save(Label label) {
        //设置id
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }

    /**
     * 更新
     *
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) {
        labelDao.deleteById(id);
    }


    /**
     * 条件查询
     *
     * @param label
     * @return
     */
    public List<Label> findSearch(Label label) {

        return labelDao.findAll(new Specification<Label>() {
            /**
             *
             * @param root  根对象，也就是要把条件封装到那个对象中，where 类名=label.getid
             * @param criteriaQuery 封装的都是查询关键字，比如group by ，order by等
             * @param criteriaBuilder   用来封装条件对象，如果直接返回null，表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                ArrayList<Predicate> list = new ArrayList<>();
                // 判断输入了标签名称
                if (!StringUtils.isEmpty(label.getLabelname())) {
                    Predicate p1 = criteriaBuilder.like(root.get("labelname"), label.getLabelname());//想当于 where labelname like '%小明%'
                    list.add(p1);
                }
                // 判断了标签状态
                if (!StringUtils.isEmpty(label.getState())) {
                    Predicate p2 = criteriaBuilder.equal(root.get("state"), label.getState());
                    list.add(p2);
                }
                // 判断了标签的是否推荐
                if (!StringUtils.isEmpty(label.getRecommend())) {
                    Predicate p3 = criteriaBuilder.like(root.get("recommend"), label.getRecommend());
                    list.add(p3);
                }

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        });
    }


    /**
     * 分页条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> pageQuery(Map searchMap, int page, int size) {


        // 1.定义条件对象
        Specification specification = createSpecification(searchMap);
        Pageable pageable = PageRequest.of(page - 1, size);
        // 2.条件查询

        return labelDao.findAll(specification, pageable);
    }

    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(searchMap.get("labelname"))) {
                    predicateList.add(cb.like(root.get("labelname").as(String.class), "%" + (String) searchMap.get("labelname") + "%"));
                }
                if (!StringUtils.isEmpty(searchMap.get("state"))) {
                    predicateList.add(cb.equal(root.get("state").as(String.class), (String) searchMap.get("state")));
                }
                if (!StringUtils.isEmpty(searchMap.get("recommend"))) {
                    predicateList.add(cb.equal(root.get("recommend").as(String.class), (String) searchMap.get("recommend")));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
