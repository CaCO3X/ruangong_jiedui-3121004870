package ruangong_2;

import org.apache.commons.math3.fraction.Fraction;

import java.io.*;
import java.util.*;

public class MathExerciseGenerator {
    private static final String[] OPERATORS = {"+", "-", "*", "÷"};//操作符集合
    private static final int MAX_OPERATORS = 3;//题目中操作符的最大数量

    private static int range;//题目中数值的范围

    public static void main(String[] args) {
        // 检查命令行参数
        if (args.length == 4 && args[0].equals("-n")&&args[2].equals("-r")) {//用来生成题目和相应的参考答案，并且用户须同时写入自己的答案
            int num = Integer.parseInt(args[1]);
            range = Integer.parseInt(args[3]);

            generateExercises(num);
            answerExercises(num);
        } else if (args.length == 4 && args[0].equals("-e") && args[2].equals("-a")) {//用来检查用户的答题情况
            String exerciseFile = args[1];
            String myanswerFile = args[3];

            checkAnswers(exerciseFile, myanswerFile);
        } else {//输入错误的命令时
            System.out.println("命令行参数错误，请输入正确的参数。");
            System.out.println("生成题目示例：-n <生成题目数量> -r <题目中的数值范围>");
            System.out.println("检查答案示例：-e <题目文件名> -a <我的答案的文件名>");
        }
    }

    //生成习题
    private static void generateExercises(int num) {
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

        saveExercises(exercises);  // 将题目保存到文件
        calculateAndSaveAnswers(exercises);  // 计算并保存答案
    }

    //生成习题中的数字
    private static String generateNumber() {
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

    //将生成的题目保存到所设的题目文件中
    private static void saveExercises(List<String> exercises) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ruangong_2/Exercises.txt"))) {
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

    //将自己的答案写入相应的文件中
    private static void answerExercises(int num){
        //String myanswers;
        int count=1;

        try ( BufferedWriter writer = new BufferedWriter(new FileWriter("ruangong_2/AnswersOfMine.txt"))){
            for(int i=0;i<num;i++){
                writer.write("我的回答"+count+":");
                writer.newLine();
                count++;
           }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //计算出答案并将答案保存到所设的答案文件中
    private static void calculateAndSaveAnswers(List<String> exercises) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ruangong_2/Answers.txt"))) {
            int count = 1;
            writer.write("=====参考答案=====");
            writer.newLine();
            for (String exercise : exercises) {
                String[] terms = exercise.split(" ");
                double result = calculateExpression(terms);  // 计算表达式的值
                String formattedResult = formatFraction(result); // 将结果转化为分数形式

                writer.write("答案" + count + ": " + formattedResult);
                writer.newLine();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //计算表达式的函数
    private static double calculateExpression(String[] terms) {
        List<String> postfix = infixToPostfix(terms);  // 将中缀表达式转换为后缀表达式
        return evaluatePostfixExpression(postfix);  // 计算后缀表达式的值
    }

    //将表达式转换为后缀表达式
    private static List<String> infixToPostfix(String[] terms) {
        List<String> postfix = new ArrayList<>();
        List<String> operatorStack = new ArrayList<>();

        for (String term : terms) {
            if (isOperator(term)) {
                //用栈来存储原表达式中操作符的顺序
                while (!operatorStack.isEmpty() && isOperator(operatorStack.get(operatorStack.size() - 1))) {
                    String topOperator = operatorStack.get(operatorStack.size() - 1);
                    if (compareOperatorPrecedence(topOperator, term) >= 0) {
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

    //判断是否是操作符
    private static boolean isOperator(String term) {
        for (String operator : OPERATORS) {
            if (operator.equals(term)) {
                return true;
            }
        }
        return false;
    }

    //比较算术表达式中操作符的顺序
    private static int compareOperatorPrecedence(String operator1, String operator2) {
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

    //计算后缀表达式的值
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

    //将表达式中的分数形式转化为具体的值便于计算
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

    //确定用户的答题对错情况
    private static void checkAnswers(String exerciseFile, String myanswerFile) {
        try {
            List<String> exercises = readFile(exerciseFile);
            List<String> myanswers = readFile(myanswerFile);
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
                double result = calculateExpression(terms);//算出题目的正确数值
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

            saveGrade(correctCount, correctBuilder, wrongCount, wrongBuilder);  // 保存统计结果
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取文件中的内容
    private static List<String> readFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        return lines;
    }

    //保存答题情况
    private static void saveGrade(int correctCount, StringBuilder correctBuilder, int wrongCount, StringBuilder wrongBuilder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ruangong_2/Grade.txt"))) {
            writer.write("====你的答题情况====");
            writer.newLine();
            writer.write("Correct: " + correctCount + " (  " + correctBuilder.toString() + ")");
            writer.newLine();
            writer.write("Wrong: " + wrongCount + " (  " + wrongBuilder.toString() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //使用Apache Commons Math库中的Fraction类来实现小数转化为分数
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