package com.devgyu.banchan.admin;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.admin.dto.AdminApiDto;
import com.devgyu.banchan.admin.dto.AdminBlockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final AccountRepository accountRepository;

    @GetMapping({"", "/"})
    public String admin_main(Model model) {
        return "admin/main";
    }

    @GetMapping("/user-block")
    public String user_block(Model model) {
        return "admin/user-block";
    }

    @PostMapping("/user-block/search")
    public String user_block(@RequestParam String firstCondition, @RequestParam String secondCondition, Model model) {
        List<Account> accountList = adminRepository.findAccountByConditions(firstCondition, secondCondition);
        if(accountList.isEmpty()){
            model.addAttribute("firstCondition", firstCondition);
            model.addAttribute("secondCondition", secondCondition);
            return "admin/user-block";
        }
        Account account = accountList.get(0);
        commonBlockMethod(firstCondition, secondCondition, model, account);
        return "admin/user-block";
    }

    @PostMapping("/user-block/{accountId}")
    public String user_block_do(@PathVariable Long accountId, @RequestParam String firstCondition, @RequestParam String secondCondition,
                                @RequestParam String blockReason, Model model) {
        Account targetAccount = adminService.userBlock(accountId, blockReason);
        commonBlockMethod(firstCondition, secondCondition, model, targetAccount);
        return "admin/user-block";
    }

    @PostMapping("/user-unblock/{accountId}")
    public String user_unBlock_do(@PathVariable Long accountId, @RequestParam String firstCondition, @RequestParam String secondCondition,
                                  Model model) {
        Account targetAccount = adminService.userUnBlock(accountId);
        commonBlockMethod(firstCondition, secondCondition, model, targetAccount);
        return "admin/user-block";
    }

    private void commonBlockMethod(String firstCondition, String secondCondition, Model model, Account account) {
        AdminBlockDto adminBlockDto = new AdminBlockDto(account.getId(), account.getBlockReason(),
                account.getNickname(), account.getBlockedDate(), account.getUnblockedDate(), account.getBlockCount(), account.isBlocked());

        model.addAttribute("account", adminBlockDto);
        model.addAttribute("firstCondition", firstCondition);
        model.addAttribute("secondCondition", secondCondition);
    }
}
