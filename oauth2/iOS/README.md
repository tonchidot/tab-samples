# tab OAuth2 認可コードサンプル(WEB版)
## 説明
iOS(iPhone)によるサンプルです。OAuth2ライブラリとして [gtm-oauth2](http://code.google.com/p/gtm-oauth2/) を利用しています。gtm-oauth2ライブラリの使い方は公式に詳しく載っているのでここでは割愛します。

## 実行手順
1. ``TABViewController.m``のうち、``kTabClientId``(Client ID)、``kTabClientSecret``(Client Secret)、``redirectUrl``(Callback URL)を環境に合わせて書き換える
2. リポジトリのトップレベルで以下のコマンドを実行して必要なライブラリをインストール

   ```
   $ git submodule update --init
   ```

3. projectファイルをXcodeで起動してRun
4. auth tabと書かれたボタンをタップ
5. tabサーバにログインしていれば認可画面が表示される
	* tabサーバにログインしていなければまずログインページが表示され、ログインに成功したら5に戻る
6. 認可画面で許可を与えた場合は7へ、そうでなければエラー画面が表示
7. 付与された認可コードを元にAccessTokenを取得し、ユーザ自身の情報を取得して表示

