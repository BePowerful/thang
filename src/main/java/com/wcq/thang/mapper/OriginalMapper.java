package com.wcq.thang.mapper;

import com.wcq.thang.model.Original;
import com.wcq.thang.model.OriginalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OriginalMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    long countByExample(OriginalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int deleteByExample(OriginalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int deleteByPrimaryKey(Integer originalId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int insert(Original record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int insertSelective(Original record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    List<Original> selectByExample(OriginalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    Original selectByPrimaryKey(Integer originalId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int updateByExampleSelective(@Param("record") Original record, @Param("example") OriginalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int updateByExample(@Param("record") Original record, @Param("example") OriginalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int updateByPrimaryKeySelective(Original record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table original
     *
     * @mbg.generated Sat Dec 07 09:30:00 CST 2019
     */
    int updateByPrimaryKey(Original record);
}