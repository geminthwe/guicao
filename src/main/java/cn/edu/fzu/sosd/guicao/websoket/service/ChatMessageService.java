package cn.edu.fzu.sosd.guicao.websoket.service;

import cn.edu.fzu.sosd.guicao.websoket.entity.ChatMessage;
import cn.edu.fzu.sosd.guicao.websoket.mapper.ChatMessageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageService(ChatMessageMapper chatMessageMapper) {
        this.chatMessageMapper = chatMessageMapper;
    }

    public void saveMessage(ChatMessage message) {
        chatMessageMapper.insert(message);
    }

    // 获取历史消息
    public List<ChatMessage> getHistoryMessagesByDormitory(Long dormitoryId) {
        return chatMessageMapper.selectList(new QueryWrapper<ChatMessage>()
                .eq("dormitory_id", dormitoryId));
    }

    // 分页查询指定宿舍的历史消息
    public Page<ChatMessage> getHistoryMessagesByDormitory(Long dormitoryId, Integer currentPage, Integer pageSize) {
        Page<ChatMessage> page = new Page<>(currentPage, pageSize);
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dormitory_id", dormitoryId)
                .orderByDesc("create_time"); // 按创建时间降序排列
        return chatMessageMapper.selectPage(page, queryWrapper);
    }
}
