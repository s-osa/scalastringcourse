package day3

import java.text.{CharacterIterator, StringCharacterIterator}

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

/**
  * @author ynupc
  *         Created on 2015/11/25
  */
class Day3TestSuite extends AssertionsForJUnit {
  @Test
  def testSurrogatePairToCodePoint(): Unit = {
    val high: Char = 0xD842
    val low : Char = 0xDFB7
    assert(Character.isHighSurrogate(high))
    assert(Character.isLowSurrogate(low))
    if (Character.isHighSurrogate(high) && Character.isLowSurrogate(low)) {
      val codePoint: Int = Character.toCodePoint(high, low)

      assert(codePoint == 0x20BB7)
    }
  }

  @Test
  def testCodePointToSurrogatePair1(): Unit = {
    val codePoint: Int = 0x20BB7
    assert(Character.isValidCodePoint(codePoint))
    if (Character.isValidCodePoint(codePoint)) {
      val charArray: Array[Char] = Character.toChars(codePoint)
      //IllegalArgumentException:
      //codePointが有効なUnicodeコードポイントではない場合発生

      assert(charArray.length == 2)
      assert(charArray.head == 0xD842)
      assert(charArray(1) == 0xDFB7)
    }
  }

  @Test
  def testCodePointToSurrogatePair2(): Unit = {
    val codePoint: Int = 0x20BB7
    val dst: Array[Char] = new Array(16)
    val index: Int = 8
    assert(Character.isValidCodePoint(codePoint))
    if (Character.isValidCodePoint(codePoint)) {
      //dst(index)またはdst(index)とdst(index + 1)に変換されたCharを代入し、代入したCharの個数を返す
      val size = Character.toChars(codePoint, dst, index)
      //IllegalArgumentException:
      //codePointが有効なUnicodeコードポイントでない場合発生
      //NullPointerException:
      //dstがnullの場合発生
      //IndexOutOfBoundsException:
      //codePointがBMP領域なら0 ≦ dstIndex < dst.length、Supplementary領域なら0 ≦ dstIndex < dst.length - 1に反した場合発生

      assert(size == 2)
      assert(dst(8) == 0xD842)
      assert(dst(9) == 0xDFB7)
    }
  }

  @Test
  def testCodePointToNumOfChars1(): Unit = {
    val codePoint: Int = 0x20BB7
    assert(Character.isValidCodePoint(codePoint))
    if (Character.isValidCodePoint(codePoint)) {
      val charArray: Array[Char] = Character.toChars(codePoint)
      val length: Int = charArray.length
      //IllegalArgumentException:
      //codePointが有効なUnicodeコードポイントではない場合発生

      assert(length == 2)
    }
  }

  @Test
  def testCodePointToNumOfChars2(): Unit = {
    val codePoint: Int = 0x20BB7
    val countOpt: Option[Int] =
      if (Character.isValidCodePoint(codePoint)) {
        Option(Character.charCount(codePoint))}
      else {
        None
      }

    assert(countOpt.nonEmpty)
    if (countOpt.nonEmpty) {
      assert(countOpt.get == 2)
    }
  }

  @Test
  def testCodePointToNumOfChars3(): Unit = {
    val codePoint: Int = 0x20BB7
    val countOpt: Option[Int] =
      if (Character.isBmpCodePoint(codePoint)) {
        Option(1)
      } else if (Character.isSupplementaryCodePoint(codePoint)) {
        Option(2)
      } else {
        None
      }

    assert(countOpt.nonEmpty)
    if (countOpt.nonEmpty) {
      assert(countOpt.get == 2)
    }
  }

  @Test
  def testCodePointToHighSurrogate(): Unit = {
    val codePoint: Int = 0x20BB7
    val high: Char = Character.highSurrogate(codePoint)

    assert(Character.isSurrogate(high))
    if (Character.isSurrogate(high)) {
      assert(Character.isHighSurrogate(high))
      if (Character.isHighSurrogate(high)) {
        assert(high == 0xD842)
      }
    }
  }

  @Test
  def testCodePointToLowSurrogate(): Unit = {
    val codePoint: Int = 0x20BB7
    val low: Char = Character.lowSurrogate(codePoint)

    assert(Character.isSurrogate(low))
    if (Character.isSurrogate(low)) {
      assert(Character.isLowSurrogate(low))
      if (Character.isLowSurrogate(low)) {
        assert(low == 0xDFB7)
      }
    }
  }

  @Test
  def testCharArrayElementToCodePointInForwardDirection1(): Unit = {
    val charArray: Array[Char] = Array[Char](0xD842, 0xDFB7)
    val index: Int = 0
    val codePoint: Int = Character.codePointAt(charArray, index)
    //NullPointerException:
    //charArrayがnullの場合発生
    //IndexOutOfBoundsException:
    //0 ≦ index < charArray.lengthに反した場合発生

    assert(codePoint == 0x20BB7)
  }

  @Test
  def testCharArrayElementToCodePointInForwardDirection2(): Unit = {
    val charArray: Array[Char] = Array[Char](0xD842, 0xDFB7, 'C')
    var index: Int = 0
    var limit: Int = 1
    var codePoint: Int = Character.codePointAt(charArray, index, limit)
    //NullPointerException:
    //charArrayがnullである場合発生
    //IndexOutOfBoundsException:
    //0 ≦ index < limit < charArray.lengthに反した場合発生

    assert(codePoint == 0xD842)

    index = 0
    limit = 2
    codePoint = Character.codePointAt(charArray, index, limit)

    assert(codePoint == 0x20BB7)

    index = 1
    limit = 3
    codePoint = Character.codePointAt(charArray, index, limit)

    assert(codePoint == 0xDFB7)
  }

  @Test
  def testCharSequenceToCodePointInForwardDirection(): Unit = {
    val charSequence: CharSequence = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val index: Int = 0
    val codePoint: Int = Character.codePointAt(charSequence, index)
    //NullPointerException:
    //charSequenceがnullである場合発生
    //IndexOutOfBoundsException:
    //0 ≦ index < seq.lengthに反した場合発生

    assert(codePoint == 'C')
  }

  @Test
  def testStringToCodePointInForwardDirection(): Unit = {
    val str: String = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val index: Int = 0
    val codePoint: Int = str.codePointAt(index)
    //NullPointerException:
    //strがnullである場合発生
    //IndexOutOfBoundsException:
    //0 ≦ index < str.lengthに反した場合発生

    assert(codePoint == 'C')
  }

  @Test
  def testCharArrayElementToCodePointInBackwardDirection1(): Unit = {
    val charArray: Array[Char] = Array[Char](0xD842, 0xDFB7)
    val index: Int = 1
    val codePoint: Int = Character.codePointBefore(charArray, index)
    //NullPointerException:
    //charArrayがnullの場合発生
    //IndexOutOfBoundsException:
    //1 ≦ index ≦ charArray.lengthに反した場合発生

    assert(codePoint == 0xD842)
  }

  @Test
  def testCharArrayElementToCodePointInBackwardDirection2(): Unit = {
    val charArray: Array[Char] = Array[Char]('C', 0xD842, 0xDFB7)
    var index: Int = 3
    var start: Int = 2
    var codePoint: Int = Character.codePointBefore(charArray, index, start)
    //NullPointerException:
    //charArrayがnullである場合発生
    //IndexOutOfBoundsException:
    //0 ≦ start < index ≦ charArray.lengthに反した場合発生

    assert(codePoint == 0xDFB7)

    index = 3
    start = 1
    codePoint = Character.codePointBefore(charArray, index, start)

    assert(codePoint == 0x20BB7)

    index = 2
    start = 0
    codePoint = Character.codePointBefore(charArray, index, start)

    assert(codePoint == 0xD842)
  }

  @Test
  def testCharSequenceToCodePointInBackwardDirection(): Unit = {
    val charSequence: CharSequence = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val index: Int = 1
    val codePoint: Int = Character.codePointBefore(charSequence, index)
    //NullPointerException:
    //charSequenceがnullである場合発生
    //IndexOutOfBoundsException:
    //1 ≦ index ≦ seq.lengthに反した場合発生

    assert(codePoint == 'C')
  }

  @Test
  def testStringToCodePointInBackwardDirection(): Unit = {
    val str: String = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val index: Int = 1
    val codePoint: Int = str.codePointBefore(index)
    //NullPointerException:
    //strがnullである場合発生
    //IndexOutOfBoundsException:
    //1 ≦ index ≦ str.lengthに反した場合発生

    assert(codePoint == 'C')
  }

  @Test
  def testStringToCharSequence(): Unit = {
    //cast
    val charSequence: CharSequence = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"

  }

  @Test
  def testCharSequenceToString(): Unit = {
    val charSequence: CharSequence = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val str: String = charSequence.toString
    //NullPointerException:
    //charSequenceがnullである場合発生

    assert(charSequence == str)
  }

  @Test
  def testCharSequenceToCharArray(): Unit = {
    val charSequence: CharSequence = "𠮷野家"
    val charArray: Array[Char] = charSequence.chars.toArray.map(_.toChar)
    //NullPointerException:
    //charSequenceがnullである場合発生

    assert(charArray.length == 4)
    assert(charArray.head == 0xD842)
    assert(charArray(1) == 0xDFB7)
    assert(charArray(2) == '野')
    assert(charArray.last == '家')
  }

  @Test
  def testStringToCharArray1(): Unit = {
    val str: String = "𠮷野家"
    val charArray: Array[Char] = str.toCharArray
    //NullPointerException:
    //strがnullである場合発生

    assert(charArray.length == 4)
    assert(charArray.head == 0xD842)
    assert(charArray(1) == 0xDFB7)
    assert(charArray(2) == '野')
    assert(charArray.last == '家')
  }

  @Test
  def testStringToCharArray2(): Unit = {
    val str: String = "𠮷野家"
    val charArray: Array[Char] = str.chars.toArray.map(_.toChar)
    //NullPointerException:
    //strがnullである場合発生

    assert(charArray.length == 4)
    assert(charArray.head == 0xD842)
    assert(charArray(1) == 0xDFB7)
    assert(charArray(2) == '野')
    assert(charArray.last == '家')
  }

  @Test
  def testCharArrayToString(): Unit = {
    val charArray: Array[Char] = Array(0xD842.toChar, 0xDFB7.toChar, '野', '家')
    val str: String = new String(charArray)

    assert(str == "𠮷野家")
  }

  @Test
  def testNumOfCharactersAndChars(): Unit = {
    val strWithSurrogatePair: String = "𠮷野家"
    val strWithoutSurrogatePair: String = "吉野家"

    val numOfCharsOfStrWithSurrogatePair: Int = strWithSurrogatePair.length
    val numOfCharsOfStrWithoutSurrogatePair: Int = strWithoutSurrogatePair.length

    assert(numOfCharsOfStrWithSurrogatePair == 4)
    assert(numOfCharsOfStrWithoutSurrogatePair == 3)
    assert(numOfCharsOfStrWithoutSurrogatePair < numOfCharsOfStrWithSurrogatePair)

    val numOfCharactersOfStrWithSurrogatePair: Int = strWithSurrogatePair.codePointCount(0, strWithSurrogatePair.length)
    val numOfCharactersOfStrWithoutSurrogatePair: Int = strWithoutSurrogatePair.codePointCount(0, strWithoutSurrogatePair.length)

    assert(numOfCharactersOfStrWithSurrogatePair == 3)
    assert(numOfCharactersOfStrWithoutSurrogatePair == 3)
    assert(numOfCharactersOfStrWithSurrogatePair == numOfCharactersOfStrWithoutSurrogatePair)
  }

  @Test
  def testCodePointArrayToString(): Unit = {
    val codePointArray: Array[Int] = Array(0x20BB7, '野', '家')
    val str: String = new String(codePointArray, 0, codePointArray.length)

    assert(str == "𠮷野家")
  }

  @Test
  def testCharSequenceToCodePointArray(): Unit = {
    val charSequence: CharSequence = "𠮷野家"
    val codePointArray: Array[Int] = charSequence.codePoints().toArray
    //NullPointerException:
    //charSequenceがnullである場合発生

    assert(codePointArray.length == 3)
    assert(codePointArray.head == 0x20BB7)
    assert(codePointArray(1) == '野')
    assert(codePointArray.last == '家')
  }

  @Test
  def testStringToCodePointArray(): Unit = {
    val str: String = "𠮷野家"
    val codePointArray: Array[Int] = str.codePoints().toArray
    //NullPointerException:
    //strがnullである場合発生

    assert(codePointArray.length == 3)
    assert(codePointArray.head == 0x20BB7)
    assert(codePointArray(1) == '野')
    assert(codePointArray.last == '家')
  }

  @Test
  def testOffsetByCodePoints1(): Unit = {
    val charArray: Array[Char] = Array(0xD842.toChar, 0xDFB7.toChar, '野', '家')
    val start: Int = 0
    val count: Int = charArray.length
    val index: Int = 0
    val numOfCodePoints: Int = 1
    val indexPlusOffsetByCodePoints: Int = Character.offsetByCodePoints(charArray, start, count, index, numOfCodePoints)
    //NullPointerException
    //charArrayがnullである場合

    //IndexOutOfBoundsException
    //１．startが負数の場合
    //２．countが負数の場合
    //３．indexがstart以上start + count以下に収まらない場合
    //４．charArray.length以下に収まらない場合
    //５．numOfCodePointsが0より大きく、
    //indexから始まりstart + count - 1で
    //終わる範囲のCode Point数が
    //numOfCodePointsより少ない場合
    //６．numOfCodePointsが0未満で
    //startから始まりindex - 1で終わる範囲のCode Point数が
    //numOfCodePointsの絶対値より少ない場合

    assert(indexPlusOffsetByCodePoints == 2)
  }

  @Test
  def testOffsetByCodePoints2(): Unit = {
    val charSequence: CharSequence = "𠮷野家"
    val index: Int = 0
    val numOfCodePoints: Int = 1
    val indexPlusOffsetByCodePoints: Int = Character.offsetByCodePoints(charSequence, index, numOfCodePoints)
    //NullPointerException
    //charSequenceがnullである場合

    //IndexOutOfBoundsException
    //１．indexが0以上charSequence.length()以下に収まらない場合
    //２．numOfCodePointsが0より大きく、
    //indexから始まるサブシーケンスの持つCode Point数が
    //numOfCodePoints未満の場合
    //３．numOfCodePointsが0未満で
    //indexの前のサブシーケンスの持つ値が
    //numCodePointsの絶対値よりも小さい場合

    assert(indexPlusOffsetByCodePoints == 2)
  }

  @Test
  def testOffsetByCodePoints3(): Unit = {
    val str: String = "𠮷野家"
    val index: Int = 0
    val numOfCodePoints: Int = 1
    val indexPlusOffsetByCodePoints: Int = str.offsetByCodePoints(index, numOfCodePoints)
    //NullPointerException
    //strがnullである場合

    //IndexOutOfBoundsException
    //１．indexが0以上str.length以下に収まらない場合
    //２．numOfCodePointsが0より大きく、
    //indexから始まるサブシーケンスの持つCode Point数が
    //numOfCodePoints未満の場合
    //３．numOfCodePointsが0未満で
    //indexの前のサブシーケンスの持つ値が
    //numCodePointsの絶対値よりも小さい場合

    assert(indexPlusOffsetByCodePoints == 2)
  }

  @Test
  def testStringCharacterForwardIterator(): Unit = {
    val str: String = "𠮷野家"
    val ghost: Char = '彁'
    val builder: StringBuilder = new StringBuilder(str.length)
    val iterator: CharacterIterator = new StringCharacterIterator(str)
    var char: Char = iterator.first()
    while (char != CharacterIterator.DONE) {
      if (Character.isHighSurrogate(char)) {
        char = iterator.next()
        if (Character.isLowSurrogate(char)) {
          builder.append(ghost)
        }
      } else {
        builder.append(char)
      }
      char = iterator.next()
    }

    assert(builder.result() == "彁野家")
  }

  @Test
  def testStringCharacterBackwardIterator(): Unit = {
    val str: String = "𠮷野家"
    val ghost: Char = '彁'
    val builder: StringBuilder = new StringBuilder(str.length)
    val iterator: CharacterIterator = new StringCharacterIterator(str)
    var char: Char = iterator.last()
    while (char != CharacterIterator.DONE) {
      if (Character.isLowSurrogate(char)) {
        char = iterator.previous()
        if (Character.isHighSurrogate(char)) {
          builder.append(ghost)
        }
      } else {
        builder.append(char)
      }
      char = iterator.previous()
    }

    assert(builder.result() == "家野彁")
  }

  @Test
  def testStringToCodePointArrayUnderJava8(): Unit = {
    val str: String = "𠮷野家"
    val codePointArray = toCodePoints(str)
    //NullPointerException
    //strがnullである場合

    assert(codePointArray sameElements Array(0x20BB7, '野', '家'))
  }

  private def toCodePoints(charSequence: CharSequence): Array[Int] = {
    if (charSequence == null) {
      throw new NullPointerException
    }

    val charArray = charSequence.toString.toCharArray
    val length = charArray.length
    var surrogatePairCount = 0
    var isSkipped = false
    for (i <- 0 until length) {
      if (isSkipped) {
        isSkipped = false
      } else {
        if (0 < i && Character.isSurrogatePair(charArray(i - 1), charArray(i))) {
          surrogatePairCount += 1
          isSkipped = true
        }
      }
    }
    isSkipped = false
    val codePoints = new Array[Int](length - surrogatePairCount)
    var j = 0
    for (i <- 0 until length) {
      if (isSkipped) {
        isSkipped = false
      } else {
        val currentChar = charArray(i)
        if (Character.isHighSurrogate(currentChar) && i + 1 < length) {
          val nextChar = charArray(i + 1)
          if (Character isLowSurrogate nextChar) {
            codePoints(j) = Character.toCodePoint(currentChar, nextChar)
            j += 1
            isSkipped = true
          }
        }
        if (!isSkipped) {
          codePoints(j) = currentChar
          j += 1
        }
      }
    }
    codePoints
  }
}
