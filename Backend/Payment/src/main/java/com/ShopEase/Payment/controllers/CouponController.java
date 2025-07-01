package com.ShopEase.Payment.controllers;

import com.ShopEase.Payment.dtos.CouponApplyDto;
import com.ShopEase.Payment.dtos.CouponDto;
import com.ShopEase.Payment.entities.Coupon;
import com.ShopEase.Payment.services.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/create/admin")
    public ResponseEntity<Coupon> createCoupon(@RequestBody CouponDto couponDto){
        Coupon coupon = couponService.createCoupon(couponDto);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/disable/{code}/admin")
    public ResponseEntity<Coupon> disableCoupon(@PathVariable String code){
        Coupon coupon = couponService.disableCoupon(code);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/getAll/admin")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/getByCode")
    public ResponseEntity<Coupon> getByCode(@RequestBody CouponDto couponDto){
        Coupon coupon = couponService.getByCode(couponDto);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/apply")
    public ResponseEntity<BigDecimal> applyCoupon(@RequestBody CouponApplyDto couponApplyDto){
        BigDecimal discount = couponService.applyCoupon(couponApplyDto);
        return ResponseEntity.ok(discount);
    }
}
