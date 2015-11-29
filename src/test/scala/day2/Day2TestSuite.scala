package day2

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

/**
  * @author ynupc
  *         Created on 2015/11/25
  */
class Day2TestSuite extends AssertionsForJUnit {
  @Test
  def testLiteral(): Unit = {
    //空リテラル
    //Java:
    //Object n = null;
    //Scala:
    val n: Object = null

    //文字リテラル
    //Java:
    //char c = ‘A’;
    //Scala:
    val c: Char = 'A'

    //文字列リテラル
    //Java:
    //String s = "AB¥¥C¥nあいう";
    //Scala:
    val s1: String = "AB\\C\nあいう"

    //生文字リテラル
    //Java:
    //なし
    //Scala:
    val s2: String = """AB\C
あいう"""
  }

  @Test
  def testRawStringLiteral(): Unit = {
    val sStringLiteral: String = "AB\\C\nあいう"

    val sRawStringLiteral: String = """AB\C
あいう"""

    val waveDashStringLiteral: String = "\u301c"

    val waveDashRawStringLiteral: String = """\u301c"""

    assert(sStringLiteral.toString == sRawStringLiteral)
    assert(waveDashStringLiteral == waveDashRawStringLiteral)
  }

  @Test
  def testStripMargin(): Unit = {
    val s: String = """AB\C
あいう
イロハ"""

    val s1: String = """AB\C
                       |あいう
                       |イロハ""".stripMargin

    val s2: String = """AB\C
                       %あいう
                       %イロハ""".stripMargin('%')

    assert(s == s1)
    assert(s == s2)
  }

  @Test
  def testSInterpolation(): Unit = {
    val universe = "宇宙"
    val result = s"生命、$universe、そして${s"万物についての${"究極の"}疑問"}の答えは、${21 + 21}"

    assert(result == "生命、宇宙、そして万物についての究極の疑問の答えは、42")
  }

  @Test
  def testFInterpolation(): Unit = {
    val result = f"サイボーグ${9}%03dVSデビルマン"

    assert(result == "サイボーグ009VSデビルマン")
  }

  @Test
  def testRawInterpolation(): Unit = {
    val sStringLiteral:    String = "AB\\C\\nあいう"
    val sRawInterpolation: String = raw"AB\C\nあいう"
    val sRawLiteral:       String = """AB\C\nあいう"""

    assert(sStringLiteral == sRawInterpolation)
    assert(sRawInterpolation == sRawLiteral)
    assert(sStringLiteral == sRawLiteral)

    val waveDashStringLiteral:   String = "\u301c"
    val waveDashRawIntepolation: String = raw"\u301c"
    val waveDashRawLiteral:      String = """\u301c"""

    assert(waveDashStringLiteral == waveDashRawIntepolation)
    assert(waveDashRawIntepolation == waveDashRawLiteral)
    assert(waveDashStringLiteral == waveDashRawLiteral)
  }

  @Test
  def testDifferenceBetweenRawInterpolationAndRawStringLiteral(): Unit = {
    //val newLineRawInterpolation: String = raw"改
//行"
    val newLineRawStringLiteral: String = """改
行""".stripMargin

    //val doubleQuotationRawInterpolation: String = raw"ダブルクォーテーション「"」"
    val doubleQuotationRawStringLiteral: String = """ダブルクォーテーション「"」"""
  }

  @Test
  def testStringContext(): Unit = {
    assert(Day2TestStringContext.test() == "{ name: 名前, id: ID }")
  }

  @Test
  def testEscapeSequence(): Unit = {
    //バックスペース
    val backSpace = '\b'
    //改ページ
    val formFeed = '\f'
    //改行
    val newLine = '\n'
    //復帰
    val carriageReturn = '\r'
    //水平タブ
    val horizontalTab = '\t'
    //バックスラッシュ
    val backSlash = '\\'
    //シングルクォーテーション
    val singleQuotation = '\''
    //ダブルクォーテーション
    val doubleQuotation = '\"'
    //８進数
    val octalNumberOfZero = '\000'
    val octalNumberOfTwoHundredFiftyFive = '\377'

    assert(octalNumberOfZero == 0)
    assert(octalNumberOfTwoHundredFiftyFive == 255)
  }

  @Test
  def testUnicodeSequence(): Unit = {
    //同上記号「〃」
    val dojo1 = '\u3003'

    assert(dojo1 == '〃')

    //漢字連続記号「々」
    val kanjiRenzoku = '\u3005'

    assert(kanjiRenzoku == '々')

    //しめ記号「〆」
    val shime = '\u3006'

    assert(shime == '〆')

    //漢数字ゼロ「〇」
    val zero = '\u3007'

    assert(zero == '〇')

    //波ダッシュ「〜」
    val waveDash = '\u301C'

    assert(waveDash == '〜')

    //中点「・」
    val chuten = '\u30FB'

    assert(chuten == '・')

    //同上記号「仝」
    val dojo2 = '\u4EDD'

    assert(dojo2 == '仝')

    //幽霊文字「妛」
    val yurei1 = '\u599B'

    assert(yurei1 == '妛')

    //幽霊文字「彁」
    val yurei2 = '\u5F41'

    assert(yurei2 == '彁')

    //全角チルダ「～」
    val fullWidthTilde = '\uFF5E'

    assert(fullWidthTilde == '～')

    //𠮷野家の「𠮷」
    val yoshi = "\uD842\uDFB7"

    assert(yoshi == "𠮷")
  }


}

object Day2TestStringContext {
  def test(): String = {
    val name = "名前"
    val id = "ID"
    val x: JSONObject = json"{ name: $name, id: $id }"
    x.toString
  }

  class JSONObject(jsonText: String) {
    override def toString: String = jsonText
  }

  implicit class JsonHelper(val sc: StringContext) extends AnyVal {
    def parseJson(charSequence: CharSequence): JSONObject = {
      new JSONObject(charSequence.toString)
    }

    def json(args: Any*): JSONObject = {
      val strings = sc.parts.iterator
      val expressions = args.iterator
      val buf = new StringBuffer(strings.next)
      while (strings.hasNext) {
        buf.append(expressions.next).
          append(strings.next)
      }
      parseJson(buf)
    }
  }
}

