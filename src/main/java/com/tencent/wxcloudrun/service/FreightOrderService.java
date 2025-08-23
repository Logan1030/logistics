package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.FreightOrder;
import com.tencent.wxcloudrun.dto.FreightOrderRequest;
import java.util.List;
import java.util.Optional;

/**
 * 运费订单服务接口
 */
public interface FreightOrderService {

    /**
     * 创建运费订单
     */
    FreightOrder createFreightOrder(FreightOrderRequest request);

    /**
     * 根据ID查询运费订单
     */
    Optional<FreightOrder> getFreightOrderById(Long id);

    /**
     * 根据订单编号查询运费订单
     */
    Optional<FreightOrder> getFreightOrderByOrderNo(String orderNo);

    /**
     * 更新运费订单
     */
    FreightOrder updateFreightOrder(FreightOrder freightOrder);

    /**
     * 删除运费订单
     */
    boolean deleteFreightOrder(Long id);

    /**
     * 查询运费订单列表
     */
    List<FreightOrder> getFreightOrderList(String senderCity, String receiverCity, Integer status);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(Long orderId, Integer status);

    /**
     * 生成订单编号
     */
    String generateOrderNo();
}
