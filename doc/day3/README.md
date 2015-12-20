<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.001.jpeg" width="500px"><br>
今日は、Code PointとSurrogate Pairについて紹介したいと思います。ご質問や間違いなどのご指摘は下記のコメント欄にお書きください。
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.003.jpeg" width="500px"><br>
Code Pointとは、文字コード上で、文字につけられた番号のことです。UnicodeのCode Pointは<a href="https://ja.wikipedia.org/wiki/Unicode%E4%B8%80%E8%A6%A7%E8%A1%A8" target="_blank">WikipediaのUnicode一覧表のページ</a>から調べることができます。
ScalaのCharにはChar１つで表せない文字が存在するため、文字単位を正確に扱いたい場合は、CharではなくCode Pointを使用します。Code Pointの符号化方式のことを<a href="https://ja.wikipedia.org/wiki/UTF-32" target="_blank">UTF-32</a>と言います。
CharやStringは<a href="https://ja.wikipedia.org/wiki/UTF-16" target="_blank">UTF-16</a>であり、<a href="https://ja.wikipedia.org/wiki/%E5%9F%BA%E6%9C%AC%E5%A4%9A%E8%A8%80%E8%AA%9E%E9%9D%A2" target="_blank">BMP領域</a>ではChar１つで１文字、<a href="https://ja.wikipedia.org/wiki/%E8%BF%BD%E5%8A%A0%E9%9D%A2" target="_blank">Supplementary領域</a>ではChar２つで１文字を表現します。
なお、プログラム上で文字を扱う場合は<a href="https://ja.wikipedia.org/wiki/%E3%83%90%E3%82%A4%E3%83%88%E3%82%AA%E3%83%BC%E3%83%80%E3%83%BC%E3%83%9E%E3%83%BC%E3%82%AF" target="_blank">BOM (Byte Order Mark)</a>はつけずに、一般的には<a href="https://ja.wikipedia.org/wiki/%E3%82%A8%E3%83%B3%E3%83%87%E3%82%A3%E3%82%A2%E3%83%B3" target="_blank">Big Endian</a>で扱います。実際には使用するEndianはCPUに依存して選択されるべきですが、ScalaやJavaなど<a href="https://ja.wikipedia.org/wiki/Java%E4%BB%AE%E6%83%B3%E3%83%9E%E3%82%B7%E3%83%B3" target="_blank">JVM</a>上で動く言語ではJVMの仕様により必ずBig Endianで扱います。
***
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.004.jpeg" width="500px"><br>
Supplementary領域ではChar２つで１文字を表現する方法のことをSurrogate Pairと呼びます。ペア（２つの対）になっているCharの前方をHi Surrogate、後方をLow Surrogateと呼びます。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.005.jpeg" width="500px"><br>
BMP領域はU+0000からU+FFFFまでの領域、Supplementary領域はU+10000からU+10FFFFまでの領域で、合わせて全容量は21bitです。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.006.jpeg" width="500px"><br>
従って、Charの16bitの容量には収まりませんが、
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.007.jpeg" width="500px"><br>
Char２つの32bitの容量やInt１つの32bitの容量には十分に収まります。
***
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.008.jpeg" width="500px"><br>
これらがCode PointとSurrogate Pairの相互変換の計算式です。
***
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.002.jpeg" width="500px"><br>
今日は、この表をインデックスとして使用し、メソッドを表す個々のリンクについて説明していきます。
***
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.009.jpeg" width="500px"><br>
Code PointとSurrogate Pairの変換方法について説明します。
Character.toCodePointメソッドがSurrogate PairからCode Pointへの変換メソッドです。
```scala
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
```
Character.toCharsメソッドがCode PointからSurrogate Pairを表すChar配列、あるいはSurrogate Pairで表されないCharを１つ含む配列への変換メソッドです。
```scala
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
```
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.010.jpeg" width="500px"><br>
Code PointがいくつのCharで表されるのかを取得する方法について説明します。まずは、先ほどのように一度Charの配列に変換してしまう方法があります。
<br>
```scala
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
```
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.011.jpeg" width="500px"><br>
次に、Character.charCountメソッドを使用して、直接Code PointからいくつのCharで表されるのかを取得することができます。
```scala
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
```
最後に、Code PointがBMP領域にあるのかSupplementary領域にあるのかを調べることで、いくつのCharで表されるのかを取得することができます。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.012.jpeg" width="500px"><br>
Code PointからHigh SurrogateをCharacter.highSurrogateメソッドで取得できます。
```scala
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
```
Code PointからLow SurrogateをCharacter.lowSurrogateメソッドで取得できます。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.013.jpeg" width="500px"><br>
CharSequenceインターフェースを実装するオブジェクト、Stringオブジェクト、Char配列からCode PointにcodePointAt/codePointBeforeメソッドを使用して変換できます。
codePointAtは順方向、codePointBeforeは逆方向に解析します。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.014.jpeg" width="500px"><br>
CharSequenceインターフェースを実装するオブジェクト、Stringオブジェクト、Char配列のどのメソッドを使用するのが良いかについては、Char配列のようなLower Levelで扱うと処理が高速化し、StringオブジェクトのようなHigher Levelで扱うと処理速度は低下します。
一般的にできるだけLower Levelで処理を書くと（究極的にはJVMや<a href="https://ja.wikipedia.org/wiki/LLVM" target="_blank">LLVM</a>、<a href="https://ja.wikipedia.org/wiki/%E3%82%A2%E3%82%BB%E3%83%B3%E3%83%96%E3%83%AA%E8%A8%80%E8%AA%9E" target="_blank">アセンブリ言語</a>、<a href="https://ja.wikipedia.org/wiki/%E6%A9%9F%E6%A2%B0%E8%AA%9E" target="_blank">機械語</a>など）処理は高速化しますが、プログラム長が長くなり、プログラムを書く時間が延び、変数が増え、可読性が下がるため、バグが発生しやすく、保守性が低いプログラムになります。逆にできるだけHigher Levelで書くと、無駄にメモリを使用したり、余計なオーバヘッドが発生し、処理が遅くなりがちです。例えば、Char配列のラッパークラスのStringクラスからCharを扱うと、Stringクラスが持つ処理に不要なメソッドがメモリに乗りメモリ効率が悪く、また、Stringクラスを介してChar配列にアクセスするため、直接Char配列にアクセスするより、速度が低下します。プログラミングでは、このようなトレードオフがよく発生します。どのくらいの保守性低下を許してどのくらいの処理高速化をとるのかはケースバイケースでのプログラマによる選択が必要です。  <br><br>
Char配列からCode Pointに順方向にCharacter.codePointAtメソッドで変換できます。
```scala
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
```
```scala
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
```
CharSequenceインターフェースを実装するオブジェクトからCode Pointに順方向にCharacter.codePointAtメソッドで変換できます。
```scala
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
```
StringオブジェクトからCode Pointに順方向にString.codePointAtメソッドで変換できます。
```scala
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
```
Char配列からCode Pointに逆方向にCharacter.codePointBeforeメソッドで変換できます。
```scala
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
```
```scala
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
```
CharSequenceインターフェースを実装するオブジェクトからCode Pointに逆方向にCharacter.codePointBeforeメソッドで変換できます。
```scala
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
```
StringオブジェクトからCode Pointに逆方向にString.codePointBeforeメソッドで変換できます。
```scala
  @Test
  def testStringToCodePointInBackwardDirection(): Unit = {
    val str: String = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val index: Int = 1
    val codePoint: Int = str.codePointBefore(index)
    //NullPointerException:
    //seqがnullである場合発生
    //IndexOutOfBoundsException:
    //1 ≦ index ≦ seq.lengthに反した場合発生

    assert(codePoint == 'C')
  }
```
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.015.jpeg" width="500px"><br>
このスライドは、codePointAt/codePointBeforeメソッドが、Surrogate Pairを壊すように解析を開始・終了した場合の挙動を示します。Surrogate Pairが途中で切れてしまった場合にはSurrogate PairのCode Pointは取得できないため、観測されているSurrogateのCode Pointを返します。
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.016.jpeg" width="500px"><br>
CharSequenceインターフェースを実装したオブジェクトとStringオブジェクトとの間の相互変換について説明します。
```scala
  @Test
  def testStringToCharSequence(): Unit = {
    //cast
    val charSequence: CharSequence = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"

  }
```
StringオブジェクトはCharSequenceインターフェースを実装しているためStringオブジェクトからCharSequenceへはキャストすることができます。
```scala
  @Test
  def testCharSequenceToString(): Unit = {
    val charSequence: CharSequence = "CharSequenceは、String、StringBuilder、 StringBuffer、CharBufferなどが実装しているインターフェース"
    val str: String = charSequence.toString
    //NullPointerException:
    //charSequenceがnullである場合発生

    assert(charSequence == str)
  }
```
CharSequenceインターフェースを実装しているオブジェクトはtoStringメソッドでStringオブジェクトに変換することができます。
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.017.jpeg" width="500px"><br>
Stringオブジェクト及びCharSequenceインターフェースを実装するオブジェクトとChar配列との相互変換について説明します。
```scala
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
```
CharSequenceインターフェースを実装しているオブジェクトからChar配列へは、CharSequenceインターフェースのcharsメソッド（Java 8以降）で<a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html" target="_blank">IntStream</a>を介して変換することができます。
```scala
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
```
StringオブジェクトからChar配列へ、toCharArrayメソッドを使用して変換できます。
```scala
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
```
CharSequenceインターフェースを実装しているStringオブジェクトからChar配列へは、CharSequenceインターフェースのcharsメソッド（Java 8以降）でIntStreamを介して変換することができます。
```scala
  @Test
  def testCharArrayToString(): Unit = {
    val charArray: Array[Char] = Array(0xD842.toChar, 0xDFB7.toChar, '野', '家')
    val str = new String(charArray)

    assert(str == "𠮷野家")
  }
```
Char配列からStringオブジェクトへは、Stringクラスのコンストラクタを使用して変換することができます。
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.018.jpeg" width="500px"><br>
StringオブジェクトからChar数とCodePoint数の取得方法を説明します。
Char数とCodePoint数はStringオブジェクト内にSurrogate Pairが存在しない場合は一致します。しかし、Surrogate Pairが含まれている場合には、Char数よりCode Point数が少なくなります。Char数からCode Point数を引いた数がStringオブジェクト内に存在するSurrogate Pairの数です。
```scala
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
```
"𠮷野家"の"𠮷"はSurrogate Pairです。従って、Char数は4、Code Point数は3、Surrogate Pair数は1です。
一方で、"吉野家"の"吉"はSurrogate Pairではありません。従って、Char数は3、Code Point数は3、Surrogate Pair数は0です。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.019.jpeg" width="500px"><br>
Char数はString.lengthメソッドで取得できます。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.020.jpeg" width="500px"><br>
Code Point数はString.codePointCountメソッドで取得できます。
***
Stringオブジェクト及びCharSequenceインターフェースを実装するオブジェクトとCode Point配列との間の相互変換について説明します。
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.021.jpeg" width="500px"><br>
Code Point配列からStringオブジェクトへはStringクラスのコンストラクタで変換できます。Code Point配列からCharSequenceへは一度Stringクラスのコンストラクトを介してStringオブジェクトに変換し、CharSequenceにキャストすることで変換可能です。
```scala
  @Test
  def testCodePointArrayToString(): Unit = {
    val codePointArray: Array[Int] = Array(0x20BB7, '野', '家')
    val str: String = new String(codePointArray, 0, codePointArray.length)

    assert(str == "𠮷野家")
  }
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.022.jpeg" width="500px"><br>
Stringオブジェクト及びCharSequenceインターフェースを実装するオブジェクトからCode Point配列へはCharSequenceインターフェースのcodePointsメソッド（Java 8以降）でIntStreamを取得し、それをtoArrayメソッドで配列に変換できます。
```scala
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
```
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.023.jpeg" width="500px"><br>
codePointAt/codePointBeforeメソッドと同様に、CharacterクラスはChar配列、CharSequenceインターフェースを実装するオブジェクトに対して、Stringクラスは自身に対してoffsetByCodePointsメソッドを持っています。
```scala
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
```
```scala
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
```
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.024.jpeg" width="500px"><br>
StringCharacterIteratorは、コンストラクタにStringオブジェクトを与えることでChar単位でイテレートすることができます。StringCharacterIteratorが実装するCharacterIteratorインターフェースは順方向から解析するためのfirstメソッドとnextメソッド、そして逆方向から解析するためのlastメソッドとpreviousメソッドを保持しています。CharacterIterator.DONEでイテレータの終了を判定します。CharacterIterator.DONEはUnicode上で文字が割り当てられていない<a href="http://www.fileformat.info/info/unicode/char/ffff/index.htm" target="_blank">U+FFFF</a>です。
```scala
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
```
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.025.jpeg" width="500px"><br>
Surrogate Pairを考慮して文字列をCode Pointで扱うためには、最低でもこのスライドで示したリンクは覚えて欲しいです。
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.026.jpeg" width="500px"><br>
上記のStringオブジェクトからCode Point配列への変換は、Java 8で初めて作られたCharSequenceインターフェースのtoCodePointsメソッドによって可能となります。Java 7以前では、StringオブジェクトからどのようにするとCode Point配列が得られるのでしょうか。これに関するIBMのMasahiko Maederaさんが英語と日本語でJava言語で技術文書を公開しています。
<ul>
<li><a href="http://www.ibm.com/developerworks/library/j-unicode/" target="_blank">http://www.ibm.com/developerworks/library/j-unicode/</a>（英語）
<li><a href="https://www.ibm.com/developerworks/jp/java/library/j-unicode/" target="_blank">https://www.ibm.com/developerworks/jp/java/library/j-unicode/</a>（日本語）
<li><a href="https://www.ibm.com/developerworks/jp/ysl/library/java/j-unicode_surrogate/" target="_blank">https://www.ibm.com/developerworks/jp/ysl/library/java/j-unicode_surrogate/</a>（日本語）
</ul>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day3/string_course.027.jpeg" width="500px"><br>
IBMのMasahiko Maederaさんの技術文書で最も高速な変換は、StringをChar配列に変換して、Char配列からCode Point配列にCharacter.toCodePointメソッドを使用して変換する方法でした。参考までに、サンプルコードには、これをScalaで書いたものを載せました。
```scala
  @Test
  def testCharSequenceToCodePointArrayUnderJava8(): Unit = {
    val str: String = "𠮷野家"
    val codePointArray: Array[Int] = toCodePoints(str)
    //NullPointerException
    //strがnullである場合

    assert(codePointArray sameElements Array(0x20BB7, '野', '家'))
  }

  private def toCodePoints(charSequence: CharSequence): Array[Int] = {
    if (charSequence == null) {
      throw new NullPointerException
    }

    val charArray: Array[Char] =
      {
        charSequence match {
          case str: String =>
            str
          case otherwise =>
            otherwise.toString
        }
      }.toCharArray
    val length: Int = charArray.length
    var surrogatePairCount: Int = 0
    var isSkipped: Boolean = false
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
    val codePoints: Array[Int] = new Array[Int](length - surrogatePairCount)
    var j: Int = 0
    for (i <- 0 until length) {
      if (isSkipped) {
        isSkipped = false
      } else {
        val currentChar = charArray(i)
        if (Character.isHighSurrogate(currentChar) && i + 1 < length) {
          val nextChar = charArray(i + 1)
          if (Character.isLowSurrogate(nextChar)) {
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
```
