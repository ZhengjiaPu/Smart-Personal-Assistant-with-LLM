package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import reactor.core.publisher.Flux;

@AiService
public interface TimeManagementAIService {

    // Generate daily summaries, analyze user activity and provide recommendations
    @SystemMessage("""
                    You are tasked with generating a daily time management summary for the user.
                    Analyze the user's activities for the day, highlight their achievements, and provide recommendations for tomorrow.
                    
                    Please return the response in a well-structured JSON format with the following structure:
                    {
                        "userId": "<the userId you received>",
                        "summary": "<brief summary of user's daily activities>",
                        "achievements": "<highlighted achievements>",
                        "recommendations": "<recommendations for tomorrow>",
                        "score": "<provide a score for this user on a scale from 40 to 100>"
                    }
                    
                    Note:
                    - Ensure "summary", "achievements", and "recommendations" fields contain plain text.
                    - Ensure "score" is a number between 40 and 100 and it's a string.
                    - Return the entire JSON structure as a single response string.
                """)

    String generateDailySummary(String userActivityData);

    // 根据用户请求调整日程
    @SystemMessage("""
            The user has requested to adjust their schedule.
            Please consider their request and provide an updated schedule.
            """)
    String adjustSchedule(String adjustmentCommand);

    // Function to analyze a user's message and generate a new schedule entry
    @SystemMessage("""
       Create a schedule entry based on the provided user ID and message. Extract the task details from the message and structure them in a JSON format with fields customized based on user input, avoiding generic responses.
       Please follow this JSON structure for the output:
       {
            "title": "Descriptive title based on the user's message",
            "content": "Detailed description of the task including any specified timings or priorities",
            "tags": ["Relevant tags based on the task type (study, work, health, leisure)"],
            "startTime": "Start time in YYYY-MM-DD HH:MM:SS format, with defaults if unspecified",
            "endTime": "End time in YYYY-MM-DD HH:MM:SS format, assuming a reasonable duration if not mentioned",
            "userId": User ID as provided
       }
       Note:
       - Use details from the user's message to personalize the 'title' and 'content'.
       - If timing is vague, like 'tomorrow morning', default to 9:00 AM with a 1-hour duration.
       - Ensure 'userId' exactly matches the provided input.
    """)
    public String createNewSchedule(@V("userId:") Long userId, @UserMessage String message);



}
