package com.devgyu.banchan.alarm;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.alarm.dto.AlarmAccountDto;
import com.devgyu.banchan.alarm.dto.AlarmApiDto;
import com.devgyu.banchan.alarm.dto.AlarmDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping({"","/"})
    public String alarm_main(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model){
        Page<Alarm> alarmList = alarmService.findAllAlertAlarms(account.getId(), pageable);
        String nowPage = "alert";
        nonApiAlarmMethod(account, model, alarmList, nowPage);
        return "alarm/main";
    }

    @GetMapping("/checked")
    public String alarm_checked(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model){
        Page<Alarm> alarmList = alarmService.findAllCheckedAlarms(account.getId(), pageable);
        String nowPage = "checked";
        nonApiAlarmMethod(account, model, alarmList, nowPage);
        return "alarm/main";
    }

    @GetMapping("/api/{command}")
    @ResponseBody
    public AlarmApiDto api_alarm_main(@CurrentUser Account account, @PathVariable String command, @PageableDefault Pageable pageable){
        Page<Alarm> alarmList;
        if(command.equals("alert")) {
            alarmList = alarmService.findAllAlertAlarms(account.getId(), pageable);
        }else if(command.equals("checked")){
            alarmList = alarmService.findAllCheckedAlarms(account.getId(), pageable);
        }else{
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        AlarmApiDto alarmApiDto = alarmApiMethod(alarmList);
        return alarmApiDto;
    }

    private void nonApiAlarmMethod(@CurrentUser Account account, Model model, Page<Alarm> alarmList, String command) {
        AlarmAccountDto alarmAccountDto = new AlarmAccountDto(account.getName(), account.getThumbnail());
        model.addAttribute("command", command);
        model.addAttribute("alarmAccountDto", alarmAccountDto);
        model.addAttribute("alarmList", alarmList.getContent());
    }

    private AlarmApiDto alarmApiMethod(Page<Alarm> alarmList) {
        List<Alarm> content = alarmList.getContent();
        List<AlarmDto> alarmDtoList = content
                .stream().map(a -> new AlarmDto(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(a.getAlertDate()),
                        a.getContent(), a.getAlarmType())).collect(Collectors.toList());
        AlarmApiDto alarmApiDto = new AlarmApiDto(alarmDtoList, alarmList.isLast());
        return alarmApiDto;
    }
}
