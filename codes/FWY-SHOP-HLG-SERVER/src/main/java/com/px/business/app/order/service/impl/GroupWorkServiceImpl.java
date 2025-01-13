package com.px.business.app.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.app.appuser.entity.UserAddress;
import com.px.business.app.appuser.mapper.UserAddressMapper;
import com.px.business.app.order.dto.CommodityOrderDTO;
import com.px.business.app.order.dto.GroupDTO;
import com.px.business.app.order.dto.PriceDTO;
import com.px.business.app.order.entity.GroupWork;
import com.px.business.app.order.entity.OrderInfo;
import com.px.business.app.order.enums.OrderStateEnum;
import com.px.business.app.order.mapper.GroupWorkMapper;
import com.px.business.app.order.mapper.OrderInfoMapper;
import com.px.business.app.order.service.GroupWorkService;
import com.px.business.app.order.service.OrderInfoService;
import com.px.business.app.order.vo.GroupVO;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.commodity.entity.Commodity;
import com.px.business.manage.commodity.entity.CommoditySpecification;
import com.px.business.manage.commodity.mapper.CommodityMapper;
import com.px.business.manage.commodity.mapper.CommoditySpecificationMapper;
import com.px.business.manage.other.dto.SysMsgDTO;
import com.px.business.manage.other.enums.MsgType;
import com.px.business.manage.other.service.SysMsgService;
import com.px.business.manage.shop.entity.Shop;
import com.px.business.manage.shop.mapper.ShopMapper;
import com.px.business.third.pay.allpay.enums.Pay;
import com.px.business.third.pay.allpay.model.dto.PayOrderDTO;
import com.px.business.third.pay.allpay.model.vo.PayOrderResultVO;
import com.px.business.third.pay.allpay.service.PayService;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BigDecimalUtil;
import com.px.common.utils.RedisUtils;
import com.px.common.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 拼团信息表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2021-04-06
 */
@Service
public class GroupWorkServiceImpl extends ServiceImpl<GroupWorkMapper, GroupWork> implements GroupWorkService {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private GroupWorkMapper groupWorkMapper;
    @Autowired
    private CommodityMapper commodityMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IAppletUserService iAppletUserService;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private CommoditySpecificationMapper commoditySpecificationMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private PayService wxPayService;
    @Autowired
    private SysMsgService msgService;
    @Autowired
    private HttpServletRequest request;

   /* @Override
    public Result joinGroupWork(GroupDTO groupDTO, String groupWorkNo,String token) {

    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createGroup(String commodityId, GroupDTO orderDTO, String token) {
        String userId = TokenUtil.getUserId(request);
        if (StringUtils.isBlank(orderDTO.getCommodityOrderDTO().getGroupWorkNo())) {
            String pt = redisUtils.getNo("PT", 2L);//生成开团号
            Commodity commodity = commodityMapper.selectById(commodityId);
            GroupWork groupWork1 = new GroupWork();
            groupWork1.setGroupWorkNo(pt)
                    .setUserNumTotal(commodity.getNumberParticipants())
                    .setStartTime(commodity.getStartTime())
                    .setEndTime(commodity.getEndTime())
                    .setGoodId(commodityId)
                    .setIsSuccess(0);
            groupWorkMapper.insert(groupWork1);
            return this.addOrder(orderDTO, token, pt);
        }
        String groupWorkNo1 = orderDTO.getCommodityOrderDTO().getGroupWorkNo();
        return this.addOrder(orderDTO, token, groupWorkNo1);
    }

    @Override
    public GroupVO selectGroupCommodity(String commodityId) {

        GroupVO groupVO = groupWorkMapper.selectGroupCommodity(commodityId);
        List<GroupWork> groupWorks = groupWorkMapper.selectList(new LambdaQueryWrapper<GroupWork>().eq(GroupWork::getGoodId, commodityId));
        if (groupWorks.size() == 0) {
            groupVO.setGroupWorkNo("");
            groupVO.setUserNumJoin(0);
            groupVO.setIsSuccess(1);
            return groupVO;
        }
        List<Integer> collect = groupWorks.stream().map(GroupWork::getIsSuccess).collect(Collectors.toList());
        if (collect.contains(0)) {
            GroupWork groupWork = groupWorkMapper.selectOne(new LambdaQueryWrapper<GroupWork>().eq(GroupWork::getIsSuccess, 0).eq(GroupWork::getGoodId, commodityId));
            groupVO.setGroupWorkNo(groupWork.getGroupWorkNo());
            groupVO.setUserNumJoin(groupWork.getUserNumJoin());
            groupVO.setIsSuccess(0);
            return groupVO;
        }
        groupVO.setIsSuccess(1)
                .setGroupWorkNo("");
        groupVO.setUserNumJoin(0);
        return groupVO;
    }

    @Override
    public Result checkGroup(String commodityId) {
        String userId = TokenUtil.getUserId(request);
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(new LambdaQueryWrapper<OrderInfo>().select(OrderInfo::getGroupWorkNo)
                .ge(OrderInfo::getState, OrderStateEnum.PAID.getTypeId())
                .eq(OrderInfo::getGoodId, commodityId)
                .eq(OrderInfo::getUserId, userId));
        if (!orderInfos.isEmpty()) {
            List<String> groupWorkNos = orderInfos.stream().map(OrderInfo::getGroupWorkNo).collect(Collectors.toList());
            Integer conut = groupWorkMapper.selectCount(new LambdaQueryWrapper<GroupWork>()
                    .in(GroupWork::getGroupWorkNo, groupWorkNos)
                    .eq(GroupWork::getIsSuccess, 0));
            if (conut >= 1) {
                throw new ErrorException(ResultType.GROUP_AREADY);
            }
        }
        return Result.success();
    }

    @Override
    public PriceDTO countPrice(String commodityId, Integer num, Integer way) {
        CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectOne(new LambdaQueryWrapper<CommoditySpecification>().eq(CommoditySpecification::getCommodityId, commodityId));
        BigDecimal sellPrice = commoditySpecification.getSellPrice();
        BigDecimal groupWorkPrice = commoditySpecification.getGroupWorkPrice();
        // 0-直接购买 1-拼团
        BigDecimal price = way == 0 ? sellPrice : groupWorkPrice;
        BigDecimal multiply = BigDecimalUtil.parse(BigDecimalUtil.multiply(new BigDecimal(num + ""), price));
        return new PriceDTO(price, multiply, new BigDecimal(0), multiply);
    }

    public Result addOrder(GroupDTO orderDTO, String token, String pt) {
        String userId = TokenUtil.getUserId(token);
        AppletUserInfoVo appletUserByToken = iAppletUserService.getAppletUserByToken(token);
        String openid = appletUserByToken.getOpenid();
        String addressId = orderDTO.getAddressId();
        //获取收货人信息
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        //获取用户选中的商品
        //List<CommodityOrderDTO> commodityOrderDTO = orderDTO.getCommodityOrderDTO();
        CommodityOrderDTO commodityOrderDTO = orderDTO.getCommodityOrderDTO();
        Assert.notNull(commodityOrderDTO, "请先选择商品再进行下单");
        String sp = redisUtils.getNo("NO", 1L);//主订单号
        //CommodityOrderDTO x = commodityOrderDTO.get(0);
        String goodId = commodityOrderDTO.getGoodId();
        //获取单个商品的信息
        Commodity commodity = commodityMapper.selectById(goodId);
        //店铺信息
        Shop shop = shopMapper.selectById(commodity.getShopId());
        //规格信息
        CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectById(commodityOrderDTO.getSpecificationId());
        Integer num = commodityOrderDTO.getNum();
        // 验证信息
        orderInfoService.verifyTimeOrNumInventory(commodity, num, commoditySpecification.getStock());
        // 商品的原价
        // BigDecimal totalPrice = BigDecimalUtil.multiply(new BigDecimal(num), commoditySpecification.getOriginalPrice());
        // 商品单价-  0-直接购买 1-拼团
        BigDecimal price = commodityOrderDTO.getWay() == 0 ? commoditySpecification.getSellPrice() : commoditySpecification.getGroupWorkPrice();
        BigDecimal totalPrice = BigDecimalUtil.multiply(new BigDecimal(num + ""), price);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(redisUtils.getNo("NO", 1L))
                .setUserId(userId)
                .setShopId(commodity.getShopId())
                .setGoodsType(4)
                .setGroupWorkNo(pt)
                .setSpecsId(commodityOrderDTO.getSpecificationId())
                .setGoodId(goodId)
                .setShopName(shop.getShopName())
                .setGoodName(commodity.getCommodityName())
                .setGoodPicture(commodity.getCoverUrl())
                .setSpecsName(commoditySpecification.getSpecificationName())
                .setSpecsPicture(commoditySpecification.getSpecificationPicture())
                .setSpecsPrice(price)
                .setNum(commodityOrderDTO.getNum())
                .setFreight(new BigDecimal("0"))
                .setState(OrderStateEnum.TO_BE_PAID.getTypeId())
                .setDeliveryMode(commodityOrderDTO.getDeliveryMode())
                .setOrderNote(commodityOrderDTO.getOrderNote())
                .setGoodsType(commodity.getCommodityType())
                .setPickAddressId(orderDTO.getAddressId())
                .setPickName(commodityOrderDTO.getPickName())
                .setPickPhone(commodityOrderDTO.getPickPhone())
                .setMasterOrderNo(sp);
        orderInfo.setDiscountPrice(new BigDecimal(0))
                .setActualPrice(totalPrice)
                .setTotalPrice(totalPrice);
        orderInfoMapper.insert(orderInfo);
        //向管理端发送消息通知
        SysMsgDTO sysMsgDTO = new SysMsgDTO();
        sysMsgDTO.setUserId(orderInfo.getShopId());
        Map<String, String> map = new HashMap<>();
        map.put("commodutyName", orderInfo.getGoodName());
        map.put("content", "你有新的订单待处理");
        map.put("orderNo", orderInfo.getOrderNo());
        map.put("orderId", orderInfo.getId());
        sysMsgDTO.setContent(JSON.toJSONString(map));
        sysMsgDTO.setMsgType(MsgType.V3.getValue());
        sysMsgDTO.setUserType(2);
        msgService.insertMsg(sysMsgDTO);
        //调用微信api发起支付
        PayOrderDTO dto = new PayOrderDTO();
        dto.setMoneyTotal(totalPrice);
        dto.setPlatformFee(null);
        dto.setChannelFee(null);
        dto.setOrderNo(sp);
        dto.setPayBusiness(Pay.PayBusiness.V4);
        dto.setBody("订单支付");
        dto.setWxTradeType("JSAPI");
        dto.setWxOpenid(openid);
        Result<PayOrderResultVO> order = wxPayService.createOrder(dto);
        PayOrderResultVO data = order.getData();
        return Result.success(data);
    }

}
