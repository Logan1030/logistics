package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.FreightOrder;
import com.tencent.wxcloudrun.model.FreightOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 运费订单数据访问接口
 */
@Mapper
public interface FreightOrderMapper {

    /**
     * 插入运费订单
     */
    int insertFreightOrder(FreightOrder freightOrder);

    /**
     * 根据ID查询运费订单
     */
    FreightOrder getFreightOrderById(@Param("id") Long id);

    /**
     * 根据订单编号查询运费订单
     */
    FreightOrder getFreightOrderByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 更新运费订单
     */
    int updateFreightOrder(FreightOrder freightOrder);

    /**
     * 删除运费订单
     */
    int deleteFreightOrder(@Param("id") Long id);

    /**
     * 查询运费订单列表
     */
    List<FreightOrder> getFreightOrderList(@Param("senderCity") String senderCity, 
                                          @Param("receiverCity") String receiverCity,
                                          @Param("status") Integer status);

    /**
     * 插入运费订单详情
     */
    int insertFreightOrderDetail(FreightOrderDetail detail);

    /**
     * 根据订单ID查询详情列表
     */
    List<FreightOrderDetail> getFreightOrderDetailsByOrderId(@Param("orderId") Long orderId);

    /**
     * 删除运费订单详情
     */
    int deleteFreightOrderDetailsByOrderId(@Param("orderId") Long orderId);
}
