package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.account.customer.CustomerRepository;
import com.devgyu.banchan.admin.Admin;
import com.devgyu.banchan.admin.AdminRepository;
import com.devgyu.banchan.alarm.AlarmRepository;
import com.devgyu.banchan.alarm.AlarmService;
import com.devgyu.banchan.chatroom.ChatRoom;
import com.devgyu.banchan.chatroom.ChatRoomReadStatus;
import com.devgyu.banchan.chatroom.ChatRoomRepository;
import com.devgyu.banchan.chatroom.ChatRoomStatus;
import com.devgyu.banchan.modules.counselor.Counselor;
import com.devgyu.banchan.modules.counselor.CounselorRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonNavInterceptor implements HandlerInterceptor {
    private final CustomerRepository customerRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RiderRepository riderRepository;
    private final AdminRepository adminRepository;
    private final AlarmRepository alarmRepository;
    private final CounselorRepository counselorRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
        } else if (authentication instanceof UsernamePasswordAuthenticationToken || authentication instanceof RememberMeAuthenticationToken) {
            System.out.println("Request URL ======" + request.getRequestURL());
            UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long accountId = 0L;
            if (userAccount.getCustomer() != null) {
                accountId = userAccount.getCustomer().getId();
                Customer account = customerRepository.findById(accountId).get();

                //?????? ???????????????. ??? ????????? ??????????????? ?????????????????? ???????????? ??????????????? ??????????????? ????????????
                refreshAuthentication(userAccount, account);

                request.setAttribute("navAccount", account);
            } else if (userAccount.getStoreOwner() != null) {
                accountId = userAccount.getStoreOwner().getId();
                StoreOwner account = storeOwnerRepository.findById(accountId).get();

                //?????? ???????????????. ??? ????????? ??????????????? ?????????????????? ???????????? ??????????????? ??????????????? ????????????
                refreshAuthentication(userAccount, account);

                request.setAttribute("navAccount", account);
            } else if (userAccount.getRider() != null) {
                accountId = userAccount.getRider().getId();
                Rider account = riderRepository.findById(accountId).get();

                //?????? ???????????????. ??? ????????? ??????????????? ?????????????????? ???????????? ??????????????? ??????????????? ????????????
                refreshAuthentication(userAccount, account);

                request.setAttribute("navAccount", account);
            } else if (userAccount.getAdmin() != null) {
                accountId = userAccount.getId();
                Admin account = adminRepository.findById(accountId).get();

                //?????? ???????????????. ??? ????????? ??????????????? ?????????????????? ???????????? ??????????????? ??????????????? ????????????
                refreshAuthentication(userAccount, account);
                request.setAttribute("navAccount", account);
            }else if(userAccount.getCounselor() != null){
                accountId = userAccount.getId();
                Counselor account = counselorRepository.findById(accountId).get();

                //?????? ???????????????. ??? ????????? ??????????????? ?????????????????? ???????????? ??????????????? ??????????????? ????????????
                refreshAuthentication(userAccount, account);
                request.setAttribute("navAccount", account);
            }
            if(userAccount.getAdmin() == null) {
                Long alarmCount = alarmRepository.countNewAlarmsByAccountId(accountId);
                request.setAttribute("alarmCount", alarmCount);

                // ?????? ????????? ???????????????????????? ???????????? ????????? ?????? ?????????
                boolean hasNewMassage = false;
                if(userAccount.getCounselor() != null){
                    List<ChatRoom> chatRoomList = chatRoomRepository.findAllByCounselorId(userAccount.getCounselor().getId());
                    for (ChatRoom chatRoom : chatRoomList) {
                        if(chatRoom.getChatRoomStatus() == ChatRoomStatus.CUSTOMER_NEWMESSAGE && chatRoom.getChatRoomReadStatus() != ChatRoomReadStatus.COUNSELOR_READ){
                            hasNewMassage = true;
                            break;
                        }
                    }
                }else{
                    List<ChatRoom> chatRoomList = chatRoomRepository.findAllByAccountId(userAccount.getAccount().getId());
                    for (ChatRoom chatRoom : chatRoomList) {
                        if(chatRoom.getChatRoomStatus() == ChatRoomStatus.COUNSELOR_NEWMESSAGE && chatRoom.getChatRoomReadStatus() != ChatRoomReadStatus.CUSTOMER_READ){
                            hasNewMassage = true;
                            break;
                        }
                    }
                }
                request.setAttribute("hasNewMessage", hasNewMassage);
            }
        }
        return true;
    }

    private void refreshAuthentication(UserAccount userAccount, Account account) {
        UserAccount newUserAccount;
        if (account instanceof Customer) {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Customer) account);
        } else if (account instanceof StoreOwner) {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (StoreOwner) account);
        } else if (account instanceof Rider) {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Rider) account);
        } else if (account instanceof Counselor){
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Counselor) account);
        } else{
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Admin) account);
        }
        Authentication newAuth = new UsernamePasswordAuthenticationToken(newUserAccount, account.getPassword(), userAccount.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
