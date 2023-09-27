package com.jiedui.utils;

import java.util.*;

/**
 * @author lyp 2023/09/25
 */
public class GenerateUtils {
    /**
     * 操作符集合
     */
    private static final String[] OPERATORS = {"+", "-", "*", "÷"};

    /**
     * 题目中操作符的最大数量
     */
    private static final int MAX_OPERATORS = 3;

    /**
     * 题目中数值的范围
     */
    private final int range;

    public GenerateUtils(int range) {
        this.range = range;
    }

    /**
     * 生成习题
     * @param num num
     */
    public void generateExercises(int num) {
        List<String> exercises = new ArrayList<>();
        Set<String> results = new HashSet<>();
        Random random = new Random();

        while (exercises.size() < num) {
            StringBuilder exercise = new StringBuilder();
            int operatorCount = random.nextInt(MAX_OPERATORS) + 1;  // 随机生成运算符个数，范围为1到MAX_OPERATORS

            // 生成随机题目
            for (int i = 0; i < operatorCount; i++) {
                exercise.append(generateNumber());
                exercise.append(" ");
                exercise.append(OPERATORS[random.nextInt(OPERATORS.length)]);
                exercise.append(" ");
            }

            exercise.append(generateNumber());

            String expression = exercise.toString();

            if (!results.contains(expression)) {
                exercises.add(expression);
                results.add(expression);
            }
        }

        SaveUtils.saveExercises(exercises);  // 将题目保存到文件
        SaveUtils.calculateAndSaveAnswers(exercises);  // 计算并保存答案
    }

    /**
     * 生成习题中的数字
     * @return {@link String} 数字字符串
     */
    private String generateNumber() {
        Random random = new Random();
        int numerator = random.nextInt(range) + 1;//产生随机的分子
        int denominator = random.nextInt(range) + 1;//产生随机的分母

        // 生成分数形式的随机数
        if (numerator > denominator) {//带分数形式
            int integerPart = numerator / denominator;
            numerator = numerator%denominator;

            if(numerator==0){
                return Integer.toString(integerPart);
            }else {
                return integerPart + "'" + numerator + "/" + denominator;
            }
        } else if (numerator < denominator) {//非带分数的真分数分数形式
            return numerator + "/" + denominator;
        } else {
            return "1";
        }
    }

    /**
     * 计算后缀表达式的值
     * @return double 返回值
     */
    private static double evaluatePostfixExpression(List<String> postfix) {
        List<Double> operandStack = new ArrayList<>();

        for (String term : postfix) {
            if (isOperator(term)) {
                double operand2 = operandStack.remove(operandStack.size() - 1);
                double operand1 = operandStack.remove(operandStack.size() - 1);

                // 根据运算符进行计算
                switch (term) {
                    case "+":
                        operandStack.add(operand1 + operand2);
                        break;
                    case "-":
                        operandStack.add(operand1 - operand2);
                        break;
                    case "*":
                        operandStack.add(operand1 * operand2);
                        break;
                    case "÷":
                        operandStack.add(operand1 / operand2);
                        break;
                }
            } else {
                operandStack.add(parseNumber(term));
            }
        }
        return operandStack.get(0);
    }

    /**
     * 将表达式中的分数形式转化为具体的值便于计算
     */
    private static double parseNumber(String term) {
        if (term.contains("'")) {//判断是否有带分数
            String[] parts = term.split("'");//分离带分数中的整数和分数部分
            int integerPart = Integer.parseInt(parts[0]);//取带分数的整数部分
            int numerator = Integer.parseInt(parts[1].split("/")[0]);//取带分数中分数的分子
            int denominator = Integer.parseInt(parts[1].split("/")[1]);//取带分数中分数的分母
            return (double) (integerPart * denominator + numerator)/(double) denominator;
        } else if(term.contains("/"))  {//判断是否有分数
            String[] parts = term.split("/");//分离分数的分子和分母
            int numerator = Integer.parseInt(parts[0]);//取分子
            int denominator = Integer.parseInt(parts[1]);//取分母
            return   (double) numerator /  (double) denominator;
        }else {//无分数
            return Integer.parseInt(term);
        }
    }

    /**
     * 判断是否是操作符
     */
    private static boolean isOperator(String term) {
        for (String operator : OPERATORS) {
            if (operator.equals(term)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将表达式转换为后缀表达式
     */
    private static List<String> infixToPostfix(String[] terms) {
        List<String> postfix = new ArrayList<>();
        List<String> operatorStack = new ArrayList<>();

        for (String term : terms) {
            if (isOperator(term)) {
                //用栈来存储原表达式中操作符的顺序
                while (!operatorStack.isEmpty() && isOperator(operatorStack.get(operatorStack.size() - 1))) {
                    String topOperator = operatorStack.get(operatorStack.size() - 1);
                    if (CheckUtils.compareOperatorPrecedence(topOperator, term) >= 0) {
                        postfix.add(operatorStack.remove(operatorStack.size() - 1));
                    } else {
                        break;
                    }
                }
                operatorStack.add(term);
            } else {
                postfix.add(term);
            }
        }

        while (!operatorStack.isEmpty()) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }
        return postfix;
    }

    /**
     * 计算表达式的函数
     */
    public static double calculateExpression(String[] terms) {
        List<String> postfix = infixToPostfix(terms);  // 将中缀表达式转换为后缀表达式
        return evaluatePostfixExpression(postfix);  // 计算后缀表达式的值
    }
}
