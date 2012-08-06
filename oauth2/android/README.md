# tab OAuth2 認可コードサンプル(Android版)
## 説明
Androidによるサンプルです。OAuth認証し、自分のプロファイル情報をtab APIで取得します。

対象SDKバージョンはGingereadr(APIレベル10)以降です。OAuth2ライブラリとして [leeloo](https://bitbucket.org/smartproject/oauth-2.0/wiki/Home) から下記の利用するjarファイルを使用しています。

*jettison-1.2.jar
*oauth2-client.jar
*oauth2-common-0.1.jar
*slf4j-api.1.6.1.jar

## 実行手順
1.[tab Application](https://tab.do/clients)でアプリケーションの登録をし、クライアントIDとクライアントシークレットを発行する。

2.[leelooのダウンロードサイト](https://bitbucket.org/smartproject/oauth-2.0/downloads)から上記4つのjarファイルを入手する。

3.サンプルソースをダウンロードし、ソースファイルをEclipseにインポートする。インポートはEclipseで[File]-[New]-[Other]-[Android Project from Existing Code]を選択し、ローカルに保存したSampleAppを指定する。

4.インポートしたプロジェクトの``libs``フォルダに、2.で入手したjarファイルをコピーする。

5.``CommonConst.java``の中の``CLIENT_ID``、``CLIENT_SECRET``を入手したクライアントIDとクライアントシークレットに置き換える。また、発行の際に登録したスキーマからなるURIを``REDIRECT_URI``に置き換える。

6.必要ならばクリーンビルドを行い実行する。

7.最初は``OAuth認証``ボタンが表示され、これを押すとブラウザが起動する。tabサービスにログインしていない場合は、ログイン画面が表示されるのでログインする。認証されるとブラウザからコールバックURIを含んだIntentが飛んでくるので、これを``MainActivity.java``がうけて、OAuth認証しプロファイル情報を取得する。

8.アクセストークンが期限切れの場合は、``AccessToken再取得``ボタンが表示される。クリックするとアクセストークンを再取得後、プロファイル情報を取得する。

