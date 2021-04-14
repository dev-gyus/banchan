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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RiderService {
    private final RiderRepository riderRepository;
    private final OrdersRepository ordersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RiderOrdersRepository riderOrdersRepository;

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
        Orders findOrders = ordersRepository.findRiderLeftFetchByOrderId(ordersId);
        if(findOrders.getRiderOrders() != null){
            throw new IllegalArgumentException("이미 배송이 시작된 주문입니다");
        }
        Rider findRider = riderRepository.findById(rider.getId()).get();
        RiderOrders riderOrders = new RiderOrders(findRider, findOrders);
        findOrders.setOrderStatus(OrderStatus.DELIVERY_READY);
        // TODO 지도 연동하면 거리에따라서 배달료 다르게 측정하는 로직 구현해볼것, 기본료는 3천원
    }

    public void startDelivery(Rider rider, Long orderId) {
        List<RiderOrders> findOrder = riderOrdersRepository.findOrderFetchByRiderIdAndOrderId(rider.getId(), orderId);
        Rider findRider = riderRepository.findById(rider.getId()).get();
        if(findOrder.isEmpty()){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        RiderOrders riderOrders = findOrder.get(0);
        riderOrders.getOrders().setOrderStatus(OrderStatus.DELIVERY_START);
    }

    public void completedDelivery(Long orderId) {
        Orders orders = ordersRepository.findById(orderId).get();
        orders.setOrderStatus(OrderStatus.COMPLETED);
    }
}