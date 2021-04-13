package com.devgyu.banchan.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminBlockDto {
    private Long id;
    private String blockReason;
    private String nickname;
    private int blockCount;
    private LocalDateTime blockDate;
    private LocalDateTime unblockDate;
    private boolean blocked;

    public AdminBlockDto(Long id, String blockReason, String nickname, LocalDateTime blockDate, LocalDateTime unblockDate, int blockCount, boolean blocked) {
        this.id = id;
        this.blockReason = blockReason;
        this.nickname = nickname;
        this.blockDate = blockDate;
        this.unblockDate = unblockDate;
        this.blockCount = blockCount;
        this.blocked = blocked;
    }
}
