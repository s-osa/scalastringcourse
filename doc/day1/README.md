<p>日々学んでいることをVlogとしてまとめていこうと思います。 ご質問やご指摘は下記のコメント欄にお書きください。</p>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day1/string_course.001.jpeg" width="500px"><p>今回から「Scalaの文字列処理」についてまとめていこうと思います。 初日は、「Scalaの文字列処理」でどんな内容に触れるのかOverviewとコーディングの環境設定を示します。</p>
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day1/string_course.002.jpeg" width="500px">
<p>まずは、リテラルについて紹介します。 Scalaには文字や文字列に関するリテラルがたくさんあります。 文字リテラル、文字列リテラル、生文字リテラルなどです。 エスケープシーケンスやUnicodeシーケンスなどについても触れます。</p>

<p>次に、Code PointやSurrogate Pairについて紹介します。 Scalaで適切に文字を扱うためには、１文字に対してChar型１つでは不十分な場合があります。 適切に文字を扱うためにはCode PointやSurrogate Pairについて学び、StringとCode Point配列への相互変換方法やStringのCode Point数を取得する方法などについて紹介します。</p>

<p>型変換では、Boolean、Int、FloatといったJavaでいうプリミティブ型にあたるScalaの型とStringとの相互変換方法について紹介します。</p>

<p>ミュータビリティでは、immutableクラスであるStringを扱うmutableクラス、StringBuilder、StringBufferなどについて紹介します。</p>

フォーマットでは、文字列のフォーマットやテンプレートに値を埋める処理について紹介します。ScalaだけではなくC言語など他の言語で`printf`という関数・メソッドが存在しますが、もし`printf`を使ったことがある方は、第一引数に"%s"や"%d"、"%f"といった記号を含めた文字列を渡しておき、第二引数以降に対応する値や変数を渡す機構に馴染みがあるかもしれません。この機構のことをフォーマットと呼び、`printf`の"f"はフォーマットを意味します。

<p>正規表現は、文字列が特定のパターンにマッチしたかどうかを調べたり、マッチした場合、その文字列を別の文字列に削除する、置き換える、あるいは取り出すといった処理が簡単かつ柔軟に書くことができます。正規表現の使い方について紹介します。</p>

<p>文字列操作では、Stringクラスのメソッドやフォーマット、正規表現など文字列を操作する方法を処理の目的別にどのようなときにどの方法を使用するのかまとめます。</p>

<p>文字の正規化ですが、日本語のテキストを処理する際、例えばカタカナの半角の「ｶ」と全角の「カ」、あるいは「ガ」と「カ」＋濁点「゛」のように一般的に同じ文字を指すがプログラム上では一致しない文字が存在します。このような字種による揺れが存在すると、文字列の一致を見たいときに問題が発生します。文字列の一致を見る前に、前処理として一方の表記に揃えておくことで不一致の問題を防ぐことができます。これを文字の正規化と呼び、具体的な方法について紹介します。</p>

<p>最後にオプションというScalaにある<a href="http://docs.oracle.com/javase/jp/8/api/java/lang/NullPointerException.html" target="_blank">NullPointerException</a>を排除するために役に立つ機構について紹介します。</p>
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day1/string_course.003.jpeg" width="500px">
<p>本動画では、開発環境としてOracle Java 8とScala 2.11を使用します。Scala Xml 1.0.5も使用します。Googleの<a href="https://github.com/google/guava" target="_blank">Guava</a>やApache <a href="https://commons.apache.org/proper/commons-lang/" target="_blank">Commons Lang</a>の<a href="https://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringUtils.html" target="_blank">StringUtils</a>には文字列処理に便利なメソッドが存在しますが、本動画では紹介しません。</p>
<h3>メモ：API</h3>
Oracle Java 8とScala 2.11とScala Xml 1.0.5のAPIのドキュメントは次です。
<ol>
<li>Java(tm) Platform, Standard Edition 8のAPI仕様<br><a href="http://docs.oracle.com/javase/jp/8/api/" target="_blank">http://docs.oracle.com/javase/jp/8/api/</a>
<li>Scala Standard Library 2.11.7 API<br><a href="http://www.scala-lang.org/api/2.11.7/" target="_blank">http://www.scala-lang.org/api/2.11.7/</a>
<li>Scala module XML API<br><a href="http://www.scala-lang.org/api/2.11.7/scala-xml/" target="_blank">http://www.scala-lang.org/api/2.11.7/scala-xml/</a>
<!--<li><a href="" target="_blank"></a>-->
</ol>
<h3>メモ：Style</h3>
言語によって慣習的に使われるコードの書き方の規約のことを、C言語ではCoding standard、JavaではCode Conventions、ScalaではStyleと言います。自分にも他人にも読みやすいプログラムを書くために一読しておくといいと思います。C/C++とJava/Scalaの<a href="https://ja.wikipedia.org/wiki/%E5%91%BD%E5%90%8D%E8%A6%8F%E5%89%87_(%E3%83%97%E3%83%AD%E3%82%B0%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B0)" target="_blank">命名規則</a>を比べてみると<a href="https://ja.wikipedia.org/wiki/%E3%82%AD%E3%83%A3%E3%83%A1%E3%83%AB%E3%82%B1%E3%83%BC%E3%82%B9" target="_blank">スネークケースなのかキャメルケースなのか</a>といった宗教色の違いが見えてくるはずです。
<ol>
<li>C Coding Standard<br><a href="https://users.ece.cmu.edu/~eno/coding/CCodingStandard.html" target="_blank">https://users.ece.cmu.edu/~eno/coding/CCodingStandard.html</a>
<li>C++ Coding Standard<br><a href="http://www.possibility.com/Cpp/CppCodingStandard.html" target="_blank">http://www.possibility.com/Cpp/CppCodingStandard.html</a>
<li>Code Conventions for the Java Programming Language<br><a href="http://www.oracle.com/technetwork/java/index-135089.html" target="_blank">http://www.oracle.com/technetwork/java/index-135089.html</a></li>
<li>Scala Style Guide<br><a href="http://docs.scala-lang.org/style/" target="_blank">http://docs.scala-lang.org/style/</a>
</ol>
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day1/string_course.004.jpeg" width="500px">
<br>
外部ライブラリの依存関係管理のために<a href="http://www.scala-sbt.org/index.html" target="_blank">SBT 0.13.9</a>を使用します。サンプルコードは出力結果が確認しやすいように基本的にすべてテストコードの形式で提供します。テストコードを書くために外部ライブラリとして<a href="http://junit.org/" target="_blank">JUnit 4.12</a>と<a href="http://www.scalatest.org/" taget="_blank">ScalaTest 2.2.4</a>を使用します。
<br>
<h3>メモ：IDE</h3>
サンプルコードを作成するために、IDEとして<a href="https://www.jetbrains.com/idea/" target="_blank">IntelliJ IDEA</a>とIntelliJ IDEAのプラグインとしてScalaとSBTと.gitignoreのプラグインを使用しています。
<h3>メモ：サンプルコードの文字コード</h3>
サンプルコードのファイルのエンコーディングはUTF-8ですので、システムプロパティの`System.getProperty("file.encoding")`がUTF-8になるように設定しておいてください。JVM始動時に指定する必要があるため`System.setProperty("file.encoding", "UTF-8")`では設定できませんので、JVMオプションで`-Dfile.encoding=UTF-8`を渡すのがいいかと思います。
