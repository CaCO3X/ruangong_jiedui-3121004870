package com.jiedui.utils;

import org.apache.commons.math3.fraction.Fraction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author lyp 2023/09/26
 */
public class SaveUtils {
    /**
     * 将生成的题目保存到所设的题目文件中
     * @param exercises 生成的题目
     */
    public static void saveExercises(List<String> exercises) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/jiedui/test/Exercises.txt"))) {
            int count = 1;

            for (String exercise : exercises) {
                writer.write("题目" + count + ": " + exercise);
                writer.newLine();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将自己的答案写入相应的文件中
     * @param num 参数1
     */
    public static void answerExercises(int num){
        int count=1;

        try ( BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/jiedui/test/AnswersOfMine.txt"))){
            for(int i=0;i<num;i++){
                writer.write("我的回答"+count+":");
                writer.newLine();
                count++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 计算出答案并将答案保存到所设的答案文件中
     */
    public static void calculateAndSaveAnswers(List<String> exercises) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/jiedui/test/Answers.txt"))) {
            int count = 1;
            writer.write("=====参考答案=====");
            writer.newLine();
            for (String exercise : exercises) {
                String[] terms = exercise.split(" ");
                double result = GenerateUtils.calculateExpression(terms);  // 计算表达式的值
                String formattedResult = formatFraction(result); // 将结果转化为分数形式

                writer.write("答案" + count + ": " + formattedResult);
                writer.newLine();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存答题情况
     */
    public static void saveGrade(int correctCount, StringBuilder correctBuilder, int wrongCount, StringBuilder wrongBuilder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/jiedui/test/Grade.txt"))) {
            writer.write("====你的答题情况====");
            writer.newLine();
            writer.write("Correct: " + correctCount + " (  " + correctBuilder.toString() + ")");
            writer.newLine();
            writer.write("Wrong: " + wrongCount + " (  " + wrongBuilder.toString() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用Apache Commons Math库中的Fraction类来实现小数转化为分数
     */
    private static String formatFraction(double result) {
        if (result%1==0) {
            int result_1=(int)result;
            return Integer.toString(result_1);
        } else {
            Fraction fraction = new Fraction(result);
            return fraction.toString();
        }
    }
}