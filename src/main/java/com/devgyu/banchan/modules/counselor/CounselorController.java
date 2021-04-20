package com.devgyu.banchan.account.counselor;

import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.chat.ChatRoom;
import com.devgyu.banchan.chat.ChatRoomRepository;
import com.devgyu.banchan.chat.ChatRoomStatus;
import com.devgyu.banchan.modules.counselor.Counselor;
import com.devgyu.banchan.modules.counselor.CounselorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/counselor")
public class CounselorController {
    private final CounselorService counselorService;
    private final CounselorRepository counselorRepository;
    private final ChatRoomRepository chatRoomRepository;
    
    @GetMapping("/prepare")
    public String prepare_counselor(){
        counselorService.saveCounselor
        return"redirect:/";
    }

    @GetMapping("/list")
    public String counselor_list(@PageableDefault Pageable pageable, Model model){
        Slice<ChatRoom> chatRoomList = chatRoomRepository.findAllByChatRoomStatusOrderByRegDateDesc(ChatRoomStatus.WAITING, pageable);
        boolean hasNext = chatRoomList.hasNext();
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("chatRoomList", chatRoomList.getContent());
        return "counselor/list";
    }
}
