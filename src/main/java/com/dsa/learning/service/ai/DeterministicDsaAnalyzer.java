package com.dsa.learning.service.ai;

import com.dsa.learning.model.DsaTopic;
import com.dsa.learning.model.MistakeCategory;
import com.dsa.learning.model.Problem;
import com.dsa.learning.model.SubmissionStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DeterministicDsaAnalyzer {

    public static class MistakeAnalysisResult {
        public MistakeCategory category;
        public String specificMistake;
        public String explanation;
        public String rootCause;
        public String suggestedFix;

        public MistakeAnalysisResult(MistakeCategory category, String specificMistake, String explanation, String rootCause, String suggestedFix) {
            this.category = category;
            this.specificMistake = specificMistake;
            this.explanation = explanation;
            this.rootCause = rootCause;
            this.suggestedFix = suggestedFix;
        }
    }

    public MistakeAnalysisResult analyze(Problem problem, String code, SubmissionStatus status, String compilerError, String failedInput, String actualOutput, String expectedOutput) {
        DsaTopic topic = problem.getTopic() != null ? problem.getTopic() : DsaTopic.ARRAYS;
        String codeLower = code != null ? code.toLowerCase(Locale.ROOT) : "";

        // 1. Check Time Limit Exceeded
        if (status == SubmissionStatus.TIME_LIMIT_EXCEEDED) {
            if (topic == DsaTopic.RECURSION || topic == DsaTopic.DYNAMIC_PROGRAMMING) {
                return new MistakeAnalysisResult(
                        MistakeCategory.TIME_COMPLEXITY,
                        "Exponential recursion without memoization",
                        "The recursive solution explores overlapping subproblems repeatedly, resulting in O(2^N) or O(N!) exponential time complexity.",
                        "Subproblem results are computed from scratch on every recursive branch without caching.",
                        "Introduce a memoization table (HashMap or array) or convert to iterative DP."
                );
            } else if (codeLower.contains("while") || codeLower.contains("for")) {
                return new MistakeAnalysisResult(
                        MistakeCategory.LOOP_ERROR,
                        "Infinite loop or missing loop termination",
                        "The loop continuation condition never becomes false because the loop variable or pointer is not progressing monotonically.",
                        "Pointer/counter update is skipped in one of the conditional branches.",
                        "Ensure the loop variables (e.g. left/right pointers, index variables) always advance inside every if/else branch."
                );
            } else {
                return new MistakeAnalysisResult(
                        MistakeCategory.TIME_COMPLEXITY,
                        "Suboptimal time complexity",
                        "The algorithm uses a brute-force approach (such as nested loops O(N^2)) which exceeds the execution limit for large inputs.",
                        "Nested iterations over large input sizes without an optimized data structure.",
                        "Use an optimal approach like HashMaps, Two Pointers, or Binary Search to achieve O(N) or O(N log N)."
                );
            }
        }

        // 2. Compilation Error
        if (status == SubmissionStatus.COMPILATION_ERROR) {
            return new MistakeAnalysisResult(
                    MistakeCategory.LOGIC_ERROR,
                    "Syntax or Type Mismatch error",
                    "The Java code contains syntax issues, incompatible return types, or unimported classes.",
                    compilerError != null && !compilerError.isEmpty() ? compilerError : "Compilation failed",
                    "Carefully check method return types, variable declarations, and semicolon placements."
            );
        }

        // 3. Topic-Specific Heuristic Diagnoses for Wrong Answer / Runtime Errors
        switch (topic) {
            case SLIDING_WINDOW:
                if (!codeLower.contains("left++") && !codeLower.contains("left +=") && !codeLower.contains("start++")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.POINTER_MOVEMENT,
                            "Incorrect left pointer movement in Sliding Window",
                            "The student expands the right boundary but does not shrink or advance the left boundary when the window condition is violated.",
                            "Window invalidation condition is detected but the left pointer remains static.",
                            "When window constraint fails, advance `left` pointer in a while loop and evict outgoing elements from the frequency map/set."
                    );
                } else if (codeLower.contains("while") && (codeLower.contains("hashset") || codeLower.contains("hashmap") || codeLower.contains("count"))) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.BOUNDARY_CONDITION,
                            "Incorrect window shrinking or off-by-one window length",
                            "The window size calculation `right - left + 1` or frequency map decrement is placed at an incorrect step in the loop.",
                            "State update order: element is removed before frequency check or window length is recorded prematurely.",
                            "Update frequency counts first, shrink while invalid, and then update max/min window length."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.POINTER_MOVEMENT,
                            "Incorrect boundary handling in Sliding Window",
                            "The window boundaries do not properly capture edge cases (e.g. single character strings, empty arrays, or identical elements).",
                            "Edge condition check is missing before processing sliding window loop.",
                            "Check for length 0 or 1 edge cases and verify the initial window state."
                    );
                }

            case RECURSION:
                if (!codeLower.contains("if") || (!codeLower.contains("return") && !codeLower.contains("base"))) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.RECURSION_BASE_CASE,
                            "Missing or incorrect base condition in Recursion",
                            "The recursive function lacks a valid termination condition, causing infinite recursion or StackOverflowError.",
                            "Base case check `if (index >= length)` or `if (target == 0)` is omitted or placed after recursive calls.",
                            "Always define and return from the base case at the very beginning of the recursive method."
                    );
                } else if (codeLower.contains("add") && codeLower.contains("remove")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.LOGIC_ERROR,
                            "Backtracking state cleanup error",
                            "The mutable state (e.g. List or StringBuilder) is not properly restored after returning from recursive branches.",
                            "Missing `tempList.remove(tempList.size() - 1)` after recursive call.",
                            "Ensure every state change in backtracking has an equal and opposite backtrack restoration step."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.RECURSION_BASE_CASE,
                            "Incorrect base condition handling",
                            "The recursion reaches invalid subproblem states (e.g., negative indices or negative targets) without returning.",
                            "Boundary condition fails to prune invalid branch before continuing.",
                            "Add guard clauses: `if (index >= n || target < 0) return;` at the top."
                    );
                }

            case TWO_POINTER:
                if (!codeLower.contains("left++") || !codeLower.contains("right--")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.POINTER_MOVEMENT,
                            "Asymmetric or improper pointer movement",
                            "The left and right pointers are not updated consistently based on the comparison of the current sum with the target.",
                            "Both pointers increment in the same direction or pointer movement condition is inverted.",
                            "If current sum < target, increase `left++`; if current sum > target, decrease `right--`."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.EDGE_CASE,
                            "Duplicate element handling in Two Pointer",
                            "The algorithm fails to skip identical adjacent elements, producing duplicate triplets/pairs in the output.",
                            "Missing `while (left < right && nums[left] == nums[left+1]) left++;` skipping step.",
                            "Add duplicate skipping loops after finding a valid matching pair."
                    );
                }

            case BINARY_SEARCH:
                if (codeLower.contains("low + high / 2") || codeLower.contains("(low+high)/2")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.BOUNDARY_CONDITION,
                            "Integer overflow in midpoint calculation",
                            "Using `(low + high) / 2` causes integer 32-bit overflow when `low + high` exceeds `Integer.MAX_VALUE`.",
                            "Arithmetic overflow before division.",
                            "Use `int mid = low + (high - low) / 2;`."
                    );
                } else if (codeLower.contains("low = mid") || codeLower.contains("high = mid")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.BOUNDARY_CONDITION,
                            "Off-by-one boundary adjustment in Binary Search",
                            "Updating `low = mid` or `high = mid` without `+ 1` or `- 1` causes an infinite loop when `low + 1 == high`.",
                            "Midpoint inclusion when search space must shrink strictly.",
                            "Use `low = mid + 1` and `high = mid - 1` with `while (low <= high)`."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.BOUNDARY_CONDITION,
                            "Incorrect search interval termination",
                            "The binary search termination condition (`low <= high` vs `low < high`) does not match the interval updates.",
                            "Mismatch between closed interval `[low, high]` and open interval `[low, high)`.",
                            "Maintain consistent closed interval invariant `low = 0, high = n - 1` and `while (low <= high)`."
                    );
                }

            case HASHMAP:
                if (codeLower.contains("containskey") && codeLower.contains("put")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.LOGIC_ERROR,
                            "Incorrect key-value mapping or premature lookup",
                            "The complement/difference is looked up after inserting the current element, causing the element to match with itself.",
                            "Order of `map.put()` before checking `map.containsKey(target - num)`.",
                            "Check `map.containsKey(complement)` BEFORE adding the current element to the map."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.WRONG_DATA_STRUCTURE,
                            "Suboptimal lookup or missing frequency tracking",
                            "The solution fails to correctly store counts or indices in the HashMap.",
                            "Map value type is incorrect or collisions are not handled.",
                            "Use `map.getOrDefault(key, 0) + 1` for frequency counting."
                    );
                }

            case STACK:
                if (!codeLower.contains("isempty")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.EDGE_CASE,
                            "Missing empty stack check before pop/peek",
                            "Calling `stack.pop()` or `stack.peek()` on an empty stack throws `EmptyStackException` on unbalanced inputs.",
                            "Unchecked stack access when closing brackets exceed opening brackets.",
                            "Always check `if (stack.isEmpty()) return false;` before popping."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.LOGIC_ERROR,
                            "Bracket matching logic error",
                            "The character popped from the stack is not properly compared with the corresponding opening/closing pair.",
                            "Incorrect mapping between '(', '{', '[' and ')', '}', ']'.",
                            "Use a Map or switch statement to verify that the popped opening parenthesis matches the current closing parenthesis."
                    );
                }

            case LINKED_LIST:
                if (codeLower.contains(".next.next") && !codeLower.contains("!= null")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.BOUNDARY_CONDITION,
                            "NullPointerException on list traversal",
                            "Accessing `curr.next.next` without verifying `curr.next != null` causes a runtime crash at list termination.",
                            "Chained pointer dereference without null guard.",
                            "Ensure `while (fast != null && fast.next != null)` before advancing pointers."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.POINTER_MOVEMENT,
                            "Lost head reference or pointer disconnection",
                            "Reversing or reordering pointers overwrites `curr.next` before caching the next node, breaking the list.",
                            "Pointer reassignment without `ListNode nextTemp = curr.next`.",
                            "Use a dummy head node and temporarily store `next` pointer before mutating links."
                    );
                }

            case DYNAMIC_PROGRAMMING:
                return new MistakeAnalysisResult(
                        MistakeCategory.WRONG_ALGORITHM,
                        "Incorrect recurrence relation or base DP state initialization",
                        "The DP table transition `dp[i] = dp[i-1] + ...` does not account for all valid predecessor states or base cases (e.g. dp[0]).",
                        "Missing initialization of `dp[0]` or incorrect loop direction for knapsack/state transitions.",
                        "Explicitly define base cases `dp[0] = 1`, write out the transition equation on paper, and check 1D vs 2D space requirements."
                );

            case ARRAYS:
            case STRINGS:
            default:
                if (codeLower.contains("<=") && codeLower.contains(".length")) {
                    return new MistakeAnalysisResult(
                            MistakeCategory.BOUNDARY_CONDITION,
                            "Array Index Out of Bounds (Off-by-one)",
                            "Loop boundary condition `i <= array.length` accesses an index past the end of the 0-indexed array.",
                            "Upper loop limit is inclusive of `length` instead of `< length`.",
                            "Change loop condition from `i <= length` to `i < length`."
                    );
                } else {
                    return new MistakeAnalysisResult(
                            MistakeCategory.LOGIC_ERROR,
                            "Incorrect condition logic or unhandled edge case",
                            "The algorithm produces an incorrect result for specific input combinations or edge cases (empty input, negative numbers, duplicates).",
                            "Conditional branch does not cover all problem constraints.",
                            "Trace through the failing test case step-by-step and verify all edge scenarios."
                    );
                }
        }
    }
}
