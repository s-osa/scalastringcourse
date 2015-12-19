<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.001.jpeg" width="500px">
<p>今日は、リテラルについて紹介したいと思います。ご質問や間違いなどのご指摘は下記のコメント欄にお書きください。</p>
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.002.jpeg" width="500px">
<br>
リテラルとは、プログラム中に値を直接記述する表現、あるいはその値そのもののことです。Javaでは、プリミティブ型とオブジェクト型、値型と参照型という言い方もありますが、int, float, booleanのようなプリミティブ型をプログラム中に記述する際に、`int i = 0;`のように代入する値0をリテラルで書きます。主にプリミティブ型をリテラルで表記しますが、ダブルクォーテーションで囲われた文字列やnullのような特殊なオブジェクト型もリテラルが存在します。Scalaは全ての型がオブジェクト型でありプリミティブ型は存在しませんが、Javaに存在するリテラルはScalaでも同様に存在します。プリミティブ型はスタック領域に保持され、オブジェクト型、は参照はスタック領域に、インスタンスはヒープ領域に保持されます。Stringクラスの<a href="http://docs.oracle.com/javase/jp/8/api/java/lang/String.html#intern--" target="_blank">internメソッド</a>を使用した場合のインスタンスは、Java 6まではPermanent領域に保持され、Java 7からはヒープ領域に保持されるようになりました。なお、本コースではStringクラスのinternメソッドもそれに類するScalaの<a href="http://www.scala-lang.org/api/current/index.html#scala.Symbol" target="_blank">Symbolクラス</a>やSymbolリテラルについても取り上げません。
<h3>メモ：非ヒープ領域によるOutOfMemoryError</h3>
static変数はJava 7までは非ヒープ領域であるPermanent領域に保持されますが、Java 8からPermanent領域が廃止され、static変数はヒープ領域に保持されるようになりました。Permanent領域の他の機能は非ヒープ領域であるMetaspace領域が引き継ぎました。OutOfMemoryErrorをはいてシステムが停止した場合、ヒープ領域とPermanent領域のどちらがいっぱいになったのか調べるなんてことを経験したことがあるかもしれませんが、容量の上限値の初期値が小さいPermanent領域が廃止され、容量の上限値の初期値が64bitプロセッサが取り扱えるメモリの上限値に設定されているMetaspace領域に移行したことで非ヒープ領域からのOutOfMemoryErrorの可能性をあまり意識しなくて済むようになりました。
<h3>メモ：OutOfMemoryErrorやStackOverflowErrorの対処法</h3>
<p><a href="http://docs.oracle.com/javase/jp/8/api/java/lang/OutOfMemoryError.html" target="_blank">OutOfMemoryError</a>や<a href="http://docs.oracle.com/javase/jp/8/api/java/lang/StackOverflowError.html" target="_blank">StackOverflowError</a>を回避するには、大まかには（１）プログラムで使用するメモリ容量を減らすか、（２）物理的に割り当てる容量を変更するかの２つあります。</p>
<h4>（１）プログラム上で使用するメモリ容量を減らす</h4>
<p>プログラム上で使用するメモリ容量を減らすためには、どの処理がどのくらいメモリを使用しているのかを探る必要があります。そのためには次のようなツールがヒントになるかもしれません。</p>
<p><a href="http://docs.oracle.com/javase/jp/8/api/java/lang/Runtime.html" target="_blank">Runtimeクラス</a>や<a href="http://docs.oracle.com/javase/jp/8/api/java/lang/management/MemoryUsage.html" target="_blank">MemoryUsageクラス</a>を使用して、メモリの使用状況をモニターする方法があります。</p>
<p>JVMオプション (<a href="http://docs.oracle.com/javase/jp/8/docs/technotes/tools/windows/java.html" target="_blank">Windows</a>, <a href="http://docs.oracle.com/javase/jp/8/docs/technotes/tools/unix/java.html">Unix</a>）で-verbose:gc, -Xloggc:filenameや-XX:+PrintGCDetailsを与えることで、OutOfMemoryErrorの原因になるFull GCの発動回数をモニタし、JVMオプションで-XX:+PrintHeapAtGCでGC発動前後のヒープ領域の使用状況をモニタする方法があります。</p>
<p>.classファイルをjavapコマンド(<a href="http://docs.oracle.com/javase/jp/8/docs/technotes/tools/windows/javap.html" target="_blank">Windows</a>, <a href="http://docs.oracle.com/javase/jp/8/docs/technotes/tools/unix/javap.html">Unix</a>)で逆アセンブリをしてどのようにメモリが使用されているか調べる方法があります。</p>
<p>ヒープ領域がいっぱいになってしまう場合は、物理的にヒープ領域に割り当てるメモリ領域を増やすか、データをすべてメモリ上に載せるのではなくHDDやSSDのようなストレージの利用を考えましょう。その際、ストレージのFile IOが遅いことが問題になるかもしれません。その場合は、読み込むファイルの読み込む位置を先頭からではなく途中から読み込む<a href="http://docs.oracle.com/javase/jp/8/api/java/io/RandomAccessFile.html" target="_blank">RandomAccessFile</a>を使用することや何度も読み込むデータを<a href="http://docs.oracle.com/javase/jp/8/api/java/util/WeakHashMap.html" target="_blank">WeakHashMap</a>でキャッシュしたりといった対策が考えられます。</p>
<h4>（２）物理的に割り当てる容量を変更する</h4>
<p>JVM始動時にJVMオプションによって確保するメモリの各領域の容量を変更することができます。</p>
<table border="1">
<thead>
<caption>JVMが始動時に確保するメモリの各領域の容量を変更するJVMオプション</caption>
<tr>
<th>&nbsp;</th><th>スタック領域</th><th>ヒープ領域</th><th>ヒープ領域内のNew領域<br>（のOld領域に対する比率）</th><th>New領域内のEden領域<br>（のSurvivor領域に対する比率）</th><th>Metaspace領域</th><th>Permanent領域</th>
</tr>
</thead>
<tbody>
<tr>
<th>容量</th><td>-Xss</td><td>-Xms</td><td>-Xmn<br>-XX:NewSize=</td><td>&nbsp;</td><td>-XX:MetaspaceSize=</td><td>-XX:PermSize=</td>
</tr>
<tr>
<th>最大容量</th><td>-Xss</td><td>-Xmx</td><td>-Xmn<br>-XX:MaxNewSize=</td><td>&nbsp;</td><td>-XX:MaxMetaspaceSize=</td><td>-XX:MaxPermSize=</td>
</tr>
<tr>
<th>比率</th><td>&nbsp;</td><td>&nbsp;</td><td>-XX:NewRatio=</td><td>-XX:SurvivorRatio=</td><td>&nbsp;</td><td>&nbsp;</td>
</tr>
</tbody>
</table>
<p>もし、メモリの容量が不足して領域に新たに容量を割り当てられない場合は、<a href="https://ja.wikipedia.org/wiki/%E4%BB%AE%E6%83%B3%E8%A8%98%E6%86%B6" target="_blank">仮想記憶</a>で見かけ上の主記憶の容量を増やす方法があります。もしくは、多少お金を払っても構わない人は、<a href="https://aws.amazon.com/jp/" target="_blank">Amazon AWS</a>や<a href="https://www.heroku.com/" target="_blank">Heroku</a>、<a href="https://azure.microsoft.com/" target="_blank">Windows Azure</a>、<a href="https://cloud.google.com/compute/" target="_blank">Google Compute Engine</a>、<a href="http://www.ibm.com/cloud-computing/jp/ja/softlayer.html" target="_blank">IBM SoftLayer</a>、<a href="http://cloud.sakura.ad.jp/" target="_blank">さくらのクラウド</a>、<a href="https://www.conoha.jp/" target="_blank">Conoha VPS</a>、<a href="http://vps.sakura.ad.jp/" target="_blank">さくらのVPS</a>などIaaSの導入を検討してみてはいかがでしょうか。計算機自体を変える場合でも、単純に使用する計算機１台のスペックを向上させるスケールアップと分散化して処理するスケールアウトのどちらで対応するかの選択肢があります。</p>
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.003.jpeg" width="500px"><br>
Scalaで文字を管理するクラスはCharクラス、文字列を管理するクラスはStringクラスです。
Javaのプリミティブ型charを、Scalaにはプリミティブ型が存在しないため、オブジェクト型のCharクラスが管理します。
Stringクラスは、Charクラスの配列を扱うラッパークラスです。
ラッパークラスというのは、データとそのデータを扱うために便利なメソッドを保持するクラスのことです。
ScalaのCharクラスのサイズはJavaのchar型と同様に16bitです。
Javaのchar型はUnicodeの発案に基づいて16bitに設計されましたが、後にSurrogate Pairが登場し、全ての１文字を１つのchar型で表せないことが判明しました。
そのため、Javaのchar型を同じサイズで引き継いだScalaのCharクラスもまた全ての１文字を１つのCharクラスで表せるわけではありません。

<h3>メモ：なぜ１文字が１つのchar型で表せなくなったのか、文字コードの歴史</h3>
なぜ１文字が１つのchar型で表せなくなったのか、文字コードの歴史を簡単に振り返ってみましょう。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.004.jpeg" width="500px">
<br>
なぜJavaで１文字が１つのchar型で表せなくなったのか、文字コードの歴史を振り返るにあたって、歴史を想像しやすいようにランドマークとして日本史・世界史と代表的なポップ音楽の歴史を足しました。***太字の箇所***だけ読んでいただければ十分です。

最初に文字を電子化したのは１８００年台半ばの<a href="https://ja.wikipedia.org/wiki/%E3%83%A2%E3%83%BC%E3%83%AB%E3%82%B9%E7%AC%A6%E5%8F%B7" target="_blank">モールス信号</a>です。
今日文字入力装置として使われるキーボードの原型は、１９００年代初めの<a href="https://ja.wikipedia.org/wiki/%E3%82%BF%E3%82%A4%E3%83%97%E3%83%A9%E3%82%A4%E3%82%BF%E3%83%BC" target="_blank">タイプライター</a>標準化で生まれました。
１９４５年に<a href="https://ja.wikipedia.org/wiki/%E7%AC%AC%E4%BA%8C%E6%AC%A1%E4%B8%96%E7%95%8C%E5%A4%A7%E6%88%A6" target="_blank">第二次世界大戦</a>が終結します。<a href="https://ja.wikipedia.org/wiki/%E5%AE%AE%E5%9F%8E%E4%BA%8B%E4%BB%B6" target="_blank">宮城事件</a>。
１９４６年に最初のコンピュータの一つと言われる<a href="https://ja.wikipedia.org/wiki/ENIAC" target="_blank">ENIAC(エニアック)</a>が登場します。
１９４９年、映画とその主題歌<a href="https://ja.wikipedia.org/wiki/%E9%9D%92%E3%81%84%E5%B1%B1%E8%84%88_(%E6%9B%B2)" target="_blank">「青い山脈」</a>発表。（映画「青い山脈」は<a href="https://ja.wikipedia.org/wiki/%E5%90%89%E6%B0%B8%E5%B0%8F%E7%99%BE%E5%90%88" target="_blank">吉永小百合</a>が寺沢新子役を演じた１９６３年版が好きです。）
１９５０年にアメリカでLeroy Andersonが<a href="https://ja.wikipedia.org/wiki/%E3%82%BF%E3%82%A4%E3%83%97%E3%83%A9%E3%82%A4%E3%82%BF%E3%83%BC_(%E3%82%A2%E3%83%B3%E3%83%80%E3%83%BC%E3%82%BD%E3%83%B3)" target="_blank">"The Typewriter"</a>という現代オーケストラ曲を作曲します。
１９５０年代に<a href="https://ja.wikipedia.org/wiki/%E7%B0%A1%E4%BD%93%E5%AD%97" target="_blank">中国簡体字</a>が登場し、５０年から５３年に<a href="https://ja.wikipedia.org/wiki/%E6%9C%9D%E9%AE%AE%E6%88%A6%E4%BA%89" target="_blank">朝鮮戦争</a>で朝鮮半島が北朝鮮と韓国に分裂します。その後、韓国でナショナリズムが高まり、７０年韓国が<a href="https://ja.wikipedia.org/wiki/%E6%9C%9D%E9%AE%AE%E3%81%AB%E3%81%8A%E3%81%91%E3%82%8B%E6%BC%A2%E5%AD%97" target="_blank">漢字廃止政策</a>を行います。６０年代から日本ではベトナム戦争に反対する形で<a href="https://ja.wikipedia.org/wiki/%E6%97%A5%E6%9C%AC%E3%81%AE%E5%AD%A6%E7%94%9F%E9%81%8B%E5%8B%95" target="_blank">学生運動</a>が<a href="https://ja.wikipedia.org/wiki/%E3%82%AA%E3%83%AB%E3%82%B0_(%E7%A4%BE%E4%BC%9A%E9%81%8B%E5%8B%95)" target="_blank">オルグ活動</a>により広まっていきます。６０年代といえばイギリスのバンド<a href="https://ja.wikipedia.org/wiki/%E3%83%93%E3%83%BC%E3%83%88%E3%83%AB%E3%82%BA" target="_blank">The Beatles</a>の時代。７０年代に<a href="https://ja.wikipedia.org/wiki/%E5%9B%BD%E9%89%84%E5%8A%B4%E5%83%8D%E7%B5%84%E5%90%88" target="_blank">国鉄労働組合</a>によるストライキが頻発。
１９７０年に<a href="https://ja.wikipedia.org/wiki/%E6%AF%9B%E6%B2%A2%E6%9D%B1" target="_blank">毛沢東</a>の<a href="https://ja.wikipedia.org/wiki/%E6%96%87%E5%8C%96%E5%A4%A7%E9%9D%A9%E5%91%BD" target="_blank">文化大革命</a>が終わり、日本では７０年<a href="https://ja.wikipedia.org/wiki/%E4%B8%89%E5%B3%B6%E4%BA%8B%E4%BB%B6" target="_blank">三島事件</a>、７２年<a href="https://ja.wikipedia.org/wiki/%E9%80%A3%E5%90%88%E8%B5%A4%E8%BB%8D" target="_blank">連合赤軍</a>による<a href="https://ja.wikipedia.org/wiki/%E5%B1%B1%E5%B2%B3%E3%83%99%E3%83%BC%E3%82%B9%E4%BA%8B%E4%BB%B6" target="_blank">山岳ベース事件</a>、<a href="https://ja.wikipedia.org/wiki/%E3%81%82%E3%81%95%E3%81%BE%E5%B1%B1%E8%8D%98%E4%BA%8B%E4%BB%B6" target="_blank">あさま山荘事件</a>や<a href="https://ja.wikipedia.org/wiki/%E6%97%A5%E6%9C%AC%E8%B5%A4%E8%BB%8D" target="_blank">日本赤軍</a>による<a href="https://ja.wikipedia.org/wiki/%E3%83%86%E3%83%AB%E3%82%A2%E3%83%93%E3%83%96%E7%A9%BA%E6%B8%AF%E4%B9%B1%E5%B0%84%E4%BA%8B%E4%BB%B6" target="_blank">テルアビブ空港乱射事件</a>などが勃発し、７３年は<a href="https://ja.wikipedia.org/wiki/%E3%82%AA%E3%82%A4%E3%83%AB%E3%82%B7%E3%83%A7%E3%83%83%E3%82%AF#.E7.AC.AC1.E6.AC.A1.E3.82.AA.E3.82.A4.E3.83.AB.E3.82.B7.E3.83.A7.E3.83.83.E3.82.AF.EF.BC.88.E7.AC.AC1.E6.AC.A1.E7.9F.B3.E6.B2.B9.E3.82.B7.E3.83.A7.E3.83.83.E3.82.AF.E3.83.BB.E7.AC.AC1.E6.AC.A1.E7.9F.B3.E6.B2.B9.E5.8D.B1.E6.A9.9F.EF.BC.89" target="_blank">第一次オイルショック</a>で日本は狂乱物価、<a href="https://ja.wikipedia.org/wiki/%E3%83%99%E3%83%88%E3%83%8A%E3%83%A0%E6%88%A6%E4%BA%89" target="_blank">ベトナム戦争</a>が終結、米軍の撤退と同時に７５年頃から４年間カンボジアで<a href="https://ja.wikipedia.org/wiki/%E3%83%9D%E3%83%AB%E3%83%BB%E3%83%9D%E3%83%88" target="_blank">ポル・ポト</a>政権が<a href="https://ja.wikipedia.org/wiki/%E6%AF%9B%E6%B2%A2%E6%9D%B1%E6%80%9D%E6%83%B3" target="_blank">毛沢東主義</a>をベースにした<a href="https://ja.wikipedia.org/wiki/%E5%8E%9F%E5%A7%8B%E5%85%B1%E7%94%A3%E5%88%B6" target="_blank">原始共産主義</a>を掲げ、自国民の主に知識人を大量に虐殺します。この時のポル・ポト政権による大虐殺が、後にUnicodeへのクメール語の文字登録に悪影響を与えます。７４年、<a href="https://ja.wikipedia.org/wiki/%E8%A5%9F%E8%A3%B3%E5%B2%AC_(%E6%A3%AE%E9%80%B2%E4%B8%80%E3%81%AE%E6%9B%B2)" target="_blank">吉田拓郎が森進一に「襟裳岬」を提供。</a>７７年日本赤軍が引き起こしたバングラデシュ・ダッカでの<a href="https://ja.wikipedia.org/wiki/%E3%83%80%E3%83%83%E3%82%AB%E6%97%A5%E8%88%AA%E6%A9%9F%E3%83%8F%E3%82%A4%E3%82%B8%E3%83%A3%E3%83%83%E3%82%AF%E4%BA%8B%E4%BB%B6" target="_blank">日航機ハイジャック事件</a>での釈放要求に対して<a href="https://ja.wikipedia.org/wiki/%E7%A6%8F%E7%94%B0%E8%B5%B3%E5%A4%AB" target="_blank">福田赳夫</a>首相が「人の命は地球より重い」と発言し、これに応じました。このことがこの前後１０年ほどの間に行われていた<a href="https://ja.wikipedia.org/wiki/%E5%8C%97%E6%9C%9D%E9%AE%AE%E3%81%AB%E3%82%88%E3%82%8B%E6%97%A5%E6%9C%AC%E4%BA%BA%E6%8B%89%E8%87%B4%E5%95%8F%E9%A1%8C" target="_blank">北朝鮮による日本人拉致事件</a>を助長したかもしれません。このころ日本の学生運動は一連の<a href="https://ja.wikipedia.org/wiki/%E5%86%85%E3%82%B2%E3%83%90" target="_blank">内ゲバ事件</a>やテロ事件により冷めていきます。
１９７９年に社会学者エズラ・ヴォーゲルによる<a href="https://ja.wikipedia.org/wiki/%E3%82%B8%E3%83%A3%E3%83%91%E3%83%B3%E3%83%BB%E3%82%A2%E3%82%BA%E3%83%BB%E3%83%8A%E3%83%B3%E3%83%90%E3%83%BC%E3%83%AF%E3%83%B3" target="_blank">"Japan as Number One: Lessons for America"</a>が出版され、日本的経営が世界的に評価されます。この年の<a href="https://ja.wikipedia.org/wiki/%E3%82%A4%E3%83%A9%E3%83%B3%E9%9D%A9%E5%91%BD" target="_blank">イラン革命</a>が<a href="https://ja.wikipedia.org/wiki/%E3%82%AA%E3%82%A4%E3%83%AB%E3%82%B7%E3%83%A7%E3%83%83%E3%82%AF#.E7.AC.AC2.E6.AC.A1.E3.82.AA.E3.82.A4.E3.83.AB.E3.82.B7.E3.83.A7.E3.83.83.E3.82.AF.EF.BC.88.E7.AC.AC2.E6.AC.A1.E7.9F.B3.E6.B2.B9.E3.82.B7.E3.83.A7.E3.83.83.E3.82.AF.E3.83.BB.E7.AC.AC2.E6.AC.A1.E7.9F.B3.E6.B2.B9.E5.8D.B1.E6.A9.9F.EF.BC.89" target="_blank">第二次オイルショック</a>を引き起こします。
１９８３年、<a href="https://ja.wikipedia.org/wiki/%E5%A4%A7%E9%9F%93%E8%88%AA%E7%A9%BA%E6%A9%9F%E6%92%83%E5%A2%9C%E4%BA%8B%E4%BB%B6" target="_blank">大韓航空機撃墜事件</a>。
***１９８４年、欧州電子計算機工業会（ECMA）と米国国家規格協会（ANSI）がECMA-94という今日Latin-1と呼ばれるラテン文字の文字コードの原型が制定されます。同年、ISOの文字コード規格委員会が16bit固定長のISO 10646を構想します。***
この頃イギリスのゲイのミュージシャンが世界的に流行りだします。８４年、<a href="https://ja.wikipedia.org/wiki/%E3%82%AB%E3%83%BC%E3%83%9E%E3%81%AF%E6%B0%97%E3%81%BE%E3%81%90%E3%82%8C" target="_blank">Culture Clubの"Karma Chameleon"</a>、ディスコ曲の<a href="https://ja.wikipedia.org/wiki/%E3%83%87%E3%83%83%E3%83%89%E3%83%BB%E3%82%AA%E3%82%A2%E3%83%BB%E3%82%A2%E3%83%A9%E3%82%A4%E3%83%B4_(%E3%83%90%E3%83%B3%E3%83%89)" target="_blank">Dead or Aliveの"You Spin Me Round"</a>発売。
１９８５年、<a href="https://ja.wikipedia.org/wiki/%E3%83%97%E3%83%A9%E3%82%B6%E5%90%88%E6%84%8F" target="_blank">プラザ合意</a>により、アメリカの第二次オイルショックの経済政策の失敗のつけを当時最も調子が良かった日本に払わせるため、アメリカの圧力により日本が円高に為替操作をさせられ、その後の日本の<a href="https://ja.wikipedia.org/wiki/%E3%83%90%E3%83%96%E3%83%AB%E6%99%AF%E6%B0%97" target="_blank">バブル景気</a>と崩壊の原因を作ります。<a href="https://ja.wikipedia.org/wiki/%E5%8D%92%E6%A5%AD_(%E5%B0%BE%E5%B4%8E%E8%B1%8A%E3%81%AE%E6%9B%B2)" target="_blank">尾崎豊「卒業」発売。</a>
***１９８７年、<a href="https://ja.wikipedia.org/wiki/%E3%82%BC%E3%83%AD%E3%83%83%E3%82%AF%E3%82%B9" targe="_blank">Xerox</a>のJoe BeckerとLee Collinsが世界中の文字を16bit固定長で表すUnicodeを構想します。*** <a href="https://ja.wikipedia.org/wiki/%E5%9B%BD%E9%89%84%E5%88%86%E5%89%B2%E6%B0%91%E5%96%B6%E5%8C%96" target="_blank">国鉄分割民営化</a>。
***１９８９年、16bit固定長のUnicode Draft 1を発表します。***同年、中国は<a href="https://ja.wikipedia.org/wiki/%E3%83%88%E3%82%A6%E5%B0%8F%E5%B9%B3" target="_blank">鄧小平</a>の時代、<a href="https://ja.wikipedia.org/wiki/%E5%85%AD%E5%9B%9B%E5%A4%A9%E5%AE%89%E9%96%80%E4%BA%8B%E4%BB%B6" target="_blank">天安門事件</a>が勃発します。<a href="https://ja.wikipedia.org/wiki/%E3%83%99%E3%83%AB%E3%83%AA%E3%83%B3%E3%81%AE%E5%A3%81%E5%B4%A9%E5%A3%8A" target="_blank">ベルリンの壁崩壊</a>。<a href="https://ja.wikipedia.org/wiki/%E7%B4%85_(X%E3%81%AE%E6%9B%B2)" target="_blank">X Japan (当時はX)の「紅」</a>が発売。
***１９９０年、ISO 10646の原案では漢字コードを32bitで各国の文字コードをそのまま扱うことになっていました。
中国が漢字を国ごとではなく統合的に扱うことを要請しCJK-JRGを設置しました。***<a href="https://ja.wikipedia.org/wiki/%E3%82%88%E3%81%A9%E5%8F%B7%E3%83%8F%E3%82%A4%E3%82%B8%E3%83%A3%E3%83%83%E3%82%AF%E4%BA%8B%E4%BB%B6" target="_blank">よど号ハイジャック事件</a>。
***１９９１年、32bit固定長のDIS 10646第一版は否決され、16bit固定長のUnicodeとの一本化が図られ、Unicode 1.0.0が発表されました。
世界中の文字を16bit、つまり65536個の容量で管理しようとしましたが、中国や台湾などの漢字が多すぎたためすぐに挫折します。***<a href="https://ja.wikipedia.org/wiki/%E3%82%BD%E9%80%A3%E5%B4%A9%E5%A3%8A" target="_blank">ソ連崩壊</a>により<a href="https://ja.wikipedia.org/wiki/%E5%86%B7%E6%88%A6" target="_blank">冷戦</a>が終結します。
中国や台湾からの漢字領域への大規模水増し申請が起こりました。
***１９９３年　CJK統合漢字が割り当てられたUnicode 1.1が制定されました。***
<a href="https://ja.wikipedia.org/wiki/%E3%83%8F%E3%83%B3%E3%82%B0%E3%83%AB" target="_blank">ハングル文字</a>は<a href="https://ja.wikipedia.org/wiki/%E3%83%8F%E3%83%B3%E3%82%B0%E3%83%AB#.E5.AD.97.E6.AF.8D.E3.81.AE.E7.B5.84.E5.90.88.E3.81.9B" target="_blank">チャモ</a>の組み合わせが大量に存在するが、韓国の申請により使用頻度の低いハングル文字も<a href="https://ja.wikipedia.org/wiki/%E5%9F%BA%E6%9C%AC%E5%A4%9A%E8%A8%80%E8%AA%9E%E9%9D%A2" target="_blank">BMP領域</a>に入れることになりハングル大移動が起こりました。
***こうした中、Java言語の前身であるOak言語がUnicodeを採用し、char型を16bit固定長に設計しました。***
***１９９４年　Oak言語がJava言語に名称変更、翌年９５年にJava言語が公開されました。***
***C/C++言語のchar型は8bit固定長で256文字しか扱えないため、日本語処理では<a href="https://ja.wikipedia.org/wiki/EUC-JP" target="_blank">EUC-JP</a>や<a href="https://ja.wikipedia.org/wiki/Shift_JIS" target="_blank">Shift JIS</a>などの文字コードを用いて無理やり2つのchar型で１文字を管理していました。そのため、Javaでchar型が16bitに倍化したことの意義は大きいです。***
***なぜ、32bitの容量がchar型に採用されなかったのかは考察に値するが、この点について私はまだよく調べられておりません。
考えられることとしては、
ラテン文字の少ない文字数で事足りる西欧人からすると8bitから16bitでメモリの使用領域が倍化するのに、32bitになれば4倍にもなり、そのほとんどが西欧人にとって必要のないものであるため基本的に容量が増えることに反対であったと考えられます。さらに漢字統合が起これば登録すべき字数が少なくなること、使用頻度により容量を変化させる折衷案が存在することなどから、折衷案に収束していったと考えられます。この結果、C/C+言語で日本語文字を8bit型２つで表していた問題は、Javaでは日本語文字の大部分は16bitのchar型１つで表せるが、一部はchar型を2つ組み合わせて表す方式になってしまいました。***
１９９５年　<a href="https://ja.wikipedia.org/wiki/%E9%98%AA%E7%A5%9E%E3%83%BB%E6%B7%A1%E8%B7%AF%E5%A4%A7%E9%9C%87%E7%81%BD" target="_blank">阪神・淡路大震災</a>。<a href="https://ja.wikipedia.org/wiki/%E5%9C%B0%E4%B8%8B%E9%89%84%E3%82%B5%E3%83%AA%E3%83%B3%E4%BA%8B%E4%BB%B6" target="_blank">地下鉄サリン事件</a>。
***１９９６年、Unicode 2.0.0が発表され、容量を16bitから21bitに拡張、未登録漢字を追加領域に登録し、16bit２つで追加領域を表現するSurrogate Pairが登場します。***
１９９９年、Unicode 3.0.0が発表され、ベトナムの文字欄が追加され、カンボジアの<a href="https://ja.wikipedia.org/wiki/%E3%82%AF%E3%83%A1%E3%83%BC%E3%83%AB%E6%96%87%E5%AD%97" target="_blank">クメール文字</a>を追加される。しかし、ポル・ポト政権により知識人が大量虐殺されたことに影響し、クメール文字に存在しない文字が登録されたり、存在する文字が登録されていなかったり、登録順序が不整合な登録でした。
同年、北朝鮮が「金正日」と「金日成」を表すハングルの追加を申請しました。
<a href="https://ja.wikipedia.org/wiki/GLAY_EXPO#GLAY_EXPO_.2799_SURVIVAL" target="_blank">GLAYの２０万人動員ライブ(GLAY EXPO '99 SURVIVAL)</a>。
２０００年、<a href="https://ja.wikipedia.org/wiki/%E3%82%A4%E3%83%B3%E3%82%BF%E3%83%BC%E3%83%8D%E3%83%83%E3%83%88%E3%83%BB%E3%83%90%E3%83%96%E3%83%AB" target="_blank">ITバブル</a>崩壊、ITバブル崩壊や<a href="https://ja.wikipedia.org/wiki/%E3%82%B5%E3%83%96%E3%83%97%E3%83%A9%E3%82%A4%E3%83%A0%E3%83%AD%E3%83%BC%E3%83%B3" target="_blank">サブプライム住宅ローン問題</a>などが２００８年に<a href="https://ja.wikipedia.org/wiki/%E3%83%AA%E3%83%BC%E3%83%9E%E3%83%B3%E3%83%BB%E3%82%B7%E3%83%A7%E3%83%83%E3%82%AF" target="_blank">リーマン・ショック</a>を引き起こす原因となり、<a href="https://ja.wikipedia.org/wiki/%E3%82%B5%E3%83%96%E3%83%97%E3%83%A9%E3%82%A4%E3%83%A0%E4%BD%8F%E5%AE%85%E3%83%AD%E3%83%BC%E3%83%B3%E5%8D%B1%E6%A9%9F" target="_blank">サブプライム住宅ローン危機</a>、世界的な金融緩和競争を呼び、２０１１年に<a href="https://ja.wikipedia.org/wiki/%E3%82%A6%E3%82%A9%E3%83%BC%E3%83%AB%E8%A1%97%E3%82%92%E5%8D%A0%E6%8B%A0%E3%81%9B%E3%82%88" target="_blank">「ウォール街を占拠せよ」</a>の原因の一つになりました（アメリカの中流層の雇用悪化はアメリカから中国への工場移転など他の理由も考えられます）。
２００１年、<a href="https://ja.wikipedia.org/wiki/%E3%82%A2%E3%83%A1%E3%83%AA%E3%82%AB%E5%90%8C%E6%99%82%E5%A4%9A%E7%99%BA%E3%83%86%E3%83%AD%E4%BA%8B%E4%BB%B6" target="_blank">アメリカ同時多発テロ発生</a>。
***２００３年、Scala言語が公開されました。***
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.005.jpeg" width="500px">
<br>
JavaとScalaの文字に関連するリテラルの対照表です。表が示すように基本的にはリテラルの表記方法はJavaとScalaに違いはありません。しかし、Javaには存在しないがScalaには存在する生文字リテラルがあります。
<h3>メモ：Scalaのvalとvarの使い分け</h3>
Javaの変数は参照を変えられますが、この表の例のようにScalaで`val`を使用すると参照を変えることができません。もしJavaと同じように参照を変えたい場合は、`val`から`var`に変更してください。
<h3>メモ：日本語の半角円記号とバックスラッシュ記号の混同問題</h3>
Shift JISやEUC-JPの一部は<a href="https://ja.wikipedia.org/wiki/JIS_X_0201" target="_blank">JIS X 0201</a>であり、<a href="https://ja.wikipedia.org/wiki/ASCII" target="_blank">ASCII</a>のバックスラッシュ記号である0x5CのCode Pointに、JIS X 0201では日本語の半角円記号が登録されています。このため見た目が半角円記号であってもバックスラッシュ記号のCode Point 0x5Cを表していることがあります。スライドでは半角円記号で表示しましたが、0x5Cの意味で半角円記号を表記しました。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.006.jpeg" width="500px">
<br>
文字リテラルや文字列リテラルで\記号が必要なエスケープシーケンスを生文字リテラルでは\記号なしで表記できます。ただし、Unicodeシーケンスの\記号は生文字リテラルでも必要です。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.007.jpeg" width="500px">
<br>
改行文字を生文字リテラルで扱うとプログラムのインデントが崩れてしまい可読性が低下する問題が発生します。<a href="http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.StringLike@stripMargin:String" target="_blank">stripMarginメソッド</a>を使用すると改行文字を含む生文字リテラルのインデントを揃えることができます。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.008.jpeg" width="500px">
<br>
文字列リテラルや生文字リテラルに、それらリテラルような直感的な書き方で処理を加える方法として補間子があります。標準で用意されているのは、s補間子、f補間子、raw補間子の３種類です。それぞれ、s, f, rawを文字列リテラルや生文字リテラルの直前に書くことで補間子として機能します。補間子は<a href="http://www.scala-lang.org/api/current/index.html#scala.StringContext" target="_blank">StringContextクラス</a>を使用することで自作することが可能です。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.009.jpeg" width="500px">
<br>
まずs補間子ですが、<a href="https://ja.wikipedia.org/wiki/Bash" target="_blank">Bash</a>や<a href="https://ja.wikipedia.org/wiki/Perl" target="_blank">Perl</a>、<a href="https://ja.wikipedia.org/wiki/PHP:_Hypertext_Preprocessor" target="_blank">PHP</a>の文字列のように変数を$記号を使用することでスライドのように直接値を埋め込むことができます。
```scala
  @Test
  def testSInterpolation(): Unit = {
    val universe = "宇宙"
    val result = s"生命、$universe、そして${s"万物についての${"究極の"}疑問"}の答えは、${21 + 21}"

    assert(result == "生命、宇宙、そして万物についての究極の疑問の答えは、42")
  }
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.010.jpeg" width="500px">
<br>
次にf補間子ですが、s補間子で埋め込む値の埋め込み方をf補間子ではC言語のprintfのフォーマットのように指定することができます。スライドでは、`${9}`で１０進整数9を埋め込むことを指定し、直後の`%03d`で１０進整数（decimalのd）に対して、３桁で埋め込み、足りない桁には0を使用することを指定しています。そのため、`${9}`の「9」が結果では「009」に変化しています。
```scala
  @Test
  def testFInterpolation(): Unit = {
    val result = f"サイボーグ${9}%03dVSデビルマン"

    assert(result == "サイボーグ009VSデビルマン")
  }
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.011.jpeg" width="500px">
<br>
raw補間子ですが、raw補間子は生文字リテラルと同じようにエスケープシーケンスをエスケープせずに表記通り表現したい際に使用します。raw補間子は生文字リテラルと同様にUnicodeシーケンスは\記号が必要です。では文字列リテラルにraw補間子を使用した場合と生文字リテラルとの間に違いはあるのでしょうか。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.012.jpeg" width="500px">
<br>
改行の扱いにおいて、文字列リテラルにraw補間子を使用した場合と生文字リテラルとの間に違いがあります。raw補間子で解釈する前は文字列リテラルなので、文字列リテラルの内部では生文字リテラルのように改行することはできません。
```scala
  @Test
  def testDifferenceBetweenRawInterpolationAndRawStringLiteral(): Unit = {
    //val newLineRawInterpolation: String = raw"改
//行"
    val newLineRawStringLiteral: String = """改
行""".stripMargin

    //val doubleQuotationRawInterpolation: String = raw"ダブルクォーテーション「"」"
    val doubleQuotationRawStringLiteral: String = """ダブルクォーテーション「"」"""
  }
```
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.013.jpeg" width="500px">
<br>
s補間子、f補間子、raw補間子のような補間子を自作することができます。スライドでは、作成したjson補間子の挙動を説明します。  
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.014.jpeg" width="500px">
<br>
まず、json補間子は暗黙的にStringContextクラスのインスタンスを作成します。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.015.jpeg" width="500px">
<br>
json補間子はStringContextの自作のラッパーであるJsonHelperクラスのインスタンスを暗黙的に作成します。
<br>
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.016.jpeg" width="500px">
<br>
JsonHelperクラスのjsonメソッドが呼ばれます。
実際に作成したjson補間子は下記サンプルコードに載せましたので、そちらを参照してください。
```scala
  @Test
  def testStringContext(): Unit = {
    assert(Day2TestStringContext.test() == "{ name: 名前, id: ID }")
  }
```
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.017.jpeg" width="500px">
<br>
Scala/Javaで使用するエスケープシーケンスの一覧です。bは"Back space"、fは"form Feed"、nは"New line"、rは"carriage Return"、tは"horizontal Tab"（ASCIIなどにはvertical tabも存在します。<a href="https://ja.wikipedia.org/wiki/ASCII#ASCII.E5.88.B6.E5.BE.A1.E6.96.87.E5.AD.97" target="_blank">ASCII制御文字</a>）の略字です。タイプライターの時代から"back space"や"carriage return"、"new line (タイプライターではline feed)"、"horizontal tab (タイプライターではtab)"が存在します。"carriage return"はタイプライターからの名残で、コンピュータでは既に役目を終えているが、Microsoft社がShift JISやその上位互換の<a href="https://ja.wikipedia.org/wiki/Microsoft%E3%82%B3%E3%83%BC%E3%83%89%E3%83%9A%E3%83%BC%E3%82%B8932" target="_blank">Windows-31J</a>に残しました。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.018.jpeg" width="500px">
<br>
Unicodeシーケンスは文字に関するリテラル内にBMP領域の16bitのCode Pointによって文字を記述する方法です。例では、漢字なのか記号なのか作成するアプリケーションの目的によって変わりそうな文字をUnicodeシーケンスで表現してみました。最後の𠮷野家の<a href="https://ja.wiktionary.org/wiki/%F0%A0%AE%B7" target="_blank">「𠮷」</a>はBMP領域にはなく追加領域の文字なのでSurrogate Pair、つまり２つもUnicodeシーケンスで表現します。このような追加領域の文字は文字列リテラルや生文字リテラルでは表現できるが、文字リテラルでは表現できません。
<h3>メモ：<a href="https://ja.wikipedia.org/wiki/%E5%B9%BD%E9%9C%8A%E6%96%87%E5%AD%97" target="_blank">幽霊文字</a></h3>
文字コードには含まれているが、一体どこで使われているのかわからない、この世には存在しない文字のことを幽霊文字と言います。代表的なものに<a href="https://ja.wiktionary.org/wiki/%E5%A6%9B" target="_blank">「妛」</a>や<a href="https://ja.wiktionary.org/wiki/%E5%BD%81" target="_blank">「彁」</a>があります。紙に書かれた大量の文字の電子化がいかに大変な作業であったかを考えれば、このようにいくつか<a href="https://ja.wikipedia.org/wiki/%E3%83%92%E3%83%A5%E3%83%BC%E3%83%9E%E3%83%B3%E3%82%A8%E3%83%A9%E3%83%BC" target="_blank">ヒューマンエラー</a>が起こるのは仕方ないことかもしれません。
```scala
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
```
***
<img src="https://github.com/ynupc/scalastringcourse/blob/master/image/day2/string_course.019.jpeg" width="500px">
<br>
OSに依存する改行文字、パスの区切り文字、クラスパスの区切り文字は次のように取得できます。ただし、Windowsはキャリッジリターン"\r"なしでも改行を行うことや、パスの区切り文字がUnix環境の"\\"と"/"の両方が混在しても許容されるため、それらについてはUnix側に合わせれば良いという考え方もあります。
```scala
  @Test
  def testOSDependentCharacter(): Unit = {
    //改行
    //Windows: \r\n
    //UNIX:      \n
    val newLine1 = System.getProperty("line.separator")
    val newLine2 = System.lineSeparator()
    val newLine3 = String.format("%n")

    assert(newLine1 == newLine2)
    assert(newLine2 == newLine3)
    assert(newLine1 == newLine3)

    //ディレクトリやファイルのパスの区切り
    //Windows: \\
    //UNIX:     /
    val fileSeparator1 = System.getProperty("file.separator")
    val fileSeparator2 = File.separator

    assert(fileSeparator1 == fileSeparator2)

    //PATHやCLASSPATHの区切り
    //Windows: ;
    //UNIX:    :
    val pathSeparator1 = System.getProperty("path.separator")
    val pathSeparator2 = File.pathSeparator

    assert(pathSeparator1 == pathSeparator2)
  }
```
