# これは
テストケースの単体・結合分類器．  
後述する 3 つの定義に基づいて分類する．

# 使い方
## ビルド
[Maven](https://maven.apache.org/) が必要．（Maven 3.9.6 かつ Java 21 で動作を確認済み）
~~~
% mvn clean package
% cp target/testcase_classifier.jar ${target project dir}
% cd ${target project dir}
~~~

## 使用
~~~
% java -jar testcase_classifier.jar [options]
~~~

オプションには次が指定できる．
|オプション名|役割|
|:---|:---|
|```-h, --help```|使い方を表示|
|```-o, --output <arg>```|結果の出力先パスを指定（デフォルトはカレントディレクトリ）|
|```-p, --product <arg>```|対象プロジェクトのプロダクトディレクトリを指定（デフォルトは ```./src/main```）|
|```-t, --test <arg>```|対象プロジェクトのテストディレクトリを指定（デフォルトは ```./src/test```）|
|```--version=<8\|11\|17\|latest>```|対象プロジェクトの Java バージョン（デフォルトは ```latest```）|

### 出力
```${output dir}/`***_profiles.csv``` が出力される．  
各ファイルにおける形式および例は以下の通り．  

| テストケースのパス | テストケース名 |  呼び出しパッケージ数 | 呼び出しクラス数 |
| :----: | :----: | :----: | :---: |
| ```src/test/Example.java``` | ```Example#testA``` | 1 | 2 |
| ```src/test/Example2.java``` | ```Example2#testB``` | 0 | 0 | 


# 単体・結合の定義
Trautsch らの解釈および定義を参考にした．
- [F. Trautsch and J. Grabowski, "Are There Any Unit Tests? An Empirical Study on Unit Testing in Open Source Python Projects," 2017 IEEE International Conference on Software Testing, Verification and Validation, 2017, pp. 207-218](https://ieeexplore.ieee.org/document/7927976)
- [F. Trautsch, S. Herbold and J. Grabowski, Are Unit and Integration Test Definitions Still Valid for Modern Java Projects? An Empirical Study on Open-Source Projects, Journal of Systems and Software, Volume 159, 2020, 110421](https://www.sciencedirect.com/science/article/pii/S0164121219301955)

以下の 3 種類である．
1. ISTQB の定義
2. IEEE の定義
3. DEV の定義

## 1. ISTQB の定義
- 単体テスト ＝ 1 つのクラスのみを検証するテスト
- 結合テスト ＝ 上記以外

## 2. IEEE の定義
- 単体テスト ＝ 1 つのパッケージのみを検証するテスト
- 結合テスト ＝ 上記以外

## 3. 開発者（DEV）の定義
- 単体テスト ＝ テストパス中に結合であることを明示するキーワードが含まれない、かつ所属するテストクラスと 1 対 1 に対応するプロダクトクラスが存在するテスト
- 結合テスト ＝ 上記以外