package com.px.business.manage.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.app.appuser.service.ShopCollectionService;
import com.px.business.app.shop.vo.AppShopInfoVo;
import com.px.business.app.shop.vo.ShopDetailVo;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.commodity.mapper.CommodityMapper;
import com.px.business.manage.commodity.vo.CommodityAppPageVo;
import com.px.business.manage.shop.dto.ShopDto;
import com.px.business.manage.shop.dto.UpdateShopDto;
import com.px.business.manage.shop.entity.Shop;
import com.px.business.manage.shop.mapper.ShopMapper;
import com.px.business.manage.shop.service.IShopService;
import com.px.business.manage.shop.vo.AppShopStatisticsVo;
import com.px.business.manage.shop.vo.ManageShopListVo;
import com.px.business.manage.shop.vo.ShopVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import com.px.sysuser.entity.SysUser;
import com.px.sysuser.service.ISysRoleService;
import com.px.sysuser.service.ISysUserRoleService;
import com.px.sysuser.service.ISysUserService;
import com.px.sysuser.vo.SysUserDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 商铺表 服务实现类
 * @Date: 2021-02-24
 **/
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IAppletUserService iAppletUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleService sysRoleService;


    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private ShopCollectionService shopCollectionService;

    /**
     * 平台新建商铺
     *
     * @param dto
     * @param userId
     * @param type   0-代表平台创建  1-代表小程序申请入驻
     * @return
     */
    @Override
    @Transactional
    public Result addShop(ShopDto dto, String userId, Integer type) {
        //商户登录账号 和 店铺名称不能重复
        Boolean existedAccount = iSysUserService.isExistedAccount(dto.getShopLoginName());
        if (existedAccount) {
            throw new ErrorException(ResultType.ACCOUNT_IS_EXIST);
        }
        //判断电话号码是否唯一
        Boolean existedPhone = iSysUserService.isExistedPhone(dto.getContactsPhone());
        if (existedPhone) {
            throw new ErrorException(ResultType.PHONE_IS_EXIST);
        }
        Boolean existedShopName = isExistedShopName(dto.getShopName());
        if (existedShopName) {
            throw new ErrorException(ResultType.SHOP_NAME_IS_EXIST);
        }
        // 保存商铺信息,平台创建的商户，默认正常，不需要审核
        Shop shop = BeanDtoVoUtils.convert(dto, Shop.class);
        shop.setCreateId(userId);
        shopMapper.insert(shop);
        String shopId = shop.getId();

        // 保存商户账号
        SysUser sysUser = new SysUser();
        sysUser.setLoginName(dto.getShopLoginName());
        String pwd = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());
        sysUser.setPassword(pwd);
        sysUser.setPhone(dto.getContactsPhone());
        sysUser.setAboutId(shopId);
        sysUser.setCreateId(userId);
        sysUser.setUserType(1);//商户
        sysUser.setType(0);//商户负责人
        sysUser.setNickname(dto.getShopLoginName());
        iSysUserService.save(sysUser);
        //关联角色
        List<String> roleList = dto.getRoleList();
        if (roleList != null && roleList.size() != 0) {
            sysUserRoleService.linkedRoles(sysUser.getId(), roleList);
        }
        return Result.success(shopId);
    }

    /**
     * 平台查询商户列表
     *
     * @param current
     * @param size
     * @param queryMap
     * @return
     */
    @Override
    public Result<Page<ManageShopListVo>> getShopPage(Long current, Long size,
                                                      Map<String, Object> queryMap) {

        Page<ManageShopListVo> page = new Page<>(current, size);
        shopMapper.getShopPage(page, queryMap);
        return Result.success(page);
    }

    /**
     * 修改商铺信息
     *
     * @param dto
     * @param user
     * @return
     */
    @Override
    public Result updateShopInfo(UpdateShopDto dto, SysUserDetailsVo user) {

        // 查询修改店铺是否存在
        String shopId = dto.getShopId();
        Shop shop = shopMapper.selectById(shopId);
        if (null == shop) {
            throw new ErrorException(ResultType.OPERATION_FAILURE.getCode(),
                    "无效的店铺id");
        }
        //联系人电话
        Boolean existedPhone = iSysUserService.isExistedPhone(dto.getContactsPhone());
        if (existedPhone && !shop.getContactsPhone().equals(dto.getContactsPhone())) {
            throw new ErrorException(ResultType.PHONE_IS_EXIST);
        }
        // 店铺名称
        Boolean existedShopName = isExistedShopName(dto.getShopName());
        if (existedShopName && !shop.getShopName().equals(dto.getShopName())) {
            throw new ErrorException(ResultType.SHOP_NAME_IS_EXIST);
        }
        // 更新商铺信息
        Shop entity = BeanDtoVoUtils.convert(dto, Shop.class);
        entity.setId(shopId);
        entity.setCreateId(user.getId());
        shopMapper.updateById(entity);

        // 电话号码变更，要修改用户的电话号码
        if (!shop.getContactsPhone().equals(dto.getContactsPhone())) {
            SysUser sysUser = new SysUser();
            sysUser.setId(dto.getUserId());
            sysUser.setPhone(dto.getContactsPhone());
            iSysUserService.updateById(sysUser);
        }

        //平台用户可以修改角色
        List<String> roleIds = dto.getRoleList();
        if (user.getUserType() == 0 && roleIds != null && roleIds.size() != 0) {
            sysUserRoleService.linkedRoles(dto.getUserId(), roleIds);
        }
        return Result.success("修改成功");
    }

    @Override
    public ShopVo getShopDetails(String shopId, SysUserDetailsVo user) {

        ShopVo vo = shopMapper.getShopDetails(shopId);
        // 平台账号可查询商家负责人的角色列表
        if (vo != null && user.getUserType() == 0) {
            // 查询该用户关联的角色
            List<String> userRole = sysRoleService.getUserRole(vo.getUserId());
            vo.setRoleList(userRole);
        }
        return vo;
    }


    /**
     * 禁用/启用商铺
     *
     * @param shopId
     * @param shopState
     * @return
     */
    @Override
    public Result updateShopStatus(String shopId, Integer shopState) {
        Shop shop = shopMapper.selectById(shopId);
        if (null == shop) {
            throw new ErrorException(ResultType.OPERATION_FAILURE.getCode(),
                    "无效的店铺id");
        }
        Shop entity = new Shop();
        entity.setId(shopId);
        entity.setState(shopState);
        boolean result = shopMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.OPERATION_FAILURE);

    }

    /**
     * 店铺设置可退货时间
     *
     * @param shopId
     * @param returnTime
     * @return
     */
    @Override
    public Result setReturnTime(String shopId, Integer returnTime) {
        Shop shop = shopMapper.selectById(shopId);
        if (null == shop) {
            throw new ErrorException(ResultType.OPERATION_FAILURE.getCode(),
                    "无效的店铺id");
        }
        Shop entity = new Shop();
        entity.setId(shopId);
        entity.setReturnTime(returnTime);
        int update = shopMapper.updateById(entity);
        if (update > 0) {
            return Result.success();
        }
        throw new ErrorException(ResultType.OPERATION_FAILURE);
    }

    /**
     * 店铺名字是否存在
     *
     * @param shopName
     * @return
     */
    @Override
    public Boolean isExistedShopName(String shopName) {
        Integer count = shopMapper.selectCount(
                new LambdaQueryWrapper<Shop>()
                        .eq(Shop::getShopName, shopName)
        );
        if (count == 0) {
            return false;
        }
        return true;
    }


    /**
     * app商铺首页信息
     *
     * @param shopId
     * @return
     */
    @Override
    public Map<String, Object> getAppShopInfo(AppletUserInfoVo appletUserInfoVo, String shopId) {

        Shop shop = shopMapper.selectById(shopId);
        if (shop == null) {
            return null;
        }
        //是否收藏
        boolean isCollect = shopCollectionService.isCollect(shopId, appletUserInfoVo == null ? null : appletUserInfoVo.getId());
        AppShopInfoVo infoVo = BeanDtoVoUtils.convert(shop, AppShopInfoVo.class);
        infoVo.setFollowFlag(isCollect);
        Map<String, Object> map = new HashMap<>();
        map.put("shopInfo", infoVo);
        List<CommodityAppPageVo> commodityAppPageVoList = commodityMapper.queryShopRecommendCommodity(shopId);
        map.put("commodityList", commodityAppPageVoList);
        return map;
    }


    /**
     * 小程序 商户统计
     *
     * @param shopId
     * @return
     */
    @Override
    public Result<AppShopStatisticsVo> appsStatistics(String shopId) {
        AppShopStatisticsVo appShopStatisticsVo = new AppShopStatisticsVo();

        //店铺商品浏览量
        Integer viewNumber = shopMapper.countCommodityViewNumber(shopId);

        if (viewNumber != null) {
            appShopStatisticsVo.setCommodityViewNumber(viewNumber);
        }
        return Result.success(appShopStatisticsVo);
    }

    @Override
    public Result<ShopDetailVo> findId(String shopId) {
        Shop shop = shopMapper.selectOne(new LambdaQueryWrapper<Shop>()
                .select(Shop::getId, Shop::getShopName, Shop::getLatitude, Shop::getLongitude)
                .eq(Shop::getId, shopId));
        return Result.success(BeanDtoVoUtils.convert(shop, ShopDetailVo.class));
    }
}

