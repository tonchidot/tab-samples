# tab OAuth2 認可コードサンプル(WEB版)

## 説明

[Ruby](http://www.ruby-lang.org/ja/) による、 [Sinatra](http://www.sinatrarb.com/) を利用した単純なWEBアプリケーションです。 OAuth2ライブラリとして [oauth2](https://rubygems.org/gems/oauth2/) を利用しています。

OAuth2認可コードのフローを開始し、tabサーバでのユーザ認証を経て、Access Tokenを得ることができます。

## 実行手順

1. ``app.rb`` のうち、 ``CLIENT_ID``, ``CLIENT_SECRET``, ``REDIRECT_URI`` を環境に合わせて書き換える
2. 必要なgemをインストール

  ```bash
  $ bundle install
  ```
3. WEBアプリケーションを起動

  ```bash
  $ bundle exec rackup
  ```

  または

  ```bash
  $ bundle exec rackup -p <port #>
  ```
4. WEBアプリケーション用のURLにアクセス。例えば ``http://localhost:9292/`` など

5. Start OAuth2 リンクをクリック

6. tabサーバにログインしていれば認可画面が表示される
      * tabサーバにログインしていなければまずログインページが表示され、ログインに成功したら6に戻る

7. 認可画面で認可を与えた場合は8へ、そうでなければエラー画面が表示

8. 付与された認可コードを元にAccess Tokenを取得し、ユーザ自身の情報を取得して表示
