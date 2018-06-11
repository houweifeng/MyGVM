package plodsoft.mygvm.testopcodes

import plodsoft.mygvm.TestingInputStream
import plodsoft.mygvm.model.DefaultRamModel
import plodsoft.mygvm.model.TestingKeyboardModel
import plodsoft.mygvm.model.TestingScreenModel
import plodsoft.mygvm.model.TestingTextModel
import plodsoft.mygvm.test
import java.io.ByteArrayOutputStream


fun testRuntime() {
    val ram = DefaultRamModel()
    val input = TestingInputStream()
    val output = ByteArrayOutputStream()
    val runtime = plodsoft.mygvm.runtime.Runtime(ram, TestingScreenModel(output), TestingTextModel(output), TestingKeyboardModel(input, output))

    /**
     void main() {
       *(int32 *) 0xe000 = 0x12345678;
       printf("%d,%d,%d", *(uint8 *) 0xe000, *(int16 *) 0xe000, *(int32 *) 0xe000);
     }
     */
    runtime.test("测试 0x04, 0x05, 0x06,  0x0d, printf, 0x3c, 0x3b, 0x03, 0x35, 0x40, 0x01", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3c, 0x00, 0x20, 0x3b, 0x17, 0x00, 0x00,
            0x3e, 0x05, 0x00, 0x00,
            0x03, 0x00, 0xe0, 0x04, 0x00, 0x03, 0x78, 0x56, 0x34, 0x12, 0x35, 0x38,
            0x0d, 0x25, 0x64, 0x2c, 0x25, 0x64, 0x2c, 0x25, 0x64, 0x00,
            0x04, 0x00, 0xe0, 0x05, 0x00, 0xe0, 0x06, 0x00, 0xe0,
            0x01, 0x04, 0x82, 0x40),
            "",
            """[add bytes (120,22136,305419896)]
               |[render text to screen: 0b0]
               |""".trimMargin())

    /**
     void main() {
       *(int32 *) 0xe000 = 0x89abcdef;
       printf("%d,%d,%d", *(uint8 *) (0xe000 + 3), *(int16 *) (0xe000 + 2), *(int32 *) (0xe000 + 0));
     }
     */
    runtime.test("测试 0x07, 0x08, 0x09", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3c, 0x00, 0x20, 0x3b, 0x17, 0x00, 0x00,
            0x3e, 0x05, 0x00, 0x00,
            0x03, 0x00, 0xe0, 0x04, 0x00, 0x03, 0xef, 0xcd, 0xab, 0x89, 0x35, 0x38,
            0x0d, 0x25, 0x64, 0x2c, 0x25, 0x64, 0x2c, 0x25, 0x64, 0x00,
            0x01, 0x03, 0x07, 0x00, 0xe0, 0x01, 0x02, 0x08, 0x00, 0xe0, 0x01, 0x00, 0x09, 0x00, 0xe0,
            0x01, 0x04, 0x82, 0x40),
            "",
            """[add bytes (137,-30293,-1985229329)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    char t[100];

    void main() {
        int a;
        a = 17;
        t[a] = 65;
        printf("%d", t[a]);
    }
     */
    runtime.test("测试 0x0a, 0x0f", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x64, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x07, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x82, 0x00, 0x01, 0x11, 0x35, 0x38,
            0x0F, 0x05, 0x00, 0x0A, 0x00, 0x20, 0x01, 0x41, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x00, 0x0F, 0x05, 0x00, 0x07, 0x00, 0x20, 0x01, 0x02, 0x82, 0x40),
            "",
            """[add bytes (65)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    int t[100];

    void main() {
    long a;
    a = 17;
    t[a] = 1000;
    printf("%d", t[a]);
    }
     */
    runtime.test("测试 0x0b, 0x10, 0x4a, (0x0c, 0x0e 已测试)", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0xC8, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x09, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x84, 0x00, 0x01, 0x11, 0x35, 0x38,
            0x10, 0x05, 0x00, 0x4A, 0x01, 0x00, 0x0B, 0x00, 0x20, 0x02, 0xE8, 0x03, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x00, 0x10, 0x05, 0x00, 0x4A, 0x01, 0x00, 0x08, 0x00, 0x20, 0x01, 0x02, 0x82, 0x40),
            "",
            """[add bytes (1000)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    void main() {
        char t[100];
        long a;
        a = 17;
        t[a] = 65;
        printf("%d", t[a]);
    }
     */
    runtime.test("测试 0x14, 0x11", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x6D, 0x00, 0x00,
            0x03, 0x69, 0x00, 0x84, 0x00, 0x01, 0x11, 0x35, 0x38,
            0x10, 0x69, 0x00, 0x14, 0x05, 0x00, 0x01, 0x41, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x00, 0x10, 0x69, 0x00, 0x11, 0x05, 0x00, 0x01, 0x02, 0x82, 0x40),
            "",
            """[add bytes (65)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    void main() {
        int t[100];
        long a;
        a = 17;
        t[a] = 1234;
        printf("%d", t[a]);
    }
     */
    runtime.test("测试 0x15, 0x12 (0x16, 0x13 已测试)", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0xD1, 0x00, 0x00,
            0x03, 0xCD, 0x00, 0x84, 0x00, 0x01, 0x11, 0x35, 0x38,
            0x10, 0xCD, 0x00, 0x4A, 0x01, 0x00, 0x15, 0x05, 0x00, 0x02, 0xD2, 0x04, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x00, 0x10, 0xCD, 0x00, 0x4A, 0x01, 0x00, 0x12, 0x05, 0x00, 0x01, 0x02, 0x82, 0x40),
            "",
            """[add bytes (1234)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    void main() {
        *0xe000 = 65;
        printf("%d", *0xe000);
    }
     */
    runtime.test("测试 0x37, 0x36,", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x05, 0x00, 0x00,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x37, 0x01, 0x41, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x00, 0x03, 0x00, 0xE0, 0x00, 0x00, 0x36, 0x01, 0x02, 0x82, 0x40),
            "",
            """[add bytes (65)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    long c;

    void main() {
        char a;
        int b;
        a = 10;
        b = 20;
        c = 30;
        printf("%d,%d,%d,%d,%d", ++a, --b, c++, c--, -c);
    }
     */
    runtime.test("测试 0x1c, 0x1d, 0x1e, 0x1f, 0x20", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x04, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x08, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x81, 0x00, 0x01, 0x0A, 0x35, 0x38,
            0x03, 0x06, 0x00, 0x82, 0x00, 0x01, 0x14, 0x35, 0x38,
            0x03, 0x00, 0x20, 0x04, 0x00, 0x01, 0x1E, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x03, 0x05, 0x00, 0x81, 0x00, 0x1D,
            0x03, 0x06, 0x00, 0x82, 0x00, 0x1E,
            0x03, 0x00, 0x20, 0x04, 0x00, 0x1F,
            0x03, 0x00, 0x20, 0x04, 0x00, 0x20,
            0x06, 0x00, 0x20, 0x1C, 0x01, 0x06, 0x82, 0x40),
            "",
            """[add bytes (11,19,30,31,-30)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    int a[] = { 0x12, 0x34 };

    void main() {
        printf("%d", a[1]);
    }
     */
    runtime.test("初始化全局数组", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x41, 0x00, 0x20, 0x04, 0x00, 0x12, 0x00, 0x34, 0x00,
            0x3C, 0x04, 0x20, 0x3B, 0x20, 0x00, 0x00,
            0x3E, 0x05, 0x00, 0x00,
            0x0D, 0x25, 0x64, 0x00, 0x05, 0x02, 0x20, 0x01, 0x02, 0x82, 0x40),
            "",
            """[add bytes (52)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    void main() {
        int a; a = 9;
        printf("%d,%d", (((a + 2) * 3 / 4 - 5) % 6) << 2, a >> 1);
    }
     */
    runtime.test("测试 0x45, 0x47, 0x48, 0x46, 0x49, 0x4a, 0x4b", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x07, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x82, 0x00, 0x01, 0x09, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x0F, 0x05, 0x00, 0x45, 0x02, 0x00, 0x47, 0x03, 0x00, 0x48, 0x04, 0x00, 0x46, 0x05, 0x00, 0x49, 0x06, 0x00, 0x4A, 0x02, 0x00, 0x0F, 0x05, 0x00, 0x4B, 0x01, 0x00, 0x01, 0x03, 0x82, 0x40),
            "",
            """[add bytes (12,4)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    void main() {
        int a, b, c;
        a = 13;
        b = 7;
        c = 0;
        printf("%d,%d,%d,%d,%d", a + b, b - a, a * b, a / b, a % b);
        printf("%d,%d,%d,%d,%d,%d", a & b, a | b, a ^ b, -a, a << b, a >> (b >> 2));
        printf("%d,%d,%d,%d,%d,%d", a == b, a != a, a > b, a < b, a >= b, a <= b);
        printf("%d,%d,%d,%d,%d,%d", a && b, a && c, a || b, a || c, !a, !c);
    }
     */
    runtime.test("0x21 ~ 0x34", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x0B, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x82, 0x00, 0x01, 0x0D, 0x35, 0x38,
            0x03, 0x07, 0x00, 0x82, 0x00, 0x01, 0x07, 0x35, 0x38,
            0x03, 0x09, 0x00, 0x82, 0x00, 0x01, 0x00, 0x35, 0x38,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x21,
            0x0F, 0x07, 0x00, 0x0F, 0x05, 0x00, 0x22,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x2A,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x2B,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x2C,
            0x01, 0x06, 0x82,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x23,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x24,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x26,
            0x0F, 0x05, 0x00, 0x25,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x2D,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x4B, 0x02, 0x00, 0x2E,
            0x01, 0x07, 0x82,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x2F,
            0x0F, 0x05, 0x00, 0x0F, 0x05, 0x00, 0x30,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x33,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x34,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x32,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x31,
            0x01, 0x07, 0x82,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x27,
            0x0F, 0x05, 0x00, 0x0F, 0x09, 0x00, 0x27,
            0x0F, 0x05, 0x00, 0x0F, 0x07, 0x00, 0x28,
            0x0F, 0x05, 0x00, 0x0F, 0x09, 0x00, 0x28,
            0x0F, 0x05, 0x00, 0x29,
            0x0F, 0x09, 0x00, 0x29,
            0x01, 0x07, 0x82, 0x40),
            "",
            """[add bytes (20,-6,91,1,6)]
              |[render text to screen: 0b0]
              |[add bytes (5,15,10,-14,1664,6)]
              |[render text to screen: 0b0]
              |[add bytes (0,0,-1,0,-1,0)]
              |[render text to screen: 0b0]
              |[add bytes (-1,0,-1,-1,0,-1)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    void main() {
        int s;
        s = "hello";
        SetScreen(1);
        UpdateLCD(0x12);
        putchar('a');
        strcpy(0xe000, s);
        printf("%d,%d", strlen(0xe000),getchar());
    }
     */
    runtime.test("putchar, getchar, SetScreen, UpdateLCD, strcpy, strlen", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x07, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x82, 0x00, 0x0D, 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x00, 0x35, 0x38,
            0x01, 0x01, 0x85,
            0x01, 0x12, 0x86,
            0x01, 0x61, 0x80,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x0F, 0x05, 0x00, 0x83,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00, 0x03, 0x00, 0xE0, 0x00, 0x00, 0x84, 0x81, 0x01, 0x03, 0x82, 0x40),
            "20",
            """[set text mode to SMALL_FONT]
              |[clear text]
              |[render text to screen: 0b10010]
              |[add byte a]
              |[render text to screen: 0b0]
              |[get key wait: 20]
              |[add bytes (5,20)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**

    void main() {
        WriteBlock(10, 20, 30, 40, 0x41, 0x1000);
        Refresh();
        TextOut(12, 34, "Hello", 0x81);
        Block(11,22,33,44,0x82);
        Rectangle(12,34,56,78,0x81);
        ClearScreen();
        Locate(5,6);
        Point(52,61,2);
        Line(34,36,48,17,0x42);
        Box(36,40,53,79,1,1);
        Circle(80,40,12,1,2);
        Ellipse(80,41,20,15,0,1);
        Beep();
        exit(50);
    }
     */
    runtime.test("WriteBlock, Refresh, TextOut, Block, Rectangle, ClearScreen, Locate, Point, Line, Box, Circle, Ellipse, Beep, exit",
            input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x05, 0x00, 0x00,
            0x01, 0x0A, 0x01, 0x14, 0x01, 0x1E, 0x01, 0x28, 0x01, 0x41, 0x02, 0x00, 0x10, 0x88,
            0x89,
            0x01, 0x0C, 0x01, 0x22, 0x0D, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x00, 0x01, 0x81, 0x8A,
            0x01, 0x0B, 0x01, 0x16, 0x01, 0x21, 0x01, 0x2C, 0x01, 0x82, 0x8B,
            0x01, 0x0C, 0x01, 0x22, 0x01, 0x38, 0x01, 0x4E, 0x01, 0x81, 0x8C,
            0x8E,
            0x01, 0x05, 0x01, 0x06, 0x92,
            0x01, 0x34, 0x01, 0x3D, 0x01, 0x02, 0x94,
            0x01, 0x22, 0x01, 0x24, 0x01, 0x30, 0x01, 0x11, 0x01, 0x42, 0x96,
            0x01, 0x24, 0x01, 0x28, 0x01, 0x35, 0x01, 0x4F, 0x01, 0x01, 0x01, 0x01, 0x97,
            0x01, 0x50, 0x01, 0x28, 0x01, 0x0C, 0x01, 0x01, 0x01, 0x02, 0x98,
            0x01, 0x50, 0x01, 0x29, 0x01, 0x14, 0x01, 0x0F, 0x01, 0x00, 0x01, 0x01, 0x99,
            0x9A,
            0x01, 0x32, 0x8D,
            0x40),
            "",
            """[write block 10,20,30,40,0x1000,0x41]
              |[refresh]
              |[text out 12,34,Hello,SMALL_FONT,0x81]
              |[draw rect 11,22,33,44,true,0xc2]
              |[draw rect 12,34,56,78,false,0xc1]
              |[clear buffer]
              |[locate to (x: 6, y: 5)]
              |[draw point 52,61,0x42]
              |[draw line 34,36,48,17,0x2]
              |[draw rect 36,40,53,79,true,0x41]
              |[draw oval 80,40,12,12,true,0x42]
              |[draw oval 80,41,20,15,false,0x41]
              |""".trimMargin())

    /**
    void main() {
        srand(17);
        printf("%d,%d,%d,%d,%d,%d,%d,%d,%d", abs(12345678), abs(-12345678), rand(), rand(), rand(), Inkey(), Inkey(), GetPoint(10, 20), GetPoint(200, 0));
    }
     */
    runtime.test("srand, rand, abs, Inkey, GetPoint", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x05, 0x00, 0x00,
            0x01, 0x11, 0x91,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x03, 0x4E, 0x61, 0xBC, 0x00, 0x8F,
            0x03, 0xB2, 0x9E, 0x43, 0xFF, 0x8F,
            0x90, 0x90, 0x90,
            0x93, 0x93,
            0x01, 0x0A, 0x01, 0x14, 0x95,
            0x01, 0xC8, 0x01, 0x00, 0x95,
            0x01, 0x0A, 0x82, 0x40),
            "16\n\n",
            """[get key no wait: 16]
              |[get key no wait: no key]
              |[test point 10,20]
              |[test point 200,0]
              |[add bytes (12345678,12345678,5887,29448,20772,16,0,0,1)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    #define S 0xe000

    void main() {
        strcpy(S, "Hello,");
        strcat(S, "here");
        printf("%s:%d,%d:%d,%d,%d,%d:%d,%d", S, strchr(S, 'l'), strchr(S, 'x'), strcmp("abc", "aC"), strcmp("ac", "ac"), strcmp("abc", "ab"), strcmp("abc", "abd"), strstr(S, "ell"), strstr(S, "els"));
        printf("%c,%c,%c,%c,%c,%c", tolower('C'), tolower('c'), tolower('='), toupper('D'), toupper('d'), toupper('#'));
        memset(S+1, 0x41, 3);
        memcpy(S+4,"!lib",4);
        printf(S);
    }
     */
    runtime.test("strcpy, strcat, strchr, strcmp, strstr, tolower, toupper, memset, memcpy", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x3C, 0x00, 0x20, 0x3B, 0x17, 0x00, 0x00,
            0x3E, 0x05, 0x00, 0x00,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x0D, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x00, 0x83,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x0D, 0x68, 0x65, 0x72, 0x65, 0x00, 0xA6,
            0x0D, 0x25, 0x73, 0x3A, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x3A, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x3A, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00,
            0x03, 0x00, 0xE0, 0x00, 0x00,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x01, 0x6C, 0xA7,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x01, 0x78, 0xA7,
            0x0D, 0x61, 0x62, 0x63, 0x00, 0x0D, 0x61, 0x43, 0x00, 0xA8,
            0x0D, 0x61, 0x63, 0x00, 0x0D, 0x61, 0x63, 0x00, 0xA8,
            0x0D, 0x61, 0x62, 0x63, 0x00, 0x0D, 0x61, 0x62, 0x00, 0xA8,
            0x0D, 0x61, 0x62, 0x63, 0x00, 0x0D, 0x61, 0x62, 0x64, 0x00, 0xA8,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x0D, 0x65, 0x6C, 0x6C, 0x00, 0xA9,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x0D, 0x65, 0x6C, 0x73, 0x00, 0xA9,
            0x01, 0x0A, 0x82,
            0x0D, 0x25, 0x63, 0x2C, 0x25, 0x63, 0x2C, 0x25, 0x63, 0x2C, 0x25, 0x63, 0x2C, 0x25, 0x63, 0x2C, 0x25, 0x63, 0x00,
            0x01, 0x43, 0xAA, 0x01, 0x63, 0xAA, 0x01, 0x3D, 0xAA,
            0x01, 0x44, 0xAB, 0x01, 0x64, 0xAB, 0x01, 0x23, 0xAB,
            0x01, 0x07, 0x82,
            0x03, 0x01, 0xE0, 0x00, 0x00, 0x01, 0x41, 0x01, 0x03, 0xAC,
            0x03, 0x04, 0xE0, 0x00, 0x00, 0x0D, 0x21, 0x6C, 0x69, 0x62, 0x00, 0x01, 0x04, 0xAD,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x01, 0x01, 0x82, 0x40),
            "",
            """[add bytes (Hello,here:57346,0:31,0,99,-1:57345,0)]
              |[render text to screen: 0b0]
              |[add bytes (c,c,=,D,D,#)]
              |[render text to screen: 0b0]
              |[add bytes (HAAA!libre)]
              |[render text to screen: 0b0]
              |""".trimMargin())

    /**
    #define A 0xe000
    #define B 0xe002

    char a[] = {0x20, 0xa1, 0x34, 0xaa, 0x3c, 0x77, 0x05};

    void main() {
        int i;
        char s[1000];
        sprintf(s, "%s,%d,%c", "hello", 1234, 65);
        printf(s);
        printf("%d,%d", CheckKey(13), CheckKey(129));
        strcpy(B, "abc");
        memmove(A, B, 4);
        printf(A);
        memmove(B, A, 4);
        printf(A);
        printf("%d", Crc16(a, 4));
        Secret(a, 7, "abc");
        for (i = 0; i < 7; i++) {
            printf("%d",a[i]);
        }
    }
     */
    runtime.test("", input, output, intArrayOf(
            0x4C, 0x41, 0x56, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x41, 0x00, 0x20, 0x07, 0x00, 0x20, 0xA1, 0x34, 0xAA, 0x3C, 0x77, 0x05,
            0x3C, 0x07, 0x20, 0x3B, 0x23, 0x00, 0x00,
            0x3E, 0xEF, 0x03, 0x00,
            0x19, 0x07, 0x00, 0x0D, 0x25, 0x73, 0x2C, 0x25, 0x64, 0x2C, 0x25, 0x63, 0x00,
            0x0D, 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x00, 0x02, 0xD2, 0x04, 0x01, 0x41, 0x01, 0x05, 0xB8,
            0x19, 0x07, 0x00, 0x01, 0x01, 0x82,
            0x0D, 0x25, 0x64, 0x2C, 0x25, 0x64, 0x00, 0x01, 0x0D, 0xBC, 0x01, 0x81, 0xBC, 0x01, 0x03, 0x82,
            0x03, 0x02, 0xE0, 0x00, 0x00, 0x0D, 0x61, 0x62, 0x63, 0x00, 0x83,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x03, 0x02, 0xE0, 0x00, 0x00, 0x01, 0x04, 0xBD,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x01, 0x01, 0x82,
            0x03, 0x02, 0xE0, 0x00, 0x00, 0x03, 0x00, 0xE0, 0x00, 0x00, 0x01, 0x04, 0xBD,
            0x03, 0x00, 0xE0, 0x00, 0x00, 0x01, 0x01, 0x82,
            0x0D, 0x25, 0x64, 0x00, 0x02, 0x00, 0x20, 0x01, 0x04, 0xBE, 0x01, 0x02, 0x82,
            0x02, 0x00, 0x20, 0x01, 0x07, 0x0D, 0x61, 0x62, 0x63, 0x00, 0xBF,
            0x03, 0x05, 0x00, 0x82, 0x00, 0x01, 0x00, 0x35, 0x38,
            0x0F, 0x05, 0x00, 0x4F, 0x07, 0x00, 0x38, 0x39, 0xDA, 0x00, 0x00,
            0x3B, 0xC9, 0x00, 0x00,
            0x03, 0x05, 0x00, 0x82, 0x00, 0x1F, 0x38, 0x3B, 0xAF, 0x00, 0x00,
            0x0D, 0x25, 0x64, 0x00, 0x0F, 0x05, 0x00, 0x07, 0x00, 0x20, 0x01, 0x02, 0x82,
            0x3B, 0xBE, 0x00, 0x00, 0x40),
            "97\n",
            """[add bytes (hello,1234,A)]
              |[render text to screen: 0b0]
              |[is key pressed: 13]
              |[get pressed key: 97]
              |[add bytes (0,97)]
              |[render text to screen: 0b0]
              |[add bytes (abc)]
              |[render text to screen: 0b0]
              |[add bytes (ababc)]
              |[render text to screen: 0b0]
              |[add bytes (24595)]
              |[render text to screen: 0b0]
              |[add bytes (65)]
              |[render text to screen: 0b0]
              |[add bytes (195)]
              |[render text to screen: 0b0]
              |[add bytes (87)]
              |[render text to screen: 0b0]
              |[add bytes (203)]
              |[render text to screen: 0b0]
              |[add bytes (94)]
              |[render text to screen: 0b0]
              |[add bytes (20)]
              |[render text to screen: 0b0]
              |[add bytes (100)]
              |[render text to screen: 0b0]
              |""".trimMargin())
}

fun main(args: Array<String>) {
    testRuntime()
    println("done")
}
