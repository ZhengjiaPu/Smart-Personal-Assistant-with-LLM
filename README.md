# Smart-Personal-Assistant-with-LLM

## How to Run the Project

1. **Clone the repository** to your local machine

2. **Install maven dependency ** : run `mvn clean install` in you terminal

   ```bash
   mvn clean install
   ```

   

3. **Modify the configuration file**: Update the database credentials in `application.ym`l:

```yml
# path of application.yml : src/main/resources/application.yml

datasource:
  driver-class-name: com.mysql.cj.jdbc.Driver
  url: jdbc:mysql://localhost:3306/spa_db
  username: your-database-username
  password: your-database-password

```

3. **Run sql** to create database and insert test data

Path : 

- sql/create_table.sql
- sql/create_test_data.sql

4. **OpenAI API Key Configuration**

The OpenAI API key is stored in the `.env` file. The project uses `Langchain4j` to integrate the GPT-3.5 Turbo model. 

- **Important Notes:** The project uses Wayne Li's OpenAI API key. Avoid making excessive requests, as it may exhaust the quota.

- **Create a new file** at `src/main/resources/.env`.   

```bash
# create .env 
OPENAI_API_KEY=your-openai-api-key
```

- **Add your OpenAI API key** **or copy the `.env` file** shared in the group into this directory.

5. **Access the API documentation**: Once the project is running, open the Swagger documentation in your browser:

```bash
# It's a website
http://localhost:8101/api/swagger-ui.html
```



## Example Requests

### Generate Diet Plan with AI

API: `POST /api/bodyData/generate-diet-plan/{userId}`

```bash
curl -X GET "http://localhost:8101/api/bodyData/generate-diet-plan/1845693955367346178" 


```

```bash
Based on your body data, it seems like your BMI is 26.23, which falls into the overweight category. To support your goal of weight loss and overall health, I have created a personalized diet plan for you. This plan focuses on creating a calorie deficit through balanced meals and regular physical activity.

Here is a sample diet plan for you:

**Meal Plan:**

**Breakfast:**
- Greek yogurt with mixed berries and a sprinkle of chia seeds
- Whole grain toast with avocado slices

**Mid-Morning Snack:**
- Handful of almonds or an apple

**Lunch:**
- Grilled chicken salad with mixed greens, cherry tomatoes, cucumbers, and a vinaigrette dressing

**Afternoon Snack:**
- Carrot sticks with hummus

**Dinner:**
- Baked salmon with roasted vegetables (broccoli, bell peppers, and carrots)
- Quinoa or brown rice

**Evening Snack (if needed):**
- Greek yogurt with a drizzle of honey

**Hydration:**
- Drink at least 8-10 glasses of water throughout the day
- You can also have herbal teas or infused water for variety

**Physical Activity:**
- Aim for at least 30 minutes of moderate exercise most days of the week
- Include a mix of cardio (like walking, jogging, cycling) and strength training (such as bodyweight exercises or weightlifting)

**Additional Tips:**
- Focus on portion control and mindful eating
- Limit processed foods, sugary drinks, and excessive snacking
- Get enough sleep and manage stress levels to support weight loss efforts

Remember, it's essential to consult with a healthcare provider or a nutritionist before making significant changes to your diet or exercise routine. This plan is a general guideline and can be adjusted based on your specific needs and preferences. Good luck on your weight loss journey!
```

