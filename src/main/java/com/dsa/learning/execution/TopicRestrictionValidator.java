package com.dsa.learning.execution;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.MistakeCategory;
import com.dsa.learning.model.Problem;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class TopicRestrictionValidator {

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final MistakeCategory mistakeCategory;
        private final String specificMistake;
        private final String suggestion;

        public ValidationResult(boolean valid, String errorMessage, MistakeCategory mistakeCategory, String specificMistake, String suggestion) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.mistakeCategory = mistakeCategory;
            this.specificMistake = specificMistake;
            this.suggestion = suggestion;
        }

        public static ValidationResult ok() {
            return new ValidationResult(true, null, null, null, null);
        }

        public static ValidationResult suboptimal(String errorMessage, MistakeCategory category, String specificMistake, String suggestion) {
            return new ValidationResult(false, errorMessage, category, specificMistake, suggestion);
        }

        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        public MistakeCategory getMistakeCategory() { return mistakeCategory; }
        public String getSpecificMistake() { return specificMistake; }
        public String getSuggestion() { return suggestion; }
    }

    public ValidationResult validate(Problem problem, String code) {
        if (problem == null || problem.getTopic() == null || code == null) {
            return ValidationResult.ok();
        }

        DsaTopic topic = problem.getTopic();
        String codeLower = code.toLowerCase(Locale.ROOT);
        String cleanCode = removeCommentsAndStrings(codeLower);

        switch (topic) {
            case RECURSION:
                return validateRecursion(problem, cleanCode);

            case SLIDING_WINDOW:
                return validateSlidingWindow(problem, cleanCode);

            case TWO_POINTER:
                return validateTwoPointer(problem, cleanCode);

            case BINARY_SEARCH:
                return validateBinarySearch(problem, cleanCode);

            case STACK:
                return validateStack(problem, cleanCode);

            case QUEUE:
                return validateQueue(problem, cleanCode);

            case HASHMAP:
                return validateHashMap(problem, cleanCode);

            case DYNAMIC_PROGRAMMING:
                return validateDynamicProgramming(problem, cleanCode);

            case LINKED_LIST:
                return validateLinkedList(problem, cleanCode);

            case TREES:
                return validateTrees(problem, cleanCode);

            case GRAPHS:
                return validateGraphs(problem, cleanCode);

            case SORTING:
            case ARRAYS:
            case STRINGS:
            default:
                return ValidationResult.ok();
        }
    }

    private ValidationResult validateRecursion(Problem problem, String code) {
        String methodName = problem.getMethodName() != null ? problem.getMethodName().toLowerCase() : "solve";
        long methodCallCount = Pattern.compile("\\b" + Pattern.quote(methodName) + "\\s*\\(").matcher(code).results().count();
        boolean hasSelfCall = methodCallCount >= 2;

        boolean hasRecursiveHelper = (code.contains("helper(") && code.indexOf("helper(") != code.lastIndexOf("helper(")) ||
                                     (code.contains("recurse(") && code.indexOf("recurse(") != code.lastIndexOf("recurse(")) ||
                                     (code.contains("dfs(") && code.indexOf("dfs(") != code.lastIndexOf("dfs(")) ||
                                     (code.contains("backtrack(") && code.indexOf("backtrack(") != code.lastIndexOf("backtrack("));

        if (!hasSelfCall && !hasRecursiveHelper) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: Your logic works, but you used an iterative loop instead of Recursion.\n" +
                    "While loops produce correct outputs for small cases, this problem is intended to build your Divide & Conquer and Call-Stack state management intuition.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: Iterative loop used instead of required Recursion",
                    "Restructure your thought process: 1) Define the base termination check (e.g. if (n <= 1) return n;), 2) Express the recurrence self-call solve(n-1) to break the problem into smaller subproblems."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateSlidingWindow(Problem problem, String code) {
        boolean hasWindowPointers = (code.contains("left") || code.contains("start") || code.contains("l =") || code.contains("i =")) &&
                                    (code.contains("right") || code.contains("end") || code.contains("r =") || code.contains("j ="));
        
        boolean hasNestedForLoops = Pattern.compile("for\\s*\\([^)]+\\)\\s*\\{[^}]*for\\s*\\([^)]+\\)").matcher(code).find();

        if (hasNestedForLoops && !code.contains("while")) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: You solved this with a nested loop brute-force re-evaluation (O(N^2) complexity).\n" +
                    "While this passes small tests, it will hit Time Limit Exceeded (TLE) on large inputs (N = 10^5). The optimal Sliding Window technique achieves O(N) linear time.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: O(N^2) Brute-Force instead of O(N) Sliding Window",
                    "Restructure your thought process: Maintain left and right boundaries. Expand the right boundary to accumulate elements, and contract the left boundary only when constraints are breached."
            );
        }

        if (!hasWindowPointers && !code.contains("while")) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: Your solution does not maintain a dynamic window boundary.\n" +
                    "To master the Sliding Window paradigm, you should track contiguous subarray states without full re-computation.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: Missing Sliding Window dynamic pointer management",
                    "Use left and right pointers to track subarray state in a single O(N) pass."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateTwoPointer(Problem problem, String code) {
        boolean hasTwoPointers = (code.contains("left") && code.contains("right")) ||
                                 (code.contains("low") && code.contains("high")) ||
                                 (code.contains("start") && code.contains("end")) ||
                                 (code.contains("i++") && code.contains("j--")) ||
                                 (code.contains("left++") && code.contains("right--")) ||
                                 (code.contains("p1") && code.contains("p2")) ||
                                 (code.contains("i = 0") && (code.contains("j =") || code.contains("length - 1")));

        boolean hasNestedBruteForce = Pattern.compile("for\\s*\\([^)]+\\)\\s*\\{[^}]*for\\s*\\([^)]+\\)").matcher(code).find() &&
                                     !code.contains("left++") && !code.contains("right--");

        if (hasNestedBruteForce) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: You solved this by checking every pair with nested O(N^2) loops.\n" +
                    "Since the problem allows directional pointer convergence, the Two Pointer technique allows you to solve it in O(N) time and O(1) extra space.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: Nested O(N^2) pair-checking instead of Two Pointer",
                    "Restructure your thought process: Initialize left at start and right at end. Advance left++ or decrement right-- based on comparison."
            );
        }

        if (!hasTwoPointers && !code.contains("while")) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: Your approach lacks explicit dual-pointer convergence.\n" +
                    "Using Two Pointers eliminates redundant comparisons and minimizes space overhead.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: Missing Two Pointer convergence logic",
                    "Use two index pointers moving toward each other to solve in O(N) time."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateBinarySearch(Problem problem, String code) {
        boolean hasBinarySearchMarkers = (code.contains("low") || code.contains("left") || code.contains("start") || code.contains("l =")) &&
                                         (code.contains("high") || code.contains("right") || code.contains("end") || code.contains("r =")) &&
                                         (code.contains("mid") || code.contains("/ 2") || code.contains(">>> 1") || code.contains(">> 1"));

        boolean hasPureLinearScan = (code.contains("for (int i = 0") || code.contains("for(int i=0")) &&
                                    (code.contains("== target") || code.contains("equals(target)")) &&
                                    !hasBinarySearchMarkers;

        if (hasPureLinearScan) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: You performed a linear O(N) sequential search.\n" +
                    "Because the input is sorted, you can discard half the remaining search space on each comparison to achieve optimal logarithmic O(log N) runtime.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: Linear O(N) scan used instead of O(log N) Binary Search",
                    "Restructure your thought process: Maintain low and high bounds, calculate mid = low + (high - low)/2, and halve the search window conditionally."
            );
        }

        if (!hasBinarySearchMarkers) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: Your solution does not utilize logarithmic interval halving.\n" +
                    "Binary Search requires dynamic midpoint calculation and boundary contraction.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: Missing Binary Search interval halving",
                    "Implement binary midpoint halving: mid = (low + high) / 2 to search in O(log N) time."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateStack(Problem problem, String code) {
        boolean hasStackUsage = code.contains("stack") || code.contains("deque") || code.contains("arraydeque") ||
                                code.contains("push(") || code.contains("pop()") || code.contains("peek()");

        if (!hasStackUsage) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: You solved this without using a Last-In-First-Out (LIFO) Stack.\n" +
                    "Stack data structures are essential for monotonic tracking and matching nested/paired hierarchies.",
                    MistakeCategory.WRONG_DATA_STRUCTURE,
                    "Sub-optimal Approach: Stack data structure not utilized",
                    "Use java.util.Stack or Deque<Character> stack = new ArrayDeque<>() with push(), pop(), and peek() operations."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateQueue(Problem problem, String code) {
        boolean hasQueueUsage = code.contains("queue") || code.contains("deque") || code.contains("arraydeque") ||
                                code.contains("offer(") || code.contains("poll()") || code.contains("peek()") ||
                                code.contains("priorityqueue") || code.contains("add(");

        if (!hasQueueUsage) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: You solved this without a First-In-First-Out (FIFO) Queue.\n" +
                    "Queue buffers are necessary for sliding windows, stream processing, and breadth-first state scheduling.",
                    MistakeCategory.WRONG_DATA_STRUCTURE,
                    "Sub-optimal Approach: Queue data structure not utilized",
                    "Use Queue<Integer> queue = new ArrayDeque<>() with offer() and poll() operations."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateHashMap(Problem problem, String code) {
        boolean hasHashUsage = code.contains("hashmap") || code.contains("map<") || code.contains("hashset") ||
                               code.contains("set<") || code.contains("put(") || code.contains("getordefault") ||
                               code.contains("containskey") || code.contains(".add(");

        boolean hasNestedSearch = Pattern.compile("for\\s*\\([^)]+\\)\\s*\\{[^}]*for\\s*\\([^)]+\\)").matcher(code).find();

        if (hasNestedSearch && !hasHashUsage) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: You used nested O(N^2) loops to check for existing elements.\n" +
                    "Using a HashMap or HashSet gives O(1) average constant-time lookups, reducing total runtime to linear O(N).",
                    MistakeCategory.WRONG_DATA_STRUCTURE,
                    "Sub-optimal Approach: Nested scanning instead of O(1) HashMap lookups",
                    "Store elements in a Map<Integer, Integer> map = new HashMap<>() for instant O(1) lookups."
            );
        }

        if (!hasHashUsage) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: Your solution does not use a Hash Table data structure.\n" +
                    "Hash tables provide O(1) key-value mapping and frequency tracking.",
                    MistakeCategory.WRONG_DATA_STRUCTURE,
                    "Sub-optimal Approach: HashMap/HashSet not utilized",
                    "Use a HashMap or HashSet to track frequencies and seen elements in O(1) time."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateDynamicProgramming(Problem problem, String code) {
        boolean hasDpUsage = code.contains("dp[") || code.contains("memo") || code.contains("table[") ||
                             code.contains("dp =") || code.contains("dp.put") || code.contains("math.max") ||
                             code.contains("math.min") || code.contains("prev") || code.contains("prev1");

        if (!hasDpUsage) {
            return ValidationResult.suboptimal(
                    "🧠 Thought Process Diagnosis: Your approach does not use Dynamic Programming state memoization.\n" +
                    "DP stores overlapping subproblem solutions to prevent exponential re-computations.",
                    MistakeCategory.WRONG_ALGORITHM,
                    "Sub-optimal Approach: DP memoization table or state caching not used",
                    "Define a state array int[] dp = new int[n + 1] and express the recurrence relation dp[i] = dp[i-1] + ..."
            );
        }
        return ValidationResult.ok();
    }

    private ValidationResult validateLinkedList(Problem problem, String code) {
        return ValidationResult.ok();
    }

    private ValidationResult validateTrees(Problem problem, String code) {
        return ValidationResult.ok();
    }

    private ValidationResult validateGraphs(Problem problem, String code) {
        return ValidationResult.ok();
    }

    private String removeCommentsAndStrings(String code) {
        if (code == null) return "";
        return code.replaceAll("//.*", "")
                   .replaceAll("/\\*.*?\\*/", "")
                   .replaceAll("\".*?\"", "\"\"");
    }
}
