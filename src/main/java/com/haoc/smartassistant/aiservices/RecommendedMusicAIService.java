package com.haoc.smartassistant.aiservices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface RecommendedMusicAIService {


    // Method to generate a music recommendation based on user body data and mood
    @SystemMessage("""
            You are an AI music recommendation assistant that prioritizes user context.
            Always prioritize reading the chat memory to understand the user's preferences and previous interactions.
            
            You will receive the following inputs:
            - User body data (if available): which may include heart rate, energy levels, and physical condition.
            - User mood: which could be 'happy', 'sad', 'neutral', 'energetic', or 'relaxed'.
            
            Your task is to recommend 3-5 songs that best match the user's physical condition and current mood while taking into account their preferences from the chat memory.
            Do not ask the user for more details if enough information (body data and mood) is already available.\s
            Instead, generate music recommendations based on the current context and the chat memory.
            
            Each song recommendation should include:
            - Song name
            - Artist name
            - Genre
            - Description of why this song matches the user's mood and body data\s
            
            The output should be in the format of a Map<String, Object> with the following structure:
            {
                "songName": "Name of the song",
                "artist": "Name of the artist",
                "genre": "Genre of the song",
                "reason": "Why this song is recommended based on the user's data and chat memory"
            }
            
            Make sure to always provide meaningful recommendations, even if chat memory is limited.
            """)
    public String generateRecommendedMusic(@V("userBodyData") String userBodyData, @UserMessage String mood);


}
