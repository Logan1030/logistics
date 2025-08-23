package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.FreightOrderMapper;
import com.tencent.wxcloudrun.model.FreightOrder;
import com.tencent.wxcloudrun.model.FreightOrderDetail;
import com.tencent.wxcloudrun.dto.FreightOrderRequest;
import com.tencent.wxcloudrun.service.FreightOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ArrayList;

/**
 * 运费订单服务实现类
 */
@Service
public class FreightOrderServiceImpl implements FreightOrderService {

    private static final Logger logger = LoggerFactory.getLogger(FreightOrderServiceImpl.class);
    
    private final FreightOrderMapper freightOrderMapper;
    
    @Autowired
    public FreightOrderServiceImpl(FreightOrderMapper freightOrderMapper) {
        this.freightOrderMapper = freightOrderMapper;
    }

    @Override
    @Transactional
    public FreightOrder createFreightOrder(FreightOrderRequest request) {
        try {
            // 创建运费订单
            FreightOrder freightOrder = new FreightOrder();
            freightOrder.setOrderNo(generateOrderNo());
            freightOrder.setSenderCity(request.getSenderCity());
            freightOrder.setReceiverCity(request.getReceiverCity());
            freightOrder.setReceiverName(request.getReceiverName());
            freightOrder.setCargoCode(request.getCargoCode());
            freightOrder.setCargoType(request.getCargoType());
            freightOrder.setVehicleType(request.getVehicleType());
            freightOrder.setFreightResult(request.getFreightResult());
            freightOrder.setBillingWeight(request.getBillingWeight());
            freightOrder.setCostStatistics(request.getCostStatistics());
            freightOrder.setTotalAmount(request.getTotalAmount() != null ? request.getTotalAmount() : BigDecimal.ZERO);
            freightOrder.setStatus(1); // 1-待确认
            freightOrder.setRemark(request.getRemark());
            freightOrder.setCreatedAt(LocalDateTime.now());
            freightOrder.setUpdatedAt(LocalDateTime.now());

            // 插入运费订单
            int result = freightOrderMapper.insertFreightOrder(freightOrder);
            if (result > 0) {
                logger.info("运费订单创建成功，订单编号: {}", freightOrder.getOrderNo());
                return freightOrder;
            } else {
                logger.error("运费订单创建失败");
                throw new RuntimeException("运费订单创建失败");
            }
        } catch (Exception e) {
            logger.error("创建运费订单异常", e);
            throw new RuntimeException("创建运费订单异常: " + e.getMessage());
        }
    }

    @Override
    public Optional<FreightOrder> getFreightOrderById(Long id) {
        try {
            FreightOrder freightOrder = freightOrderMapper.getFreightOrderById(id);
            return Optional.ofNullable(freightOrder);
        } catch (Exception e) {
            logger.error("根据ID查询运费订单异常", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<FreightOrder> getFreightOrderByOrderNo(String orderNo) {
        try {
            FreightOrder freightOrder = freightOrderMapper.getFreightOrderByOrderNo(orderNo);
            return Optional.ofNullable(freightOrder);
        } catch (Exception e) {
            logger.error("根据订单编号查询运费订单异常", e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public FreightOrder updateFreightOrder(FreightOrder freightOrder) {
        try {
            freightOrder.setUpdatedAt(LocalDateTime.now());
            int result = freightOrderMapper.updateFreightOrder(freightOrder);
            if (result > 0) {
                logger.info("运费订单更新成功，订单ID: {}", freightOrder.getId());
                return freightOrder;
            } else {
                logger.error("运费订单更新失败");
                throw new RuntimeException("运费订单更新失败");
            }
        } catch (Exception e) {
            logger.error("更新运费订单异常", e);
            throw new RuntimeException("更新运费订单异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteFreightOrder(Long id) {
        try {
            // 先删除订单详情
            freightOrderMapper.deleteFreightOrderDetailsByOrderId(id);
            // 再删除订单
            int result = freightOrderMapper.deleteFreightOrder(id);
            if (result > 0) {
                logger.info("运费订单删除成功，订单ID: {}", id);
                return true;
            } else {
                logger.error("运费订单删除失败");
                return false;
            }
        } catch (Exception e) {
            logger.error("删除运费订单异常", e);
            return false;
        }
    }

    @Override
    public List<FreightOrder> getFreightOrderList(String senderCity, String receiverCity, Integer status) {
        try {
            return freightOrderMapper.getFreightOrderList(senderCity, receiverCity, status);
        } catch (Exception e) {
            logger.error("查询运费订单列表异常", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, Integer status) {
        try {
            FreightOrder freightOrder = new FreightOrder();
            freightOrder.setId(orderId);
            freightOrder.setStatus(status);
            freightOrder.setUpdatedAt(LocalDateTime.now());
            
            int result = freightOrderMapper.updateFreightOrder(freightOrder);
            if (result > 0) {
                logger.info("运费订单状态更新成功，订单ID: {}, 状态: {}", orderId, status);
                return true;
            } else {
                logger.error("运费订单状态更新失败");
                return false;
            }
        } catch (Exception e) {
            logger.error("更新运费订单状态异常", e);
            return false;
        }
    }

    @Override
    public String generateOrderNo() {
        // 生成订单编号：年月日时分秒 + 4位随机数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String random = String.format("%04d", new Random().nextInt(10000));
        return "FO" + timestamp + random;
    }
}
