package com.tensquare.spit.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Date;

/**
 * 吐槽实体
 * @author Chen
 * @created 2018-11-24-15:01.
 */
@Data
public class Spit implements Serializable {
    @Id
    private String _id;
    private String content;
    private Date publishtime;
    private String userid;
    private String nickname;
    private Integer visits;
    private Integer thumbup;
    private Integer share;
    private Integer comment;
    private String state;
    private String parentid;
}
