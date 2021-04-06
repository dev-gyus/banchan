package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.account.dto.MypageDto;
import com.devgyu.banchan.modules.rider.dto.RiderPageDto;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RiderService {
    private final RiderRepository riderRepository;
    private final OrdersRepository ordersRepository;
    private final PasswordEncoder passwordEncoder;

    public void modifyRider(Account account, RiderMainDto riderMainDto) {
        Rider rider = riderRepository.findById(account.getId()).get();
        Address modifyAddress =
                new Address(riderMainDto.getZipcode(), riderMainDto.getRoad(),
                        riderMainDto.getJibun(), riderMainDto.getDetail(), riderMainDto.getExtra());
        rider.setDriverLicense(riderMainDto.getDriverLicense());
        rider.setAddress(modifyAddress);
        rider.setNickname(riderMainDto.getNickname());
        rider.setName(riderMainDto.getName());
        rider.setPhone(riderMainDto.getPhone());
        if (!riderMainDto.getThumbnail().equals("")) {
            rider.setThumbnail(riderMainDto.getThumbnail());
        }
    }
    public void modifyPassword(String email, ModifyPasswordDto modifyPasswordDto) throws IllegalAccessException {
        Rider findRider = riderRepository.findByEmail(email);
        if(findRider == null){
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }
        findRider.setPassword(passwordEncoder.encode(modifyPasswordDto.getPassword()));
    }

    public void addOrders(Rider rider, Long ordersId) {
        Rider findRider = riderRepository.findById(rider.getId()).get();
        Orders findOrders = ordersRepository.findById(ordersId).get();
        RiderOrders riderOrders = new RiderOrders(findRider, findOrders);
        findOrders.setOrderStatus(OrderStatus.DELIVERY);
        // TODO 지도 연동하면 거리에따라서 배달료 다르게 측정하는 로직 구현해볼것, 기본료는 3천원
    }
}
