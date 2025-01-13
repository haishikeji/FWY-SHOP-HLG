package com.px.business.app.commodity.controller;

import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.commodity.dto.ShoppingCartDto;
import com.px.business.manage.commodity.service.IShoppingCartService;
import com.px.business.manage.commodity.vo.ShoppingCartListVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 商品购物车 前端控制器
 * @Date: 2021-03-03
 **/
@RestController
@RequestMapping("/app/shoppingCart")
@Api(value = "/app/shoppingCart", tags = "商品购物车")
public class AppShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;


    @Autowired
    private IAppletUserService iAppletUserService;

    /**
     * 加入购物车
     *
     * @param token
     * @param dto
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("加入购物车")
    public Result add(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Valid ShoppingCartDto dto) {
        AppletUserInfoVo user = iAppletUserService.getAppletUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return shoppingCartService.add(user, dto);
    }


    /**
     * 查询我的购物车列表
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/getMyShoppingCart", method = RequestMethod.GET)
    @ApiOperation("查询我的购物车列表")
    public Result<List<ShoppingCartListVo>> getMyShoppingCart(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        AppletUserInfoVo user = iAppletUserService.getAppletUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);

        }
        return shoppingCartService.getMyShoppingCart(user);
    }

    /**
     * 修改商品购买数量
     *
     * @param token
     * @param cartId
     * @return
     */
    @RequestMapping(value = "/modifyPurchaseQuantity", method = RequestMethod.PUT)
    @ApiOperation("修改商品购买数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cartId", value = "购物车商品id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "quantity", value = "商品数量", required = true, paramType = "query"),
    })
    public Result modifyPurchaseQuantity(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("cartId") String cartId,
            @RequestParam("quantity") Integer quantity
    ) {
        AppletUserInfoVo user = iAppletUserService.getAppletUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return shoppingCartService.modifyPurchaseQuantity(user, cartId, quantity);
    }


    /**
     * 删除购物车商品
     *
     * @param token
     * @param cardList
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation("删除购物车商品")
    public Result delete(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<String> cardList
    ) {
        AppletUserInfoVo user = iAppletUserService.getAppletUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        if (cardList == null || cardList.size() == 0) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        shoppingCartService.removeByIds(cardList);
        return Result.success();
    }

}
