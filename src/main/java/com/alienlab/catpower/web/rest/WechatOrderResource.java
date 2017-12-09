package com.alienlab.catpower.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.catpower.domain.WechatGoodsList;
import com.alienlab.catpower.service.WechatGoodsListService;
import com.alienlab.catpower.web.rest.util.ExecResult;
import com.alienlab.catpower.web.wechat.bean.entity.WechatUser;
import com.alienlab.catpower.web.wechat.service.WechatMessageService;
import com.alienlab.catpower.web.wechat.service.WechatService;
import com.alienlab.catpower.web.wechat.service.WechatUserService;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.catpower.domain.WechatOrder;
import com.alienlab.catpower.service.WechatOrderService;
import com.alienlab.catpower.web.rest.util.HeaderUtil;
import com.alienlab.catpower.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing WechatOrder.
 */
@RestController
@RequestMapping("/api")
public class WechatOrderResource {

    private final Logger log = LoggerFactory.getLogger(WechatOrderResource.class);

    private static final String ENTITY_NAME = "wechatOrder";

    private final WechatOrderService wechatOrderService;


    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatService wechatService;

    @Autowired
    WechatMessageService wechatMessageService;

    @Autowired
    WechatGoodsListService wechatGoodsListService;

    public WechatOrderResource(WechatOrderService wechatOrderService) {
        this.wechatOrderService = wechatOrderService;
    }

    /**
     * POST  /wechat-orders : Create a new wechatOrder.
     *
     * @param wechatOrder the wechatOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wechatOrder, or with status 400 (Bad Request) if the wechatOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wechat-orders")
    @Timed
    public ResponseEntity<WechatOrder> createWechatOrder(@RequestBody WechatOrder wechatOrder) throws URISyntaxException {
        log.debug("REST request to save WechatOrder : {}", wechatOrder);
        if (wechatOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wechatOrder cannot already have an ID")).body(null);
        }
        WechatOrder result = wechatOrderService.save(wechatOrder);
        return ResponseEntity.created(new URI("/api/wechat-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wechat-orders : Updates an existing wechatOrder.
     *
     * @param wechatOrder the wechatOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wechatOrder,
     * or with status 400 (Bad Request) if the wechatOrder is not valid,
     * or with status 500 (Internal Server Error) if the wechatOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wechat-orders")
    @Timed
    public ResponseEntity<WechatOrder> updateWechatOrder(@RequestBody WechatOrder wechatOrder) throws URISyntaxException {
        log.debug("REST request to update WechatOrder : {}", wechatOrder);
        if (wechatOrder.getId() == null) {
            return createWechatOrder(wechatOrder);
        }
        WechatOrder result = wechatOrderService.save(wechatOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wechatOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wechat-orders : get all the wechatOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wechatOrders in body
     */
    @GetMapping("/wechat-orders")
    @Timed
    public ResponseEntity<List<WechatOrder>> getAllWechatOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WechatOrders");
        Page<WechatOrder> page = wechatOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wechat-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wechat-orders/:id : get the "id" wechatOrder.
     *
     * @param id the id of the wechatOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wechatOrder, or with status 404 (Not Found)
     */
    @GetMapping("/wechat-orders/{id}")
    @Timed
    public ResponseEntity<WechatOrder> getWechatOrder(@PathVariable Long id) {
        log.debug("REST request to get WechatOrder : {}", id);
        WechatOrder wechatOrder = wechatOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wechatOrder));
    }

    /**
     * DELETE  /wechat-orders/:id : delete the "id" wechatOrder.
     *
     * @param id the id of the wechatOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wechat-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteWechatOrder(@PathVariable Long id) {
        log.debug("REST request to delete WechatOrder : {}", id);
        wechatOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }



    @ApiOperation("微信售卖商品下单")
    @PostMapping("/wechat-orders/json")
    @Timed
    public ResponseEntity createArtworkOrder(@RequestBody Map<String,String> param, HttpServletRequest request) throws URISyntaxException {
        log.debug("createArtworkOrder>>>>"+ JSON.toJSONString(param));

        String openid=param.get("openid");
        WechatUser user=wechatUserService.findUserByOpenid(openid);
        if(user==null){
            ExecResult er=new ExecResult(false,"用户id不存在,"+openid);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        String goodsId=param.get("goodsId");
        WechatGoodsList goods=wechatGoodsListService.findOne(Long.parseLong(goodsId));
        if(goods==null){
            ExecResult er=new ExecResult(false,"未找到ID为"+goodsId+"的商品");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }

        try {
            //获取该用户已经完成的订单
            List<WechatOrder> orders=wechatOrderService.findBuyRecordsByOpenid(openid);
            if(orders.size()>=goods.getLimitCount()){
                ExecResult er=new ExecResult(false,"已超出限制的购买次数.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        } catch (Exception e) {
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }

        WechatOrder order=new WechatOrder();
        order.setOrderNo(UUID.randomUUID().toString().replaceAll("-",""));
        order.setOrderTime(ZonedDateTime.now());
        order.setOrderStatus("未支付");
        order.setWechatGoodsList(goods);
        order.setWechatUser(user);

        //微信下单支付
        Map<String,String> orderResult=wechatService.makeOrder(goods.getGoodsName(),order.getOrderNo(),
            goods.getGoodsPrice().intValue(),request.getRemoteAddr(),openid);

        if(orderResult==null){
            ExecResult er=new ExecResult(false,"调用微信支付失败。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        String orderflag=orderResult.get("return_code");
        String resultcode=orderResult.get("result_code");
        if(orderflag.equalsIgnoreCase("SUCCESS")) {
            if (resultcode.equalsIgnoreCase("SUCCESS")) {//订单创建成功
                JSONObject orderInfo=wechatService.getPayParam(orderResult);
                order=wechatOrderService.save(order);
                JSONObject result=new JSONObject();
                result.put("order",order);
                result.put("orderInfo",orderInfo);
                return ResponseEntity.ok().body(result);
            }else{ //如果下单出现错误，返回错误信息到页面
                ExecResult er=new ExecResult(false,orderResult.get("err_code_des"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{ //如果获取订单错误，返回错误信息到页面
            ExecResult er=new ExecResult(false,orderResult.get("return_msg"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("微信订单支付完成")
    @PostMapping("/wechat-orders/pay")
    @Timed
    public ResponseEntity payOrder(@RequestBody Map param){
        log.debug("payOrder>>>"+JSON.toJSONString(param));
        Long orderId= TypeUtils.castToLong(param.get("orderId"));
        String openid=TypeUtils.castToString(param.get("openid"));
        WechatOrder order=wechatOrderService.findOne(orderId);
        if(order==null){
            ExecResult er=new ExecResult(false,"查询不到订单信息,"+orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        Map<String,String> orderinfo=wechatService.getOrder(order.getOrderNo());
        log.info("orderpay>>>"+ JSON.toJSONString(orderinfo));
        if(orderinfo==null){
            ExecResult er=new ExecResult(false,"获取订单信息出错");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        String returncode=orderinfo.get("return_code");
        String resultcode=orderinfo.get("result_code");
        if(!order.getWechatUser().getOpenId().equals(openid)){
            ExecResult er=new ExecResult(false,"订单创建用户与支付用户不匹配，支付失败。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if(orderinfo.containsKey("trade_state")){
            String tradestatus=orderinfo.get("trade_state");
            if(tradestatus.equalsIgnoreCase("SUCCESS")){
                order.setPayTime(ZonedDateTime.now());
                order.setOrderStatus("已支付");

                //完成支付逻辑（如果商品关联卡券，如果商品关联卡券，由前台显示领取卡券按钮，如果关联课程，此处需直接发放课程到学员账户）

                return ResponseEntity.ok().body(order);
            }else{
                ExecResult er=new ExecResult(false,"微信支付未成功，支付状态："+tradestatus);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{
            ExecResult er=new ExecResult(false,"微信订单数据获取失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
}
