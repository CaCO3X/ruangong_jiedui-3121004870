package com.jiedui.utils;

import org.apache.commons.math3.fraction.Fraction;

import java.io.IOException;
import java.util.List;

/**
 * @author lyp 2023/09/27
 */
public class CheckUtils {
    /**
     * 确定用户的答题对错情况
     * @param exerciseFile 正确答案文件
     * @param myanswerFile 回答文件
     */
    public static void checkAnswers(String exerciseFile, String myanswerFile) {
        try {
            List<String> exercises = FileUtils.readFile(exerciseFile);
            List<String> myanswers = FileUtils.readFile(myanswerFile);
            StringBuilder correctBuilder = new StringBuilder();
            StringBuilder wrongBuilder = new StringBuilder();
            int correctCount = 0;
            int wrongCount = 0;

            for (int i = 0; i < exercises.size(); i++) {
                String exercise = exercises.get(i).substring(exercises.get(i).indexOf(":") + 1).trim();//取出题目中的表达式
                double myAnswer;
                Fraction myAnswer_1;
                String[] parts = myanswers.get(i).split(":");//分离出用户答案的数值部分

                if(parts[1].contains("/")){
                    int numerator = Integer.parseInt(parts[1].split("/")[0]);
                    int denominator = Integer.parseInt(parts[1].split("/")[1]);
                    myAnswer=(double) numerator/(double) denominator;
                    myAnswer_1 = new Fraction(myAnswer);
                }else {
                    myAnswer=Double.parseDouble(parts[1]);
                    myAnswer_1 = new Fraction(myAnswer);
                }

                String[] terms = exercise.split(" ");
                double result = GenerateUtils.calculateExpression(terms);//算出题目的正确数值
                Fraction result_1 = new Fraction(result);

                // 检查答案是否正确
                if (result_1.equals(myAnswer_1)) {
                    correctCount++;//统计正确数目
                    correctBuilder.append(i + 1).append("  ");//统计正确题号
                } else {
                    wrongCount++;//统计错误数目
                    wrongBuilder.append(i + 1).append("  ");//统计错误题号
                }
            }

            SaveUtils.saveGrade(correctCount, correctBuilder, wrongCount, wrongBuilder);  // 保存统计结果
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 比较算术表达式中操作符的顺序
     * @param operator1 操作符1
     * @param operator2 操作符2
     * @return int 返回标识
     */
    public static int compareOperatorPrecedence(String operator1, String operator2) {
        if ((operator1.equals("+") || operator1.equals("-")) &&
                (operator2.equals("*") || operator2.equals("÷"))) {
            return -1;
        } else if ((operator1.equals("*") || operator1.equals("÷")) &&
                (operator2.equals("+") || operator2.equals("-"))) {
            return 1;
        } else {
            return 0;
        }
    }
}
