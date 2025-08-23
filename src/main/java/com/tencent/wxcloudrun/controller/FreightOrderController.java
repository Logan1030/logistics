package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.FreightOrderRequest;
import com.tencent.wxcloudrun.model.FreightOrder;
import com.tencent.wxcloudrun.service.FreightOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * 运费订单控制器
 */
@RestController
@RequestMapping("/api/freight")
public class FreightOrderController {

    private static final Logger logger = LoggerFactory.getLogger(FreightOrderController.class);
    
    private final FreightOrderService freightOrderService;
    
    @Autowired
    public FreightOrderController(FreightOrderService freightOrderService) {
        this.freightOrderService = freightOrderService;
    }

    /**
     * 一键下单 - 创建运费订单
     */
    @PostMapping("/order")
    public ApiResponse createOrder(@RequestBody FreightOrderRequest request) {
        try {
            logger.info("创建运费订单请求: {}", request);
            
            // 参数验证
            if (request.getSenderCity() == null || request.getSenderCity().trim().isEmpty()) {
                return ApiResponse.error("发货城市不能为空");
            }
            if (request.getReceiverCity() == null || request.getReceiverCity().trim().isEmpty()) {
                return ApiResponse.error("收货城市不能为空");
            }
            if (request.getReceiverName() == null || request.getReceiverName().trim().isEmpty()) {
                return ApiResponse.error("收货人姓名不能为空");
            }
            
            // 创建运费订单
            FreightOrder freightOrder = freightOrderService.createFreightOrder(request);
            
            logger.info("运费订单创建成功，订单编号: {}", freightOrder.getOrderNo());
            return ApiResponse.ok(freightOrder);
            
        } catch (Exception e) {
            logger.error("创建运费订单异常", e);
            return ApiResponse.error("创建运费订单失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询运费订单
     */
    @GetMapping("/order/{id}")
    public ApiResponse getOrderById(@PathVariable Long id) {
        try {
            logger.info("查询运费订单，ID: {}", id);
            
            Optional<FreightOrder> freightOrder = freightOrderService.getFreightOrderById(id);
            if (freightOrder.isPresent()) {
                return ApiResponse.ok(freightOrder.get());
            } else {
                return ApiResponse.error("运费订单不存在");
            }
            
        } catch (Exception e) {
            logger.error("查询运费订单异常", e);
            return ApiResponse.error("查询运费订单失败: " + e.getMessage());
        }
    }

    /**
     * 根据订单编号查询运费订单
     */
    @GetMapping("/order/no/{orderNo}")
    public ApiResponse getOrderByOrderNo(@PathVariable String orderNo) {
        try {
            logger.info("查询运费订单，订单编号: {}", orderNo);
            
            Optional<FreightOrder> freightOrder = freightOrderService.getFreightOrderByOrderNo(orderNo);
            if (freightOrder.isPresent()) {
                return ApiResponse.ok(freightOrder.get());
            } else {
                return ApiResponse.error("运费订单不存在");
            }
            
        } catch (Exception e) {
            logger.error("查询运费订单异常", e);
            return ApiResponse.error("查询运费订单失败: " + e.getMessage());
        }
    }

    /**
     * 查询运费订单列表
     */
    @GetMapping("/orders")
    public ApiResponse getOrderList(@RequestParam(required = false) String senderCity,
                                   @RequestParam(required = false) String receiverCity,
                                   @RequestParam(required = false) Integer status) {
        try {
            logger.info("查询运费订单列表，发货城市: {}, 收货城市: {}, 状态: {}", senderCity, receiverCity, status);
            
            List<FreightOrder> orders = freightOrderService.getFreightOrderList(senderCity, receiverCity, status);
            return ApiResponse.ok(orders);
            
        } catch (Exception e) {
            logger.error("查询运费订单列表异常", e);
            return ApiResponse.error("查询运费订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 更新运费订单状态
     */
    @PutMapping("/order/{id}/status")
    public ApiResponse updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            logger.info("更新运费订单状态，订单ID: {}, 状态: {}", id, status);
            
            // 状态值验证
            if (status < 1 || status > 5) {
                return ApiResponse.error("无效的订单状态值");
            }
            
            boolean success = freightOrderService.updateOrderStatus(id, status);
            if (success) {
                return ApiResponse.ok("订单状态更新成功");
            } else {
                return ApiResponse.error("订单状态更新失败");
            }
            
        } catch (Exception e) {
            logger.error("更新运费订单状态异常", e);
            return ApiResponse.error("更新运费订单状态失败: " + e.getMessage());
        }
    }

    /**
     * 删除运费订单
     */
    @DeleteMapping("/order/{id}")
    public ApiResponse deleteOrder(@PathVariable Long id) {
        try {
            logger.info("删除运费订单，订单ID: {}", id);
            
            boolean success = freightOrderService.deleteFreightOrder(id);
            if (success) {
                return ApiResponse.ok("运费订单删除成功");
            } else {
                return ApiResponse.error("运费订单删除失败");
            }
            
        } catch (Exception e) {
            logger.error("删除运费订单异常", e);
            return ApiResponse.error("删除运费订单失败: " + e.getMessage());
        }
    }
}
