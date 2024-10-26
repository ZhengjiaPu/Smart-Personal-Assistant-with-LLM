package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface TimeManagementAIService {

    // 生成每日总结，分析用户活动并提供建议
    @SystemMessage("""
            You are tasked with generating a daily time management summary for the user.
            Analyze the user's activities for the day, highlight their achievements, and provide recommendations for tomorrow.
            The summary should be returned in the following format:
            {
                "summary": "<brief summary of user's daily activities>",
                "achievements": "<highlighted achievements>",
                "recommendations": "<recommendations for tomorrow>"
            }
            """)
    Flux<String> generateDailySummary(String userActivityData);

    // 根据用户请求调整日程
    @SystemMessage("""
            The user has requested to adjust their schedule.
            Please consider their request and provide an updated schedule.
            """)
    Flux<String> adjustSchedule(String adjustmentCommand);
}
