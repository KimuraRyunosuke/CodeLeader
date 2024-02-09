# CodeLeader
このプロジェクトではプログラミング初学者がコードリーディングを気軽に行えるようにするためのwebアプリケーションを作成する。

## 使い方
* Java17
* BuildTool: Gradle

### 準備
1. MySQLをダウンロードする。
2. MySQLを起動する。
3. ```CREATE DATABASE demo;```を実行する。
4. ```CREATE USER 'demouser'@'localhost' IDENTIFIED BY 'demodemo';```を実行する。
5. ```GRANT ALL PRIVILEGES ON demo.* TO 'demouser'@'localhost';```を実行する。
6. exitする。

### 実行3
1. VScodeの拡張機能をSpring Boot関係一式を適用する。
2. 実行ボタンを押して実行する。（Spring Boot Dashboardを参照）

### 実行2
1. ```gradle build```でビルドする。
2. ```java -jar build/libs/CodeLeader-0.0.1-SNAPSHOT.jar```で実行する。
