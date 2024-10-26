package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface HealthReportAIService {

    // Method to generate a health report based on user health data
    @SystemMessage("""
            You are tasked with generating a personalized health report for a user based on their health data. The report should follow this structured format:
             
            #### Observations:
            1. **Heart Rate:** Provide an analysis of the user's average heart rate and its implications for cardiovascular health.
            2. **Physical Activity:** Comment on the user's physical activity level based on their steps per minute, noting any benefits for overall health.
            3. **Sleep Quality:** Assess the user's sleep duration and distribution across deep, light, and REM stages, providing insights on their sleep quality.
            4. **Calories Burned:** Describe what the user's calorie expenditure suggests about their physical activity levels.
                                                                                                                                    
            #### Potential Concerns:
            - Identify any significant health concerns based on the provided data, or state "No significant concerns based on the provided health data" if applicable.
                                                                                                                                    
            #### Recommendations:
            1. **Maintain Physical Activity:** Offer advice on maintaining or adjusting physical activity to support health and wellness.
            2. **Sleep Hygiene:** Suggest ways to optimize sleep quality, such as consistent sleep schedules or good sleep hygiene practices.
            3. **Healthy Diet:** Recommend pairing physical activity with a balanced diet to support fitness and health goals.
                                                                                                                                    
            #### Next Steps:
            - Encourage the user to consult with their healthcare provider (e.g., Dr. [Doctor’s Name]) if they have specific health goals or concerns, for more personalized guidance.
            
            #### give a scores from 60 to 100 based on the health condition and should be the int.
                                                                                                                              
            End the report with a reminder:\s
            "Maintaining a balance of physical activity, quality sleep, and a healthy diet is key to overall well-being. Keep up the good work!"
                                                                                                                 
            ---
            This report is based on the health data provided and is intended for informational purposes only. It is not a substitute for professional medical advice.
            """)
    public String generateHealthReport(String userHealthData);

    // Method to adjust an existing health report based on user feedback
    @SystemMessage("""
            The user has provided feedback to adjust their health report. Consider the user's request and preferences
            and return an updated report that reflects these adjustments.
            """)
    public String adjustHealthReport(String userHealthData, String adjustmentCommand);
}
