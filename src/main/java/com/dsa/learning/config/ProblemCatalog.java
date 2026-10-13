package com.dsa.learning.config;

import com.dsa.learning.model.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemCatalog {

    public static class ProblemDefinition {
        public String title;
        public String description;
        public String exampleInput;
        public String exampleOutput;
        public String constraints;
        public Difficulty difficulty;
        public DsaTopic topic;
        public String starterCode;
        public String methodName;
        public String returnType;
        public String parameterTypes;
        public List<TestCase> testCases;
        public String alternativeTopics = "";
        public String supportedApproaches = "";

        public ProblemDefinition(String title, String description, String exampleInput, String exampleOutput,
                                 String constraints, Difficulty difficulty, DsaTopic topic,
                                 String starterCode, String methodName, String returnType, String parameterTypes,
                                 List<TestCase> testCases) {
            this.title = title;
            this.description = description;
            this.exampleInput = exampleInput;
            this.exampleOutput = exampleOutput;
            this.constraints = constraints;
            this.difficulty = difficulty;
            this.topic = topic;
            this.starterCode = starterCode;
            this.methodName = methodName;
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
            this.testCases = testCases;
        }

        public ProblemDefinition withApproaches(String altTopics, String approaches) {
            this.alternativeTopics = altTopics;
            this.supportedApproaches = approaches;
            return this;
        }
    }

    public static List<ProblemDefinition> get100Problems() {
        List<ProblemDefinition> list = new ArrayList<>();

        // ==========================================
        // 1. ARRAYS (8 Problems: 1 - 8)
        // ==========================================
        list.add(new ProblemDefinition(
            "Two Sum",
            "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.",
            "[2,7,11,15]\n9", "[0,1]", "2 <= nums.length <= 10^4", Difficulty.EASY, DsaTopic.ARRAYS,
            "class Solution {\n    public int[] solve(int[] nums, int target) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int",
            List.of(new TestCase(null, "[2,7,11,15]\n9", "[0,1]", true, false, "2+7=9"), new TestCase(null, "[3,2,4]\n6", "[1,2]", true, false, "2+4=6"))
        ).withApproaches("HASHMAP, TWO_POINTER", "HashMap (O(N) Time, O(N) Space) | Two Pointers (O(N log N)) | Brute Force (O(N^2))"));

        list.add(new ProblemDefinition(
            "Maximum Subarray",
            "Given an integer array nums, find the contiguous subarray with the largest sum, and return its sum.",
            "[-2,1,-3,4,-1,2,1,-5,4]", "6", "1 <= nums.length <= 10^5", Difficulty.MEDIUM, DsaTopic.ARRAYS,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[-2,1,-3,4,-1,2,1,-5,4]", "6", true, false, "Max subarray sum 6"), new TestCase(null, "[1]", "1", true, false, "Single element"))
        ).withApproaches("DYNAMIC_PROGRAMMING, SLIDING_WINDOW", "Kadane's Algorithm (O(N) Time, O(1) Space) | Divide and Conquer (O(N log N))"));

        list.add(new ProblemDefinition(
            "Rotate Array",
            "Given an integer array nums, rotate the array to the right by k steps, where k is non-negative.",
            "[1,2,3,4,5,6,7]\n3", "[5,6,7,1,2,3,4]", "1 <= nums.length <= 10^5", Difficulty.MEDIUM, DsaTopic.ARRAYS,
            "class Solution {\n    public int[] solve(int[] nums, int k) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int",
            List.of(new TestCase(null, "[1,2,3,4,5,6,7]\n3", "[5,6,7,1,2,3,4]", true, false, "Rotated 3 steps"), new TestCase(null, "[-1,-100,3,99]\n2", "[3,99,-1,-100]", true, false, "Rotated 2 steps"))
        ).withApproaches("TWO_POINTER", "Triple Reverse Algorithm (O(N) Time, O(1) Space) | Cyclic Replacements (O(N))"));

        list.add(new ProblemDefinition(
            "Move Zeroes",
            "Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.",
            "[0,1,0,3,12]", "[1,3,12,0,0]", "1 <= nums.length <= 10^4", Difficulty.EASY, DsaTopic.ARRAYS,
            "class Solution {\n    public int[] solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[0,1,0,3,12]", "[1,3,12,0,0]", true, false, "Moved zeroes to end"), new TestCase(null, "[0]", "[0]", true, false, "Single zero"))
        ).withApproaches("TWO_POINTER", "Two Pointer In-place Swap (O(N) Time, O(1) Space) | Extra Memory Shift"));

        list.add(new ProblemDefinition(
            "Product of Array Except Self",
            "Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].",
            "[1,2,3,4]", "[24,12,8,6]", "2 <= nums.length <= 10^5", Difficulty.MEDIUM, DsaTopic.ARRAYS,
            "class Solution {\n    public int[] solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[1,2,3,4]", "[24,12,8,6]", true, false, "Prefix/suffix product"), new TestCase(null, "[-1,1,0,-3,3]", "[0,0,9,0,0]", true, false, "Contains zero"))
        ).withApproaches("DYNAMIC_PROGRAMMING", "Prefix and Suffix Accumulator (O(N) Time, O(1) Space) | Two-Pass Arrays"));

        list.add(new ProblemDefinition(
            "Find Pivot Index",
            "Given an array of integers nums, calculate the pivot index of this array where left sum equals right sum.",
            "[1,7,3,6,5,6]", "3", "1 <= nums.length <= 10^4", Difficulty.EASY, DsaTopic.ARRAYS,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,7,3,6,5,6]", "3", true, false, "Pivot at 3"), new TestCase(null, "[1,2,3]", "-1", true, false, "No pivot"))
        ).withApproaches("SLIDING_WINDOW", "Total Sum & Prefix Subtraction (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Majority Element",
            "Given an array nums of size n, return the majority element that appears more than ⌊n / 2⌋ times.",
            "[3,2,3]", "3", "1 <= n <= 5 * 10^4", Difficulty.EASY, DsaTopic.ARRAYS,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[3,2,3]", "3", true, false, "Majority 3"), new TestCase(null, "[2,2,1,1,1,2,2]", "2", true, false, "Majority 2"))
        ).withApproaches("HASHMAP", "Boyer-Moore Voting Algorithm (O(N) Time, O(1) Space) | Frequency HashMap (O(N))"));

        list.add(new ProblemDefinition(
            "Running Sum of 1d Array",
            "Given an array nums, return the running sum of nums where runningSum[i] = sum(nums[0]...nums[i]).",
            "[1,2,3,4]", "[1,3,6,10]", "1 <= nums.length <= 1000", Difficulty.EASY, DsaTopic.ARRAYS,
            "class Solution {\n    public int[] solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[1,2,3,4]", "[1,3,6,10]", true, false, "Prefix sums"), new TestCase(null, "[1,1,1,1,1]", "[1,2,3,4,5]", true, false, "All ones"))
        ).withApproaches("DYNAMIC_PROGRAMMING", "In-place Prefix Sum Accumulation (O(N) Time, O(1) Space)"));

        // ==========================================
        // 2. STRINGS (8 Problems: 9 - 16)
        // ==========================================
        list.add(new ProblemDefinition(
            "Valid Anagram",
            "Given two strings s and t, return true if t is an anagram of s, and false otherwise.",
            "anagram\nnagaram", "true", "1 <= s.length, t.length <= 5 * 10^4", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public boolean solve(String s, String t) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "anagram\nnagaram", "true", true, false, "Valid anagram"), new TestCase(null, "rat\ncar", "false", true, false, "Not anagram"))
        ).withApproaches("HASHMAP, ARRAYS, SORTING", "Frequency Array (O(N) Time, O(1) Space) | Sorting (O(N log N)) | HashMap"));

        list.add(new ProblemDefinition(
            "Reverse String",
            "Write a function that reverses a string given as an array of characters.",
            "[\"h\",\"e\",\"l\",\"l\",\"o\"]", "[\"o\",\"l\",\"l\",\"e\",\"h\"]", "1 <= s.length <= 10^5", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public String[] solve(String[] s) {\n        // Write code\n    }\n}",
            "solve", "String[]", "String[]",
            List.of(new TestCase(null, "[\"h\",\"e\",\"l\",\"l\",\"o\"]", "[\"o\",\"l\",\"l\",\"e\",\"h\"]", true, false, "Reversed hello"), new TestCase(null, "[\"H\",\"a\",\"n\",\"n\",\"a\",\"h\"]", "[\"h\",\"a\",\"n\",\"n\",\"a\",\"H\"]", true, false, "Reversed Hannah"))
        ).withApproaches("TWO_POINTER, RECURSION", "Two Pointer In-place Swap (O(N) Time, O(1) Space) | Recursive Call Stack"));

        list.add(new ProblemDefinition(
            "Longest Common Prefix",
            "Write a function to find the longest common prefix string amongst an array of strings.",
            "[\"flower\",\"flow\",\"flight\"]", "fl", "1 <= strs.length <= 200", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public String solve(String[] strs) {\n        // Write code\n    }\n}",
            "solve", "String", "String[]",
            List.of(new TestCase(null, "[\"flower\",\"flow\",\"flight\"]", "fl", true, false, "Prefix fl"), new TestCase(null, "[\"dog\",\"racecar\",\"car\"]", "", true, false, "No prefix"))
        ).withApproaches("BINARY_SEARCH", "Vertical Scanning (O(S) Time) | Horizontal Scanning | Binary Search on Length"));

        list.add(new ProblemDefinition(
            "First Unique Character in a String",
            "Given a string s, find the first non-repeating character in it and return its index. If it does not exist, return -1.",
            "leetcode", "0", "1 <= s.length <= 10^5", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public int solve(String s) {\n        // Write code\n    }\n}",
            "solve", "int", "String",
            List.of(new TestCase(null, "leetcode", "0", true, false, "First unique l"), new TestCase(null, "loveleetcode", "2", true, false, "First unique v"), new TestCase(null, "aabb", "-1", true, false, "No unique"))
        ).withApproaches("HASHMAP, QUEUE", "Two-Pass Frequency Array (O(N) Time, O(1) Space) | LinkedHashMap Stream"));

        list.add(new ProblemDefinition(
            "Valid Palindrome",
            "A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.",
            "A man, a plan, a canal: Panama", "true", "1 <= s.length <= 2 * 10^5", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public boolean solve(String s) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String",
            List.of(new TestCase(null, "A man, a plan, a canal: Panama", "true", true, false, "Valid palindrome"), new TestCase(null, "race a car", "false", true, false, "Not palindrome"))
        ).withApproaches("TWO_POINTER", "Two Pointer In-place Scan (O(N) Time, O(1) Space) | Filter and Reverse"));

        list.add(new ProblemDefinition(
            "Is Subsequence",
            "Given two strings s and t, return true if s is a subsequence of t, or false otherwise.",
            "abc\nahbgdc", "true", "0 <= s.length <= 100", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public boolean solve(String s, String t) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "abc\nahbgdc", "true", true, false, "Is subsequence"), new TestCase(null, "axc\nahbgdc", "false", true, false, "Not subsequence"))
        ).withApproaches("TWO_POINTER, DYNAMIC_PROGRAMMING", "Two Pointer Greedy Scan (O(T) Time, O(1) Space) | DP Matching"));

        list.add(new ProblemDefinition(
            "Group Anagrams",
            "Given an array of strings strs, group the anagrams together. Return total number of anagram groups.",
            "[\"eat\",\"tea\",\"tan\",\"ate\",\"nat\",\"bat\"]", "3", "1 <= strs.length <= 10^4", Difficulty.MEDIUM, DsaTopic.STRINGS,
            "class Solution {\n    public int solve(String[] strs) {\n        // Write code\n    }\n}",
            "solve", "int", "String[]",
            List.of(new TestCase(null, "[\"eat\",\"tea\",\"tan\",\"ate\",\"nat\",\"bat\"]", "3", true, false, "3 distinct groups"), new TestCase(null, "[\"\"]", "1", true, false, "Single empty string"))
        ).withApproaches("HASHMAP, SORTING", "Sorted String Key HashMap (O(N * K log K)) | Character Count Hash (O(N * K))"));

        list.add(new ProblemDefinition(
            "Length of Last Word",
            "Given a string s consisting of words and spaces, return the length of the last word in the string.",
            "Hello World", "5", "1 <= s.length <= 10^4", Difficulty.EASY, DsaTopic.STRINGS,
            "class Solution {\n    public int solve(String s) {\n        // Write code\n    }\n}",
            "solve", "int", "String",
            List.of(new TestCase(null, "Hello World", "5", true, false, "World length 5"), new TestCase(null, "   fly me   to   the moon  ", "4", true, false, "Moon length 4"))
        ).withApproaches("TWO_POINTER", "Reverse Traversal Pointer (O(N) Time, O(1) Space) | Built-in Split"));

        // ==========================================
        // 3. HASHMAP (8 Problems: 17 - 24)
        // ==========================================
        list.add(new ProblemDefinition(
            "Contains Duplicate",
            "Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.",
            "[1,2,3,1]", "true", "1 <= nums.length <= 10^5", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public boolean solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[]",
            List.of(new TestCase(null, "[1,2,3,1]", "true", true, false, "Duplicate 1 found"), new TestCase(null, "[1,2,3,4]", "false", true, false, "All distinct"))
        ).withApproaches("ARRAYS, SORTING", "HashSet (O(N) Time, O(N) Space) | Sorting (O(N log N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Single Number",
            "Given a non-empty array of integers nums, every element appears twice except for one. Find that single one.",
            "[2,2,1]", "1", "1 <= nums.length <= 3 * 10^4", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[2,2,1]", "1", true, false, "Single element is 1"), new TestCase(null, "[4,1,2,1,2]", "4", true, false, "Single element is 4"))
        ).withApproaches("ARRAYS", "Bitwise XOR In-place (O(N) Time, O(1) Space) | HashSet Tracking (O(N) Space)"));

        list.add(new ProblemDefinition(
            "Intersection of Two Arrays",
            "Given two integer arrays nums1 and nums2, return an array of their intersection. Each element in the result must be unique.",
            "[1,2,2,1]\n[2,2]", "[2]", "1 <= nums1.length, nums2.length <= 1000", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public int[] solve(int[] nums1, int[] nums2) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int[]",
            List.of(new TestCase(null, "[1,2,2,1]\n[2,2]", "[2]", true, false, "Intersection [2]"), new TestCase(null, "[4,9,5]\n[9,4,9,8,4]", "[9,4]", true, false, "Intersection [9,4]"))
        ).withApproaches("TWO_POINTER, BINARY_SEARCH", "Two HashSets (O(N + M) Time) | Sort and Two Pointers (O(N log N))"));

        list.add(new ProblemDefinition(
            "Happy Number",
            "Write an algorithm to determine if a number n is happy. A happy number reaches 1 through sum of squares of its digits.",
            "19", "true", "1 <= n <= 2^31 - 1", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public boolean solve(int n) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int",
            List.of(new TestCase(null, "19", "true", true, false, "19 is happy"), new TestCase(null, "2", "false", true, false, "2 cycles infinitely"))
        ).withApproaches("TWO_POINTER, LINKED_LIST", "Floyd's Tortoise and Hare Cycle Detection (O(1) Space) | HashSet Lookup"));

        list.add(new ProblemDefinition(
            "Ransom Note",
            "Given two strings ransomNote and magazine, return true if ransomNote can be constructed by using the letters from magazine.",
            "a\nb", "false", "1 <= ransomNote.length, magazine.length <= 10^5", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public boolean solve(String ransomNote, String magazine) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "a\nb", "false", true, false, "Cannot construct"), new TestCase(null, "aa\naab", "true", true, false, "Can construct"))
        ).withApproaches("STRINGS, ARRAYS", "Frequency Counter Array (O(N) Time, O(1) Space) | HashMap"));

        list.add(new ProblemDefinition(
            "Isomorphic Strings",
            "Given two strings s and t, determine if they are isomorphic.",
            "egg\nadd", "true", "1 <= s.length <= 5 * 10^4", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public boolean solve(String s, String t) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "egg\nadd", "true", true, false, "e->a, g->d"), new TestCase(null, "foo\nbar", "false", true, false, "Not isomorphic"))
        ).withApproaches("STRINGS", "Two Character Arrays Index Mapping (O(N) Time, O(1) Space) | Two HashMaps"));

        list.add(new ProblemDefinition(
            "Word Pattern",
            "Given a pattern and a string s, find if s follows the same pattern.",
            "abba\ndog cat cat dog", "true", "1 <= pattern.length <= 300", Difficulty.EASY, DsaTopic.HASHMAP,
            "class Solution {\n    public boolean solve(String pattern, String s) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "abba\ndog cat cat dog", "true", true, false, "Follows pattern"), new TestCase(null, "abba\ndog cat cat fish", "false", true, false, "Does not follow pattern"))
        ).withApproaches("STRINGS", "Bi-directional HashMaps (O(N) Time) | Single Map with Index Match"));

        list.add(new ProblemDefinition(
            "Subarray Sum Equals K",
            "Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.",
            "[1,1,1]\n2", "2", "1 <= nums.length <= 2 * 10^4", Difficulty.MEDIUM, DsaTopic.HASHMAP,
            "class Solution {\n    public int solve(int[] nums, int k) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[1,1,1]\n2", "2", true, false, "2 subarrays with sum 2"), new TestCase(null, "[1,2,3]\n3", "2", true, false, "[1,2] and [3]"))
        ).withApproaches("ARRAYS, SLIDING_WINDOW", "Prefix Sum Cumulative Map (O(N) Time, O(N) Space) | Brute Force (O(N^2))"));

        // ==========================================
        // 4. STACK (8 Problems: 25 - 32)
        // ==========================================
        list.add(new ProblemDefinition(
            "Valid Parentheses",
            "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.",
            "()[]{}", "true", "1 <= s.length <= 10^4", Difficulty.EASY, DsaTopic.STACK,
            "class Solution {\n    public boolean solve(String s) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String",
            List.of(new TestCase(null, "()[]{}", "true", true, false, "All closed correctly"), new TestCase(null, "(]", "false", true, false, "Mismatched bracket"))
        ).withApproaches("STRINGS", "LIFO Character Stack (O(N) Time, O(N) Space)"));

        list.add(new ProblemDefinition(
            "Daily Temperatures",
            "Given an array of integers temperatures, return an array answer such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature.",
            "[73,74,75,71,69,72,76,73]", "[1,1,4,2,1,1,0,0]", "1 <= temperatures.length <= 10^5", Difficulty.MEDIUM, DsaTopic.STACK,
            "class Solution {\n    public int[] solve(int[] temperatures) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[73,74,75,71,69,72,76,73]", "[1,1,4,2,1,1,0,0]", true, false, "Wait days computed"), new TestCase(null, "[30,40,50,60]", "[1,1,1,0]", true, false, "Strictly increasing"))
        ).withApproaches("ARRAYS", "Monotonic Decreasing Stack (O(N) Time, O(N) Space) | Brute Force Lookahead (O(N^2))"));

        list.add(new ProblemDefinition(
            "Evaluate Reverse Polish Notation",
            "Evaluate the value of an arithmetic expression in Reverse Polish Notation. Valid operators are +, -, *, and /.",
            "[\"2\",\"1\",\"+\",\"3\",\"*\"]", "9", "1 <= tokens.length <= 10^4", Difficulty.MEDIUM, DsaTopic.STACK,
            "class Solution {\n    public int solve(String[] tokens) {\n        // Write code\n    }\n}",
            "solve", "int", "String[]",
            List.of(new TestCase(null, "[\"2\",\"1\",\"+\",\"3\",\"*\"]", "9", true, false, "((2 + 1) * 3) = 9"), new TestCase(null, "[\"4\",\"13\",\"5\",\"/\",\"+\"]", "6", true, false, "(4 + (13 / 5)) = 6"))
        ).withApproaches("RECURSION", "Operand Evaluation Stack (O(N) Time, O(N) Space)"));

        list.add(new ProblemDefinition(
            "Remove All Adjacent Duplicates In String",
            "You are given a string s. A duplicate removal consists of choosing two adjacent and equal letters and removing them.",
            "abbaca", "ca", "1 <= s.length <= 10^5", Difficulty.EASY, DsaTopic.STACK,
            "class Solution {\n    public String solve(String s) {\n        // Write code\n    }\n}",
            "solve", "String", "String",
            List.of(new TestCase(null, "abbaca", "ca", true, false, "bb removed -> aaca -> aa removed -> ca"), new TestCase(null, "azxxzy", "ay", true, false, "Duplicates removed"))
        ).withApproaches("TWO_POINTER, STRINGS", "StringBuilder / Character Stack (O(N) Time, O(N) Space) | Two Pointers In-place"));

        list.add(new ProblemDefinition(
            "Backspace String Compare",
            "Given two strings s and t, return true if they are equal when both are typed into empty text editors. '#' means a backspace character.",
            "ab#c\nad#c", "true", "1 <= s.length, t.length <= 200", Difficulty.EASY, DsaTopic.STACK,
            "class Solution {\n    public boolean solve(String s, String t) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "ab#c\nad#c", "true", true, false, "Both become ac"), new TestCase(null, "a#c\nb", "false", true, false, "c != b"))
        ).withApproaches("TWO_POINTER", "Two Pointer Reverse Scan (O(N) Time, O(1) Space) | Stack Simulation (O(N) Space)"));

        list.add(new ProblemDefinition(
            "Next Greater Element I",
            "The next greater element of some element x in an array is the first greater element that is to the right of x in the same array.",
            "[4,1,2]\n[1,3,4,2]", "[-1,3,-1]", "1 <= nums1.length <= nums2.length <= 1000", Difficulty.EASY, DsaTopic.STACK,
            "class Solution {\n    public int[] solve(int[] nums1, int[] nums2) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int[]",
            List.of(new TestCase(null, "[4,1,2]\n[1,3,4,2]", "[-1,3,-1]", true, false, "Next greater mapped"), new TestCase(null, "[2,4]\n[1,2,3,4]", "[3,-1]", true, false, "Next greater mapped"))
        ).withApproaches("HASHMAP, ARRAYS", "Monotonic Stack + HashMap (O(N + M) Time)"));

        list.add(new ProblemDefinition(
            "Min Stack",
            "Design a stack that supports push, pop, top, and retrieving the minimum element in constant time. Return min element after sequence of operations.",
            "[-2,0,-3]", "-3", "Operations count <= 3 * 10^4", Difficulty.MEDIUM, DsaTopic.STACK,
            "class Solution {\n    public int solve(int[] nums) {\n        // Return min element of stack\n        int min = nums[0];\n        for (int x : nums) if (x < min) min = x;\n        return min;\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[-2,0,-3]", "-3", true, false, "Min is -3"), new TestCase(null, "[5,1,3]", "1", true, false, "Min is 1"))
        ).withApproaches("ARRAYS", "Two Stacks (Values & Auxiliary Mins) (O(1) Retrieval) | Stack of Nodes"));

        list.add(new ProblemDefinition(
            "Asteroid Collision",
            "We are given an array asteroids of integers representing asteroids in a row. Find out the state of the asteroids after all collisions.",
            "[5,10,-5]", "[5,10]", "2 <= asteroids.length <= 10^4", Difficulty.MEDIUM, DsaTopic.STACK,
            "class Solution {\n    public int[] solve(int[] asteroids) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[5,10,-5]", "[5,10]", true, false, "10 destroys -5"), new TestCase(null, "[8,-8]", "[]", true, false, "Both destroy each other"))
        ).withApproaches("ARRAYS", "LIFO Collision Stack Simulation (O(N) Time, O(N) Space)"));

        // ==========================================
        // 5. QUEUE (6 Problems: 33 - 38)
        // ==========================================
        list.add(new ProblemDefinition(
            "Number of Recent Calls",
            "You have a RecentCounter class which counts the number of recent requests within a certain time frame [t - 3000, t].",
            "[1,100,3001,3002]", "3", "1 <= t <= 10^9", Difficulty.EASY, DsaTopic.QUEUE,
            "class Solution {\n    public int solve(int[] pingTimes) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,100,3001,3002]", "3", true, false, "Recent count within 3000ms is 3"), new TestCase(null, "[1,2,3,4]", "4", true, false, "All within window"))
        ).withApproaches("SLIDING_WINDOW", "FIFO Sliding Window Queue (O(1) Amortized Time)"));

        list.add(new ProblemDefinition(
            "First Unique Character in Stream",
            "Given a stream of characters, find the first non-repeating character from the start of the stream.",
            "aabccxb", "x", "1 <= s.length <= 10^4", Difficulty.MEDIUM, DsaTopic.QUEUE,
            "class Solution {\n    public String solve(String stream) {\n        // Write code\n    }\n}",
            "solve", "String", "String",
            List.of(new TestCase(null, "aabccxb", "x", true, false, "First non-repeating is x"), new TestCase(null, "abc", "a", true, false, "First non-repeating is a"))
        ).withApproaches("HASHMAP, STRINGS", "Queue + Frequency Map (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Dota2 Senate",
            "In the world of Dota2, there are two parties: the Radiant and the Dire. Predict which party will declare victory.",
            "RD", "Radiant", "1 <= senate.length <= 10^4", Difficulty.MEDIUM, DsaTopic.QUEUE,
            "class Solution {\n    public String solve(String senate) {\n        // Write code\n    }\n}",
            "solve", "String", "String",
            List.of(new TestCase(null, "RD", "Radiant", true, false, "Radiant wins"), new TestCase(null, "RDD", "Dire", true, false, "Dire wins"))
        ).withApproaches("GREEDY, STRINGS", "Two Queues Index Round-Robin Simulation (O(N) Time)"));

        list.add(new ProblemDefinition(
            "Moving Average from Data Stream",
            "Given a stream of integers and a window size, calculate the moving average of all integers in the sliding window.",
            "[1,10,3,5]\n3", "6.0", "1 <= size <= 1000", Difficulty.EASY, DsaTopic.QUEUE,
            "class Solution {\n    public double solve(int[] nums, int size) {\n        // Write code\n    }\n}",
            "solve", "double", "int[];int",
            List.of(new TestCase(null, "[1,10,3,5]\n3", "6.0", true, false, "Average of last 3 (10+3+5)/3 = 6.0"), new TestCase(null, "[1,2,3]\n2", "2.5", true, false, "(2+3)/2 = 2.5"))
        ).withApproaches("SLIDING_WINDOW, ARRAYS", "Circular Array / Queue Running Sum (O(1) Time, O(size) Space)"));

        list.add(new ProblemDefinition(
            "Implement Queue using Stacks",
            "Implement a first in first out (FIFO) queue using only two stacks.",
            "[1,2,3]", "[1,2,3]", "1 <= ops <= 100", Difficulty.EASY, DsaTopic.QUEUE,
            "class Solution {\n    public int[] solve(int[] values) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[1,2,3]", "[1,2,3]", true, false, "FIFO sequence maintained"), new TestCase(null, "[5,10]", "[5,10]", true, false, "FIFO sequence maintained"))
        ).withApproaches("STACK", "Two Stacks Amortized O(1) Push/Pop In-Out Transfer"));

        list.add(new ProblemDefinition(
            "Design Circular Queue",
            "Design your implementation of the circular queue with fixed size k. Return boolean indicating if elements fit.",
            "[1,2,3,4]\n3", "true", "1 <= k <= 1000", Difficulty.MEDIUM, DsaTopic.QUEUE,
            "class Solution {\n    public boolean solve(int[] vals, int k) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[];int",
            List.of(new TestCase(null, "[1,2,3,4]\n3", "true", true, false, "Circular queue manages buffer"), new TestCase(null, "[1]\n1", "true", true, false, "Single element fits"))
        ).withApproaches("ARRAYS, LINKED_LIST", "Fixed-size Circular Array with Head/Tail Pointers"));

        // ==========================================
        // 6. LINKED_LIST (8 Problems: 39 - 46)
        // ==========================================
        list.add(new ProblemDefinition(
            "Reverse Linked List",
            "Given the head of a singly linked list represented as array, reverse the list, and return the reversed array.",
            "[1,2,3,4,5]", "[5,4,3,2,1]", "0 <= list size <= 5000", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public int[] solve(int[] head) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[1,2,3,4,5]", "[5,4,3,2,1]", true, false, "Reversed 1..5"), new TestCase(null, "[1,2]", "[2,1]", true, false, "Reversed 1..2"))
        ).withApproaches("RECURSION, TWO_POINTER", "3-Pointer Iterative (Prev, Curr, Next) (O(N) Time, O(1) Space) | Recursive Call Stack"));

        list.add(new ProblemDefinition(
            "Merge Two Sorted Lists",
            "You are given the heads of two sorted linked lists list1 and list2. Merge the two lists into one sorted list.",
            "[1,2,4]\n[1,3,4]", "[1,1,2,3,4,4]", "0 <= length <= 50", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public int[] solve(int[] l1, int[] l2) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int[]",
            List.of(new TestCase(null, "[1,2,4]\n[1,3,4]", "[1,1,2,3,4,4]", true, false, "Merged sorted arrays"), new TestCase(null, "[]\n[0]", "[0]", true, false, "Merged with empty list"))
        ).withApproaches("TWO_POINTER, RECURSION", "Iterative Dummy Head Two Pointers (O(N + M) Time, O(1) Space) | Recursive"));

        list.add(new ProblemDefinition(
            "Linked List Cycle",
            "Given head, the head of a linked list, determine if the linked list has a cycle in it.",
            "[3,2,0,-4]\n1", "true", "0 <= nodes <= 10^4", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public boolean solve(int[] nums, int pos) {\n        // pos represents cycle index (-1 for none)\n        return pos != -1;\n    }\n}",
            "solve", "boolean", "int[];int",
            List.of(new TestCase(null, "[3,2,0,-4]\n1", "true", true, false, "Cycle exists at index 1"), new TestCase(null, "[1]\n-1", "false", true, false, "No cycle"))
        ).withApproaches("TWO_POINTER, HASHMAP", "Floyd's Tortoise and Hare Two Pointers (O(N) Time, O(1) Space) | HashSet Lookup"));

        list.add(new ProblemDefinition(
            "Middle of the Linked List",
            "Given the head of a singly linked list, return the middle node of the linked list.",
            "[1,2,3,4,5]", "3", "1 <= nodes <= 100", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public int solve(int[] head) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,2,3,4,5]", "3", true, false, "Middle is 3"), new TestCase(null, "[1,2,3,4,5,6]", "4", true, false, "Second middle is 4"))
        ).withApproaches("TWO_POINTER", "Fast and Slow Two Pointers (O(N) Time, O(1) Space) | Array Length Calculation"));

        list.add(new ProblemDefinition(
            "Remove Nth Node From End of List",
            "Given the head of a linked list, remove the nth node from the end of the list and return its head.",
            "[1,2,3,4,5]\n2", "[1,2,3,5]", "1 <= nodes <= 30", Difficulty.MEDIUM, DsaTopic.LINKED_LIST,
            "class Solution {\n    public int[] solve(int[] head, int n) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int",
            List.of(new TestCase(null, "[1,2,3,4,5]\n2", "[1,2,3,5]", true, false, "4 removed"), new TestCase(null, "[1]\n1", "[]", true, false, "Single node removed"))
        ).withApproaches("TWO_POINTER", "Fast/Slow Two Pointers with N-Gap (O(N) Time, O(1) Space) | Two-Pass Count"));

        list.add(new ProblemDefinition(
            "Palindrome Linked List",
            "Given the head of a singly linked list, return true if it is a palindrome or false otherwise.",
            "[1,2,2,1]", "true", "1 <= nodes <= 10^5", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public boolean solve(int[] head) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[]",
            List.of(new TestCase(null, "[1,2,2,1]", "true", true, false, "Palindrome"), new TestCase(null, "[1,2]", "false", true, false, "Not palindrome"))
        ).withApproaches("TWO_POINTER, STACK", "Find Mid, Reverse 2nd Half, Two Pointers (O(N) Time, O(1) Space) | Stack"));

        list.add(new ProblemDefinition(
            "Remove Duplicates from Sorted List",
            "Given the head of a sorted linked list, delete all duplicates such that each element appears only once.",
            "[1,1,2]", "[1,2]", "0 <= nodes <= 300", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public int[] solve(int[] head) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[1,1,2]", "[1,2]", true, false, "Duplicates removed"), new TestCase(null, "[1,1,2,3,3]", "[1,2,3]", true, false, "Duplicates removed"))
        ).withApproaches("TWO_POINTER", "Single Pass Pointer Linkage (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Intersection of Two Linked Lists",
            "Given the heads of two singly linked-lists headA and headB, return the value of the intersecting node.",
            "[4,1,8,4,5]\n[5,6,1,8,4,5]", "8", "1 <= nodes <= 3 * 10^4", Difficulty.EASY, DsaTopic.LINKED_LIST,
            "class Solution {\n    public int solve(int[] a, int[] b) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int[]",
            List.of(new TestCase(null, "[4,1,8,4,5]\n[5,6,1,8,4,5]", "8", true, false, "Intersect at 8"), new TestCase(null, "[2,6,4]\n[1,5]", "-1", true, false, "No intersection"))
        ).withApproaches("TWO_POINTER, HASHMAP", "Two Pointer Traversal Switching (O(N + M) Time, O(1) Space) | HashSet Lookup"));

        // ==========================================
        // 7. RECURSION (8 Problems: 47 - 54)
        // ==========================================
        list.add(new ProblemDefinition(
            "Fibonacci Number",
            "The Fibonacci numbers form a sequence such that each number is the sum of the two preceding ones.",
            "4", "3", "0 <= n <= 30", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public int solve(int n) {\n        // Write code\n    }\n}",
            "solve", "int", "int",
            List.of(new TestCase(null, "4", "3", true, false, "F(4) = 3"), new TestCase(null, "2", "1", true, false, "F(2) = 1"))
        ).withApproaches("DYNAMIC_PROGRAMMING, MATH", "Bottom-Up DP / Memoization (O(N) Time, O(1) Space) | Matrix Exponentiation (O(log N)) | Recursion (O(2^N))"));

        list.add(new ProblemDefinition(
            "Climbing Stairs",
            "You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps.",
            "3", "3", "1 <= n <= 45", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public int solve(int n) {\n        // Write code\n    }\n}",
            "solve", "int", "int",
            List.of(new TestCase(null, "3", "3", true, false, "3 distinct ways"), new TestCase(null, "2", "2", true, false, "2 ways"))
        ).withApproaches("DYNAMIC_PROGRAMMING", "Fibonacci DP (O(N) Time, O(1) Space) | Recursion with Memoization"));

        list.add(new ProblemDefinition(
            "Power of Two (Recursive)",
            "Given an integer n, return true if it is a power of two using recursive division.",
            "16", "true", "-2^31 <= n <= 2^31 - 1", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public boolean solve(int n) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int",
            List.of(new TestCase(null, "16", "true", true, false, "16 is 2^4"), new TestCase(null, "3", "false", true, false, "3 is not power of 2"))
        ).withApproaches("BIT_MANIPULATION, MATH", "Bitwise `(n & (n - 1)) == 0` (O(1) Time) | Recursive Halving (O(log N))"));

        list.add(new ProblemDefinition(
            "Power of Three",
            "Given an integer n, return true if it is a power of three.",
            "27", "true", "-2^31 <= n <= 2^31 - 1", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public boolean solve(int n) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int",
            List.of(new TestCase(null, "27", "true", true, false, "27 is 3^3"), new TestCase(null, "0", "false", true, false, "0 is not power of 3"))
        ).withApproaches("MATH", "Integer Max Divisibility Check (O(1) Time) | Recursive Division by 3"));

        list.add(new ProblemDefinition(
            "Factorial of a Number",
            "Calculate the factorial of a non-negative integer n using recursion.",
            "5", "120", "0 <= n <= 12", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public int solve(int n) {\n        // Write code\n    }\n}",
            "solve", "int", "int",
            List.of(new TestCase(null, "5", "120", true, false, "5! = 120"), new TestCase(null, "0", "1", true, false, "0! = 1"))
        ).withApproaches("DYNAMIC_PROGRAMMING", "Linear Recursion Call Stack (O(N) Time) | Iterative Loop"));

        list.add(new ProblemDefinition(
            "Sum of Digits of a Number",
            "Given an integer n, compute the sum of its digits recursively until a single sum is obtained.",
            "1234", "10", "1 <= n <= 10^9", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public int solve(int n) {\n        // Write code\n    }\n}",
            "solve", "int", "int",
            List.of(new TestCase(null, "1234", "10", true, false, "1+2+3+4 = 10"), new TestCase(null, "9", "9", true, false, "Single digit 9"))
        ).withApproaches("MATH", "Tail Recursive Modulo (O(log_10 N) Time) | Iterative While Loop"));

        list.add(new ProblemDefinition(
            "GCD of Two Numbers",
            "Given two integers a and b, find their Greatest Common Divisor (GCD) using the Euclidean algorithm.",
            "48\n18", "6", "1 <= a, b <= 10^9", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public int solve(int a, int b) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int",
            List.of(new TestCase(null, "48\n18", "6", true, false, "GCD of 48 and 18 is 6"), new TestCase(null, "101\n10", "1", true, false, "Co-prime numbers"))
        ).withApproaches("MATH", "Euclidean Recursive Algorithm `gcd(b, a % b)` (O(log(min(a, b)))) | Iterative Modulo"));

        list.add(new ProblemDefinition(
            "Recursive Array Sum",
            "Given an array of integers, calculate the sum of its elements using divide and conquer recursion.",
            "[1,2,3,4,5]", "15", "0 <= length <= 1000", Difficulty.EASY, DsaTopic.RECURSION,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,2,3,4,5]", "15", true, false, "Sum is 15"), new TestCase(null, "[10,-5,20]", "25", true, false, "Sum with negatives"))
        ).withApproaches("ARRAYS", "Divide and Conquer (O(N) Time, O(log N) Space) | Linear Recursion"));

        // ==========================================
        // 8. BINARY_SEARCH (8 Problems: 55 - 62)
        // ==========================================
        list.add(new ProblemDefinition(
            "Binary Search",
            "Given an array of integers nums which is sorted in ascending order, and an integer target, return its index or -1.",
            "[-1,0,3,5,9,12]\n9", "4", "1 <= nums.length <= 10^4", Difficulty.EASY, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int[] nums, int target) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[-1,0,3,5,9,12]\n9", "4", true, false, "Target 9 at index 4"), new TestCase(null, "[-1,0,3,5,9,12]\n2", "-1", true, false, "Target 2 not found"))
        ).withApproaches("RECURSION, TWO_POINTER", "Iterative Two Pointers (O(log N) Time, O(1) Space) | Recursive Binary Search"));

        list.add(new ProblemDefinition(
            "Search Insert Position",
            "Given a sorted array of distinct integers and a target value, return the index if target is found or where it would be inserted.",
            "[1,3,5,6]\n5", "2", "1 <= nums.length <= 10^4", Difficulty.EASY, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int[] nums, int target) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[1,3,5,6]\n5", "2", true, false, "Found at index 2"), new TestCase(null, "[1,3,5,6]\n2", "1", true, false, "Insert at index 1"))
        ).withApproaches("ARRAYS", "Lower Bound Binary Search (O(log N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "First Bad Version",
            "You are a product manager and currently leading a team to develop a new product. Find the first bad version using binary search.",
            "5\n4", "4", "1 <= bad <= n <= 2^31 - 1", Difficulty.EASY, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int n, int bad) {\n        // Binary search for first bad version\n        return bad;\n    }\n}",
            "solve", "int", "int;int",
            List.of(new TestCase(null, "5\n4", "4", true, false, "First bad version is 4"), new TestCase(null, "1\n1", "1", true, false, "First is 1"))
        ).withApproaches("ARRAYS", "Left-leaning Binary Search Boundary (O(log N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Sqrt(x)",
            "Given a non-negative integer x, return the square root of x rounded down to the nearest integer.",
            "8", "2", "0 <= x <= 2^31 - 1", Difficulty.EASY, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int x) {\n        // Write code\n    }\n}",
            "solve", "int", "int",
            List.of(new TestCase(null, "8", "2", true, false, "sqrt(8) = 2"), new TestCase(null, "4", "2", true, false, "sqrt(4) = 2"))
        ).withApproaches("MATH", "Binary Search on Search Space [1..x] (O(log x)) | Newton-Raphson Method"));

        list.add(new ProblemDefinition(
            "Peak Index in a Mountain Array",
            "An array arr is a mountain if arr[0] < arr[1] < ... < arr[i] > ... > arr[n-1]. Return the index of peak.",
            "[0,1,0]", "1", "3 <= arr.length <= 10^5", Difficulty.MEDIUM, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int[] arr) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[0,1,0]", "1", true, false, "Peak is at index 1"), new TestCase(null, "[0,2,1,0]", "1", true, false, "Peak at index 1"))
        ).withApproaches("ARRAYS, TWO_POINTER", "Binary Search for Inflection Peak (O(log N) Time) | Linear Scan"));

        list.add(new ProblemDefinition(
            "Search a 2D Matrix",
            "You are given an m x n integer matrix with sorted rows. Return true if target is in matrix.",
            "[1,3,5,7]\n3", "true", "1 <= m, n <= 100", Difficulty.MEDIUM, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public boolean solve(int[] matrixRow, int target) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[];int",
            List.of(new TestCase(null, "[1,3,5,7]\n3", "true", true, false, "Target 3 in row"), new TestCase(null, "[1,3,5,7]\n13", "false", true, false, "Target 13 not in row"))
        ).withApproaches("ARRAYS", "Virtual 1D Array Binary Search (O(log(M * N)) Time) | Top-Right Matrix Search"));

        list.add(new ProblemDefinition(
            "Find Minimum in Rotated Sorted Array",
            "Given the sorted rotated array nums of unique elements, return the minimum element of this array.",
            "[3,4,5,1,2]", "1", "1 <= nums.length <= 5000", Difficulty.MEDIUM, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[3,4,5,1,2]", "1", true, false, "Min element is 1"), new TestCase(null, "[4,5,6,7,0,1,2]", "0", true, false, "Min element is 0"))
        ).withApproaches("ARRAYS", "Binary Search on Unsorted Half (O(log N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Count Negative Numbers in a Sorted Matrix",
            "Given a m x n matrix grid which is sorted in non-increasing order both row-wise and column-wise, return the number of negative numbers.",
            "[4,3,2,-1]", "1", "1 <= m, n <= 100", Difficulty.EASY, DsaTopic.BINARY_SEARCH,
            "class Solution {\n    public int solve(int[] row) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[4,3,2,-1]", "1", true, false, "1 negative element"), new TestCase(null, "[-1,-2,-3]", "3", true, false, "3 negative elements"))
        ).withApproaches("TWO_POINTER, ARRAYS", "Binary Search on Row (O(M log N)) | Staircase Two Pointers (O(M + N))"));

        // ==========================================
        // 9. SLIDING_WINDOW (7 Problems: 63 - 69)
        // ==========================================
        list.add(new ProblemDefinition(
            "Maximum Sum Subarray of Size K",
            "Given an array of integers and a number k, find the maximum sum of a subarray of size k.",
            "[2,1,5,1,3,2]\n3", "9", "1 <= k <= nums.length <= 10^5", Difficulty.EASY, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public int solve(int[] nums, int k) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[2,1,5,1,3,2]\n3", "9", true, false, "Max sum subarray [5,1,3] = 9"), new TestCase(null, "[2,3,4,1,5]\n2", "7", true, false, "Max sum [3,4] = 7"))
        ).withApproaches("ARRAYS", "Fixed-Size Sliding Window (O(N) Time, O(1) Space) | Brute Force (O(N * K))"));

        list.add(new ProblemDefinition(
            "Minimum Size Subarray Sum",
            "Given an array of positive integers nums and a positive integer target, return the minimal length of a subarray whose sum >= target.",
            "7\n[2,3,1,2,4,3]", "2", "1 <= target <= 10^9", Difficulty.MEDIUM, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public int solve(int target, int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int[]",
            List.of(new TestCase(null, "7\n[2,3,1,2,4,3]", "2", true, false, "[4,3] gives minimal length 2"), new TestCase(null, "4\n[1,4,4]", "1", true, false, "Single element [4]"))
        ).withApproaches("TWO_POINTER, BINARY_SEARCH", "Dynamic Variable-Size Sliding Window (O(N) Time) | Prefix Sums + Binary Search (O(N log N))"));

        list.add(new ProblemDefinition(
            "Longest Substring Without Repeating Characters",
            "Given a string s, find the length of the longest substring without repeating characters.",
            "abcabcbb", "3", "0 <= s.length <= 5 * 10^4", Difficulty.MEDIUM, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public int solve(String s) {\n        // Write code\n    }\n}",
            "solve", "int", "String",
            List.of(new TestCase(null, "abcabcbb", "3", true, false, "abc length 3"), new TestCase(null, "bbbbb", "1", true, false, "b length 1"))
        ).withApproaches("HASHMAP, TWO_POINTER", "Sliding Window + Character Last Seen Index (O(N) Time, O(1) Space) | Set Scan"));

        list.add(new ProblemDefinition(
            "Max Consecutive Ones III",
            "Given a binary array nums and an integer k, return the maximum number of consecutive 1's if you can flip at most k 0's.",
            "[1,1,1,0,0,0,1,1,1,1,0]\n2", "6", "1 <= nums.length <= 10^5", Difficulty.MEDIUM, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public int solve(int[] nums, int k) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[1,1,1,0,0,0,1,1,1,1,0]\n2", "6", true, false, "Flipping 2 zeroes gives length 6"), new TestCase(null, "[0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1]\n3", "10", true, false, "Length 10"))
        ).withApproaches("TWO_POINTER, ARRAYS", "Variable-Size Sliding Window with Zero Count (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Permutation in String",
            "Given two strings s1 and s2, return true if s2 contains a permutation of s1, or false otherwise.",
            "ab\neidbaooo", "true", "1 <= s1.length, s2.length <= 10^4", Difficulty.MEDIUM, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public boolean solve(String s1, String s2) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String;String",
            List.of(new TestCase(null, "ab\neidbaooo", "true", true, false, "ba is permutation of ab"), new TestCase(null, "ab\neidboaoo", "false", true, false, "No permutation found"))
        ).withApproaches("HASHMAP, STRINGS", "Fixed-Size 26-Char Frequency Sliding Window (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Fruit Into Baskets",
            "You are visiting a farm that has a single row of fruit trees. Pick the maximum number of fruits using at most 2 baskets.",
            "[1,2,1]", "3", "1 <= fruits.length <= 10^5", Difficulty.MEDIUM, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public int solve(int[] fruits) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,2,1]", "3", true, false, "All 3 fruits picked"), new TestCase(null, "[0,1,2,2]", "3", true, false, "[1,2,2] picked"))
        ).withApproaches("HASHMAP, TWO_POINTER", "Sliding Window with at most 2 Distinct Keys (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Substrings of Size Three with Distinct Characters",
            "A string is good if there are no repeated characters. Return the number of good substrings of length three.",
            "xyzzaz", "1", "1 <= s.length <= 100", Difficulty.EASY, DsaTopic.SLIDING_WINDOW,
            "class Solution {\n    public int solve(String s) {\n        // Write code\n    }\n}",
            "solve", "int", "String",
            List.of(new TestCase(null, "xyzzaz", "1", true, false, "xyz is the only good substring"), new TestCase(null, "aababcabc", "4", true, false, "4 good substrings"))
        ).withApproaches("STRINGS", "Fixed Window of Size 3 Character Uniqueness Check (O(N) Time)"));

        // ==========================================
        // 10. TWO_POINTER (8 Problems: 70 - 77)
        // ==========================================
        list.add(new ProblemDefinition(
            "Valid Palindrome II",
            "Given a string s, return true if the s can be palindrome after deleting at most one character from it.",
            "aba", "true", "1 <= s.length <= 10^5", Difficulty.EASY, DsaTopic.TWO_POINTER,
            "class Solution {\n    public boolean solve(String s) {\n        // Write code\n    }\n}",
            "solve", "boolean", "String",
            List.of(new TestCase(null, "aba", "true", true, false, "Already palindrome"), new TestCase(null, "abca", "true", true, false, "Delete c -> aba"))
        ).withApproaches("STRINGS", "Greedy Two Pointers with 1-Mismatch Branching (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "3Sum",
            "Given an integer array nums, return the count of all the triplets [nums[i], nums[j], nums[k]] such that they sum to 0.",
            "[-1,0,1,2,-1,-4]", "2", "3 <= nums.length <= 3000", Difficulty.MEDIUM, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[-1,0,1,2,-1,-4]", "2", true, false, "Two unique triplets found"), new TestCase(null, "[0,1,1]", "0", true, false, "No triplets sum to 0"))
        ).withApproaches("ARRAYS, SORTING", "Sort + Two Pointers on Remaining Pair (O(N^2) Time, O(1) Space) | HashSet Lookup"));

        list.add(new ProblemDefinition(
            "Container With Most Water",
            "You are given an integer array height of length n. Find two lines that together with the x-axis form a container that contains the most water.",
            "[1,8,6,2,5,4,8,3,7]", "49", "2 <= n <= 10^5", Difficulty.MEDIUM, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int solve(int[] height) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,8,6,2,5,4,8,3,7]", "49", true, false, "Max area between index 1 and 8 = 49"), new TestCase(null, "[1,1]", "1", true, false, "Max area 1"))
        ).withApproaches("ARRAYS", "Two Pointers Greedy Inward Shrink (O(N) Time, O(1) Space) | Brute Force (O(N^2))"));

        list.add(new ProblemDefinition(
            "Two Sum II - Input Array Is Sorted",
            "Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers that add up to target.",
            "[2,7,11,15]\n9", "[1,2]", "2 <= numbers.length <= 3 * 10^4", Difficulty.MEDIUM, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int[] solve(int[] numbers, int target) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int",
            List.of(new TestCase(null, "[2,7,11,15]\n9", "[1,2]", true, false, "1-indexed [1,2]"), new TestCase(null, "[2,3,4]\n6", "[1,3]", true, false, "1-indexed [1,3]"))
        ).withApproaches("BINARY_SEARCH, ARRAYS", "Two Pointers Left/Right Convergence (O(N) Time, O(1) Space) | Binary Search (O(N log N))"));

        list.add(new ProblemDefinition(
            "Remove Duplicates from Sorted Array",
            "Given an integer array nums sorted in non-decreasing order, remove duplicates in-place such that each unique element appears only once. Return length.",
            "[1,1,2]", "2", "1 <= nums.length <= 3 * 10^4", Difficulty.EASY, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,1,2]", "2", true, false, "2 unique elements [1,2]"), new TestCase(null, "[0,0,1,1,1,2,2,3,3,4]", "5", true, false, "5 unique elements"))
        ).withApproaches("ARRAYS", "Slow/Fast Two Pointers In-place Overwrite (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Sort Colors (Dutch National Flag)",
            "Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color are adjacent.",
            "[2,0,2,1,1,0]", "[0,0,1,1,2,2]", "1 <= n <= 300", Difficulty.MEDIUM, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int[] solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[2,0,2,1,1,0]", "[0,0,1,1,2,2]", true, false, "Sorted in-place"), new TestCase(null, "[2,0,1]", "[0,1,2]", true, false, "Sorted in-place"))
        ).withApproaches("SORTING, ARRAYS", "Dutch National Flag 3-Way Partition (O(N) Time, O(1) Space) | Counting Sort"));

        list.add(new ProblemDefinition(
            "Squares of a Sorted Array",
            "Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.",
            "[-4,-1,0,3,10]", "[0,1,9,16,100]", "1 <= nums.length <= 10^4", Difficulty.EASY, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int[] solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[-4,-1,0,3,10]", "[0,1,9,16,100]", true, false, "Squares sorted"), new TestCase(null, "[-7,-3,2,3,11]", "[4,9,9,49,121]", true, false, "Squares sorted"))
        ).withApproaches("SORTING, ARRAYS", "Two Pointers from Extremes (O(N) Time, O(N) Space) | Square & Sort (O(N log N))"));

        list.add(new ProblemDefinition(
            "Trapping Rain Water",
            "Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.",
            "[0,1,0,2,1,0,1,3,2,1,2,1]", "6", "n == height.length <= 2 * 10^4", Difficulty.HARD, DsaTopic.TWO_POINTER,
            "class Solution {\n    public int solve(int[] height) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[0,1,0,2,1,0,1,3,2,1,2,1]", "6", true, false, "Trapped water 6"), new TestCase(null, "[4,2,0,3,2,5]", "9", true, false, "Trapped water 9"))
        ).withApproaches("STACK, DYNAMIC_PROGRAMMING", "Two Pointers Left/Right Max (O(N) Time, O(1) Space) | Monotonic Stack | Prefix/Suffix Max DP"));

        // ==========================================
        // 11. SORTING (8 Problems: 78 - 85)
        // ==========================================
        list.add(new ProblemDefinition(
            "Merge Sorted Array",
            "You are given two integer arrays nums1 and nums2, sorted in non-decreasing order. Merge nums2 into nums1 as one sorted array.",
            "[1,2,3,0,0,0]\n3\n[2,5,6]\n3", "[1,2,2,3,5,6]", "0 <= m, n <= 200", Difficulty.EASY, DsaTopic.SORTING,
            "class Solution {\n    public int[] solve(int[] nums1, int m, int[] nums2, int n) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int;int[];int",
            List.of(new TestCase(null, "[1,2,3,0,0,0]\n3\n[2,5,6]\n3", "[1,2,2,3,5,6]", true, false, "Merged in-place from back"), new TestCase(null, "[1]\n1\n[]\n0", "[1]", true, false, "Single element"))
        ).withApproaches("TWO_POINTER, ARRAYS", "Three Pointers from Back (O(M + N) Time, O(1) Space) | Append & Sort"));

        list.add(new ProblemDefinition(
            "Intersection of Two Arrays II",
            "Given two integer arrays nums1 and nums2, return an array of their intersection with counts preserved.",
            "[1,2,2,1]\n[2,2]", "[2,2]", "1 <= nums1.length, nums2.length <= 1000", Difficulty.EASY, DsaTopic.SORTING,
            "class Solution {\n    public int[] solve(int[] nums1, int[] nums2) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int[]",
            List.of(new TestCase(null, "[1,2,2,1]\n[2,2]", "[2,2]", true, false, "Preserves count [2,2]"), new TestCase(null, "[4,9,5]\n[9,4,9,8,4]", "[4,9]", true, false, "[4,9]"))
        ).withApproaches("HASHMAP, TWO_POINTER", "Frequency HashMap (O(N + M) Time) | Sort Both + Two Pointers (O(N log N))"));

        list.add(new ProblemDefinition(
            "Kth Largest Element in an Array",
            "Given an integer array nums and an integer k, return the kth largest element in the array.",
            "[3,2,1,5,6,4]\n2", "5", "1 <= k <= nums.length <= 10^5", Difficulty.MEDIUM, DsaTopic.SORTING,
            "class Solution {\n    public int solve(int[] nums, int k) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[3,2,1,5,6,4]\n2", "5", true, false, "2nd largest is 5"), new TestCase(null, "[3,2,3,1,2,4,5,5,6]\n4", "4", true, false, "4th largest is 4"))
        ).withApproaches("ARRAYS", "QuickSelect Partitioning (O(N) Average Time) | Min-Heap PriorityQueue (O(N log K)) | Full Sort"));

        list.add(new ProblemDefinition(
            "Sort Characters By Frequency",
            "Given a string s, sort it in decreasing order based on the frequency of the characters.",
            "tree", "eert", "1 <= s.length <= 5 * 10^5", Difficulty.MEDIUM, DsaTopic.SORTING,
            "class Solution {\n    public String solve(String s) {\n        // Write code\n    }\n}",
            "solve", "String", "String",
            List.of(new TestCase(null, "tree", "eert", true, false, "e appears twice"), new TestCase(null, "cccaaa", "aaaccc", true, false, "Equal frequency"))
        ).withApproaches("HASHMAP, STRINGS", "Bucket Sort (O(N) Time, O(N) Space) | Max-Heap Frequency Queue (O(N log K))"));

        list.add(new ProblemDefinition(
            "Top K Frequent Elements",
            "Given an integer array nums and an integer k, return the k most frequent elements.",
            "[1,1,1,2,2,3]\n2", "[1,2]", "1 <= nums.length <= 10^5", Difficulty.MEDIUM, DsaTopic.SORTING,
            "class Solution {\n    public int[] solve(int[] nums, int k) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[];int",
            List.of(new TestCase(null, "[1,1,1,2,2,3]\n2", "[1,2]", true, false, "Top 2 frequent [1,2]"), new TestCase(null, "[1]\n1", "[1]", true, false, "Single element"))
        ).withApproaches("HASHMAP, ARRAYS", "Bucket Sort by Frequency (O(N) Time, O(N) Space) | Min-Heap PriorityQueue (O(N log K))"));

        list.add(new ProblemDefinition(
            "Merge Intervals",
            "Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals. Return merged count.",
            "[1,3,2,6,8,10,15,18]", "3", "1 <= intervals.length <= 10^4", Difficulty.MEDIUM, DsaTopic.SORTING,
            "class Solution {\n    public int solve(int[] flatIntervals) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,3,2,6,8,10,15,18]", "3", true, false, "Merged into 3 intervals: [1,6],[8,10],[15,18]"), new TestCase(null, "[1,4,4,5]", "1", true, false, "Merged into [1,5]"))
        ).withApproaches("ARRAYS", "Sort by Start Time + Linear Merge (O(N log N) Time, O(N) Space)"));

        list.add(new ProblemDefinition(
            "Non-overlapping Intervals",
            "Given an array of intervals intervals where intervals[i] = [starti, endi], return min removals for non-overlapping.",
            "[1,2,2,3,3,4,1,3]", "1", "1 <= intervals.length <= 10^5", Difficulty.MEDIUM, DsaTopic.SORTING,
            "class Solution {\n    public int solve(int[] flatIntervals) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,2,2,3,3,4,1,3]", "1", true, false, "Remove [1,3]"), new TestCase(null, "[1,2,1,2,1,2]", "2", true, false, "Remove 2 duplicates"))
        ).withApproaches("ARRAYS", "Greedy Sort by End Time (O(N log N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Largest Number",
            "Given a list of non-negative integers nums, arrange them such that they form the largest number and return it.",
            "[10,2]", "210", "1 <= nums.length <= 100", Difficulty.MEDIUM, DsaTopic.SORTING,
            "class Solution {\n    public String solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "String", "int[]",
            List.of(new TestCase(null, "[10,2]", "210", true, false, "210 > 102"), new TestCase(null, "[3,30,34,5,9]", "9534330", true, false, "Largest combination"))
        ).withApproaches("STRINGS", "Custom String Comparator `(b + a).compareTo(a + b)` (O(N log N) Time)"));

        // ==========================================
        // 12. TREES (8 Problems: 86 - 93)
        // ==========================================
        list.add(new ProblemDefinition(
            "Maximum Depth of Binary Tree",
            "Given the root of a binary tree as array representation, return its maximum depth.",
            "[3,9,20,0,0,15,7]", "3", "0 <= nodes <= 10^4", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public int solve(int[] tree) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[3,9,20,0,0,15,7]", "3", true, false, "Max depth is 3"), new TestCase(null, "[1,0,2]", "2", true, false, "Max depth is 2"))
        ).withApproaches("RECURSION, QUEUE", "Recursive DFS Depth (O(N) Time, O(H) Space) | Iterative BFS Level Order (O(N) Space)"));

        list.add(new ProblemDefinition(
            "Invert Binary Tree",
            "Given the root of a binary tree represented as array, invert the tree, and return its array representation.",
            "[4,2,7,1,3,6,9]", "[4,7,2,9,6,3,1]", "0 <= nodes <= 100", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public int[] solve(int[] tree) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[4,2,7,1,3,6,9]", "[4,7,2,9,6,3,1]", true, false, "Inverted tree"), new TestCase(null, "[2,1,3]", "[2,3,1]", true, false, "Inverted 2-1-3"))
        ).withApproaches("RECURSION, QUEUE", "Recursive DFS Child Swap (O(N) Time) | Iterative BFS Queue (O(N) Space)"));

        list.add(new ProblemDefinition(
            "Same Tree",
            "Given the roots of two binary trees p and q, write a function to check if they are the same or not.",
            "[1,2,3]\n[1,2,3]", "true", "0 <= nodes <= 100", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public boolean solve(int[] p, int[] q) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[];int[]",
            List.of(new TestCase(null, "[1,2,3]\n[1,2,3]", "true", true, false, "Identical trees"), new TestCase(null, "[1,2]\n[1,0,2]", "false", true, false, "Different structures"))
        ).withApproaches("RECURSION, QUEUE", "Recursive Dual DFS Traversal (O(N) Time) | Iterative Queue Pair Traversal"));

        list.add(new ProblemDefinition(
            "Symmetric Tree",
            "Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).",
            "[1,2,2,3,4,4,3]", "true", "1 <= nodes <= 1000", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public boolean solve(int[] tree) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[]",
            List.of(new TestCase(null, "[1,2,2,3,4,4,3]", "true", true, false, "Symmetric tree"), new TestCase(null, "[1,2,2,0,3,0,3]", "false", true, false, "Not symmetric"))
        ).withApproaches("RECURSION, QUEUE", "Recursive Dual DFS Mirror (O(N) Time, O(H) Space) | Iterative Queue"));

        list.add(new ProblemDefinition(
            "Path Sum",
            "Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path summing to targetSum.",
            "[5,4,8,11,0,13,4,7,2,0,0,0,1]\n22", "true", "0 <= nodes <= 5000", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public boolean solve(int[] tree, int targetSum) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[];int",
            List.of(new TestCase(null, "[5,4,8,11,0,13,4,7,2,0,0,0,1]\n22", "true", true, false, "5+4+11+2 = 22"), new TestCase(null, "[1,2,3]\n5", "false", true, false, "No path equals 5"))
        ).withApproaches("RECURSION, STACK", "DFS Preorder Subtraction (O(N) Time, O(H) Space) | Iterative DFS Stack"));

        list.add(new ProblemDefinition(
            "Binary Tree Inorder Traversal",
            "Given the root of a binary tree, return the inorder traversal of its nodes' values.",
            "[1,0,2,3]", "[1,3,2]", "0 <= nodes <= 100", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public int[] solve(int[] tree) {\n        // Write code\n    }\n}",
            "solve", "int[]", "int[]",
            List.of(new TestCase(null, "[1,0,2,3]", "[1,3,2]", true, false, "Inorder is 1,3,2"), new TestCase(null, "[]", "[]", true, false, "Empty tree"))
        ).withApproaches("STACK, RECURSION", "Morris Traversal (O(N) Time, O(1) Space) | Iterative Stack (O(N) Space) | Recursive DFS"));

        list.add(new ProblemDefinition(
            "Subtree of Another Tree",
            "Given the roots of two binary trees root and subRoot, return true if there is a subtree of root with the same structure and node values.",
            "[3,4,5,1,2]\n[4,1,2]", "true", "1 <= nodes <= 2000", Difficulty.EASY, DsaTopic.TREES,
            "class Solution {\n    public boolean solve(int[] root, int[] subRoot) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[];int[]",
            List.of(new TestCase(null, "[3,4,5,1,2]\n[4,1,2]", "true", true, false, "Subtree matched"), new TestCase(null, "[3,4,5,1,2,0,0,0,0,0,0]\n[4,1,2]", "true", true, false, "Subtree matched"))
        ).withApproaches("RECURSION, STRINGS", "DFS with IsSameTree check (O(M * N) Time) | Tree Serialization + KMP"));

        list.add(new ProblemDefinition(
            "Lowest Common Ancestor of a BST",
            "Given a binary search tree (BST), find the lowest common ancestor (LCA) node of two given nodes p and q.",
            "[6,2,8,0,4,7,9]\n2\n8", "6", "2 <= nodes <= 10^5", Difficulty.MEDIUM, DsaTopic.TREES,
            "class Solution {\n    public int solve(int[] bst, int p, int q) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int;int",
            List.of(new TestCase(null, "[6,2,8,0,4,7,9]\n2\n8", "6", true, false, "LCA of 2 and 8 is 6"), new TestCase(null, "[6,2,8,0,4,7,9]\n2\n4", "2", true, false, "LCA of 2 and 4 is 2"))
        ).withApproaches("BINARY_SEARCH, RECURSION", "BST Node Value Splitting (O(H) Time, O(1) Space) | Recursive BST Walk"));

        // ==========================================
        // 13. GRAPHS (8 Problems: 94 - 101)
        // ==========================================
        list.add(new ProblemDefinition(
            "Find Center of Star Graph",
            "There is an undirected star graph consisting of n nodes. Return the center node of the star graph.",
            "[1,2,2,3,4,2]", "2", "3 <= n <= 10^5", Difficulty.EASY, DsaTopic.GRAPHS,
            "class Solution {\n    public int solve(int[] edges) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,2,2,3,4,2]", "2", true, false, "Center is node 2"), new TestCase(null, "[1,2,5,1,1,3,1,4]", "1", true, false, "Center is node 1"))
        ).withApproaches("ARRAYS", "Common Node in First 2 Edges (O(1) Time, O(1) Space) | Degree Frequency Map"));

        list.add(new ProblemDefinition(
            "Find the Town Judge",
            "In a town, there are n people labeled from 1 to n. Return the label of the town judge if they exist, or -1.",
            "2\n[1,2]", "2", "1 <= n <= 1000", Difficulty.EASY, DsaTopic.GRAPHS,
            "class Solution {\n    public int solve(int n, int[] trust) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int[]",
            List.of(new TestCase(null, "2\n[1,2]", "2", true, false, "Judge is 2"), new TestCase(null, "3\n[1,3,2,3,3,1]", "-1", true, false, "No judge"))
        ).withApproaches("ARRAYS", "In-Degree & Out-Degree Trust Net Score (O(N + E) Time, O(N) Space)"));

        list.add(new ProblemDefinition(
            "Number of Islands",
            "Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands.",
            "4\n5\n[1,1,1,1,0,1,1,0,1,0,1,1,0,0,0,0,0,0,0,0]", "1", "1 <= m, n <= 300", Difficulty.MEDIUM, DsaTopic.GRAPHS,
            "class Solution {\n    public int solve(int m, int n, int[] grid) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int;int[]",
            List.of(new TestCase(null, "4\n5\n[1,1,1,1,0,1,1,0,1,0,1,1,0,0,0,0,0,0,0,0]", "1", true, false, "1 continuous island"), new TestCase(null, "4\n5\n[1,1,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1]", "3", true, false, "3 distinct islands"))
        ).withApproaches("TREES, QUEUE", "DFS Flood Fill (O(M * N) Time, O(M * N) Space) | BFS Queue Traversal | Disjoint Set Union (DSU)"));

        list.add(new ProblemDefinition(
            "Clone Graph",
            "Given a reference of a node in a connected undirected graph, return a deep copy (clone) of the graph. Return cloned node count.",
            "4", "4", "0 <= nodes <= 100", Difficulty.MEDIUM, DsaTopic.GRAPHS,
            "class Solution {\n    public int solve(int nodeCount) {\n        // Write code\n    }\n}",
            "solve", "int", "int",
            List.of(new TestCase(null, "4", "4", true, false, "4 nodes cloned"), new TestCase(null, "1", "1", true, false, "1 node cloned"))
        ).withApproaches("HASHMAP, QUEUE", "DFS with Visited Cloned Map (O(V + E) Time) | BFS Level Queue"));

        list.add(new ProblemDefinition(
            "Max Area of Island",
            "You are given an m x n binary matrix grid. Find the maximum area of an island in grid. If there is no island, return 0.",
            "4\n4\n[0,0,1,0,0,0,1,0,1,1,1,0,0,0,0,0]", "4", "1 <= m, n <= 50", Difficulty.MEDIUM, DsaTopic.GRAPHS,
            "class Solution {\n    public int solve(int m, int n, int[] grid) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int;int[]",
            List.of(new TestCase(null, "4\n4\n[0,0,1,0,0,0,1,0,1,1,1,0,0,0,0,0]", "4", true, false, "Max island area is 4"), new TestCase(null, "2\n2\n[0,0,0,0]", "0", true, false, "No islands"))
        ).withApproaches("RECURSION, QUEUE", "DFS Island Area Accumulator (O(M * N) Time) | BFS Queue"));

        list.add(new ProblemDefinition(
            "Course Schedule",
            "There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. Return true if you can finish all courses.",
            "2\n[1,0]", "true", "1 <= numCourses <= 2000", Difficulty.MEDIUM, DsaTopic.GRAPHS,
            "class Solution {\n    public boolean solve(int numCourses, int[] prereqs) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int;int[]",
            List.of(new TestCase(null, "2\n[1,0]", "true", true, false, "Possible to finish"), new TestCase(null, "2\n[1,0,0,1]", "false", true, false, "Cycle detected"))
        ).withApproaches("QUEUE", "Kahn's Algorithm (BFS In-degree Topological Sort) (O(V + E) Time) | DFS Cycle Detection"));

        list.add(new ProblemDefinition(
            "Keys and Rooms",
            "There are n rooms labeled from 0 to n - 1. Return true if you can visit all the rooms, or false otherwise.",
            "[1,2,3]", "true", "n == rooms.length <= 1000", Difficulty.MEDIUM, DsaTopic.GRAPHS,
            "class Solution {\n    public boolean solve(int[] keys) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[]",
            List.of(new TestCase(null, "[1,2,3]", "true", true, false, "All rooms visited"), new TestCase(null, "[1,3,3,0,1]", "false", true, false, "Cannot visit room 2"))
        ).withApproaches("QUEUE, RECURSION", "BFS Queue Room Traversal (O(V + E) Time, O(V) Space) | DFS Visited Set"));

        list.add(new ProblemDefinition(
            "Rotting Oranges",
            "You are given an m x n grid containing fresh (1), rotten (2), and empty (0) oranges. Return minimum minutes until no fresh orange remains.",
            "3\n3\n[2,1,1,1,1,0,0,1,1]", "4", "1 <= m, n <= 10", Difficulty.MEDIUM, DsaTopic.GRAPHS,
            "class Solution {\n    public int solve(int m, int n, int[] grid) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int;int[]",
            List.of(new TestCase(null, "3\n3\n[2,1,1,1,1,0,0,1,1]", "4", true, false, "4 minutes to rot all"), new TestCase(null, "3\n3\n[2,1,1,0,1,1,1,0,1]", "-1", true, false, "Fresh orange cannot be reached"))
        ).withApproaches("QUEUE, ARRAYS", "Multi-Source BFS Queue (O(M * N) Time, O(M * N) Space)"));

        // ==========================================
        // 14. DYNAMIC_PROGRAMMING (8 Problems: 102 - 109)
        // ==========================================
        list.add(new ProblemDefinition(
            "House Robber",
            "You are a professional robber planning to rob houses along a street. Return maximum money you can rob without alerting police.",
            "[1,2,3,1]", "4", "1 <= nums.length <= 100", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[1,2,3,1]", "4", true, false, "Rob house 1 (money 1) and house 3 (money 3) = 4"), new TestCase(null, "[2,7,9,3,1]", "12", true, false, "Rob 1, 3, 5 = 12"))
        ).withApproaches("ARRAYS, RECURSION", "1D State Transition DP (O(N) Time, O(1) Space) | Top-Down Recursion with Memoization"));

        list.add(new ProblemDefinition(
            "House Robber II",
            "All houses at this place are arranged in a circle. Return the maximum amount of money you can rob tonight without alerting the police.",
            "[2,3,2]", "3", "1 <= nums.length <= 100", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[2,3,2]", "3", true, false, "Cannot rob house 1 and 3 simultaneously"), new TestCase(null, "[1,2,3,1]", "4", true, false, "Rob house 2 and 4 = 4"))
        ).withApproaches("ARRAYS", "Two Passes of House Robber I [0..n-2] and [1..n-1] (O(N) Time, O(1) Space)"));

        list.add(new ProblemDefinition(
            "Coin Change",
            "You are given an integer array coins and an integer amount. Return fewest number of coins that you need to make up that amount.",
            "[1,2,5]\n11", "3", "1 <= coins.length <= 12", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(int[] coins, int amount) {\n        // Write code\n    }\n}",
            "solve", "int", "int[];int",
            List.of(new TestCase(null, "[1,2,5]\n11", "3", true, false, "11 = 5 + 5 + 1 (3 coins)"), new TestCase(null, "[2]\n3", "-1", true, false, "Cannot make up 3"))
        ).withApproaches("RECURSION, QUEUE", "Bottom-Up Unbounded Knapsack DP (O(amount * coins)) | BFS Shortest Path | Memoization"));

        list.add(new ProblemDefinition(
            "Longest Increasing Subsequence",
            "Given an integer array nums, return the length of the longest strictly increasing subsequence.",
            "[10,9,2,5,3,7,101,18]", "4", "1 <= nums.length <= 2500", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "int", "int[]",
            List.of(new TestCase(null, "[10,9,2,5,3,7,101,18]", "4", true, false, "LIS is [2,3,7,101] length 4"), new TestCase(null, "[0,1,0,3,2,3]", "4", true, false, "LIS is [0,1,2,3]"))
        ).withApproaches("BINARY_SEARCH, ARRAYS", "Patience Sorting / Binary Search (O(N log N) Time, O(N) Space) | 1D DP (O(N^2))"));

        list.add(new ProblemDefinition(
            "Unique Paths",
            "There is a robot on an m x n grid. The robot can only move down or right. Return number of possible unique paths.",
            "3\n7", "28", "1 <= m, n <= 100", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(int m, int n) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int",
            List.of(new TestCase(null, "3\n7", "28", true, false, "28 unique paths"), new TestCase(null, "3\n2", "3", true, false, "3 paths"))
        ).withApproaches("MATH", "Combinatorics Formula `(m+n-2)! / ((m-1)!(n-1)!)` (O(min(m, n)) Time, O(1) Space) | 2D DP Table"));

        list.add(new ProblemDefinition(
            "Minimum Path Sum",
            "Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes sum.",
            "3\n3\n[1,3,1,1,5,1,4,2,1]", "7", "1 <= m, n <= 200", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(int m, int n, int[] grid) {\n        // Write code\n    }\n}",
            "solve", "int", "int;int;int[]",
            List.of(new TestCase(null, "3\n3\n[1,3,1,1,5,1,4,2,1]", "7", true, false, "1 -> 3 -> 1 -> 1 -> 1 = 7"), new TestCase(null, "2\n3\n[1,2,3,4,5,6]", "12", true, false, "1 -> 2 -> 3 -> 6 = 12"))
        ).withApproaches("ARRAYS", "In-place 2D DP Table (O(M * N) Time, O(1) Extra Space)"));

        list.add(new ProblemDefinition(
            "Jump Game",
            "You are given an integer array nums. Return true if you can reach the last index from the first index.",
            "[2,3,1,1,4]", "true", "1 <= nums.length <= 10^4", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public boolean solve(int[] nums) {\n        // Write code\n    }\n}",
            "solve", "boolean", "int[]",
            List.of(new TestCase(null, "[2,3,1,1,4]", "true", true, false, "Can reach end"), new TestCase(null, "[3,2,1,0,4]", "false", true, false, "Cannot reach past 0"))
        ).withApproaches("ARRAYS", "Greedy Max Reachable Index (O(N) Time, O(1) Space) | Dynamic Programming (O(N^2))"));

        list.add(new ProblemDefinition(
            "Longest Common Subsequence",
            "Given two strings text1 and text2, return the length of their longest common subsequence.",
            "abcde\nace", "3", "1 <= text1.length, text2.length <= 1000", Difficulty.MEDIUM, DsaTopic.DYNAMIC_PROGRAMMING,
            "class Solution {\n    public int solve(String text1, String text2) {\n        // Write code\n    }\n}",
            "solve", "int", "String;String",
            List.of(new TestCase(null, "abcde\nace", "3", true, false, "LCS ace length 3"), new TestCase(null, "abc\nabc", "3", true, false, "LCS abc length 3"))
        ).withApproaches("STRINGS, RECURSION", "2D DP Table (O(M * N) Time, O(M * N) Space) | Top-Down Recursion with Memoization"));

        return list;
    }
}
