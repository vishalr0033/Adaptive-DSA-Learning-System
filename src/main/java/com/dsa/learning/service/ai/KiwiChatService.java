package com.dsa.learning.service.ai;

import com.dsa.learning.dto.KiwiChatRequestDto;
import com.dsa.learning.dto.KiwiChatResponseDto;
import com.dsa.learning.model.Problem;
import com.dsa.learning.repository.ProblemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Service
public class KiwiChatService {

    private static final Logger logger = LoggerFactory.getLogger(KiwiChatService.class);

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    private final ProblemRepository problemRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Autowired
    public KiwiChatService(ProblemRepository problemRepository, ObjectMapper objectMapper) {
        this.problemRepository = problemRepository;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build();
    }

    public KiwiChatResponseDto chat(KiwiChatRequestDto request) {
        Problem problem = null;
        if (request.getProblemId() != null) {
            problem = problemRepository.findById(request.getProblemId()).orElse(null);
        }

        String effectiveApiKey = getEffectiveApiKey();
        if (effectiveApiKey != null && !effectiveApiKey.trim().isEmpty()) {
            try {
                String geminiReply = callGeminiForChat(effectiveApiKey, request, problem);
                if (geminiReply != null && !geminiReply.trim().isEmpty()) {
                    List<String> suggestions = generateFollowUpSuggestions(request.getMessage(), problem);
                    return new KiwiChatResponseDto(geminiReply, suggestions, "friendly", true);
                }
            } catch (Exception e) {
                logger.warn("Kiwi AI API call failed. Using embedded Kiwi knowledge engine: {}", e.getMessage());
            }
        }

        // Embedded Intelligent Kiwi Engine
        return generateLocalKiwiReply(request, problem);
    }

    private String getEffectiveApiKey() {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey.trim();
        }
        String envKey = System.getenv("GEMINI_API_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey.trim();
        }
        return null;
    }

    private String callGeminiForChat(String key, KiwiChatRequestDto request, Problem problem) throws Exception {
        String endpoint = apiUrl + "?key=" + key;

        StringBuilder systemInstruction = new StringBuilder();
        systemInstruction.append("You are Kiwi 🥝, an exceptionally friendly, encouraging, and intelligent AI Data Structures and Algorithms (DSA) Mentor and Tutor. ");
        systemInstruction.append("You help students understand concepts, debug code mistakes, learn multiple approaches, and optimize time/space complexity. ");
        systemInstruction.append("Adopt a supportive, Socratic teaching style. Give progressive hints rather than immediately dumping complete solutions, unless specifically asked. ");
        systemInstruction.append("Use Markdown formatting with clean bullet points and code formatting where relevant. Keep answers clear, engaging, and directly applicable.");

        StringBuilder userContext = new StringBuilder();
        if (problem != null) {
            userContext.append("### Active Problem Context:\n");
            userContext.append("- **Title**: ").append(problem.getTitle()).append("\n");
            userContext.append("- **Primary Topic**: ").append(problem.getTopic()).append("\n");
            if (problem.getAlternativeTopics() != null && !problem.getAlternativeTopics().isEmpty()) {
                userContext.append("- **Alternative Topics**: ").append(problem.getAlternativeTopics()).append("\n");
            }
            if (problem.getSupportedApproaches() != null && !problem.getSupportedApproaches().isEmpty()) {
                userContext.append("- **Supported Approaches**: ").append(problem.getSupportedApproaches()).append("\n");
            }
            userContext.append("- **Description**: ").append(problem.getDescription()).append("\n\n");
        }

        if (request.getCurrentCode() != null && !request.getCurrentCode().trim().isEmpty()) {
            userContext.append("### Student's Current Code:\n```java\n").append(request.getCurrentCode()).append("\n```\n\n");
        }

        if (request.getLastError() != null && !request.getLastError().trim().isEmpty()) {
            userContext.append("### Recent Compiler/Execution Error:\n```\n").append(request.getLastError()).append("\n```\n\n");
        }

        userContext.append("### Student's Question:\n").append(request.getMessage());

        Map<String, Object> contentMap = new HashMap<>();
        Map<String, Object> partsMap = new HashMap<>();
        partsMap.put("text", systemInstruction + "\n\n" + userContext);
        contentMap.put("parts", List.of(partsMap));

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(contentMap));

        String requestJson = objectMapper.writeValueAsString(payload);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini returned HTTP " + response.statusCode());
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode candidates = root.path("candidates");
        if (candidates.isArray() && !candidates.isEmpty()) {
            JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
            if (!textNode.isMissingNode()) {
                return textNode.asText();
            }
        }
        return null;
    }

    private KiwiChatResponseDto generateLocalKiwiReply(KiwiChatRequestDto request, Problem problem) {
        String msg = request.getMessage() != null ? request.getMessage().toLowerCase().trim() : "";
        String code = request.getCurrentCode() != null ? request.getCurrentCode() : "";
        String error = request.getLastError() != null ? request.getLastError() : "";

        StringBuilder reply = new StringBuilder();
        List<String> suggestions = new ArrayList<>();
        String mood = "friendly";

        // 1. Error / Mistake Analysis Intent
        if (msg.contains("error") || msg.contains("mistake") || msg.contains("fail") || msg.contains("bug") || msg.contains("wrong") || !error.isEmpty() && msg.contains("why")) {
            mood = "analyzing";
            reply.append("### 🔍 Kiwi's Mistake & Error Diagnostic\n\n");
            
            if (error.contains("ArrayIndexOutOfBoundsException") || error.contains("IndexOutOfBoundsException")) {
                reply.append("⚠️ **Index Out of Bounds Detected!**\n");
                reply.append("Your code is trying to access an element outside the valid array boundary `[0 ... length - 1]`.\n\n");
                reply.append("**Quick Checklist:**\n");
                reply.append("1. Check loops: Are you using `i <= nums.length` instead of `i < nums.length`?\n");
                reply.append("2. When doing lookaheads (`i + 1` or `j - 1`), verify boundary guards (`i + 1 < nums.length`).\n");
            } else if (error.contains("NullPointerException")) {
                reply.append("⚠️ **NullPointer Exception Detected!**\n");
                reply.append("An object or reference variable is `null` when you invoked a method or accessed a field on it.\n\n");
                reply.append("**Quick Checklist:**\n");
                reply.append("1. In Linked Lists: Always check `while (current != null && current.next != null)` before accessing `current.next`.\n");
                reply.append("2. In HashMaps: Check `map.containsKey(key)` before calling `map.get(key).operation()`.\n");
            } else if (error.contains("Time Limit Exceeded") || error.contains("TLE") || msg.contains("tle") || msg.contains("slow")) {
                reply.append("⚡ **Time Limit Exceeded (Optimization Needed)!**\n");
                reply.append("Your current approach has a time complexity that is too high for the problem constraints (e.g. $O(N^2)$ brute force on $N = 10^5$).\n\n");
                if (problem != null && problem.getSupportedApproaches() != null) {
                    reply.append("**Optimal Approaches to consider:**\n");
                    reply.append("💡 ").append(problem.getSupportedApproaches()).append("\n");
                }
            } else if (!error.isEmpty()) {
                reply.append("Here is what went wrong during execution:\n");
                reply.append("> `").append(error).append("`\n\n");
                reply.append("Make sure your return values match the expected types and all edge cases (empty inputs, single elements, negative numbers) are guarded.");
            } else {
                reply.append("Let's debug your code together! 🛠️\n\n");
                reply.append("1. Trace the input with a small sample test case.\n");
                reply.append("2. Check variable initialization before loops.\n");
                reply.append("3. Verify return statements cover all branch paths.\n");
            }

            suggestions.add("💡 Give me a hint for this problem");
            suggestions.add("⚡ What is the optimal time complexity?");
            suggestions.add("🎯 Show supported approaches");
        }
        // 2. Hint Request
        else if (msg.contains("hint") || msg.contains("clue") || msg.contains("stuck") || msg.contains("help")) {
            mood = "hinting";
            reply.append("### 💡 Kiwi's Algorithm Hint\n\n");
            if (problem != null) {
                reply.append("**Problem:** ").append(problem.getTitle()).append(" (").append(problem.getTopic()).append(")\n\n");
                reply.append("🎯 **Key Intuition:**\n");
                
                String topicStr = problem.getTopic().name();
                if ("ARRAYS".equals(topicStr) || "HASHMAP".equals(topicStr)) {
                    reply.append("- Can you store previously seen elements in a `HashMap` or `HashSet` to turn an $O(N^2)$ nested search into an $O(N)$ lookup?\n");
                    reply.append("- Look for patterns like prefix sums, two-pointer scanning, or frequency counting.\n");
                } else if ("TWO_POINTER".equals(topicStr)) {
                    reply.append("- Consider placing one pointer at the start `left = 0` and one at the end `right = n - 1`.\n");
                    reply.append("- When should you increment `left` versus decrement `right`?\n");
                } else if ("SLIDING_WINDOW".equals(topicStr)) {
                    reply.append("- Expand your `right` pointer to include elements.\n");
                    reply.append("- Once the window condition is violated, advance `left` until valid again, updating your maximum/minimum result at each step.\n");
                } else if ("STACK".equals(topicStr)) {
                    reply.append("- Think about Last-In-First-Out (LIFO) order.\n");
                    reply.append("- Stacks are ideal for matching pairs (like parentheses) and maintaining monotonic increasing/decreasing orders.\n");
                } else if ("TREES".equals(topicStr)) {
                    reply.append("- What is the base case? Usually `if (root == null) return ...;`.\n");
                    reply.append("- Can you solve the problem for `root.left` and `root.right` recursively and combine their results?\n");
                } else if ("DYNAMIC_PROGRAMMING".equals(topicStr)) {
                    reply.append("- Identify the state: What parameters uniquely define a subproblem?\n");
                    reply.append("- Formulate the recurrence relation: $dp[i] = \\text{choice}(dp[i-1], dp[i-2] \\dots)$.\n");
                } else {
                    reply.append("- Break the problem into smaller subproblems.\n");
                    reply.append("- Identify what state needs to be maintained at each step.\n");
                }

                if (problem.getSupportedApproaches() != null && !problem.getSupportedApproaches().isEmpty()) {
                    reply.append("\n**Recommended Paradigms:**\n");
                    reply.append("✨ ").append(problem.getSupportedApproaches()).append("\n");
                }
            } else {
                reply.append("Select or open any DSA problem, and I will give you step-by-step intuition tailored to that specific challenge!");
            }

            suggestions.add("⚡ Can you analyze my code complexity?");
            suggestions.add("🔍 Explain common mistakes for this topic");
            suggestions.add("🧠 Give me pseudocode guidance");
        }
        // 3. Complexity Request
        else if (msg.contains("complexity") || msg.contains("time complexity") || msg.contains("space complexity") || msg.contains("big o") || msg.contains("big-o")) {
            mood = "analyzing";
            reply.append("### ⚡ Kiwi's Big-O Complexity Analysis\n\n");
            
            boolean hasNestedLoops = code.contains("for") && (code.indexOf("for") != code.lastIndexOf("for"));
            boolean hasMap = code.contains("Map") || code.contains("Set") || code.contains("List") || code.contains("new int[");
            
            if (hasNestedLoops) {
                reply.append("📊 **Estimated Current Code Complexity:**\n");
                reply.append("- **Time Complexity:** ~$O(N^2)$ (detected nested loops)\n");
                reply.append("- **Space Complexity:** ").append(hasMap ? "$O(N)$ (auxiliary data structures)" : "$O(1)$ (constant space)").append("\n\n");
                reply.append("💡 **Can we optimize to $O(N)$ or $O(N \\log N)$?**\n");
                reply.append("Try using a Hash Table, Two Pointers, or Sorting to eliminate the inner loop!");
            } else if (code.contains("for") || code.contains("while")) {
                reply.append("📊 **Estimated Current Code Complexity:**\n");
                reply.append("- **Time Complexity:** ~$O(N)$ (single linear pass)\n");
                reply.append("- **Space Complexity:** ").append(hasMap ? "$O(N)$" : "$O(1)$").append("\n\n");
                reply.append("🎉 Great linear time efficiency! Verify that all auxiliary space constraints are met.");
            } else {
                reply.append("Share or run your code in the editor, and I will calculate both its asymptotic Time and Space complexities!");
            }

            suggestions.add("💡 Give me an optimization hint");
            suggestions.add("🎯 What approaches does this problem support?");
        }
        // 4. Approaches Request
        else if (msg.contains("approach") || msg.contains("topics") || msg.contains("solution") || msg.contains("method")) {
            mood = "friendly";
            reply.append("### 🎯 Supported Algorithmic Approaches\n\n");
            if (problem != null) {
                reply.append("For **").append(problem.getTitle()).append("**:\n\n");
                if (problem.getSupportedApproaches() != null && !problem.getSupportedApproaches().isEmpty()) {
                    String[] parts = problem.getSupportedApproaches().split("\\|");
                    for (String part : parts) {
                        reply.append("- 🚀 **").append(part.trim()).append("**\n");
                    }
                }
                if (problem.getAlternativeTopics() != null && !problem.getAlternativeTopics().isEmpty()) {
                    reply.append("\n**Related DSA Topics:** `").append(problem.getTopic()).append("`, `").append(problem.getAlternativeTopics()).append("`\n");
                }
            } else {
                reply.append("Open any problem in the editor, and I will list all valid algorithmic approaches (e.g. Brute Force, Two Pointers, Hash Table, DP) with their trade-offs!");
            }

            suggestions.add("💡 Give me a hint for the best approach");
            suggestions.add("⚡ Compare their time complexities");
        }
        // 5. General / Greeting
        else {
            mood = "celebrating";
            reply.append("👋 **Hi! I'm Kiwi 🥝**, your AI DSA Mentor!\n\n");
            reply.append("I can help you with:\n");
            reply.append("- 💡 **Socratic Hints** (without spoiling the full answer)\n");
            reply.append("- 🔍 **Debugging Compiler & Logical Errors**\n");
            reply.append("- ⚡ **Time & Space Complexity Analysis ($O(N)$)**\n");
            reply.append("- 🧠 **Exploring Multiple DSA Approaches**\n\n");
            if (problem != null) {
                reply.append("You are currently viewing **").append(problem.getTitle()).append("** (`").append(problem.getTopic()).append("`). How can I assist you?");
            } else {
                reply.append("What DSA concept or problem would you like to explore today?");
            }

            suggestions.add("💡 Give me a hint for this problem");
            suggestions.add("🔍 Why is my code failing?");
            suggestions.add("⚡ Check my time complexity");
            suggestions.add("🎯 What are the supported approaches?");
        }

        return new KiwiChatResponseDto(reply.toString(), suggestions, mood, true);
    }

    private List<String> generateFollowUpSuggestions(String userMsg, Problem problem) {
        List<String> list = new ArrayList<>();
        list.add("💡 Can you give me another hint?");
        list.add("⚡ What is the space complexity?");
        list.add("🎯 What is the most optimal approach?");
        return list;
    }
}
