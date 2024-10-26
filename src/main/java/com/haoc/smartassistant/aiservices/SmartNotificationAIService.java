package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface SmartNotificationAIService extends AiService {


        /**
         * 基于用户当前活动和上下文生成实时通知
         */
        @SystemMessage("""
            Generate real-time notifications based on the user's current activity and context. 
            Include reminders for scheduled tasks, wellness checks, and context-sensitive prompts.
            """)
        String generateContextAwareNotification(String userContextData);

        /**
         * 根据任务优先级生成通知
         */
        @SystemMessage("""
            Generate notifications based on task priority. Highlight urgent tasks and 
            suggest appropriate timing for lower-priority tasks.
            """)
        String generatePriorityBasedNotification(String taskData);

        /**
         * 预测用户可能的偏离情况并提前通知
         */
        @SystemMessage("""
            Predict potential schedule deviations based on historical activity and provide reminders 
            to help the user stay on track.
            """)
        String predictAndNotifyPotentialDeviation(String historicalActivityData);

        /**
         * 提供健康与福利提醒
         */
        @SystemMessage("""
            Use the user's health data, such as heart rate and sleep duration, to suggest wellness actions 
            like taking breaks, drinking water, or doing light exercises.
            """)
        String generateHealthAndWellnessNotification(String healthData);

        /**
         * 基于用户情绪和表现生成激励通知
         */
        @SystemMessage("""
            Based on the user's mood and task completion rate, create motivational notifications 
            to provide encouragement and emotional support.
            """)
        String generateMotivationalNotification(String moodAndPerformanceData);



}
