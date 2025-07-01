package com.ShopEase.Payment.services;

import com.ShopEase.Payment.dtos.CouponApplyDto;
import com.ShopEase.Payment.dtos.CouponDto;
import com.ShopEase.Payment.entities.Coupon;
import com.ShopEase.Payment.entities.DiscountType;
import com.ShopEase.Payment.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    
    public Coupon createCoupon(CouponDto couponDto){
        Coupon coupon = new Coupon();
        coupon.setCode(couponDto.getCode());
        coupon.setDiscountType(couponDto.getDiscountType());
        coupon.setDiscountValue(couponDto.getDiscountValue());
        coupon.setMinOrderValue(couponDto.getMinOrderValue());
        coupon.setActive(true);

        couponRepository.save(coupon);
        
        return coupon;
    }

    public Coupon disableCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(()-> new NoSuchElementException("Invalid coupon code"));
        coupon.setActive(false);
        couponRepository.save(coupon);
        return coupon;
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getByCode(CouponDto couponDto) {
        return couponRepository.findByCode(couponDto.getCode()).orElseThrow(()-> new NoSuchElementException("Invalid coupon code"));
    }

    public BigDecimal applyCoupon(CouponApplyDto couponApplyDto) {
        Coupon coupon = couponRepository.findByCode(couponApplyDto.getCode()).orElseThrow(()-> new NoSuchElementException("Invalid coupon code"));
        BigDecimal amount = couponApplyDto.getPrice().multiply(BigDecimal.valueOf(couponApplyDto.getQuantity()));

        if(!coupon.isActive()){
            throw new IllegalArgumentException("coupon expired");
        }

        if(amount.compareTo(coupon.getMinOrderValue()) < 0){
            throw new IllegalArgumentException("This order can only be apply to orders above "+coupon.getMinOrderValue());
        }

        if(coupon.getDiscountType() == DiscountType.PERCENTAGE){
            return couponApplyDto.getPrice()
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        return coupon.getDiscountValue();
    }
}
