package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.account.dto.MypageDto;
import com.devgyu.banchan.alarm.Alarm;
import com.devgyu.banchan.alarm.AlarmType;
import com.devgyu.banchan.modules.rider.dto.RiderPageDto;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        RiderOrders riderOrders = new RiderOrders(rider, findOrders);
        findOrders.setOrderStatus(OrderStatus.DELIVERY_READY);

        StoreOwner storeOwner = findOrders.getOrdersItemList().get(0).getItem().getStoreOwner();
        String content = rider.getNickname() + "님이 배달을 수락하셨습니다!";
        new Alarm(storeOwner, content, AlarmType.RIDER_ORDER_ACCEPT, LocalDateTime.now());
    }

    public void startDelivery(Rider rider, Long orderId) {
        List<RiderOrders> findOrder = riderOrdersRepository.findAccountOrdersItemStoreFetchByOrdersIdAndRiderId(orderId, rider.getId());
        if(findOrder.isEmpty()){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        RiderOrders riderOrders = findOrder.get(0);
        Orders orders = riderOrders.getOrders();
        orders.setOrderStatus(OrderStatus.DELIVERY_START);
        Account account = orders.getAccount();

        String nickname = orders.getOrdersItemList().get(0).getItem().getStoreOwner().getNickname();
        String content = nickname + "가게에서 배달을 시작합니다!";

        new Alarm(account, content, AlarmType.DELIVERY_START, LocalDateTime.now());
    }

    public void completedDelivery(Long orderId) {
        List<Orders> tempOrders = ordersRepository.findAccountFetchById(orderId);
        if(tempOrders.isEmpty()){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        Orders orders = tempOrders.get(0);
        orders.setOrderStatus(OrderStatus.COMPLETED);

        Account account = orders.getAccount();
        String content = "배달이 완료되었습니다. 즐거운 식사시간 보내세요!";
        new Alarm(account, content, AlarmType.COMPLETED, LocalDateTime.now());
    }
}
