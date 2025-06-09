package com.example.danmaku.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.danmaku.dto.DanmakuDTO;
import com.example.danmaku.mapper.DanmakuMapper;
import com.example.danmaku.model.Danmaku;
import com.example.danmaku.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danmaku")
public class DanmakuController {

    private final DanmakuService danmakuService;
    private final DanmakuMapper danmakuMapper;

    @Autowired
    public DanmakuController(DanmakuService danmakuService, DanmakuMapper danmakuMapper) {
        this.danmakuService = danmakuService;
        this.danmakuMapper = danmakuMapper;
    }

    /**
     * 发送弹幕
     */
    @MessageMapping("/danmaku/send")
    public Danmaku sendDanmaku(DanmakuDTO danmakuDTO) {
        return danmakuService.saveDanmaku(danmakuDTO);
    }

    /**
     * 获取视频的所有弹幕（REST API）
     */
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<Danmaku>> getDanmakusByVideoId(@PathVariable String videoId) {
        List<Danmaku> danmakus = danmakuService.getDanmakusByVideoId(videoId);
        return ResponseEntity.ok(danmakus);
    }

    /**
     * 获取指定时间范围内的弹幕（REST API）
     */
    @GetMapping("/video/{videoId}/timerange")
    public ResponseEntity<List<Danmaku>> getDanmakusByTimeRange(
            @PathVariable String videoId,
            @RequestParam Double start,
            @RequestParam Double end) {
        List<Danmaku> danmakus = danmakuService.getDanmakusByVideoIdAndTimeRange(videoId, start, end);
        return ResponseEntity.ok(danmakus);
    }


    /**
     * 对于长视频，分段获取弹幕数据
     */
    @GetMapping("/video/{videoId}/paged")
    public ResponseEntity<IPage<Danmaku>> getPagedDanmakus(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        Page<Danmaku> pageParam = new Page<>(page, size);
        QueryWrapper<Danmaku> queryWrapper = new QueryWrapper<Danmaku>()
                .eq("video_id", videoId)
                .orderByAsc("time");

        IPage<Danmaku> danmakus = danmakuMapper.selectPage(pageParam, queryWrapper);
        return ResponseEntity.ok(danmakus);
    }
}