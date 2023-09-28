# 结对项目：自动生成四则运算题目

> 项目成员：
>
> 李奇龙 3121004869
>
> 李钰平 3121004870
>
> github地址：[作业仓库](https://github.com/CaCO3X/ruangong_jiedui-3121004870)

| **这个作业属于哪个课程** | [点击这里](https://edu.cnblogs.com/campus/gdgy/CSGrade21-12) |
| :----------------------: | :----------------------------------------------------------: |
|    这个作业要求在哪里    | [点击这里](https://edu.cnblogs.com/campus/gdgy/CSGrade21-12/homework/13016) |
|      这个作业的目标      |            与队友共同完成结对项目——四则运算生成器            |

# 一、PSP表格

|                PSP2.1                 |   Personal Software Process Stages    | 预估耗时（分钟） | 实际耗时（分钟） |
| :-----------------------------------: | :-----------------------------------: | :--------------: | :--------------: |
|               Planning                |                 计划                  |        60        |        30        |
|               Estimate                |       估计这个任务需要多少时间        |        30        |        10        |
|              Development              |                 开发                  |       300        |       300        |
|               Analysis                |       需求分析 (包括学习新技术)       |        60        |        30        |
|              Design Spec              |             生成设计文档              |        60        |        60        |
|             Design Review             |               设计复审                |        15        |        10        |
|            Coding Standard            | 代码规范 (为目前的开发制定合适的规范) |        15        |        10        |
|                Design                 |               具体设计                |        30        |        60        |
|                Coding                 |               具体编码                |        30        |        60        |
|              Code Review              |               代码复审                |        15        |        10        |
|                 Test                  | 测试（自我测试，修改代码，提交修改）  |        15        |        60        |
|               Reporting               |                 报告                  |        60        |       120        |
|              Test Report              |               测试报告                |        30        |        60        |
|           Size Measurement            |              计算工作量               |        10        |        10        |
| Postmortem & Process Improvement Plan |     事后总结, 并提出过程改进计划      |        10        |        20        |
|                                       |                 合计                  |       740        |       850        |

# 二、项目结构

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230927212107073-2013123863.png)

1. Main类：主程序入口
2. CheckUtils类：检查答题对错情况和操作符顺序
3. FileUtils类：用于读取文件
4. GenerateUtils类：最主要的工具类，用于生成题目等等
5. SaveUtils类：保存答案到文件等等操作

# 三、设计思路和实现过程

1. 命令行参数解析部分：根据命令行参数的不同，程序执行相应的功能。通过判断参数个数和参数值来确定执行生成题目、检查答案还是输出帮助信息。
2. 题目生成部分：通过随机数生成器Random，循环生成指定数量的四则运算式子。每个式子由随机生成的操作数和操作符构成，操作符从预定义的OPERATORS数组中随机选择。
3. 数字生成部分：利用随机数生成器Random生成随机的分子和分母，并根据范围限制确定整数部分和是否有分数部分。根据不同情况，将数字以特定格式转化为字符串表示。
4. 题目保存部分：通过BufferedWriter将生成的题目保存到名为"Exercises.txt"的文本文件中。同时，使用计数器变量保持题目序号，方便阅读。
5. 答案计算和保存部分：根据生成的题目，将每个题目表达式按照空格拆分为运算项，通过后缀表达式转换和求值的方法计算每个题目的结果。使用BufferedWriter将答案保存到名为"Answers.txt"的文本文件中。
6. 用户回答问题并写入自己答案的部分：根据生成的题目，把自己对于题目的回答用BufferedWriter写入”AnswersOfMine.txt”文件中。
7. **后缀表达式转换部分**：通过两个栈，一个保存运算项的后缀表达式（postfix），另一个保存运算符的栈（operatorStack），遍历每个运算项，根据运算项是运算符还是操作数进行相应的处理和判断。最后，将运算符栈中剩余的运算符按照顺序弹出，放入后缀表达式栈中。
8. 答案检查部分：通过读取题目文件和答案文件的内容，并逐行进行比较，对每个题目求解并与答案比对，统计正确和错误数量。通过两个StringBuilder分别保存正确和错误题目的序号，方便最后输出成绩单。
9. 文件读写部分：使用BufferedReader和BufferedWriter实现文件的读取和写入操作，**其中使用try-with-resources语句，自动关闭资源，提高代码的可读性和简洁性。**
10. 成绩单保存部分：通过BufferedWriter将正确题目数量、正确题目的序号列表、错误题目数量和错误题目的序号列表保存到名为"Grade.txt"的文本文件中。

---

后缀表达式的转换：

> 通过使用两个栈来实现后缀表达式的转换。

1. 首先，定义一个栈 operatorStack ，用于保存运算符。

​	遍历输入的中缀表达式，对于每个运算项（数字或运算符）执行以下操作：

2. 如果当前运算项是数字，则直接将其输出到后缀表达式栈 postfix 中。

3. 如果当前运算项是左括号"("，则将其入栈。

4. 如果当前运算项是右括号")"，则将 operatorStack 栈顶的运算符弹出并输出到后缀表达式栈 postfix 中，直到遇到左括号"("为止，然后将左括号从 operatorStack 中弹出，但不输出到 postfix 中。

5. 如果当前运算项是运算符，比较其与 operatorStack 栈顶运算符的优先级：如果 operatorStack 栈顶的运算符优先级大于等于当前运算符，将 operatorStack 栈顶的运算符弹出并输出到 postfix 中，重复此步骤，直到栈顶运算符优先级小于当前运算符或栈为空。

6. 将当前运算符入栈。

7. 当所有运算项遍历完毕后，将 operatorStack 中剩余的运算符依次弹出并输出到 postfix 中。

8. 转换完成后，栈 operatorStack 中剩余的运算符就是按照优先级逆序排列的后缀表达式。

**代码如下：**

```java
/**
 * 计算表达式的函数
 */
public static double calculateExpression(String[] terms) {
    List<String> postfix = infixToPostfix(terms);  // 将中缀表达式转换为后缀表达式
    return evaluatePostfixExpression(postfix);  // 计算后缀表达式的值
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
```

# 四、效能分析

Override如下：

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230927214537290-1460231217.png)

---

内存分布如下：

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230927214613009-375256442.png)

> 由于操作时要将数字大量的转为字符串，同时在判断算数运算符的时候需要使用到char，因此char类型和string类型占用的空间内存最大

# 五、测试与运行

测试代码如下：

```java
public class MainTest {
    @Test
    public void mainTest(){
        // 生成10个题目以及范围为6
        System.out.println(">>>生成问题");
        System.out.println();
        String[] args = {"-n","10","-r","6"};
        Main.main(args);
        System.out.println(">>>回答问题");
        System.out.println();
        // 第二个分支
        String[] args2 = {"-e","src/main/java/com/jiedui/test/Exercises.txt","-a","src/main/java/com/jiedui/test/AnswersOfMine.txt"};
        Main.main(args2);

        // 异常分支
        System.out.println(">>>故意进入异常分支");
        Main.main(new String[0]);
    }
}
```

测试结果如下：

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230928135050082-469586905.png)

覆盖率如下：

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230928135213880-1173664377.png)

---

生成的文件如下：

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230928135337242-1475477112.png)

---

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230928135411864-634365028.png)
---

---

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230928135507143-934650986.png)

---

![](https://img2023.cnblogs.com/blog/3274666/202309/3274666-20230928135525226-1099865608.png)

# 六、项目小结

李钰平：负责了大部分的代码设计以及编码规范等等。同时在写代码的时候由于有同伴的提醒和纠错，帮助提高了自身素质和代码质量。同时，在设计项目初始的时候，合作思考可以考虑的更加全面。

李奇龙：主要负责文档编写和代码测试，以及代码注释的编写等等。在代码测试的时候，可以帮助找到代码中的一些bug，也为使用idea的调试功能更加熟练。

