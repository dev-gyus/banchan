package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.orders.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorColumn
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider extends Account {
    private String driverLicense;
    @OneToMany(mappedBy = "rider", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private List<RiderOrders> riderOrdersList = new ArrayList<>();

    public Rider(String email, String nickname, String password, String name, String phone, Address address, String emailToken, String driverLicense) {
        super(email, nickname, password, name, phone, Roles.ROLE_RIDER, address, emailToken);
        this.driverLicense = driverLicense;
    }

    public void addRiderOrders(RiderOrders riderOrders){
        this.riderOrdersList.add(riderOrders);
        riderOrders.setRider(this);
    }
}
